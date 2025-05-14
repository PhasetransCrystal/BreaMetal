package com.phasetranscrystal.metal.datagen;

import com.google.common.hash.Hashing;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class TextureMixProvider implements DataProvider {
    public static final Logger LOGGER = LogManager.getLogger("BreaMetal-Datagen:TextureMix");

    public final PackOutput output;
    public final ExistingFileHelper existingFileHelper;
    public final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected final Set<RootTextureProvider> combinations = new HashSet<>();

    public TextureMixProvider(PackOutput pOutput, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = pOutput;
        this.existingFileHelper = existingFileHelper;
        this.lookupProvider = lookupProvider;
    }

    public boolean addCombination(RootTextureProvider combination) {
        if (!adjustTextureProvider(combination)) return false;
        this.combinations.add(combination);
        return true;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (RootTextureProvider combo : combinations) {
            existingFileHelper.trackGenerated(combo.getOutputPath(), ModelProvider.TEXTURE);
            // 生成材质
            futures.add(generateTexture(output, combo));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> generateTexture(CachedOutput output, RootTextureProvider combo) {
        return CompletableFuture.runAsync(() -> {
            try {
                BufferedImage[] images = new BufferedImage[combo.getTexture().length];

                for (int i = 0; i < combo.getTexture().length; ++i) {
                    images[i] = existingFileHelper.exists(combo.getTexture()[i], ModelProvider.TEXTURE) ? readImage(combo.getTexture()[i]) : null;
                }

                BufferedImage outputImage = execute(images);

                // 保存输出
                Path outputPath = this.output.getOutputFolder()
                        .resolve(combo.getOutputPath().getNamespace())
                        .resolve("textures")
                        .resolve(combo.getOutputPath().getPath() + ".png");
                saveImage(output, outputImage, "PNG", outputPath);
            } catch (Exception e) {
                LOGGER.warn("Unable to generate texture: {}", combo, e);
            }
        });
    }

    private BufferedImage readImage(ResourceLocation location) throws IOException {
        // 通过 ExistingFileHelper 获取资源流
        Resource supplier = existingFileHelper.getResource(
                location, PackType.CLIENT_RESOURCES, ".png", "textures"
        );
        try (InputStream stream = supplier.open()) {
            return ImageIO.read(stream);
        }
    }

    private void saveImage(CachedOutput output, BufferedImage image, String format, Path path) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, format, os);
        byte[] data = os.toByteArray();
        output.writeIfNeeded(path, data, Hashing.sha256().hashBytes(data)); // 使用缓存校验
    }

    // 修改后的 execute 方法签名示例（需自行实现）
    public abstract BufferedImage execute(BufferedImage[] images);

    public abstract boolean adjustTextureProvider(RootTextureProvider provider);

    public interface RootTextureProvider {
        ResourceLocation getOutputPath();

        ResourceLocation[] getTexture();
    }
}
