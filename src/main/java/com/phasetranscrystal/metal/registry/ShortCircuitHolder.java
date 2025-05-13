package com.phasetranscrystal.metal.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ShortCircuitHolder<R, T extends R> extends DeferredHolder<R, T> {
    public final Supplier<T> value;

    protected ShortCircuitHolder(ResourceKey<? extends Registry<R>> registry, ResourceLocation key,  Supplier<T> value) {
        super(ResourceKey.create(registry, key));
        this.value = value;
    }

    @Override
    public T value() {
        return value.get();
    }
}
