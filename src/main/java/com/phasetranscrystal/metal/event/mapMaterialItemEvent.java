package com.phasetranscrystal.metal.event;

import com.phasetranscrystal.metal.mitemtype.TypedMaterialInfo;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.Map;

public class mapMaterialItemEvent extends Event implements IModBusEvent {
    public final Map<TypedMaterialInfo, Item>
}
