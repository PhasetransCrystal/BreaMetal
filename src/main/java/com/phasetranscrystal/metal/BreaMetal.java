package com.phasetranscrystal.metal;

import com.phasetranscrystal.metal.mfeature.MaterialFeatureType;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Map;

@Mod(BreaMetal.MODID)
public class BreaMetal
{
    public static final String MODID = "brea_metal";

    public BreaMetal(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        ModDataComponents.register(modEventBus);
        BreaMetalRegistries.bootstrap(modEventBus);
        //TODO
//        TextureGen.textureMix(
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/combustible.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/combustible.png")
//        );
//        TextureGen.textureMix(
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/ingot.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/ingot.png")
//        );
//        TextureGen.textureMix(
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/ingothot.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/ingothot.png")
//        );
//        TextureGen.textureMix(
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/turbine.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/turbine.png")
//        );
//        TextureGen.textureMix(
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/wrench.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
//                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/wrench.png")
//        );
    }



    @Nullable
    public static ITypedMaterialObj getMaterialInfo(Item target){
        return target instanceof ITypedMaterialObj obj ? obj : ModBusConsumer.getMaterialItemNegativeExpandMap().get(target);
    }

    public static boolean haveMaterialInfo(Item target){
        return target instanceof ITypedMaterialObj || ModBusConsumer.getMaterialItemNegativeExpandMap().get(target) != null;
    }

    @Nullable
    public static Item getMaterialItem(ITypedMaterialObj obj){
        return ModBusConsumer.getMaterialItemPositiveMap().get(obj);
    }

}
