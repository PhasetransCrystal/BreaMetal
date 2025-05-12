package com.phasetranscrystal.metal;

import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.expansion.CombustibleType;
import com.phasetranscrystal.metal.expansion.IngotType;
import com.phasetranscrystal.metal.expansion.materialfeature.CombustibleMF;
import com.phasetranscrystal.metal.expansion.materialfeature.MetalMF;
import com.phasetranscrystal.metal.expansion.materialfeature.PhaseTransitMF;
import com.phasetranscrystal.metal.expansion.materialfeature.ThermoMF;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;

public class BreaRegistries {
    public static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, BreaMetal.MODID);
    public static final DeferredRegister<Item> ITEM = DeferredRegister.createItems(BreaMetal.MODID);
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPE = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, BreaMetal.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BREA_TAB = TAB.register("default", () -> CreativeModeTab.builder().displayItems((parameters, output) -> {
        for (DeferredHolder<Item, ? extends Item> i : ITEM.getEntries()) {
            output.accept((DeferredItem<Item>) i);
        }
    }).title(Component.translatable("tab.brea.default")).icon(() -> new ItemStack(Items.COMMAND_BLOCK)).build());


    /**
     * 自定义的材料系统的注册。
     */

    public static class MaterialReg {
        public static final DeferredRegister<MaterialItemType> TYPE = DeferredRegister.create(Registries.MATERIAL_ITEM_TYPE, BreaMetal.MODID);
        public static final DeferredRegister<MaterialFeatureType<?>> FEATURE = DeferredRegister.create(Registries.MATERIAL_FEATURE, BreaMetal.MODID);
        public static final DeferredRegister<Material> MATERIAL = DeferredRegister.create(Registries.MATERIAL, BreaMetal.MODID);

        public static <I extends Material> DeferredHolder<Material, I> material(String id, Function<ResourceLocation, I> provider) {
            return MATERIAL.register(id, () -> provider.apply(ResourceLocation.fromNamespaceAndPath(MATERIAL.getNamespace(), id)));
        }

        public static final DeferredHolder<MaterialItemType, IngotType> INGOT = TYPE.register("ingot", () -> new IngotType(90));
        public static final DeferredHolder<MaterialItemType, CombustibleType> COMBUSTIBLE_TYPE = TYPE.register("combustible", CombustibleType::new);


        public static final DeferredHolder<MaterialFeatureType<?>, MaterialFeatureType<PhaseTransitMF>> PHASE_TRANSIT = FEATURE.register("phase_transit", () -> new MaterialFeatureType<>(PhaseTransitMF.CODEC, PhaseTransitMF.class));//TODO 添加气态与液态支持
        public static final DeferredHolder<MaterialFeatureType<?>, MaterialFeatureType<ThermoMF>> THERMO = FEATURE.register("thermo", () -> new MaterialFeatureType<>(ThermoMF.CODEC, ThermoMF.class));
        public static final DeferredHolder<MaterialFeatureType<?>, MaterialFeatureType<MetalMF>> METAL = FEATURE.register("metal", () -> new MaterialFeatureType<>(MetalMF.CODEC, MetalMF.class, ImmutableSet.of(BreaRegistries.MaterialReg.INGOT.get()), BreaRegistries.MaterialReg.PHASE_TRANSIT.getId(), BreaRegistries.MaterialReg.THERMO.getId()));
        public static final DeferredHolder<MaterialFeatureType<?>, MaterialFeatureType<CombustibleMF>> COMBUSTIBLE = FEATURE.register("combustible", () -> new MaterialFeatureType<>(CombustibleMF.CODEC, CombustibleMF.class, BreaRegistries.MaterialReg.COMBUSTIBLE_TYPE.get()));

        public static final DeferredHolder<Material, Material> IRON = material("iron", r -> new Material(r, new MetalMF(), new PhaseTransitMF(1539, 3000), new ThermoMF(0.46F, 80.9F, 7.87F)));
        public static final DeferredHolder<Material, Material> LIGNITE = material("lignite", r -> new Material(r, new CombustibleMF(18000, 1150)));
    }

    public static class JsonCodecReg {
        public static final DeferredRegister<LootPoolEntryType> LOOT_POOL = DeferredRegister.create(net.minecraft.core.registries.Registries.LOOT_POOL_ENTRY_TYPE, BreaMetal.MODID);

        //public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> MATERIAL_ITEM_LOOT_POOL = LOOT_POOL.register("material_item", () -> new LootPoolEntryType(LootMaterialItem.CODEC));
    }
}