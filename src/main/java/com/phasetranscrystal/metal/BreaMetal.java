package com.phasetranscrystal.metal;

import net.minecraft.client.renderer.block.model.ItemModelGenerator;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.io.IOException;

@Mod(BreaMetal.MODID)
public class BreaMetal
{
    public static final String MODID = "brea_metal";


    public BreaMetal(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        ModDataComponents.register(modEventBus);
        BreaRegistries.ITEM.register(modEventBus);
        BreaRegistries.TAB.register(modEventBus);
        BreaRegistries.INGREDIENT_TYPE.register(modEventBus);
        BreaRegistries.MaterialReg.MATERIAL.register(modEventBus);
        BreaRegistries.MaterialReg.FEATURE.register(modEventBus);
        BreaRegistries.MaterialReg.TYPE.register(modEventBus);
        BreaRegistries.JsonCodecReg.LOOT_POOL.register(modEventBus);
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

    private static ItemModelGenerator ITEM_MODELGEN;

    public static ItemModelGenerator getItemModelgen() {
        if (ITEM_MODELGEN == null) {
            ITEM_MODELGEN = new ItemModelGenerator();
        }
        return ITEM_MODELGEN;
    }

}
