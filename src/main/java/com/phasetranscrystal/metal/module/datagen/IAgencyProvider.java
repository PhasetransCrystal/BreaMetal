package com.phasetranscrystal.metal.module.datagen;

import java.util.List;

public interface IAgencyProvider<I> {
    void addAgency(I instance);
    List<I> getAgency();

    void execute(I instance);

    default void agency(){
        for(I instance : getAgency()){
            execute(instance);
        }
    }
}
