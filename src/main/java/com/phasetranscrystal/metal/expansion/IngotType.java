package com.phasetranscrystal.metal.expansion;

import com.phasetranscrystal.metal.Material;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.datagen.ConsumerItemModelGen;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO
public class IngotType extends MaterialItemType {

    public static final Logger LOGGER = LogManager.getLogger("BREA:Material:MaterialItemType/Ingot");

//    public final HashMap<Material, IngotItem> WITH_MATERIAL = new HashMap<>();
//    public final HashMap<ResourceLocation, IngotItem> WITH_RESOURCE = new HashMap<>();

    public IngotType(float purity) {
        super(90, purity);
    }

    @Override
    public void secondaryRegistry(RegisterEvent event, Material material) {
//        Registry<Item> registry = (Registry<Item>) ModBusConsumer.REGS_MAP.get(Registries.ITEM);
//
//        if (material.id.toString().equals("brea:missing")) {
//            return;
//        }
//        IngotItem ingot = new IngotItem(material);
//        ResourceLocation location = material.id.withPath(id -> id + "_material_ingot");
//        Registry.register(registry, location, ingot);
//        WITH_MATERIAL.put(material, ingot);
//        WITH_RESOURCE.put(location, ingot);
    }

    @Override
    public void attachToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        super.attachToCreativeTab(event);
//        for (IngotItem item : WITH_RESOURCE.values()) {
//            event.accept(item);
//        }
    }
}
