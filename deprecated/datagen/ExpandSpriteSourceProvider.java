package com.phasetranscrystal.metal.module.datagen;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.resources.ResourceLocation.parse;

public abstract class ExpandSpriteSourceProvider extends JsonCodecProvider<HashSet<SpriteSource>> implements IAgencyProvider<ExpandSpriteSourceProvider>{

    public static final ResourceLocation BLOCKS_ATLAS = parse("blocks");
    public static final ResourceLocation BANNER_PATTERNS_ATLAS = parse("banner_patterns");
    public static final ResourceLocation BEDS_ATLAS = parse("beds");
    public static final ResourceLocation CHESTS_ATLAS = parse("chests");
    public static final ResourceLocation SHIELD_PATTERNS_ATLAS = parse("shield_patterns");
    public static final ResourceLocation SHULKER_BOXES_ATLAS = parse("shulker_boxes");
    public static final ResourceLocation SIGNS_ATLAS = parse("signs");
    public static final ResourceLocation MOB_EFFECTS_ATLAS = parse("mob_effects");
    public static final ResourceLocation PAINTINGS_ATLAS = parse("paintings");
    public static final ResourceLocation PARTICLES_ATLAS = parse("particles");
    private final List<ExpandSpriteSourceProvider> agency = new ArrayList<>();

    private final Map<ResourceLocation,SourceList> atlases = new HashMap<>();

    public ExpandSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.RESOURCE_PACK, "atlases", PackType.CLIENT_RESOURCES, SpriteSources.FILE_CODEC.xmap(HashSet::new,List::copyOf), lookupProvider, modId, existingFileHelper);
    }

    protected final SourceList atlas(ResourceLocation id) {
        return atlases.computeIfAbsent(id, i -> {
            final SourceList newAtlas = new SourceList(new HashSet<>());
            unconditional(i, newAtlas.sources());
            return newAtlas;
        });
    }

    public record SourceList(HashSet<SpriteSource> sources) {
        public SourceList addSource(SpriteSource source) {
            sources.add(source);
            return this;
        }

        public SourceList merge(SourceList list){
            sources.addAll(list.sources);
            return this;
        }
    }

    @Override
    public void addAgency(ExpandSpriteSourceProvider instance) {
        agency.add(instance);
    }

    @Override
    public List<ExpandSpriteSourceProvider> getAgency() {
        return agency;
    }

    @Override
    public void execute(ExpandSpriteSourceProvider instance) {
        instance.gather();

    }

    @Override
    public void gather() {
        agency();
    }

    public SourceList getAtlas(ResourceLocation id) {
        return atlas(id);
    }

    public void add(ResourceLocation atlas, SpriteSource source) {
        this.atlas(atlas).addSource(source);
    }

    public void add(ResourceLocation atlas, ResourceLocation single) {
        this.atlas(atlas).addSource(new SingleFile(single, Optional.empty()));
    }

    public void add(ResourceLocation single) {
        this.atlas(BLOCKS_ATLAS).addSource(new SingleFile(single, Optional.empty()));
    }
}
