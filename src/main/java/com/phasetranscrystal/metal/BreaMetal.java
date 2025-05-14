package com.phasetranscrystal.metal;

import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;

@Mod(BreaMetal.MODID)
public class BreaMetal
{
    public static final String MODID = "brea_metal";

    public BreaMetal(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        ModDataComponents.register(modEventBus);
        BreaMetalRegistries.bootstrap(modEventBus);
    }



    @Nullable
    public static ITypedMaterialObj getMaterialInfo(Item target){
        return ModBusConsumer.getMaterialItemMap().inverse().get(target);
    }

    public static boolean haveMaterialInfo(Item target){
        return ModBusConsumer.getMaterialItemMap().inverse().get(target) != null;
    }

    public static boolean isTexturegenBlacklist(ResourceLocation rl){
        return ModBusConsumer.texturgenBlacklist.contains(rl);
    }

    @Nullable
    public static Item getMaterialItem(ITypedMaterialObj obj){
        return ModBusConsumer.getMaterialItemMap().get(obj);
    }

}
