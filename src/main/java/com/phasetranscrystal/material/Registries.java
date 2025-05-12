package com.phasetranscrystal.material;

import com.mojang.serialization.Codec;
import com.phasetranscrystal.material.system.material.IMaterialFeature;
import com.phasetranscrystal.material.system.material.Material;
import com.phasetranscrystal.material.system.material.MaterialFeatureType;
import com.phasetranscrystal.material.system.material.MaterialItemType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Registries {
    public static final Logger LOGGER = LogManager.getLogger("BREA:Material:Registry");

    public static final Registry<MaterialItemType> MATERIAL_ITEM_TYPE = new RegistryBuilder<>(Keys.MATERIAL_ITEM_TYPE).sync(true).create();
    public static final Registry<MaterialFeatureType<?>> MATERIAL_FEATURE = new RegistryBuilder<>(Keys.MATERIAL_FEATURE).sync(true).create();
    public static final Registry<Material> MATERIAL = new RegistryBuilder<>(Keys.MATERIAL).sync(true).create();

    public static class Keys{
        public static final ResourceKey<Registry<MaterialItemType>> MATERIAL_ITEM_TYPE = create("material_item_type");
        public static final ResourceKey<Registry<MaterialFeatureType<?>>> MATERIAL_FEATURE = create("material_feature");
        public static final ResourceKey<Registry<Material>> MATERIAL = create("material");

        public static <T> ResourceKey<Registry<T>> create(String name){
            return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,name));
        }
    }
}
