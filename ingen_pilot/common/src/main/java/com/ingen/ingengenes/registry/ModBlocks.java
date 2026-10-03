package com.ingen.ingengenes.registry;

import com.ingen.ingengenes.InGenGenes;
import com.ingen.ingengenes.block.DNAExtractorBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(InGenGenes.MODID, Registries.BLOCK);

    public static final RegistrySupplier<Block> DNA_EXTRACTOR = BLOCKS.register("dna_extractor",
        () -> new DNAExtractorBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(4.0f, 6.0f)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()));

    private ModBlocks() {
    }
}
