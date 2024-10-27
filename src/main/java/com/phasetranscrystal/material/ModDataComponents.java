package com.phasetranscrystal.material;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, BreaMaterials.MODID);


    public static final DeferredHolder<DataComponentType<?>,DataComponentType<String>> MATERIALS = register(
            "material", builder -> builder.persistent(Codec.STRING)
    );


    /* 注册一个自己的DataComponentType
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<MyCustomData>> CUSTOM_DATA = register(
            "my_custom_data", builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );*/

    private static <T> DeferredHolder<DataComponentType<?>,DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENTS.register(name,()->  builder.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus){
        DATA_COMPONENTS.register(eventBus);
    }
}