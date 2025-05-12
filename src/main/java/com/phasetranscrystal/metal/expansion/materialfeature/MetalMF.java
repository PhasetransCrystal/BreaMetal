package com.phasetranscrystal.metal.expansion.materialfeature;

import com.mojang.serialization.Codec;
import com.phasetranscrystal.metal.BreaRegistries;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;

public class MetalMF implements IMaterialFeature<MetalMF> {
    public static final Codec<MetalMF> CODEC = Codec.unit(MetalMF::new);

    public MetalMF(){}

    @Override
    public MaterialFeatureType<MetalMF> getType() {
//        return Registries.MaterialReg.METAL;
        return BreaRegistries.MaterialReg.METAL.get();
    }
}
