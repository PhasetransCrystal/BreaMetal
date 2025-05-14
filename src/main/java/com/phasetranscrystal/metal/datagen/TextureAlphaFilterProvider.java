package com.phasetranscrystal.metal.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.helper.ImageHelper;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class TextureAlphaFilterProvider extends TextureMixProvider {
    public TextureAlphaFilterProvider(PackOutput pOutput, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, existingFileHelper, lookupProvider);
    }

    public TextureAlphaFilterProvider(GatherDataEvent event){
        this(event.getGenerator().getPackOutput(), event.getExistingFileHelper(), event.getLookupProvider());
    }

    public void addCombination(MaterialItemType pathMIT, Material pathMaterial, ResourceLocation pathOutput) {
        this.combinations.add(new Combination(pathMIT,pathMaterial,pathOutput));
    }

    @Override
    public BufferedImage execute(BufferedImage[] images) {
        BufferedImage image0 = images[0];
        BufferedImage image1 = images[1];
        BufferedImage image2 = images.length == 3 ? images[2] : null;
        if (image0 == null || image1 == null)
            throw new IllegalStateException("Texture in must not exist:[" + (image0 == null) + "," + (image1 == null) + "]");
        else if (image0.getWidth() != image1.getWidth() || image0.getHeight() != image1.getHeight() || (image2 != null && (image2.getWidth() != image1.getWidth() || image2.getHeight() != image1.getHeight())))
            throw new IllegalStateException("Texture in must have the same width and height: [ " +
                    image0.getWidth() + "x" + image0.getHeight() + " , " + image1.getWidth() + "x" + image1.getHeight() +
                    (image2 == null ? "" : ", " + image2.getWidth() + "x" + image2.getHeight()) + "]");

        BufferedImage outImage = ImageHelper.alphaFilter(image1, image0);
        if (image2 != null) {
            outImage = ImageHelper.blend(outImage, image2);
        }

        return outImage;

    }

    @Override
    public boolean adjustTextureProvider(RootTextureProvider provider) {
        return provider.getTexture().length == 2 || provider.getTexture().length == 3;
    }

    @Override
    public String getName() {
        return "Texture Mix - Multiply";
    }

    public record Combination(
            ResourceLocation pathAlpha,
            ResourceLocation pathTexture,
            ResourceLocation pathCover,
            ResourceLocation pathOutput
    ) implements RootTextureProvider {

        public Combination(MaterialItemType pathMIT, Material pathMaterial, ResourceLocation pathOutput) {
            this(
                    ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "template/mit/" + pathMIT.getLocation().getNamespace() + "/" + pathMIT.getLocation().getPath()),
                    ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "template/material/" + pathMaterial.getLocation().getNamespace() + "/" + pathMaterial.getLocation().getPath()),
                    ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID, "template/mit_cover/" + pathMIT.getLocation().getNamespace() + "/" + pathMIT.getLocation().getPath()),
                    pathOutput
            );
        }

        @Override
        public ResourceLocation getOutputPath() {
            return pathOutput();
        }

        @Override
        public ResourceLocation[] getTexture() {
            return new ResourceLocation[]{pathAlpha, pathTexture, pathCover};
        }
    }
}
