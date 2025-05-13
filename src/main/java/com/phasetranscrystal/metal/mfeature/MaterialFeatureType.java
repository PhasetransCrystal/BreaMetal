package com.phasetranscrystal.metal.mfeature;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.NewRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;

/**
 * MaterialFeatureType材料特征类型<br><p>
 * 材料特征类型是对{@link IMaterialFeature 材料特征(MaterialFeature)}的类型的包装。<br>
 * 这一包装在注册中被使用，同时也用于快速的将特征类型与特征实例进行相互对应。
 *
 * @see IMaterialFeature 继续浏览。查看MaterialFeature的详细信息
 */
//特征类型的编解码器，类对象，衍生材料物品类型，依赖的其它特征类型
//虽然说这个codec目前还没啥用 先放着吧）
public record MaterialFeatureType<I extends IMaterialFeature<I>>(Codec<I> codec, Class<I> clazz,
                                                                 ImmutableSet<? extends MaterialItemType> types,
                                                                 ResourceLocation... dependencies) {
    public MaterialFeatureType(Codec<I> codec, Class<I> clazz, MaterialItemType... types) {
        this(codec, clazz, ImmutableSet.copyOf(types));
    }

    public ResourceKey<MaterialFeatureType<?>> getResourceKey() {
        return NewRegistries.MATERIAL_FEATURE.getResourceKey(this).get();
    }

    public ResourceLocation getLocation() {
        return getResourceKey().location();
    }

    @Override
    public String toString() {
        return "BreaMetal-MaterialFeatureType{key=" + getResourceKey() + ", types=" + types + ", dependencies=" + Arrays.toString(dependencies) + "}";
    }
}