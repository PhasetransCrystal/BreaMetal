package com.phasetranscrystal.metal.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public class TextureMultiplyMixProvider extends TextureMixProvider {
    public TextureMultiplyMixProvider(PackOutput pOutput, ExistingFileHelper existingFileHelper, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pOutput, existingFileHelper, lookupProvider);
    }

    @Override
    public BufferedImage execute(BufferedImage[] images) {
        BufferedImage image0 = images[0];
        BufferedImage image1 = images[1];
        if(image0 == null || image1 == null)
            throw new IllegalStateException("Texture in must not exist:[" + (image0 == null) + "," + (image1 == null) + "]");
        else if(image0.getWidth() != image1.getWidth() || image0.getHeight() != image1.getHeight())
            throw new IllegalStateException("Texture in must have the same width and height: [ " + image0.getWidth() + "x" + image0.getHeight() + " , " + image1.getWidth() + "x" + image1.getHeight() + " ]");

        BufferedImage outImage = new BufferedImage(image0.getWidth(), image0.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < image0.getWidth(); i++) {
            for (int j = 0; j < image1.getHeight(); j++) {
                int p1 = image0.getRGB(i,j);
                int p2 = image1.getRGB(i,j);
                outImage.setRGB(i,j, FastColor.ARGB32.multiply(p1,p2));
            }
        }
        return outImage;

    }

    @Override
    public boolean adjustTextureProvider(RootTextureProvider provider) {
        return provider.getTexture().length == 2;
    }

    @Override
    public String getName() {
        return "Texture Mix - Multiply";
    }
}
