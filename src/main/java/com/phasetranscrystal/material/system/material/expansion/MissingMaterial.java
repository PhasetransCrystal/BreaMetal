package com.phasetranscrystal.material.system.material.expansion;

import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.material.BreaMaterials;
import com.phasetranscrystal.material.system.material.Material;
import com.phasetranscrystal.material.system.material.MaterialItemType;
import com.phasetranscrystal.material.Registries;
import net.minecraft.resources.ResourceLocation;

public final class MissingMaterial extends Material {
    private ImmutableSet<MaterialItemType> types;

    public MissingMaterial() {
        super(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,"missing"),false);
    }

    @Override
    public ImmutableSet<MaterialItemType> getOrCreateTypes() {
        if(types == null){
            types = ImmutableSet.copyOf(Registries.MATERIAL_ITEM_TYPE);
        }
        return types;
    }
}
