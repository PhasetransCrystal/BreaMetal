package com.phasetranscrystal.metal.event;

import com.google.common.collect.HashMultimap;
import com.phasetranscrystal.metal.helper.ImmutableBiMultiMap;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.mitemtype.TypedMaterialInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于收集材料信息与物品的映射，在事件结束后会生成双向表。<br>
 * 此事件位于mod总线上，于注册表冻结后触发。此事件不可被取消。
 */
public class MapMaterialItemEvent extends Event implements IModBusEvent {
    public final ImmutableBiMultiMap.Builder<ITypedMaterialObj, Item> reflectMap = new ImmutableBiMultiMap.Builder<>();

    public ImmutableBiMultiMap.Builder<ITypedMaterialObj, Item> getReflectMap() {
        return reflectMap;
    }

    public boolean addReflectMap(TypedMaterialInfo info, Item item) {
        if(reflectMap.contains(item)) return false;
        reflectMap.put(info, item);
        return true;
    }

    public boolean addReflectMap(Material material, MaterialItemType type, Item item) {
        return addReflectMap(new TypedMaterialInfo(material, type), item);
    }
}
