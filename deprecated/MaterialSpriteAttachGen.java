package com.phasetranscrystal.metal.system.material.datagen;

import com.phasetranscrystal.metal.BreaMetal;
import com.phasetranscrystal.metal.module.datagen.ExpandSpriteSourceProvider;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

@Deprecated
public class MaterialSpriteAttachGen extends ExpandSpriteSourceProvider {
    public MaterialSpriteAttachGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BreaMetal.MODID, existingFileHelper);
    }

    @Override
    public void gather() {
        super.gather();
        SourceList sourceList = atlas(BLOCKS_ATLAS);
//        for(Material material : Registry$Material.MATERIAL){
//            sourceList.addSource(new SingleFile(material.id.withPath(id -> "breamaterial/material/" + id), Optional.empty()));
//        }
//        for(MaterialItemType type : Registry$Material.MATERIAL_ITEM_TYPE){
//            sourceList.addSource(new SingleFile(type.id.withPath(s -> "breamaterial/mit/" + s),Optional.empty()));
//            if(this.existingFileHelper.exists(type.id.withPath(s -> "textures/breamaterial/mit_cover/" + s + ".png"), PackType.CLIENT_RESOURCES)){
//                sourceList.addSource(new SingleFile(type.id.withPath(s -> "breamaterial/mit_cover/" + s),Optional.empty()));
//            }
//        }

        sourceList.addSource(new DirectoryLister("breamaterial/material/mit","breamaterial/material/mit/"));
//        sourceList.addSource(new DirectoryLister("breamaterial/mit","breamaterial/mit/"));
//        sourceList.addSource(new DirectoryLister("breamaterial/mit_cover","breamaterial/mit_cover/"));
    }
}
