package com.phasetranscrystal.metal.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class TextureMultiplyMixProvider extends TextureMixProvider {
    public TextureMultiplyMixProvider(PackOutput pOutput, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, existingFileHelper, lookupProvider);
    }

    public TextureMultiplyMixProvider(GatherDataEvent event) {
        this(event.getGenerator().getPackOutput(), event.getExistingFileHelper(), event.getLookupProvider());
    }

    @Override
    public BufferedImage execute(BufferedImage[] images) {
        BufferedImage image0 = images[0];
        BufferedImage image1 = images[1];
        if (image0 == null || image1 == null)
            throw new IllegalStateException("Texture in must not exist:[" + (image0 == null) + "," + (image1 == null) + "]");
        else if (image0.getWidth() != image1.getWidth() || image0.getHeight() != image1.getHeight())
            throw new IllegalStateException("Texture in must have the same width and height: [ " + image0.getWidth() + "x" + image0.getHeight() + " , " + image1.getWidth() + "x" + image1.getHeight() + " ]");

        BufferedImage outImage = new BufferedImage(image0.getWidth(), image0.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image0.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                int p1 = image0.getRGB(i, j);
                int p2 = image1.getRGB(i, j);
                outImage.setRGB(i, j, FastColor.ARGB32.multiply(p1, p2));
            }
        }
        return outImage;
    }


    public boolean addCombination(MaterialItemType pathMIT, Material pathMaterial, ResourceLocation output) {
        return super.addCombination(new Combination(pathMIT, pathMaterial, output));
    }

    @Override
    public boolean adjustTextureProvider(RootTextureProvider provider) {
        return provider.getTexture().length == 2;
    }

    @Override
    public String getName() {
        return "Texture Mix - Multiply";
    }

    public record Combination(
            ResourceLocation pathType,
            ResourceLocation pathMaterial,
            ResourceLocation pathOutput
    ) implements RootTextureProvider {

        public Combination(MaterialItemType pathMIT, Material pathMaterial, ResourceLocation pathOutput) {
            this(
                    ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "template/mit/" + pathMIT.getLocation().getNamespace() + "/" + pathMIT.getLocation().getPath()),
                    ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "template/material/" + pathMaterial.getLocation().getNamespace() + "/" + pathMaterial.getLocation().getPath()),
                    pathOutput
            );
        }

        @Override
        public ResourceLocation getOutputPath() {
            return pathOutput();
        }

        @Override
        public ResourceLocation[] getTexture() {
            return new ResourceLocation[]{pathType, pathMaterial};
        }
    }
}
