package com.phasetranscrystal.metal.system.material.client;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.helper.ModelHelper;
import com.phasetranscrystal.metal.module.render.model.ItemORResolveOnly;
import com.phasetranscrystal.metal.system.material.ITypedMaterialObj;
import com.phasetranscrystal.metal.system.material.Material;
import com.phasetranscrystal.metal.system.material.MaterialItemType;
import com.phasetranscrystal.metal.system.material.System$Material;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

@Deprecated(forRemoval = true)
public class TMIModel implements BakedModel {
    private final ItemOverrides ITEM_OVERRIDES = new ItemORResolveOnly((model, stack, level, entity, seed) -> {
        ITypedMaterialObj info = System$Material.getMaterialInfo(stack.getItem());
        if (info != null) {
//            setMaterialType(info.getMaterialOrMissing(stack));
            return getModel();
        }
        return model;
    });

    public final ModelBakery bakery;
    private BlockModel shapeModel;
    public final MaterialItemType type;
    private final Map<Material, BakedModel> modelCache = new HashMap<>();
    private final Map<Integer, BakedModel> idpCache = new HashMap<>();
    private BakedModel missingModel;
    //    public final TextureAtlasSprite MISSING_SPRITE = Minecraft.getInstance().getTextureAtlas(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,"material")).apply(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,"material/missing"));
    private TextureAtlasSprite missingSprite;
    private boolean cached = false;
    private boolean inited = false;
//    @Nonnull
//    private Material materialType = BreaRegistries.MaterialReg.MISSING.get();
    private Material materialType = null;

    public TMIModel(ModelBakery bakery, MaterialItemType type) {
        this.type = type;
        this.bakery = bakery;
    }

    public void setMaterialType(Material materialType) {
        if (materialType != this.materialType) {
            this.cached = false;
            this.materialType = Objects.requireNonNullElse(materialType, null);
        }
    }

    @Nonnull
    public Material getMaterialType() {
        return materialType;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction pDirection, RandomSource pRandom) {
        bootstrapCache();
        return missingModel.getQuads(pState, pDirection, pRandom, ModelData.EMPTY, null);
    }

    public @NotNull BakedModel getModel() {
        bootstrapCache();
        return modelCache.get(materialType);
//        return missingModel;
    }

    private void bootstrapCache() {
        if (!inited) {
            this.missingSprite = Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "material/missing"));

            ResourceLocation bakeName = ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "material_item_bake/" + type.getResourceKey().toString().replace(":", "_"));

            this.shapeModel = (BlockModel) bakery.getModel(System$Material.basicModel(this.type.getLocation()));
//            this.shapeModel.customData.setRenderTypeHint(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,"material"));
            shapeModel.parent = ModelBakery.GENERATION_MARKER;
            shapeModel.transforms = ModelHelper.copyDefaultItemTransforms();
            this.missingModel = BreaMetal.getItemModelgen().generateBlockModel(net.minecraft.client.resources.model.Material::sprite, shapeModel)
                    .bake(bakery.new ModelBakerImpl((location, material) -> material.sprite(),
                            ModelResourceLocation.inventory(bakeName)), net.minecraft.client.resources.model.Material::sprite, new SimpleModelState(Transformation.identity()));//好丑……
            //TODO 工作？
            // OLD                .bake(bakery.new ModelBakerImpl((location, material) -> material.sprite(),
            //                            ModelResourceLocation.inventory(bakeName)), net.minecraft.client.resources.model.Material::sprite, new SimpleModelState(Transformation.identity()), bakeName);
            inited = true;
        }

        if (!cached) {
            if (!modelCache.containsKey(materialType)) {
                ResourceLocation bakeName = ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "material_item_bake/" + type.getLocation().toString().replace(":", "_"));
//                BakedModel baked = BreaMaterials.getItemModelgen().generateBlockModel(m -> MaterialAtlasManager.getInstance().getSprite(materialType,type), shapeModel)
                BakedModel baked;
                if (idpCache.containsKey(materialType.x16color)) {
//                    TextureAtlasSprite sprite = ((TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(BLOCK_ATLAS)).getSprite(materialType.intermediateProduct ? System$Material.idpForAtlasID(materialType.x16color, type) : System$Material.combineForAtlasID(materialType, type));
                    TextureAtlasSprite sprite = null;
                    baked = BreaMetal.getItemModelgen().generateBlockModel(m -> sprite, shapeModel).bake(bakery.new ModelBakerImpl((m, n) -> sprite, ModelResourceLocation.inventory(bakeName)),
                            shapeModel,
                            m -> sprite,
                            new SimpleModelState(Transformation.identity()),
                            false
                            ////OLD
                            //bake(bakery.new ModelBakerImpl((m, n) -> sprite, bakeName),
                            //shapeModel,
                            //m -> sprite,
                            //new SimpleModelState(Transformation.identity()),
                            //bakeName,
                            //false
                    );
                } else {
                    baked = idpCache.get(materialType.x16color);
                }
                modelCache.put(materialType, baked);
            }
            cached = true;
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return missingSprite;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ITEM_OVERRIDES;
    }

}
