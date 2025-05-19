package com.phasetranscrystal.metal;


import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;
import com.phasetranscrystal.metal.event.MapMaterialItemEvent;
import com.phasetranscrystal.metal.event.ModifyMaterialFeatureEvent;
import com.phasetranscrystal.metal.event.TextureGenBlackListEvent;
import com.phasetranscrystal.metal.helper.ImmutableBiMultiMap;
import com.phasetranscrystal.metal.material.Material;
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
import net.neoforged.neoforge.data.event.GatherDataEvent;
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
    protected static ModifyMaterialFeatureEvent materialModifyCache;
    protected static final List<ItemStack> creativeTabAutoAttachList = new ArrayList<>();

    protected static ImmutableBiMultiMap<ITypedMaterialObj, Item> materialItemMap;
    protected static ImmutableSet<ResourceLocation> texturgenBlacklist = ImmutableSet.of();

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(NewRegistries.MATERIAL_ITEM_TYPE);
        event.register(NewRegistries.MATERIAL_FEATURE);
        event.register(NewRegistries.MATERIAL);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void modifyMaterialFeature(RegisterEvent event) {
        if (!event.getRegistryKey().location().getNamespace().equals("minecraft") && materialModifyCache == null) {
            materialModifyCache = new ModifyMaterialFeatureEvent();
            ModLoader.postEvent(materialModifyCache);
        }
    }

    /**
     * 在最后一种注册的最低优先级执行 等效于所有注册完成但注册表尚未冻结
     *
     * @see GameData#postRegisterEvents()
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void materialItemAutoRegistry(RegisterEvent event) {
        if (event.getRegistryKey().location().equals(LAST_REGISTRY_TYPE.get())) {
            for (Material material : NewRegistries.MATERIAL) {
                for (MaterialItemType type : material.toTypes) {
                    type.registryBootstrap(material);
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


    //---[材料物品表建设]---

    @SubscribeEvent
    public static void buildItemReflect(FMLCommonSetupEvent event) {
        generateMaterialItemMap();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void buildItemReflectDatagen(GatherDataEvent event) {
        generateMaterialItemMap();
        texturgenBlacklist = ImmutableSet.copyOf(ModLoader.postEventWithReturn(new TextureGenBlackListEvent()).texturegenBlacklist);
    }

    private static void generateMaterialItemMap() {
        MapMaterialItemEvent mapMaterialItemEvent = new MapMaterialItemEvent();
        ModLoader.postEvent(mapMaterialItemEvent);

        ImmutableBiMultiMap.Builder<ITypedMaterialObj, Item> builder = ImmutableBiMultiMap.builder();

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof ITypedMaterialObj obj) {
                builder.put(obj, item);
            }
        }

        builder.merge(mapMaterialItemEvent.getReflectMap());

        materialItemMap = builder.build();
    }

    @SubscribeEvent
    public static void attachMaterialData(MapMaterialItemEvent event) {
        event.addReflectMap(BreaMetalRegistries.UNKNOWN.get(), BreaMetalRegistries.INGOT.get(), BreaMetalRegistries.UNKNOWN_INGOT.get());
    }
}
