package com.phasetranscrystal.metal.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashSet;
import java.util.Set;

public class TextureGenBlackListEvent extends Event implements IModBusEvent {
    public final Set<ResourceLocation> texturegenBlacklist = new HashSet<>();


    public boolean addTexturegenBlacklist(ResourceLocation itemId) {
        return texturegenBlacklist.add(itemId);
    }

    public boolean addTexturegenBlacklist(Item item) {
        return addTexturegenBlacklist(BuiltInRegistries.ITEM.getKey(item));
    }
}
