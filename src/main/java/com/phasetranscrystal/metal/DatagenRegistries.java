package com.phasetranscrystal.metal;

import com.phasetranscrystal.metal.datagen.CompoundClientDatagen;
import com.phasetranscrystal.metal.datagen.MaterialLanguageList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD,modid = BreaMetal.MODID)
public class DatagenRegistries {

    @SubscribeEvent
    public static void generateBreaMaterialModel(GatherDataEvent event) {
        generateForMod(event, BreaMetal.MODID);
    }

    public static void generateForMod(GatherDataEvent event, String modid){
        CompoundClientDatagen dataGen = new CompoundClientDatagen(event, modid);
        MaterialLanguageList.TranGen();
        if (event.includeClient()) {
            dataGen.getLanguageProviderZH("zh_cn");
            dataGen.getLanguageProviderEN("en_us");
        }
        dataGen.build();
    }
}
