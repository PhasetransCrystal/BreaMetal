package com.phasetranscrystal.metal.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ShortCircuitRegistry<T> extends DeferredRegister<T> {
    private Supplier<T> cache;

    protected ShortCircuitRegistry(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        super(registryKey, namespace);
    }

    @Override
    public <I extends T> ShortCircuitHolder<T, I> register(String name, Supplier<? extends I> sup) {
        return register(name, r -> sup.get());
    }

    @Override
    public <I extends T> ShortCircuitHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        cache = ShortCircuitRegMap.buildSupplier(this.getRegistryKey(), ResourceLocation.fromNamespaceAndPath(this.getNamespace(), name), func);
        return (ShortCircuitHolder<T, I>) super.register(name, rl -> (I) cache.get());
    }

    @Override
    protected <I extends T> ShortCircuitHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
        return new ShortCircuitHolder<>(registryKey, key, (Supplier<I>) cache);
    }

    public static <T> ShortCircuitRegistry<T> create(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return new ShortCircuitRegistry<>(registryKey, namespace);
    }

    public static <T> ShortCircuitRegistry<T> create(Registry<T> registry, String namespace) {
        return new ShortCircuitRegistry<>(registry.key(), namespace);
    }

    public static <B> ShortCircuitRegistry<B> create(ResourceLocation registryName, String modid) {
        return new ShortCircuitRegistry<>(ResourceKey.createRegistryKey(registryName), modid);
    }
}
