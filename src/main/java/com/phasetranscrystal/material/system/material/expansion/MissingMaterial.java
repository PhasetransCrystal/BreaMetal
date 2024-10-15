package com.phasetranscrystal.material.system.material.expansion;

import com.google.common.collect.ImmutableSet;
import com.landis.breakdowncore.BreakdownCore;
import com.phasetranscrystal.material.system.material.Material;
import com.phasetranscrystal.material.system.material.MaterialItemType;
import com.phasetranscrystal.material.system.material.Registry$Material;
import net.minecraft.resources.ResourceLocation;

public final class MissingMaterial extends Material {
    private ImmutableSet<MaterialItemType> types;

    public MissingMaterial() {
        super(new ResourceLocation(BreakdownCore.MODID,"missing"),false);
    }

    @Override
    public ImmutableSet<MaterialItemType> getOrCreateTypes() {
        if(types == null){
            types = ImmutableSet.copyOf(Registry$Material.MATERIAL_ITEM_TYPE);
        }
        return types;
    }
}
