package com.phasetranscrystal.metal;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Material材料<br><p>
 * 材料是材料系统的最前端对象，也是MF、MIT与ItemStack交互的中枢层级。<br>
 * 创建一个材料时，您可以为其添加材料特征实例，它们将被统一存储且内容不可变。<br><p>
 * 在创建实例后的初始化方法调用后，将自动生成一系列对应的表，包括材料包含的物品类型，特征类型对特征实例的映射等。您可以在适当的时机调用它们以获取信息<br><p>
 * 材质方面，物品模型的组装中，除了来自MIT的物品形状与叠加层外，就是来自材料的材质。其路径也与id绑定，对应为<br>
 * [NameSpace]/textures/brea/material/material/[Path].png<br>
 * 的材质。另外，材料系统可以将该种材料指定为中间产物(isIntermediateProduct)，这样它将不会尝试拉取材料材质，而是仅使用MIT的材质，并使用设定的颜色(x16color)进行着色。<br>
 * 注意：我们建议您在使用后一种方法时<font color="yellow">尽量使用少种类的颜色</font>，因为同种颜色动态生成的材质会被统一编入并调用，更多的颜色意味着更多的材质，更多的内存负担。
 *
 * @see BreaRegistries.MaterialReg 在Registries中查看系统的注册方法
 */
public class Material {
    public static final Logger LOGGER = LogManager.getLogger("BreaMetal:Material");
    public final int x16color;

    public final ImmutableMap<MaterialFeatureType<? extends IMaterialFeature<?>>, IMaterialFeature<?>> toFeature;//由特征类型向特征实例的映射表
    public final ImmutableSet<MaterialItemType> toTypes;//材料具有的所有物品类型

    public Material(ResourceLocation materialId, int x16color, IMaterialFeature<?>... fIns) {
        //材料标准颜色
        this.x16color = x16color;

        //材料特性收集
        HashMap<MaterialFeatureType<?>, IMaterialFeature<?>> features = new HashMap<>();
        for (IMaterialFeature<?> feature : fIns) {
            features.put(feature.getType(), feature);
        }
        ModBusConsumer.materialModifyCache.featureAttach.get(materialId).forEach(feature -> features.put(feature.getType(), feature));
        ModBusConsumer.materialModifyCache.featureRemove.get(materialId).forEach(features::remove);

        //材料依赖判定
        List<MaterialFeatureType<?>> typeExceptions = new ArrayList<>();
        Set<ResourceLocation> typeExceptionRequire = new HashSet<>();
        // 初始化 existingIds 为所有类型的 ID 集合（仅生成一次）
        Set<ResourceLocation> existingIds = features.keySet().stream().map(MaterialFeatureType::getLocation).collect(Collectors.toSet());

        boolean hasMissingDependencies;
        do {
            hasMissingDependencies = false;
            List<MaterialFeatureType<?>> toRemove = new ArrayList<>();

            // 检查每个类型的依赖
            for (MaterialFeatureType<?> type : features.keySet()) {
                for (ResourceLocation dependency : type.dependencies()) {
                    if (!existingIds.contains(dependency)) {
                        toRemove.add(type);
                        hasMissingDependencies = true;
                        typeExceptionRequire.add(dependency);
                    }
                }
            }

            // 更新 existingIds 并移除无效类型
            if (!toRemove.isEmpty()) {
                // 从 existingIds 中移除即将删除的类型的 ID
                toRemove.stream().map(MaterialFeatureType::getLocation).forEach(existingIds::remove);
                toRemove.forEach(features::remove);
                typeExceptions.addAll(toRemove);
            }
        } while (hasMissingDependencies);

        //如果存在依赖错误的情况 报错输出
        if(!typeExceptions.isEmpty()){
            typeExceptions.stream().map(MaterialFeatureType::getLocation).forEach(typeExceptionRequire::remove);
            LOGGER.warn("Material({}) removed features({}) with not existed dependencies. Required: {}", materialId, typeExceptions.toArray(), typeExceptionRequire);
        }

        //最终集合 生成数据
        this.toFeature = ImmutableMap.copyOf(features);
        this.toTypes = toFeature.keySet().stream().map(MaterialFeatureType::types).flatMap(ImmutableSet::stream).collect(ImmutableSet.toImmutableSet());

    }

    public Material(ResourceLocation materialId, IMaterialFeature<?>... fIns) {
        this(materialId, 0xEBEEF0, fIns);
    }

    /**获取某种材料特性的实例
     * @return 该特性的实例。当材料不具有该特性时，返回内容为空。*/
    @Nullable
    public <I extends IMaterialFeature<I>> IMaterialFeature<I> getFeature(MaterialFeatureType<I> handle) {
        return (IMaterialFeature<I>) toFeature.get(handle);
    }

    public boolean containsFeature(MaterialFeatureType<?> feature) {
        return toFeature.containsKey(feature);
    }

    public boolean containsType(MaterialItemType type) {
        return toTypes.contains(type);
    }

    //TODO
    //当返回值为null时，将会继续尝试使用对应的ItemType获取结果。
    public @Nullable ItemStack createItem(MaterialItemType type) {
//        if (getOrCreateTypes().contains(type)) {
//            return null;
//        }
        return new ItemStack(Items.AIR);
    }

    public ResourceKey<Material> getResourceKey() {
        return Registries.MATERIAL.getResourceKey(this).get();
    }

    public ResourceLocation getLocation() {
        return getResourceKey().location();
    }

    public String getTranslateKey(){
        return "brea.metal.material." + getLocation().toLanguageKey();
    }

    @Override
    public String toString() {
        return "BreaMetal-Material(" + getResourceKey() + ")";
    }

}
