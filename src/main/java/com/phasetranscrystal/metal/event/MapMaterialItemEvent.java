package com.phasetranscrystal.metal.event;

import com.phasetranscrystal.metal.mitemtype.TypedMaterialInfo;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.LinkedHashMap;

/**
 * 用于收集材料信息与物品的映射，在事件结束后会生成双向表。<br>
 * 此事件位于mod总线上，于注册表冻结后触发。此事件不可被取消。
 */
public class MapMaterialItemEvent extends Event implements IModBusEvent {
    public final LinkedHashMap<TypedMaterialInfo, Item> reflectMap = new LinkedHashMap<>();

    public MapMaterialItemEvent() {
    }

    public LinkedHashMap<TypedMaterialInfo, Item> getReflectMap() {
        return reflectMap;
    }

    public Item addReflectMap(TypedMaterialInfo info, Item item) {
        return reflectMap.put(info, item);
    }

}
