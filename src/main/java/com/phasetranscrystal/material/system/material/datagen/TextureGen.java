package com.phasetranscrystal.material.system.material.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TextureGen {
    public static final int IMAGE_XY = 16;
    public static void textureMix(ResourceLocation sourceTexture, Color argb, ResourceLocation outTexture) throws IOException {
        BufferedImage image = textureFileLoad(convertToFilePath(sourceTexture));
        BufferedImage outImage = new BufferedImage(IMAGE_XY,IMAGE_XY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 16 ; i++){
            for(int j = 0; j < 16 ; j++){
                int p = image.getRGB(i,j);
                outImage.setRGB(i,j,FastColor.ARGB32.multiply(p,argb.getRGB()));
            }
        }
        textureFileSave(outImage, convertToFilePath(outTexture));
    }
    public static void textureMix(ResourceLocation sourceTexture, ResourceLocation mixTexture, ResourceLocation outTexture) throws IOException{
        BufferedImage image = textureFileLoad(convertToFilePath(sourceTexture));
        BufferedImage mixImage = textureFileLoad(convertToFilePath(mixTexture));
        BufferedImage outImage = new BufferedImage(IMAGE_XY,IMAGE_XY,BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < 16 ; i++){
            for(int j = 0; j < 16 ; j++){
                int p1 = image.getRGB(i,j);
                int p2 = mixImage.getRGB(i,j);
                outImage.setRGB(i,j,FastColor.ARGB32.multiply(p1,p2));
            }
        }
        textureFileSave(outImage, convertToFilePath(outTexture));
    }
    private static BufferedImage textureFileLoad(String textureFilePath) throws IOException
    {
        BufferedImage image = null;
        try{
            File input = new File(textureFilePath);
            image = new BufferedImage(IMAGE_XY,IMAGE_XY,BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(input);
            return image;
        }
        catch (IOException e){
        }
        return null;

    }
    private static void textureFileSave(BufferedImage image, String textureFilePath) throws IOException{
        try{
            File output = new File(textureFilePath);
            ImageIO.write(image,"png",output);
        }
        catch (IOException e){
        }
    }
    public static String convertToFilePath(ResourceLocation resourceLocation) {
        ModContainer modContainer = ModList.get()
                .getModContainerById(resourceLocation.getNamespace())
                .orElseThrow(() -> new IllegalArgumentException("Mod not found: " + resourceLocation.getNamespace()));
        //模组文件根路径（
        Path modRoot = modContainer.getModInfo().getOwningFile().getFile().getFilePath();
        //相对路径
        Path resourcePath = Path.of("assets", resourceLocation.getNamespace(), "textures",resourceLocation.getNamespace() , resourceLocation.getPath());
        Path fullPath = modRoot.resolve(resourcePath);
        return fullPath.toString();
    }
}
