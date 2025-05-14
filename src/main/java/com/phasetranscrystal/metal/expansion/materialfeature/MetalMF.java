package com.phasetranscrystal.metal.expansion.materialfeature;

import com.mojang.serialization.Codec;
import com.phasetranscrystal.metal.BreaMetalRegistries;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.registry.ShortCircuitHolder;

public class MetalMF implements IMaterialFeature<MetalMF> {
    public static final Codec<MetalMF> CODEC = Codec.unit(MetalMF::new);

    public MetalMF(){}

    @Override
    public ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<MetalMF>> getTypeHolder() {
        return BreaMetalRegistries.METAL;
    }
}
