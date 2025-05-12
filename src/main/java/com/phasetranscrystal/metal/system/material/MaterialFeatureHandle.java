package com.phasetranscrystal.metal.system.material;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class MaterialFeatureHandle<I extends IMaterialFeature<I>> {
    public static final Logger LOGGER = LogManager.getLogger("BREA:Material/MFH");

    public final ResourceLocation id;
    public final Class<I> clazz;
    private ImmutableSet<MaterialItemType> typeSet;

    public MaterialFeatureHandle(ResourceLocation id, Class<I> clazz, MaterialItemType... types) {
        this.typeSet = ImmutableSet.copyOf(types);
        this.clazz = clazz;
        this.id = id;
    }

    public MaterialFeatureHandle(String modid, String path, Class<I> clazz) {
        this(ResourceLocation.fromNamespaceAndPath(modid, path), clazz);
    }
}
