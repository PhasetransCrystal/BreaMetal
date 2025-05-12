package com.phasetranscrystal.material;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = BreaMaterials.MODID)
public class DatagenRegistries {

    @SubscribeEvent
    public static void generateBreaMaterialModel(GatherDataEvent event) {
        //
    }

    public static void generateForMod(GatherDataEvent event, String modid){
        ItemModelProvider itemModelProvider = new ItemModelProvider() {
            @Override
            protected void registerModels() {
                Registries.MATERIAL.stream().filter(m -> m.id)
            }
        };
        event.getGenerator().
    }
}
