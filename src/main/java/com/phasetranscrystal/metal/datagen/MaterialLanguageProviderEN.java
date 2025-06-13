package com.phasetranscrystal.metal.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class MaterialLanguageProviderEN extends LanguageProvider {

    public MaterialLanguageProviderEN(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        HashMap<String,String> TransKey = MaterialLanguageList.TranGet("en_us");
        for (Map.Entry<String,String> transKey : TransKey.entrySet()) {
            add(transKey.getKey(),transKey.getValue());
        }
    }
}
