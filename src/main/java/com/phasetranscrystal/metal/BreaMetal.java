package com.phasetranscrystal.metal;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.phasetranscrystal.metal.event.ModifyMaterialFeatureEvent;
import com.phasetranscrystal.metal.helper.ImmutableBiMultiMap;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;

import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import com.phasetranscrystal.metal.mitemtype.TypedMaterialInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Collection;

@Mod(BreaMetal.MODID)
public class BreaMetal {
    public static final String MODID = "brea_metal";

    public BreaMetal(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        ModDataComponents.register(modEventBus);
        BreaMetalRegistries.bootstrap(modEventBus);
    }


    //TODO component support
    @Nullable
    public static ITypedMaterialObj getMaterialInfo(Item target) {
        return getMaterialItemMap().getKey(target);
    }

    public static boolean haveMaterialInfo(Item target) {
        return getMaterialItemMap().getKey(target) != null;
    }

    public static boolean isTexturegenBlacklist(ResourceLocation rl) {
        return ModBusConsumer.texturgenBlacklist.contains(rl);
    }

    public static Collection<Item> getMaterialItem(ITypedMaterialObj obj) {
        return getMaterialItemMap().getValues(obj);
    }

    public static void addCreativeTabStack(ItemStack stack) {
        ModBusConsumer.creativeTabAutoAttachList.add(stack);
    }

    public static ModifyMaterialFeatureEvent getMaterialModifyCache() {
        return ModBusConsumer.materialModifyCache;
    }

    public static ImmutableBiMultiMap<ITypedMaterialObj, Item> getMaterialItemMap() {
        return ModBusConsumer.materialItemMap;
    }

    public static boolean isMaterialItemRemap(ITypedMaterialObj obj){
        return ModBusConsumer.materialItemPreMap.containsKey(obj);
    }

    public static boolean isMaterialItemRemap(Material material, MaterialItemType type){
        return isMaterialItemRemap(new TypedMaterialInfo(material,type));
    }
}
