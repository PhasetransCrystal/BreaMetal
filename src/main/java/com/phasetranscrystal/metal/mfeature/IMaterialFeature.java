package com.phasetranscrystal.metal.mfeature;

import com.phasetranscrystal.metal.Material;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.expansion.materialfeature.MetalMF;
import com.phasetranscrystal.metal.expansion.materialfeature.PhaseTransitMF;
import com.phasetranscrystal.metal.expansion.materialfeature.ThermoMF;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredHolder;

/**IMaterialFeature材料特征接口(MF)<br><p>
 * <font color="red">警告：此接口的实例在被创建后其内容不应被以任何形式进行任何更改，这有可能导致出现不可预料的严重问题。<br>
 * 为了确保正常运作，请将泛型I设置为您的实现的类。</font><br><p>
 * 材料特征的主要用途就是为同种材料添加共通的，不随物品形态而改变的特征：比如相变温度，热力学参数，导热率等一系列特征。
 * 可以将材料特征视为一种内嵌在材料(Material)中的，统一管理的数据接口，有点类似于capability的设计。<br>
 * 其中的内容可以通过材料层快速的判断有无，获取参数等。<br><p>
 * 除了对数据信息的保有外，其也可以作为单纯的特征标识使用。<br>
 * 同时，材料被允许存在的{@link MaterialItemType 物品类型(MIT)}也在其中被指定。
 * @see PhaseTransitMF PhaseTransitMF实现，这里存储材料的相变温度
 * @see ThermoMF ThermoMF实现，这用于处理材料的热力学属性，例如导热，比热等
 * @see MetalMF MetalMF实现，展示了MF的标志作用与物品类型(MIT)许可作用
 * @see Material 继续阅览。查看有关Material的详细信息
 * */
//TODO 正在考虑有关物品的外置特性部分 也许可以给物品加个feature override的组件？
public interface IMaterialFeature<I extends IMaterialFeature<I>> {

    /**返回实现类的实例*/
    default I getInstance(){return (I) this;}

    /**返回一个注册了的材料特征类型*/
    default MaterialFeatureType<I> getType(){
        return getTypeHolder().get();
    };

    DeferredHolder<MaterialFeatureType<?>,MaterialFeatureType<I>> getTypeHolder();
}
