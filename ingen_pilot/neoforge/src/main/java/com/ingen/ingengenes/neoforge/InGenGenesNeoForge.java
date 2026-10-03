package com.ingen.ingengenes.neoforge;

import com.ingen.ingengenes.InGenGenes;
import com.ingen.ingengenes.client.DNAExtractorScreen;
import com.ingen.ingengenes.registry.ModMenuTypes;
import dev.architectury.registry.menu.MenuRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(InGenGenes.MODID)
public class InGenGenesNeoForge {

    public InGenGenesNeoForge(IEventBus modEventBus) {
        // Nao precisa registrar o event bus manualmente - o Architectury
        // faz isso sozinho por baixo dos panos no NeoForge (confirmado:
        // EventBusesHooks so serve pra LER um bus ja disponivel, nao
        // existe metodo de registro manual nessa API).
        InGenGenes.init();
        modEventBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
            MenuRegistry.registerScreenFactory(ModMenuTypes.DNA_EXTRACTOR.get(), DNAExtractorScreen::new));
    }
}
