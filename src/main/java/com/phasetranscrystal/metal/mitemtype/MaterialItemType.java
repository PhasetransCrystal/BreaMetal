package com.phasetranscrystal.metal.mitemtype;

import com.phasetranscrystal.metal.*;
import com.phasetranscrystal.metal.datagen.CompoundClientDatagen;
import com.phasetranscrystal.metal.expansion.IngotType;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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
public class MaterialItemType {
    public final long content;
    public final float purity;

    public MaterialItemType(ResourceLocation idCache, long content, float purity) {
        this.content = content;
        this.purity = purity;
    }

    /**
     * 注册<br>
     * 这一阶段，每一种拥有该物品类型的材料将会相应的调用一次此方法，以此来实现根据特殊材料进行额外的注册。<br>
     * 这一过程您可能需要相应的配置自定义的物品材料特征映射表。
     */
    public void registryBootstrap(Material material) {
        Registry<Item> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) net.minecraft.core.registries.Registries.ITEM);
        if(isRegistryPrevented(material)) return;

        Item item = new TypedMaterialItem(this, material);
        Registry.register(registry,
                getLocation().withPrefix(material.getLocation().getNamespace() + "_" + material.getLocation().getPath() + "_"),
                item
        );
        BreaMetal.addCreativeTabStack(new ItemStack(item));
    }

    //收集数据生成信息，用于自动生成模型
    public void gatherKeyForDatagen(CompoundClientDatagen datagen, GatherDataEvent event, Material material, ResourceLocation itemLocation) {
        if (material == null || BreaMetal.isTexturegenBlacklist(itemLocation)) return;

        String texturePath = defaultTextureKey(material, itemLocation);

        //TODO 美工 加油）
//        datagen.getAlphaFilterProvider().addCombination(this, material, ResourceLocation.parse(texturePath));
        datagen.getMultiplyMixProvider().addCombination(this, material, ResourceLocation.parse(texturePath));

        datagen.getItemModelGen().addConsumer(g -> g.getBuilder(itemLocation.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texturePath));

    }

    public boolean isRegistryPrevented(Material material) {
        return BreaMetal.isMaterialItemRemap(material, this);
    }

    public String defaultTextureKey(Material material, ResourceLocation itemLocation) {
        return BreaMetal.MODID + ":item/mi_common/" + getLocation().getNamespace() + "_" + getLocation().getPath() + "/" +
                material.getLocation().getNamespace() + "_" + material.getLocation().getPath();
    }

    public ResourceKey<MaterialItemType> getResourceKey() {
        return NewRegistries.MATERIAL_ITEM_TYPE.getResourceKey(this).get();
    }

    public ResourceLocation getLocation() {
        return getResourceKey().location();
    }

    public String getTranslateKey() {
        return "brea.metal.mit." + getLocation().toLanguageKey();
    }

    @Override
    public String toString() {
        return "BreaMetal-MaterialItemType{ id=" + getResourceKey() + ", content=" + content + ", purity=" + purity + "}";
    }
}
