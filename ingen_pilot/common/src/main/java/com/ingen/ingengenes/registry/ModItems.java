package com.ingen.ingengenes.registry;

import com.ingen.ingengenes.InGenGenes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(InGenGenes.MODID, Registries.ITEM);

    // Bloco -> item, mesma logica do vanilla (furnace, etc.)
    public static final RegistrySupplier<Item> DNA_EXTRACTOR_ITEM = ITEMS.register("dna_extractor",
        () -> new BlockItem(ModBlocks.DNA_EXTRACTOR.get(), new Item.Properties()));

    // Entrada 1: fossil ja existia como referencia solta em loot table/receita antiga; agora vira item real
    public static final RegistrySupplier<Item> DINOSAUR_FOSSIL = ITEMS.register("dinosaur_fossil",
        () -> new Item(new Item.Properties().stacksTo(16)));

    // Entrada 2: reagente consumido junto com o fossil
    public static final RegistrySupplier<Item> EXTRACTION_BUFFER = ITEMS.register("extraction_buffer",
        () -> new Item(new Item.Properties().stacksTo(16)));

    // Saida da maquina
    public static final RegistrySupplier<Item> RAW_DNA_SAMPLE = ITEMS.register("raw_dna_sample",
        () -> new Item(new Item.Properties().stacksTo(16)));

    private ModItems() {
    }
}
