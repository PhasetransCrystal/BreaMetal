package com.phasetranscrystal.metal.expansion;

import com.phasetranscrystal.metal.mitemtype.MaterialItemType;
import net.minecraft.resources.ResourceLocation;


public class CombustibleType extends MaterialItemType {
    public CombustibleType(ResourceLocation location) {
        //我们在此指定该种物品的内容量为125mB，纯度为40%。id由注册时自行传入。
        super(location,125, 0.4F);
    }
}
