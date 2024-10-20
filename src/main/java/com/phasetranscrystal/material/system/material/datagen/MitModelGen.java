package com.phasetranscrystal.material.system.material.datagen;

import com.phasetranscrystal.material.BreaMaterials;
import com.phasetranscrystal.material.module.datagen.ExpandItemModelProvider;
import com.phasetranscrystal.material.system.material.MaterialItemType;
import com.phasetranscrystal.material.system.material.Registry$Material;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MitModelGen extends ExpandItemModelProvider {
    public MitModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BreaMaterials.MODID, existingFileHelper);
    }

    @Override
    public void registerModels() {
        super.registerModels();
        for(MaterialItemType type : Registry$Material.MATERIAL_ITEM_TYPE){
            type.gatherKeyForDatagen(this);
        }
    }
}
