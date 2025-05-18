package com.phasetranscrystal.metal.mfeature;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.NewRegistries;
import com.phasetranscrystal.metal.registry.ShortCircuitHolder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * MaterialFeatureType材料特征类型<br><p>
 * 材料特征类型是对{@link IMaterialFeature 材料特征(MaterialFeature)}的类型的指定。<br>
 * 这一指定在短路注册中被使用，用于处理与其它特征的依赖以及为材料生成物品类型列表。
 *
 * @see IMaterialFeature 继续浏览。查看MaterialFeature的详细信息
 */
//特征类型的编解码器，类对象，衍生材料物品类型，依赖的其它特征类型
//虽然说这个codec目前还没啥用 先放着吧）
public class MaterialFeatureType<I extends IMaterialFeature<I>> {
    public final Codec<I> codec;
    public final Class<I> clazz;
    public final ImmutableSet<? extends MaterialItemType> types;
    public final ImmutableSet<ResourceLocation> dependencies;

    public MaterialFeatureType(ResourceLocation idCache, Codec<I> codec, Class<I> clazz, ImmutableSet<ShortCircuitHolder<MaterialItemType, ? extends MaterialItemType>> types, ResourceLocation... dependencies) {
        this.codec = codec;
        this.clazz = clazz;
        this.types = types.stream().map(ShortCircuitHolder::value).collect(ImmutableSet.toImmutableSet());
        this.dependencies = ImmutableSet.copyOf(dependencies);
    }

    public MaterialFeatureType(ResourceLocation idCache, Codec<I> codec, Class<I> clazz, ShortCircuitHolder<MaterialItemType, ? extends MaterialItemType>... types) {
        this(idCache, codec, clazz, ImmutableSet.copyOf(types));
    }

    public ResourceKey<MaterialFeatureType<?>> getResourceKey() {
        return NewRegistries.MATERIAL_FEATURE.getResourceKey(this).get();
    }

    public ResourceLocation getLocation() {
        return getResourceKey().location();
    }

    @Override
    public String toString() {
        return "BreaMetal-MaterialFeatureType{key=" + getResourceKey() + ", types=" + types + ", dependencies=" + dependencies + "}";
    }

    public ImmutableSet<? extends MaterialItemType> types() {
        return types;
    }
}