package com.phasetranscrystal.metal.datagen;

import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CompoundClientDatagen {
    public final String modid;
    public final GatherDataEvent event;
    private final LinkedHashMap<Class<?>,DataProvider> datagenMap;

    public CompoundClientDatagen(GatherDataEvent event, String modid){
        this.event = event;
        this.modid = modid;
        this.datagenMap = new LinkedHashMap<>();
    }

    public CompoundClientDatagen build(GatherDataEvent event) {
        BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(modid))
                .filter(item -> item instanceof ITypedMaterialObj)
                .forEach(item -> {
                    ITypedMaterialObj obj = (ITypedMaterialObj) item;
                    obj.getMIType().ifPresent(mit -> mit.gatherKeyForDatagen(this, event,obj.getMaterial().orElse(null),BuiltInRegistries.ITEM.getKey(item)));
                });

        Stream<DataProvider> dataProviderStream = datagenMap.values().stream().filter(dp -> dp instanceof TextureMixProvider);
        dataProviderStream.forEach(p -> event.getGenerator().addProvider(true,p));
        dataProviderStream.map(dp -> datagenMap.re)
        datagenMap.values().forEach(p -> event.getGenerator().addProvider(true,p));
        return this;
    }

    public <T extends DataProvider> T getDataProvider(Class<T> type, Function<CompoundClientDatagen,T> supplier){
        return (T) datagenMap.putIfAbsent(type,supplier.apply(this));
    }

    public ConsumerItemModelGen getItemModelGen(){
        return getDataProvider(ConsumerItemModelGen.class,v -> new ConsumerItemModelGen(event,modid));
    }

    public TextureAlphaFilterProvider getAlphaFilterProvider(){
        return getDataProvider(TextureAlphaFilterProvider.class,v -> new TextureAlphaFilterProvider(event));
    }
}
