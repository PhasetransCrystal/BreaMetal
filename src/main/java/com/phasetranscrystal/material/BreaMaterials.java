package com.phasetranscrystal.material;

import com.phasetranscrystal.material.module.registry.RegroupController;
import com.phasetranscrystal.material.system.material.datagen.MaterialSpriteAttachGen;
import com.phasetranscrystal.material.system.material.datagen.MitModelGen;
import com.phasetranscrystal.material.system.material.datagen.TextureGen;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Mod(BreaMaterials.MODID)
public class BreaMaterials
{
    public static RegroupController REGISTER;
    public static final String MODID = "breamaterial";
    private static final Logger LOGGER = LogUtils.getLogger();


    public BreaMaterials(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        REGISTER = RegroupController.create(modEventBus,MODID,((event, regroupController) -> {
            PackOutput output = event.getGenerator().getPackOutput();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();
            CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

            regroupController.itemModelProvider.addAgency(new MitModelGen(output,fileHelper));
            regroupController.spriteProvider.addAgency(new MaterialSpriteAttachGen(output,lookup,fileHelper));
        }));
        ModDataComponents.register(modEventBus);
        BreaRegistries.ITEM.register(modEventBus);
        BreaRegistries.TAB.register(modEventBus);
        BreaRegistries.INGREDIENT_TYPE.register(modEventBus);
        BreaRegistries.MaterialReg.MATERIAL.register(modEventBus);
        BreaRegistries.MaterialReg.FEATURE.register(modEventBus);
        BreaRegistries.MaterialReg.TYPE.register(modEventBus);
        BreaRegistries.JsonCodecReg.LOOT_POOL.register(modEventBus);
        TextureGen.textureMix(
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/combustible.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/combustible.png")
        );
        TextureGen.textureMix(
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/ingot.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/ingot.png")
        );
        TextureGen.textureMix(
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/ingothot.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/ingothot.png")
        );
        TextureGen.textureMix(
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/turbine.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/turbine.png")
        );
        TextureGen.textureMix(
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/mit/wrench.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/missing.png"),
                ResourceLocation.fromNamespaceAndPath("breamaterial","material/material/wrench.png")
        );
    }

    private static ItemModelGenerator ITEM_MODELGEN;

    public static ItemModelGenerator getItemModelgen() {
        if (ITEM_MODELGEN == null) {
            ITEM_MODELGEN = new ItemModelGenerator();
        }
        return ITEM_MODELGEN;
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

}
