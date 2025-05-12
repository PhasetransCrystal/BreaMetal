package com.phasetranscrystal.metal.event;

import com.phasetranscrystal.metal.event.render.SpriteBeforeStitchEvent;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.neoforged.fml.ModLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventHooks {
    public static List<SpriteContents> postSpriteBeforeStitchEvent(SpriteBeforeStitchEvent event){
        ModLoader.postEventWrapContainerInModOrder(event);
        List<SpriteContents> contents = new ArrayList<>(event.getAttached().stream().filter(Objects::nonNull).toList());
        contents.addAll(event.contents);
        return contents;
    }
}
