package com.ingen.ingengenes;

import com.ingen.ingengenes.registry.ModBlockEntities;
import com.ingen.ingengenes.registry.ModBlocks;
import com.ingen.ingengenes.registry.ModCreativeTabs;
import com.ingen.ingengenes.registry.ModItems;
import com.ingen.ingengenes.registry.ModMenuTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ponto de entrada compartilhado entre NeoForge e Fabric.
 * Cada loader (neoforge/, fabric/) chama init() a partir do seu proprio
 * entrypoint especifico (@Mod / ModInitializer).
 */
public final class InGenGenes {

    public static final String MODID = "ingen_genes";
    public static final Logger LOGGER = LoggerFactory.getLogger("InGenGenes");

    private InGenGenes() {
    }

    public static void init() {
        LOGGER.info("[InGenGenes] Inicializando piloto DNA Extractor (comum a todos os loaders)");
        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModMenuTypes.MENUS.register();
        ModCreativeTabs.TABS.register();
    }
}
