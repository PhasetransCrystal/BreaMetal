package com.phasetranscrystal.metal;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = BreaMetal.MODID)
public class RegistrationShortCircuit {
    public static final BiMap<ResourceLocation, MaterialItemType> MIT = HashBiMap.create();
    public static final BiMap<ResourceLocation, MaterialFeatureType<?>> FEATURES = HashBiMap.create();
    public static final BiMap<ResourceLocation, Material> MATERIAL = HashBiMap.create();

    public static MaterialItemType getMaterialItemType(Holder<MaterialItemType> holder) {
        return MIT.get(holder.getKey().location());
    }

    public static MaterialFeatureType<?> getFeatureType(IMaterialFeature<?> feature) {
        return FEATURES.get(feature.getTypeHolder().getId());
    }


    @SubscribeEvent
    public static void clear(FMLCommonSetupEvent event){
//        MIT.clear();
//        FEATURES.clear();
//        MATERIAL.clear();
    }
}
