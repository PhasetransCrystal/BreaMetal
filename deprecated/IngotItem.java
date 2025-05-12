package com.phasetranscrystal.metal.material.expansion;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.BreaRegistries;
import com.phasetranscrystal.metal.material.Material;
import com.phasetranscrystal.metal.material.TypedMaterialItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@Deprecated(forRemoval = true)
public class IngotItem extends TypedMaterialItem {
    public IngotItem(Material material) {
//        super(BreaRegistries.MaterialReg.INGOT);
//        this.material = material;
        super(BreaRegistries.MaterialReg.INGOT.get(),material);
    }

//    @Override
    public Optional<Material> getMaterial(ItemStack stack) {
        if(stack.is(this)){
            return Optional.of(material);
        }
        return Optional.empty();
    }

//    @Override
    public Optional<ResourceLocation> getMaterialId(ItemStack stack) {
        if(stack.is(this)){
            return Optional.ofNullable(material.getLocation());
        }
        return Optional.of(ResourceLocation.fromNamespaceAndPath(BreaMetal.MODID,"fallback"));
    }

//    @Override
    public void setMaterial(ItemStack stack, Material material) {
    }
}
