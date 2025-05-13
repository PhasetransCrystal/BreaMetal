package com.phasetranscrystal.metal.registry;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ShortCircuitDeferredRegistry<T> extends DeferredRegister<T> {
    private Supplier<T> cache;

    protected ShortCircuitDeferredRegistry(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        super(registryKey, namespace);
    }

    @Override
    public <I extends T> ShortCircuitHolder<T, I> register(String name, Supplier<? extends I> sup) {
        return register(name, r -> sup.get());
    }

    @Override
    public <I extends T> ShortCircuitHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
        cache = Suppliers.memoize(() -> func.apply(ResourceLocation.fromNamespaceAndPath(getNamespace(), name)));
        return (ShortCircuitHolder<T, I>) super.register(name, rl -> (I) cache.get());
    }

    @Override
    protected <I extends T> ShortCircuitHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
        return new ShortCircuitHolder<>(registryKey, key, (Supplier<I>) cache);
    }
}
