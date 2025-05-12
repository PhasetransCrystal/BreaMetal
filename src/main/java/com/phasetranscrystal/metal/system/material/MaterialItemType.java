package com.phasetranscrystal.metal.system.material;

import com.phasetranscrystal.metal.Registries;
import com.phasetranscrystal.metal.system.material.datagen.MitModelGen;
import com.phasetranscrystal.metal.system.material.expansion.IngotType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * MaterialItemType材料物品类型(MIT)<br><p>
 * 材料物品类型用于创建一个材料物品模板。比如说，一个“板”类型将可以被使用作铁板，铜板，金板等。<br><p>
 * 材料物品内包含三个必须设定的参数与一个会自动配置的物品容器。此容器用来获取其自动额外注册的物品，即使它们可能不被使用。<br>
 * 另外的三个参数分别是：<br>
 * --- {@link MaterialItemType#content} 指定该种物品包含多少单位的对应材料。<br>
 * --- {@link MaterialItemType#purity}  指定该种物品的纯度。纯度将与content数值相乘获得直接产量。<br>
 * ---  为该物品的id，也将会被用于获取材料的透明度图。<br>
 * 在默认提供的{@link TypedMaterialItem}中，最终材质的获取会先检查有无用json额外配置的材质，在不存在时再进行自动处理。<br><p>
 * 在材质方面，{@link MaterialItemType}的id指向该物品在使用material系统的模型时，其将会在:<br>
 * [NameSpace]/textures/brea/material/mit/[Path].png<br>
 * 下寻找形状层材质文件。mit_cover则是可选的，在与材料颜色组装完成后的叠加层文件。<br>
 *
 * @see IngotType IngotType中有关于一些预备方法的特别使用方法
 * @see MaterialFeatureType 继续浏览。查看MaterialFeatureType的详细信息
 */

//TODO
public class MaterialItemType {
    public final long content;
    public final float purity;

    public MaterialItemType(long content, float purity) {
        this.content = content;
        this.purity = purity;
    }

    /**
     * WARN:<br>
     * 如果您设置了自定义的全局注册或材料特性接口，您可能需要覆写此方法来保证获取的正确运行。
     */
    public @NonNull ItemStack createItem(Material material) {
//        ItemStack stack = getHolder();
//        StringTag tag = StringTag.valueOf(material.id.toString());
//        //TODO 啊---
//        stack.set(ModDataComponents.MATERIALS,material.id.toString());//.putString("material", material.id.toString());
//        return stack;
        return ItemStack.EMPTY;
    }


    /**
     * 注册<br>
     * 这一阶段，每一种拥有该物品类型的材料将会相应的调用一次此方法，以此来实现根据特殊材料进行额外的注册。<br>
     * 这一过程您可能需要相应的配置自定义的物品材料特征映射表。
     */
    public void secondaryRegistry(@Nullable RegisterEvent event, Material material) {

    }

    //收集数据生成信息，用于自动生成模型
    public void gatherKeyForDatagen(MitModelGen ins) {

//        //basic model
//        ins.getBuilder(getLocation().withPath(s -> "mit_basic/" + s).toString())
//                .parent(new ModelFile.UncheckedModelFile("item/generated"))
//                .texture("layer0", id.withPath(s -> "brea/material/mit/" + s));
//
//        ins.getBuilder(autoRegKey.location().toString())
//                .parent(new ModelFile.UncheckedModelFile("item/generated"));
    }

    //将物品添加至创造模式物品栏
    public void attachToCreativeTab(BuildCreativeModeTabContentsEvent event) {
//        ItemStack i = new ItemStack(BuiltInRegistries.ITEM.get(autoRegKey.location()));
//        ((ITypedMaterialObj) i.getItem()).setMaterial(i);
//        event.accept(i);
    }


    public ResourceKey<MaterialItemType> getResourceKey() {
        return Registries.MATERIAL_ITEM_TYPE.getResourceKey(this).get();
    }

    public ResourceLocation getLocation() {
        return getResourceKey().location();
    }

    public String getTranslateKey(){
        return "brea.metal.mit." + getLocation().toLanguageKey();
    }

    @Override
    public String toString() {
        return "BreaMetal-MaterialItemType{ id=" + getResourceKey() + ", content=" + content + ", purity=" + purity + "}";
    }
}
