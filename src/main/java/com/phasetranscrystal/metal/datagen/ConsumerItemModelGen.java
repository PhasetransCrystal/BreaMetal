package com.phasetranscrystal.metal.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.mitemtype.ITypedMaterialObj;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//TODO
public class ConsumerItemModelGen extends ItemModelProvider {
    public ConsumerItemModelGen(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    public ConsumerItemModelGen(GatherDataEvent event, String modid) {
        this(event.getGenerator().getPackOutput(), modid, event.getExistingFileHelper());
    }

    public final List<Consumer<ConsumerItemModelGen>> consumers = new ArrayList<>();

    @Override
    public void registerModels() {
        consumers.forEach(c -> c.accept(this));
    }

    public void addConsumer(Consumer<ConsumerItemModelGen> consumer) {
        consumers.add(consumer);
    }
}
