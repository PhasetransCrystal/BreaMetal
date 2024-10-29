package com.phasetranscrystal.material.system.material.expansion;

import com.phasetranscrystal.material.BreaMaterials;
import com.phasetranscrystal.material.BreaRegistries;
import com.phasetranscrystal.material.system.material.Material;
import com.phasetranscrystal.material.system.material.TypedMaterialItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class IngotItem extends TypedMaterialItem {
    public final Material material;
    public IngotItem(Material material) {
        super(BreaRegistries.MaterialReg.INGOT);
        this.material = material;
    }

    @Override
    public Optional<Material> getMaterial(ItemStack stack) {
        if(stack.is(this)){
            return Optional.of(material);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ResourceLocation> getMaterialId(ItemStack stack) {
        if(stack.is(this)){
            return Optional.ofNullable(material.id);
        }
        return Optional.of(ResourceLocation.fromNamespaceAndPath(BreaMaterials.MODID,"fallback"));
    }

    @Override
    public void setMaterial(ItemStack stack, Material material) {
    }
}
