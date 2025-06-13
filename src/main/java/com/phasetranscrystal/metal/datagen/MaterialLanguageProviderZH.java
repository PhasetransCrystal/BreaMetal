package com.phasetranscrystal.metal.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class MaterialLanguageProviderZH extends LanguageProvider {

    public MaterialLanguageProviderZH(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        HashMap<String,String> TransKey = MaterialLanguageList.TranGet("zh_cn");
        for (Map.Entry<String,String> transKey : TransKey.entrySet()) {
            add(transKey.getKey(),transKey.getValue());
        }
    }
}
