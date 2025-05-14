package com.phasetranscrystal.metal;

import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.expansion.CombustibleType;
import com.phasetranscrystal.metal.expansion.IngotType;
import com.phasetranscrystal.metal.expansion.materialfeature.CombustibleMF;
import com.phasetranscrystal.metal.expansion.materialfeature.MetalMF;
import com.phasetranscrystal.metal.expansion.materialfeature.PhaseTransitMF;
import com.phasetranscrystal.metal.expansion.materialfeature.ThermoMF;
import com.phasetranscrystal.metal.registry.ShortCircuitHolder;
import com.phasetranscrystal.metal.registry.ShortCircuitRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BreaMetalRegistries {
    public static final DeferredRegister.Items ITEM = DeferredRegister.createItems(BreaMetal.MODID);
    private static final DeferredRegister<CreativeModeTab> TAB = DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, BreaMetal.MODID);
    private static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPE = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, BreaMetal.MODID);
    private static final ShortCircuitRegistry<MaterialItemType> TYPE = ShortCircuitRegistry.create(NewRegistries.MATERIAL_ITEM_TYPE, BreaMetal.MODID);
    private static final ShortCircuitRegistry<MaterialFeatureType<?>> FEATURE = ShortCircuitRegistry.create(NewRegistries.MATERIAL_FEATURE, BreaMetal.MODID);
    private static final ShortCircuitRegistry<Material> MATERIAL = ShortCircuitRegistry.create(NewRegistries.MATERIAL, BreaMetal.MODID);

    public static void bootstrap(IEventBus bus) {
        ITEM.register(bus);
        TAB.register(bus);
        INGREDIENT_TYPE.register(bus);
        TYPE.register(bus);
        FEATURE.register(bus);
        MATERIAL.register(bus);
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BREA_TAB = TAB.register("default", () ->
            CreativeModeTab.builder().title(Component.translatable("tab.brea.metal.default")).icon(() -> new ItemStack(Items.COMMAND_BLOCK)).build()
    );

    public static final ShortCircuitHolder<MaterialItemType, IngotType> INGOT = TYPE.register("ingot", r -> new IngotType(r, 90));
    public static final ShortCircuitHolder<MaterialItemType, CombustibleType> COMBUSTIBLE_TYPE = TYPE.register("combustible", CombustibleType::new);

    public static final ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<PhaseTransitMF>> PHASE_TRANSIT = FEATURE.register("phase_transit", r -> new MaterialFeatureType<>(r, PhaseTransitMF.CODEC, PhaseTransitMF.class));//TODO 添加气态与液态支持
    public static final ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<ThermoMF>> THERMO = FEATURE.register("thermo", r -> new MaterialFeatureType<>(r, ThermoMF.CODEC, ThermoMF.class));
    public static final ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<MetalMF>> METAL = FEATURE.register("metal", r -> new MaterialFeatureType<>(r, MetalMF.CODEC, MetalMF.class, ImmutableSet.of(BreaMetalRegistries.INGOT), BreaMetalRegistries.PHASE_TRANSIT.getId(), BreaMetalRegistries.THERMO.getId()));
    public static final ShortCircuitHolder<MaterialFeatureType<?>, MaterialFeatureType<CombustibleMF>> COMBUSTIBLE = FEATURE.register("combustible", r -> new MaterialFeatureType<>(r, CombustibleMF.CODEC, CombustibleMF.class, BreaMetalRegistries.COMBUSTIBLE_TYPE));

    public static final ShortCircuitHolder<Material, Material> IRON = MATERIAL.register("iron", r -> new Material(r, new MetalMF(), new PhaseTransitMF(1539, 3000), new ThermoMF(0.46F, 80.9F, 7.87F)));
    public static final ShortCircuitHolder<Material, Material> LIGNITE = MATERIAL.register("lignite", r -> new Material(r, new CombustibleMF(18000, 1150)));

    public static final DeferredItem<Item> UNKNOWN_INGOT = ITEM.register("unknown_ingot", () -> new Item(new Item.Properties()));
    public static final ShortCircuitHolder<Material, Material> UNKNOWN = MATERIAL.register("unknown", Material::new);
}