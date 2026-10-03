package com.ingen.ingengenes.fabric;

import com.ingen.ingengenes.InGenGenes;
import net.fabricmc.api.ModInitializer;

public class InGenGenesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        InGenGenes.init();
    }
}
