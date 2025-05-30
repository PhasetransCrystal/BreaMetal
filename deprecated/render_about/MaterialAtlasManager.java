package com.phasetranscrystal.metal.material.client;

import com.phasetranscrystal.metal.helper.SpriteHelper;
import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.material.MaterialItemType;
import com.phasetranscrystal.metal.Registries;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceList;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.minecraft.client.renderer.texture.SpriteLoader.runSpriteSuppliers;

@Deprecated(forRemoval = true)
public class MaterialAtlasManager extends TextureAtlasHolder {
    public static final Logger LOGGER = LogManager.getLogger("BREAMATERIAL:Material:AtlasManager");
    public static final ResourceLocation MISSING = ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID,"textures/breamaterial/material/material/missing.png");
    private static MaterialAtlasManager manager;

    public static MaterialAtlasManager init() {
        if (manager == null && Minecraft.getInstance() != null) {
            manager = new MaterialAtlasManager(Minecraft.getInstance().getTextureManager());
        }
        return getInstance();
    }

    public static MaterialAtlasManager getInstance() {
        if (manager != null) {
            return manager;
        }
        throw new IllegalStateException("MaterialAtlas Manager hasn't created. This will be created in RegisterClientReloadListenersEvent only in client.");
    }


    public static final ResourceLocation MATERIAL_SHEET = ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "textures/atlas/material.png");
    public static final ResourceLocation MATERIAL_ATLAS = ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "material");
    public static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(MATERIAL_SHEET);


    public MaterialAtlasManager(TextureManager pTextureManager) {
        super(pTextureManager, MATERIAL_SHEET, MATERIAL_ATLAS);
    }


    @Override
    public CompletableFuture<Void> reload(PreparationBarrier pPreparationBarrier, ResourceManager pResourceManager, ProfilerFiller pPreparationsProfiler, ProfilerFiller pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        SpriteLoader loader = SpriteLoader.create(this.textureAtlas);

        //load and stitch
        SpriteResourceLoader resourceLoader = SpriteResourceLoader.create(SpriteLoader.DEFAULT_METADATA_SECTIONS);
        return CompletableFuture.supplyAsync(
                        () -> SpriteSourceList.load(pResourceManager, MATERIAL_ATLAS).list(pResourceManager), pBackgroundExecutor
                )
                .thenCompose(list -> runSpriteSuppliers(resourceLoader, list, pBackgroundExecutor))
                .thenApply(list -> {
                    //自定义加载逻辑
                    final List<SpriteContents> contents = new ArrayList<>();

                    ResourceManager rm = Minecraft.getInstance().getResourceManager();

                    //虽然不是这么用的，但是比较方便
                    Map<MaterialItemType, SpriteContents> alphaCache = new HashMap<>();
                    Map<MaterialItemType, SpriteContents> coverCache = new HashMap<>();

                    IntOpenHashSet colors = new IntOpenHashSet();

                    for (Material material : Registries.MATERIAL) {
//                        if(material.intermediateProduct){
//                            colors.add(material.x16color);
//                        };

                        //加载材料纹理
                        SpriteContents mat;
                        {
                            ResourceLocation location = material.getLocation().withPath(s -> "textures/breamaterial/material/material/" + s + ".png");
                            mat = resourceLoader.loadSprite(material.getLocation(),rm.getResource(location).orElseGet(()->{
                                LOGGER.warn("Can't find texture at ResourceLocation={} for Material={id:{}}. Use MISSING.",location,material.getLocation());
                                return rm.getResource(MISSING).get();
                            }));
                        }

//                        contents.add(mat);


//                        for (MaterialItemType type : (material.equals(BreaRegistries.MaterialReg.MISSING.get())) ? Registries.MATERIAL_ITEM_TYPE : material.toTypes) {
                        for (MaterialItemType type : material.toTypes) {
                            //创建物品类型alpha通道缓存
                            if (!alphaCache.containsKey(type)) {
                                ResourceLocation location = type.getLocation().withPath(s -> "textures/breamaterial/material/mit/" + s + ".png");
                                rm.getResource(location).ifPresentOrElse(r -> alphaCache.put(type,resourceLoader.loadSprite(type.getLocation(),r)), ()->{
                                    LOGGER.warn("Can't find texture at ResourceLocation={} for MaterialItemType={id:{}}.",location,type.getLocation());
                                    alphaCache.put(type,null);
                                });
                            }

                            //创建物品覆盖层缓存
                            if (!coverCache.containsKey(type)) {
                                coverCache.put(type,rm.getResource(type.getLocation().withPath(s -> "textures/breamaterial/material/mit_cover/" + s + ".png"))
                                        .map(r -> resourceLoader.loadSprite(type.getLocation(),r)).orElse(null));
                            }

                            ResourceLocation l = combine(material,type);
                            //加载可选的替换材质
                            rm.getResource(material.getLocation().withPath(s -> "textures/breamaterial/material/override/" + s + "/" + type.getLocation().getNamespace() + "_" + type.getLocation().getPath() + ".png")).ifPresentOrElse(
                                    r -> contents.add(resourceLoader.loadSprite(l,r)),
                                    ()->{//以及……强制组合  我不想写动态解析了
                                        SpriteContents alpha = alphaCache.get(type);
                                        SpriteContents cover = coverCache.get(type);

                                        if(alpha == null){
                                        }else if(mat.animatedTexture == null && alpha.animatedTexture == null){
                                            //全静态材质叠合
                                            List<NativeImage> images = new ArrayList<>();
                                            images.add(SpriteHelper.firstFrame(mat));
                                            images.add(SpriteHelper.firstFrame(alpha));
                                            if(cover != null){
                                                images.add(SpriteHelper.firstFrame(cover));
                                            }
                                            images = SpriteHelper.scaleToSame(images,true);

                                            NativeImage f = SpriteHelper.alphaFilter(images.get(0),images.get(1));
                                            if(images.size() == 3){
//                                                f = SpriteHelper.blend(f,images.get(2));
                                            }

                                            contents.add(new SpriteContents(l,new FrameSize(f.getWidth(),f.getHeight()),f, ResourceMetadata.EMPTY));
                                        }
                                    }
                            );

                        }

                    }

                    List<SpriteContents> e = new ArrayList<>(contents.stream().filter(Objects::nonNull).toList());
                    //和原有加载目标合并
                    e.addAll(list);
                    return e;
                })
                .thenApply(list -> loader.stitch(list, 0, pBackgroundExecutor))
                .thenCompose(SpriteLoader.Preparations::waitForUpload)
                .thenCompose(pPreparationBarrier::wait)
                .thenAcceptAsync(preparations -> this.apply(preparations, pReloadProfiler), pGameExecutor);
    }


    public static NativeImage read(Resource resource) {
        try {
            return NativeImage.read(resource.open());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read resource and create an image. Resource:" + resource.sourcePackId(),e);
        }
    }

    public static ResourceLocation combine(Material material,MaterialItemType type){
        return ResourceLocation.fromNamespaceAndPath(material.getLocation().getNamespace() + "_" + type.getLocation().getNamespace(),material.getLocation().getPath() + "_" + type.getLocation().getPath());
    }

    public TextureAtlasSprite getSprite(Material material,MaterialItemType type){
        if(material == null || material.toTypes.contains(type)){
            return getSprite(combine(material,type));
        }
        LOGGER.warn("Material({}) doesn't has MIT({}).",material,type);
        LOGGER.warn("StackTrace:",new IllegalArgumentException());
        return null;
    }

    public TextureAtlasSprite getSprite(Material material){
        return getSprite(material.getLocation());
    }
}