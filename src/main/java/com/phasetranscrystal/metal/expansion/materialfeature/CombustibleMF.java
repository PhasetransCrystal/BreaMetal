package com.phasetranscrystal.metal.expansion.materialfeature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phasetranscrystal.metal.BreaMetalRegistries;
import com.phasetranscrystal.metal.mfeature.IMaterialFeature;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.registry.ShortCircuitHolder;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CombustibleMF implements IMaterialFeature<CombustibleMF> {
    public static final Codec<CombustibleMF> CODEC = Codec.withAlternative(Codec.LONG.xmap(CombustibleMF::new, i -> i.q), RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("cv").forGetter(o -> 0),//不支持通过此方式编码内容
            Codec.INT.fieldOf("density").forGetter(o -> 0)
    ).apply(instance, CombustibleMF::new)));
    /**
     * <table border="1">
     *   <caption>数据单位对照</caption>
     *   <tr>
     *     <th>变量名</th>
     *     <th>英文名称</th>
     *     <th>中文名称</th>
     *     <th>单位</th>
     *   </tr>
     *   <tr>
     *     <td>cv</td>
     *     <td>Calorific Value</td>
     *     <td>热值</td>
     *     <td>kJ/kg</td>
     *   </tr>
     *   <tr>
     *     <td>density</td>
     *     <td>Density</td>
     *     <td>密度</td>
     *     <td>kg/m³</td>
     *   </tr>
     *   <tr>
     *     <td>heat</td>
     *     <td>Heat</td>
     *     <td>总热量</td>
     *     <td>kJ</td>
     *   </tr>
     *   <tr>
     *     <td>q</td>
     *     <td>Combustion Internal Energy</td>
     *     <td>燃烧内能</td>
     *     <td>kJ/mB</td>
     *   </tr>
     * </table>
     */
    public final long q;

    public CombustibleMF(int cv, int density) {
        this.q = (long) cv * density / 1000;
    }

    protected CombustibleMF(long q) {
        this.q = q;
    }

    @Override
    public ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<CombustibleMF>> getTypeHolder() {
        return BreaMetalRegistries.COMBUSTIBLE;
    }
}
