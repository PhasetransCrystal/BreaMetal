package com.phasetranscrystal.metal;

import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import com.phasetranscrystal.metal.mitemtype.TypedMaterialInfo;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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



    @Nullable
    public static ITypedMaterialObj getMaterialInfo(Item target){
        return target instanceof ITypedMaterialObj obj ? obj : ModBusConsumer.getMaterialItemNegativeExpandMap().get(target);
    }

    @Nullable
    public static Item getMaterialItem(ITypedMaterialObj obj){
        return ModBusConsumer.getMaterialItemPositiveMap().get(obj);
    }

}
