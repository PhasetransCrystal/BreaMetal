package com.phasetranscrystal.metal.mitemtype;

import com.phasetranscrystal.metal.material.Material;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * SimpleTypedMaterialItem一般材料类型物品<br>
 * 自动注册的最标准的材料类型物品。包含物品类型与其材料。
 */
public class TypedMaterialItem extends Item implements ITypedMaterialObj {
    public final MaterialItemType type;
    public final Material material;

    public TypedMaterialItem(Properties properties ,MaterialItemType type, Material material) {
        super(properties);
        this.type = type;
        this.material = material;
    }

    public TypedMaterialItem(MaterialItemType type, Material material) {
        this(new Properties(), type, material);
    }

    @Override
    public Optional<MaterialItemType> getMIType() {
        return Optional.of(type);
    }

    @Override
    public Optional<Material> getMaterial() {
        return Optional.of(material);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return materialTrans(material).append(" ").append(mitTrans(type));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        //一个由(材料)制成的(物品类型)
        //(材料描述，灰色)
    }

    public static MutableComponent materialTrans(Material material) {
        if(I18n.exists(material.getTranslateKey())) {
            return Component.translatable(material.getTranslateKey());
        }else return Component.literal(material.getLocation().toString().toUpperCase(Locale.ROOT));
    }

    public static MutableComponent mitTrans(MaterialItemType mit) {
        if(I18n.exists(mit.getTranslateKey())) {
            return Component.translatable(mit.getTranslateKey());
        }else return Component.literal(mit.getLocation().toString().toUpperCase(Locale.ROOT));
    }
}
