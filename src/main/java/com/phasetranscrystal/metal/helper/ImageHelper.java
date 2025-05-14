package com.phasetranscrystal.metal.helper;

import net.minecraft.util.FastColor;

import java.awt.image.BufferedImage;

//在NativeImage中的RGBA颜色，所有你能看到的，不管是获取到的还是要设置的
//都是他妈的ABGR
//我很怀疑写这东西的时候的人的精神状态
public class ImageHelper {
//    public static BufferedImage firstFrame(SpriteContents contents){
//        BufferedImage image = new BufferedImage(contents.width(),contents.height(),false);
//        boolean animate = contents.animatedTexture != null;
//
//        contents.originalImage.copyRect(image, 0, 0,0,0,contents.width(),contents.height(),false,false);
//        return image;
//    }

//    public static List<BufferedImage> scaleToSame(List<BufferedImage> images, boolean alignX){
//        int clm = MathHelper.lcm(images.stream().map(alignX ? BufferedImage::getWidth : BufferedImage::getHeight).toList());
//        List<BufferedImage> imgs = new ArrayList<>();
//        for(BufferedImage image : images){
//            int s = clm / (alignX ? image.getWidth() : image.getHeight());
//            imgs.add(scale(image,s));
//        }
//        return imgs;
//    }
//
//    public static BufferedImage scale(BufferedImage image,float xScale,float yScale){
//        return scale(image,(int)(image.getWidth() * xScale),(int)(image.getHeight() * yScale));
//    }
//
//    public static BufferedImage scale(BufferedImage image,int width,int height){
//        BufferedImage i = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
//        image.resizeSubRectTo(0,0,image.getWidth(),image.getHeight(),i);
//        return i;
//    }

//    public static BufferedImage scale(BufferedImage image,float scale){
//        if(scale == 1) {
//            return copy(image);
//        }
//        return scale(image, scale, scale);
//    }

//    public static BufferedImage copy(BufferedImage image){
//        BufferedImage i = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
//        i.copyFrom(image);
//        return i;
//    }

//    public static BufferedImage withColor(int x, int y, int colorABGR){
//        BufferedImage i = new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
//        i.fillRect(0,0,x - 1,y - 1,colorABGR);
//        return i;
//    }

//    public static SpriteContents pack(BufferedImage image){
//        return new SpriteContents(ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID,"invalid"),new FrameSize(image.getWidth(),image.getHeight()),image, ResourceMetadata.EMPTY);
//    }

    public static BufferedImage colorCombineHandle(BufferedImage a, BufferedImage b, Color.ColorHandle handle) {
        BufferedImage copied = new BufferedImage(a.getWidth(), a.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < a.getWidth(); x++) {
            for (int y = 0; y < a.getHeight(); y++) {
                int color = a.getRGB(x, y);
                copied.setRGB(x, y, handle.handle(color, b.getRGB(x, y)));
            }
        }
        return copied;
    }

    public static BufferedImage alphaFilter(BufferedImage original, BufferedImage alpha) {
        return colorCombineHandle(original, alpha, Color::alphaFilter);
    }

    public static BufferedImage blend(BufferedImage background, BufferedImage foreground) {
        return colorCombineHandle(background, foreground, Color::blend);
    }

    public static class Color {
        public static int invert(int x16rgbaColor) {
            return ((x16rgbaColor & 0xff) << 24) | ((x16rgbaColor & 0xff00) << 8) | ((x16rgbaColor & 0xff0000) >>> 8) | ((x16rgbaColor >>> 24) & 0xff);
        }

        public static int blend(int backgroundColor, int foregroundColor) {

            // 将Alpha值标准化到[0, 1]范围
            float alphaBackground = getColorByte(backgroundColor, 0) / 255.0f;
            float alphaForeground = getColorByte(foregroundColor, 0) / 255.0f;

            // 对每个颜色分量执行Alpha混合
            int finalRed = (int) ((alphaForeground * getColorByte(foregroundColor, 3)) + ((1 - alphaForeground) * alphaBackground * getColorByte(backgroundColor, 3)));
            int finalGreen = (int) ((alphaForeground * getColorByte(foregroundColor, 2)) + ((1 - alphaForeground) * alphaBackground * getColorByte(backgroundColor, 2)));
            int finalBlue = (int) ((alphaForeground * getColorByte(foregroundColor, 1)) + ((1 - alphaForeground) * alphaBackground * getColorByte(backgroundColor, 1)));
            int finalAlpha = (int) (255 * (alphaForeground + (1 - alphaForeground) * alphaBackground));

            // 将混合后的分量重新组合成一个整数
            return (finalAlpha << 24) | (finalBlue << 16) | (finalGreen << 8) | finalRed;
        }

        public static int alphaFilter(int original, int alpha) {
            alpha = FastColor.ARGB32.alpha(alpha);
            return original & 0x00FFFFFF | alpha << 24;
        }

        //A=0 B=1 G=2 R=3
        public static int getColorByte(int color, int index) {
            if (index < 0 || index > 3) {
                throw new IllegalArgumentException("index must in range[0,3].");
            }
            return color >>> 24 - 8 * index & 0xFF;
        }

        public interface ColorHandle {
            int handle(int a, int b);
        }
    }


}
