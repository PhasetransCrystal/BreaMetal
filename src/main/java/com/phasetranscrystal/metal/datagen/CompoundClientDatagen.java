package com.phasetranscrystal.metal.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompoundClientDatagen {
    public static final Predicate<DataProvider> TEXTUREGEN_PREDICATE = p -> p instanceof TextureMixProvider;
    public final String modid;
    public final GatherDataEvent event;
    private final HashMap<Class<?>, DataProvider> datagenMap;
    private final List<Predicate<DataProvider>> highPriorityFilter;

    public CompoundClientDatagen(GatherDataEvent event, String modid) {
        this.event = event;
        this.modid = modid;
        this.datagenMap = new HashMap<>();
        this.highPriorityFilter = new ArrayList<>();
    }

    public void build() {
        BuiltInRegistries.ITEM.stream()
                .filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(modid))
                .filter(BreaMetal::haveMaterialInfo)
                .forEach(item -> {
                    ITypedMaterialObj obj = BreaMetal.getMaterialInfo(item);
                    obj.getMIType().ifPresent(mit -> mit.gatherKeyForDatagen(this, event, obj.getMaterial().orElse(null), BuiltInRegistries.ITEM.getKey(item)));
                });


        if (!highPriorityFilter.isEmpty()) {
            // 将过滤条件合并为一个组合谓词
            Predicate<DataProvider> isHighPriority = provider -> highPriorityFilter.stream().anyMatch(filter -> filter.test(provider));

            // 使用流分割数据提供者
            Map<Boolean, List<DataProvider>> partitioned = datagenMap.values().stream().collect(Collectors.partitioningBy(isHighPriority));

            // 按优先级顺序添加（先高后低）
            Stream.concat(
                    partitioned.get(true).stream(),
                    partitioned.get(false).stream()
            ).forEach(provider -> event.getGenerator().addProvider(true, provider));
        } else
            datagenMap.values().forEach(provider -> event.getGenerator().addProvider(true, provider));
    }

    public <T extends DataProvider> T getDataProvider(Class<T> type, Function<CompoundClientDatagen, T> supplier) {
        return (T) datagenMap.computeIfAbsent(type, c -> supplier.apply(this));
    }

    public void addHighPriorityFilter(Predicate<DataProvider> filter) {
        highPriorityFilter.add(filter);
    }

    public ConsumerItemModelGen getItemModelGen() {
        return getDataProvider(ConsumerItemModelGen.class, v -> new ConsumerItemModelGen(event, modid));
    }

    public TextureAlphaFilterProvider getAlphaFilterProvider() {
        this.highPriorityFilter.add(TEXTUREGEN_PREDICATE);
        return getDataProvider(TextureAlphaFilterProvider.class, v -> new TextureAlphaFilterProvider(event));
    }

    public TextureMultiplyMixProvider getMultiplyMixProvider() {
        this.highPriorityFilter.add(TEXTUREGEN_PREDICATE);
        return getDataProvider(TextureMultiplyMixProvider.class, v -> new TextureMultiplyMixProvider(event));
    }
}
