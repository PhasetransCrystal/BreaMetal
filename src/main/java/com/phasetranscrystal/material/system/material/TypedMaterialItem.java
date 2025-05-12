package com.phasetranscrystal.material.system.material;

import com.phasetranscrystal.material.ModDataComponents;
import com.phasetranscrystal.material.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**TypedMaterialItem材料类型物品<br>
 * 材料类型物品是一个独立的物品，其内部包含的nbt数据将使其在不同材料条件下变成不同的样式。也就是说，所有这个形态的材料物品都是同一个物品。<br>
 * 因此请不要直接通过物品判断来决定它是否为你需要的物品。这有可能导致，比如说，你需要下届合金齿轮，但匹配到一个地狱岩齿轮。
 * */
public class TypedMaterialItem extends Item implements ITypedMaterialObj{
    public final Supplier<? extends MaterialItemType> type;

    public TypedMaterialItem(Supplier<? extends MaterialItemType> type) {
        super(new Properties().fireResistant());
        this.type = type;
    }


    @Override
    public Optional<ResourceLocation> getMaterialId(ItemStack stack) {
        if(stack.is(this)){
            return Optional.ofNullable(stack.get(ModDataComponents.MATERIALS)).map(ResourceLocation::parse);
        }
        return Optional.empty();
    }

    @Override
    public MaterialItemType getMIType() {
        return type.get();
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        if(getMaterialId(pStack).isPresent())
            return materialId2Component(getMaterialId(pStack).get()).append(" ").append(mitId2Component(getMIType().id));
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        //一个由(材料)制成的(物品类型)
        //(材料描述，灰色)
    }

    public static MutableComponent materialId2Component(ResourceLocation id){
        if(Registries.MATERIAL.containsKey(id)){
            return Component.translatable("brea.material." + id.getNamespace() + "." + id.getPath());
        }
        return Component.translatable("brea.material.missing");
    }

    public static MutableComponent mitId2Component(ResourceLocation id){
        return Component.translatable("brea.mit." + id.getNamespace() + "." + id.getPath());
    }
}
