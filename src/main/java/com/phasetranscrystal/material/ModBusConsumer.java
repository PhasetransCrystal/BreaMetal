package com.phasetranscrystal.material;


import com.phasetranscrystal.material.system.material.*;
import com.phasetranscrystal.material.system.material.datagen.MaterialReflectDataGatherEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = BreaMaterials.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusConsumer {
    public static final Map<ResourceKey<? extends Registry<?>>, Registry<?>> REGS_MAP = new HashMap<>();


    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(Registry$Material.MATERIAL_ITEM_TYPE);
        event.register(Registry$Material.MATERIAL_FEATURE);
        event.register(Registry$Material.MATERIAL);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void reg(RegisterEvent event) {
        REGS_MAP.put(event.getRegistryKey(), event.getRegistry());
    }

    @SubscribeEvent
    public static void attachMaterialData(MaterialReflectDataGatherEvent event) {
        event.handler.registryReflectItemMaterialInfo(new Holder.Direct<>(Items.COAL), BreaRegistries.MaterialReg.LIGNITE.getId(), BreaRegistries.MaterialReg.COMBUSTIBLE_TYPE.getId());
    }

//    @Mod.EventBusSubscriber(modid = BreakdownCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class Client {
////        @SubscribeEvent
////        public static void resourceReload(RegisterClientReloadListenersEvent event){
////            event.registerReloadListener(MaterialAtlasManager.init());
////        }
////
////        @SubscribeEvent
////        public static void renderTypesRegistry(RegisterNamedRenderTypesEvent event){
////            event.register(new ResourceLocation(BreakdownCore.MODID,"material"), RenderType.translucent(), MaterialAtlasManager.RENDER_TYPE);
////        }
//    }
}
