package com.phasetranscrystal.metal;


import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.event.MapMaterialItemEvent;
import com.phasetranscrystal.metal.event.ModifyMaterialFeatureEvent;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.GameData;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = BreaMetal.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusConsumer {
    //获取最后一项注册类型
    public static final Supplier<ResourceLocation> LAST_REGISTRY_TYPE = Suppliers.memoize(() -> ((LinkedHashSet<ResourceLocation>) GameData.getRegistrationOrder()).getLast());
    //材料系统数据修饰缓存
    public static ModifyMaterialFeatureEvent materialModifyCache;
    public static final List<ItemStack> creativeTabAutoAttachList = new ArrayList<>();

    private static ImmutableMap<ITypedMaterialObj, Item> materialItemPositiveMap;
    private static ImmutableMap<Item, ITypedMaterialObj> materialItemNegativeExpandMap;
    private static ImmutableSet<Item> texturgenBlacklist = ImmutableSet.of();

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(NewRegistries.MATERIAL_ITEM_TYPE);
        event.register(NewRegistries.MATERIAL_FEATURE);
        event.register(NewRegistries.MATERIAL);
    }

    /**
     * 在最后一种注册的最低优先级执行 等效于所有注册完成但注册表尚未冻结
     *
     * @see GameData#postRegisterEvents()
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registryEventConsumer(RegisterEvent event) {
        //在完成MF注册后 发布材料特性修饰处理事件
        if (event.getRegistryKey().equals(NewRegistries.Keys.MATERIAL_FEATURE)) {
            materialModifyCache = new ModifyMaterialFeatureEvent();
            ModLoader.postEvent(materialModifyCache);
        }

        if (event.getRegistryKey().location().equals(LAST_REGISTRY_TYPE.get())) {
            for (Material material : NewRegistries.MATERIAL) {
                for (MaterialItemType type : material.toTypes) {
                    type.registryBootstrap( material);
                }
            }
        }
    }

    //---[创造模式物品分页]---

    @SubscribeEvent
    public static void attachToCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(BreaMetalRegistries.BREA_TAB.get())) {
            creativeTabAutoAttachList.forEach(event::accept);
        }
        creativeTabAutoAttachList.clear();
    }

    public static void addCreativeTabStack(ItemStack stack) {
        creativeTabAutoAttachList.add(stack);
    }

    //---[材料物品表建设]---

    @SubscribeEvent
    public static void buildItemReflect(FMLCommonSetupEvent event) {
        MapMaterialItemEvent mapMaterialItemEvent = new MapMaterialItemEvent();
        ModLoader.postEvent(mapMaterialItemEvent);

        if(DatagenModLoader.isRunningDataGen()){
            texturgenBlacklist = ImmutableSet.copyOf(mapMaterialItemEvent.texturegenBlacklist);
        }

        ImmutableMap.Builder<ITypedMaterialObj, Item> positiveBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<Item, ITypedMaterialObj> negativeExpandBuilder = ImmutableMap.builder();

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof ITypedMaterialObj obj) {
                positiveBuilder.put(obj, item);
            }
        }

        mapMaterialItemEvent.reflectMap.forEach((info, item) -> {
            positiveBuilder.put(info, item);
            negativeExpandBuilder.put(item, info);
        });

        materialItemPositiveMap = positiveBuilder.build();
        materialItemNegativeExpandMap = negativeExpandBuilder.build();
    }

    public static ImmutableMap<Item, ITypedMaterialObj> getMaterialItemNegativeExpandMap() {
        return materialItemNegativeExpandMap;
    }

    public static ImmutableMap<ITypedMaterialObj, Item> getMaterialItemPositiveMap() {
        return materialItemPositiveMap;
    }

    @SubscribeEvent
    public static void attachMaterialData(MapMaterialItemEvent event) {
        event.addReflectMap(BreaMetalRegistries.UNKNOWN.get(), BreaMetalRegistries.INGOT.get(), BreaMetalRegistries.UNKNOWN_INGOT.get());
    }
}
