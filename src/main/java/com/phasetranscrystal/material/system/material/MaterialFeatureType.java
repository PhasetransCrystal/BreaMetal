package com.phasetranscrystal.material.system.material;

import com.mojang.serialization.Codec;
import com.phasetranscrystal.material.BreaRegistries;
import com.phasetranscrystal.material.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

/**
 * MaterialFeatureType材料特征类型<br><p>
 * 材料特征类型是对{@link IMaterialFeature 材料特征(MaterialFeature)}的类型的包装。<br>
 * 这一包装在注册中被使用，同时也用于快速的将特征类型与特征实例进行相互对应。
 *
 * @see BreaRegistries.MaterialReg#feature(String, Class)  在Registries中查看MF的注册方法
 * @see IMaterialFeature 继续浏览。查看MaterialFeature的详细信息
 */
public record MaterialFeatureType<I extends IMaterialFeature<I>>(Codec<I> codec, HashSet<? extends MaterialItemType> types, Class<I> clazz, ResourceLocation... dependencies) {
    public ResourceKey<MaterialFeatureType<?>> getResourceKey() {
        return Registries.MATERIAL_FEATURE.getResourceKey(this).get();
    }

    @Override
    public String toString() {
        return "BreaMetal-MaterialFeatureType{key=" + getResourceKey() + ", types=" + types + ", dependencies=" + Arrays.toString(dependencies) + "}";
    }
}