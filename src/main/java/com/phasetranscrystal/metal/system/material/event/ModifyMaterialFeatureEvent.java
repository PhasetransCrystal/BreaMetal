package com.phasetranscrystal.metal.system.material.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.phasetranscrystal.metal.system.material.IMaterialFeature;
import com.phasetranscrystal.metal.system.material.MaterialFeatureType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Arrays;

/**此事件用于修饰材料具有的属性，具有添加/删除功能。在材料物品类型，材料特性类型注册完成，材料类型开始注册前触发<br>
 * 此事件于MOD总线上触发，不可被取消。<br>
 * 注：添加后有关的材料材质可能需要自主生成。
 * @see com.phasetranscrystal.metal.ModBusConsumer#registryEventConsumer(RegisterEvent) ModBusConsumer#registryEventConsumer
 * */
public class ModifyMaterialFeatureEvent extends Event implements IModBusEvent {
    //也可以用于覆盖原有的属性
    public final Multimap<ResourceLocation, IMaterialFeature<?>> featureAttach = HashMultimap.create();
    public final Multimap<ResourceLocation, MaterialFeatureType<?>> featureRemove = HashMultimap.create();

    public void addFeatureAttachment(final ResourceLocation location, final IMaterialFeature<?>... features) {
        if(features == null || features.length == 0) return;
        featureAttach.putAll(location, Arrays.asList(features));
    }

    public void removeFeatureAttachment(final ResourceLocation location, final MaterialFeatureType<?>... feature) {
        if(feature == null || feature.length == 0) return;
        featureRemove.putAll(location, Arrays.asList(feature));
    }
}
