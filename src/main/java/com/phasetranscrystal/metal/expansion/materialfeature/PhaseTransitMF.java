package com.phasetranscrystal.metal.expansion.materialfeature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phasetranscrystal.metal.BreaMetalRegistries;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.registry.ShortCircuitHolder;
import net.neoforged.neoforge.registries.DeferredHolder;

public class PhaseTransitMF implements IMaterialFeature<PhaseTransitMF> {
    public static final Codec<PhaseTransitMF> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("mp").forGetter(o -> o.mp),
            Codec.INT.fieldOf("bp").forGetter(o -> o.bp)
    ).apply(i, PhaseTransitMF::new));

    // mp | Melting point    | 熔点    | ℃
    // bp | Boiling point    | 沸点    | ℃
    public final int mp;
    public final int bp;

    public PhaseTransitMF(int meltingPoint, int boilingPoint) {
        this.mp = meltingPoint;
        this.bp = boilingPoint;
    }

    @Override
    public ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<PhaseTransitMF>> getTypeHolder() {
        return BreaMetalRegistries.PHASE_TRANSIT;
    }
}
