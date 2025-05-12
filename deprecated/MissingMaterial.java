package com.phasetranscrystal.metal.system.material.expansion;

import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.system.material.Material;
import com.phasetranscrystal.metal.system.material.MaterialItemType;
import com.phasetranscrystal.metal.Registries;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true)
public final class MissingMaterial extends Material {
    private ImmutableSet<MaterialItemType> types;

    public MissingMaterial() {
        super(ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID,"missing"));
    }

//    @Override
    public ImmutableSet<MaterialItemType> getOrCreateTypes() {
        if(types == null){
            types = ImmutableSet.copyOf(Registries.MATERIAL_ITEM_TYPE);
        }
        return types;
    }
}
