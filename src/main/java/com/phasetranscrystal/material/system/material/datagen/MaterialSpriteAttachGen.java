package com.phasetranscrystal.material.system.material.datagen;

import com.phasetranscrystal.material.BreaMaterials;
import com.phasetranscrystal.material.module.datagen.ExpandSpriteSourceProvider;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class MaterialSpriteAttachGen extends ExpandSpriteSourceProvider {
    public MaterialSpriteAttachGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BreaMaterials.MODID, existingFileHelper);
    }

    @Override
    public void gather() {
        super.gather();
        SourceList sourceList = atlas(BLOCKS_ATLAS);
//        for(Material material : Registry$Material.MATERIAL){
//            sourceList.addSource(new SingleFile(material.id.withPath(id -> "brea/material/" + id), Optional.empty()));
//        }
//        for(MaterialItemType type : Registry$Material.MATERIAL_ITEM_TYPE){
//            sourceList.addSource(new SingleFile(type.id.withPath(s -> "brea/mit/" + s),Optional.empty()));
//            if(this.existingFileHelper.exists(type.id.withPath(s -> "textures/brea/mit_cover/" + s + ".png"), PackType.CLIENT_RESOURCES)){
//                sourceList.addSource(new SingleFile(type.id.withPath(s -> "brea/mit_cover/" + s),Optional.empty()));
//            }
//        }

        sourceList.addSource(new DirectoryLister("brea/material/mit","brea/material/mit/"));
//        sourceList.addSource(new DirectoryLister("brea/mit","brea/mit/"));
//        sourceList.addSource(new DirectoryLister("brea/mit_cover","brea/mit_cover/"));
    }
}
