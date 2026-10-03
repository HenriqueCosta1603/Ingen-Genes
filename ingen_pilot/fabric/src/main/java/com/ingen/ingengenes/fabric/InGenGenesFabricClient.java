package com.ingen.ingengenes.fabric;

import com.ingen.ingengenes.client.DNAExtractorScreen;
import com.ingen.ingengenes.registry.ModMenuTypes;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.ClientModInitializer;

public class InGenGenesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuRegistry.registerScreenFactory(ModMenuTypes.DNA_EXTRACTOR.get(), DNAExtractorScreen::new);
    }
}
