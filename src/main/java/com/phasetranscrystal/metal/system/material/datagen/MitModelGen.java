package com.phasetranscrystal.metal.system.material.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.module.datagen.ExpandItemModelProvider;
import com.phasetranscrystal.metal.system.material.MaterialItemType;
import com.phasetranscrystal.metal.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MitModelGen extends ExpandItemModelProvider {
    public MitModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BreaMetal.MODID, existingFileHelper);
    }

    @Override
    public void registerModels() {
        super.registerModels();
        for(MaterialItemType type : Registries.MATERIAL_ITEM_TYPE){
            type.gatherKeyForDatagen(this);
        }
    }
}
