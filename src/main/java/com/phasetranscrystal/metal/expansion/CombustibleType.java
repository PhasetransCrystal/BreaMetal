package com.phasetranscrystal.metal.expansion;

import com.phasetranscrystal.metal.mitemtype.MaterialItemType;


public class CombustibleType extends MaterialItemType {
    public CombustibleType() {
        //我们在此指定该种物品的内容量为125mB，纯度为40%。id由注册时自行传入。
        super(125, 0.4F);
    }
}
