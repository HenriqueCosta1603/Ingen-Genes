package com.ingen.ingengenes.registry;

import com.ingen.ingengenes.InGenGenes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
        DeferredRegister.create(InGenGenes.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MAIN_TAB = TABS.register("main_tab",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.ingen_genes.main_tab"))
            .icon(() -> new ItemStack(ModBlocks.DNA_EXTRACTOR.get()))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.DNA_EXTRACTOR.get());
                output.accept(ModItems.DINOSAUR_FOSSIL.get());
                output.accept(ModItems.EXTRACTION_BUFFER.get());
                output.accept(ModItems.RAW_DNA_SAMPLE.get());
            })
            .build());

    private ModCreativeTabs() {
    }
}
