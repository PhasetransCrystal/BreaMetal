package com.phasetranscrystal.metal.datagen;

import java.util.HashMap;
import java.util.Map;

public class MaterialLanguageList {
    private static HashMap<String,HashMap<String, String>> Trans = new HashMap<>();
    public static void TranGen(){
        Trans.put("zh_cn",new HashMap<>());
        Trans.put("en_us",new HashMap<>());
        Trans.put("zh_cn_m",new HashMap<>());
        Trans.put("zh_cn_mit",new HashMap<>());
        Trans.put("en_us_m",new HashMap<>());
        Trans.put("en_us_mit",new HashMap<>());

        Trans.get("zh_cn_m").put("BREA_METAL:IRON","铁");
        Trans.get("zh_cn_m").put("BREA_METAL:LIGNITE","褐煤");

        Trans.get("zh_cn_mit").put("BREA_METAL:INGOT","锭");
        Trans.get("zh_cn_mit").put("BREA_METAL:COMBUSTIBLE","可燃");

        Trans.get("en_us_m").put("BREA_METAL:IRON","Iron");
        Trans.get("en_us_m").put("BREA_METAL:LIGNITE","Lignite");

        Trans.get("en_us_mit").put("BREA_METAL:INGOT","Ingot");
        Trans.get("en_us_mit").put("BREA_METAL:COMBUSTIBLE","Combustible");

        for (Map.Entry<String,String> material : Trans.get("zh_cn_m").entrySet()) {
            for (Map.Entry<String,String> mit : Trans.get("zh_cn_mit").entrySet()) {
                Trans.get("zh_cn").put(material.getKey()+" "+mit.getKey(),material.getValue()+" "+mit.getValue());
            }
        }
        for (Map.Entry<String,String> material : Trans.get("en_us_m").entrySet()) {
            for (Map.Entry<String,String> mit : Trans.get("en_us_mit").entrySet()) {
                Trans.get("en_us").put(material.getKey()+" "+mit.getKey(),material.getValue()+" "+mit.getValue());
            }
        }
    }
    public static HashMap<String,String> TranGet(String locale){
        return Trans.get(locale);
    }
}
