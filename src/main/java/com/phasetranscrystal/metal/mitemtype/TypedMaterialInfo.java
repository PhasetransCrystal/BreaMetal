package com.phasetranscrystal.metal.mitemtype;

import com.phasetranscrystal.metal.NewRegistries;
import com.phasetranscrystal.metal.material.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**TypedMaterialInfo类型材料信息<br>
 * 类型材料信息是一个record类，使你可以在不修改一个物品/方块或其它内容的情况下创建其对应的材料信息。<br>
 * 这些内容可以在相应事件中注册。在获取一个物品成分时将优先检查注册的物品-信息再尝试直接从接口获取。
 * */
public record TypedMaterialInfo(Material material, MaterialItemType mit) implements ITypedMaterialObj{

    public TypedMaterialInfo(ResourceLocation material, ResourceLocation mit){
        this(NewRegistries.MATERIAL.get(material), NewRegistries.MATERIAL_ITEM_TYPE.get(mit));
    }

    @Override
    public Optional<Material> getMaterial() {
        return Optional.of(material);
    }

    @Override
    public Optional<MaterialItemType> getMIType() {
        return Optional.of(mit);
    }
}
