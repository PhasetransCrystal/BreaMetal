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
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashSet;
import java.util.Set;


/**
 * 用于收集材料信息与物品的映射，将会用于生成双向表或控制有关对象的生成。
 */
public abstract class MapMaterialItemEvent extends Event implements IModBusEvent {
    public final ImmutableBiMultiMap.Builder<ITypedMaterialObj, ItemLike> reflectMap = new ImmutableBiMultiMap.Builder<>();

    public ImmutableBiMultiMap.Builder<ITypedMaterialObj, ItemLike> getReflectMap() {
        return reflectMap;
    }

    public boolean addReflectMap(TypedMaterialInfo info, ItemLike item) {
        return addReflectMap(info, item, false);
    }

    public boolean addReflectMap(TypedMaterialInfo info, ItemLike item, boolean asMainResult) {
        if (reflectMap.contains(item)) return false;
        reflectMap.put(info, item);
        if (asMainResult) {
            reflectMap.setMainResult(info, item);
        }
        return true;
    }

    public boolean addReflectMap(Material material, MaterialItemType type, ItemLike item) {
        return addReflectMap(material, type, item, false);
    }

    public boolean addReflectMap(Material material, MaterialItemType type, ItemLike item, boolean asMainResult) {
        return addReflectMap(new TypedMaterialInfo(material, type), item, asMainResult);
    }


    /**
     * 发布于自动材料对象注册之前。内容可能会阻止并替代部分的TypedMaterialItem。<br>
     * 这些内容同样会自动进入映射表。
     */
    public static class Pre extends MapMaterialItemEvent {

    }

    /**
     * 此事件位于mod总线上，于注册表冻结后触发。此事件不可被取消。
     */
    public static class Post extends MapMaterialItemEvent {

    }
}
