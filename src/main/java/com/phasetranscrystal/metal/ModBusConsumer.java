package com.phasetranscrystal.metal;


import com.google.common.base.Suppliers;
import com.phasetranscrystal.metal.system.material.Material;
import com.phasetranscrystal.metal.system.material.MaterialItemType;
import com.phasetranscrystal.metal.system.material.event.ModifyMaterialFeatureEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.GameData;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

@EventBusSubscriber(modid = BreaMetal.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusConsumer {
    //获取最后一项注册类型
    public static final Supplier<ResourceLocation> LAST_REGISTRY_TYPE = Suppliers.memoize(() -> ((LinkedHashSet<ResourceLocation>) GameData.getRegistrationOrder()).getLast());
    //材料系统数据修饰缓存
    public static ModifyMaterialFeatureEvent materialModifyCache;

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent event) {
        event.register(Registries.MATERIAL_ITEM_TYPE);
        event.register(Registries.MATERIAL_FEATURE);
        event.register(Registries.MATERIAL);
    }

    /**
     * 在最后一种注册的最低优先级执行 等效于所有注册完成但注册表尚未冻结
     *
     * @see GameData#postRegisterEvents()
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registryEventConsumer(RegisterEvent event) {
        //在完成MF注册后 发布材料特性修饰处理事件
        if(event.getRegistryKey().equals(Registries.Keys.MATERIAL_FEATURE)){
            materialModifyCache = new ModifyMaterialFeatureEvent();
            ModLoader.postEvent(materialModifyCache);
        }

        if(event.getRegistryKey().location().equals(LAST_REGISTRY_TYPE.get())){
            //TODO 完成自动的额外注册

            for (Material material : Registries.MATERIAL) {
                for (MaterialItemType type : material.toTypes) {
                    type.secondaryRegistry(event, material);
                }
            }
        }
    }

    //TODO 创造模式物品分页自动注册
    @SubscribeEvent
    public static void attachToCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(BreaRegistries.BREA_TAB.get())) {
            for (MaterialItemType mit : Registries.MATERIAL_ITEM_TYPE) {
                mit.attachToCreativeTab(event);
            }
        }
    }

    //TODO
//    @SubscribeEvent
//    public static void attachMaterialData(MaterialReflectDataGatherEvent event) {
//        event.handler.registryReflectItemMaterialInfo(new Holder.Direct<>(Items.COAL), BreaRegistries.MaterialReg.LIGNITE.getId(), BreaRegistries.MaterialReg.COMBUSTIBLE_TYPE.getId());
//    }

//    @Mod.EventBusSubscriber(modid = BreakdownCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class Client {
////        @SubscribeEvent
////        public static void resourceReload(RegisterClientReloadListenersEvent event){
////            event.registerReloadListener(MaterialAtlasManager.init());
////        }
////
////        @SubscribeEvent
////        public static void renderTypesRegistry(RegisterNamedRenderTypesEvent event){
////            event.register(new ResourceLocation(BreakdownCore.MODID,"material"), RenderType.translucent(), MaterialAtlasManager.RENDER_TYPE);
////        }
//    }
}
