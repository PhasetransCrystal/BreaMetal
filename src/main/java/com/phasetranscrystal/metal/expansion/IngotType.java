package com.phasetranscrystal.metal.expansion;

import com.phasetranscrystal.metal.BreaMetalRegistries;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IngotType extends MaterialItemType {

    public static final Logger LOGGER = LogManager.getLogger("BreaMetal:MaterialItemType/Ingot");


    public IngotType(ResourceLocation location, float purity) {
        super(location, 90, purity);
    }

    //TODO for test only
    @Override
    public void registryBootstrap(Material material) {
        if (!material.equals(BreaMetalRegistries.IRON.get())) {
            super.registryBootstrap(material);
        }
    }
}
