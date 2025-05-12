package com.phasetranscrystal.metal.module.render.model;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemORResolveOnly extends ItemOverrides {
    private final ModelProvider provider;

    public ItemORResolveOnly(ModelProvider provider){
        this.provider = provider;
    }


    @Override
    public BakedModel resolve(@NotNull BakedModel pModel, @NotNull ItemStack pStack, @Nullable ClientLevel pLevel, @Nullable LivingEntity pEntity, int pSeed) {
        return provider.resolve(pModel, pStack, pLevel, pEntity, pSeed);
    }

    public interface ModelProvider {
        BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed);
    }
}
