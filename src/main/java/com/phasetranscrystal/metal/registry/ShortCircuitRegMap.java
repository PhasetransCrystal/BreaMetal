package com.phasetranscrystal.metal.registry;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.phasetranscrystal.metal.BreaMetal;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BreaMetal.MODID)
public class ShortCircuitRegMap {
    private static final Map<ResourceKey<? extends Registry<?>>, BiMap<ResourceLocation, ?>> cache = new HashMap<>();

    public static <T> T put(final ResourceKey<? extends Registry<T>> registry, final ResourceLocation key, final T value) {
        return ((BiMap<ResourceLocation, T>) cache.computeIfAbsent(registry, r -> HashBiMap.create())).put(key, value);
    }

    public static <T> T get(final ResourceKey<? extends Registry<T>> registry, final ResourceLocation key) {
        return (T) cache.getOrDefault(registry, ImmutableBiMap.of()).get(key);
    }

    public static <T> ResourceLocation get(final ResourceKey<? extends Registry<T>> registry, final T value) {
        return cache.getOrDefault(registry, ImmutableBiMap.of()).inverse().get(value);
    }

    public static <T> BiMap<ResourceLocation, T> getMap(final ResourceKey<? extends Registry<T>> registry) {
        return (BiMap<ResourceLocation, T>) cache.getOrDefault(registry, HashBiMap.create());
    }

    public static <T> Supplier<T> buildSupplier(final ResourceKey<? extends Registry<T>> registry, ResourceLocation id, final Function<ResourceLocation, ? extends T> defaultValue) {
        return Suppliers.memoize(() -> {
            var value = defaultValue.apply(id);
            put(registry, id, value);
            return value;
        });
    }

    @SubscribeEvent
    public static void clear(FMLCommonSetupEvent event) {
        cache.clear();
    }
}
