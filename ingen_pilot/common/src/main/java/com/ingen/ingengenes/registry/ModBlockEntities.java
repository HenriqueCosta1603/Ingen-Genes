package com.ingen.ingengenes.registry;

import com.ingen.ingengenes.InGenGenes;
import com.ingen.ingengenes.blockentity.DNAExtractorBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(InGenGenes.MODID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<DNAExtractorBlockEntity>> DNA_EXTRACTOR =
        BLOCK_ENTITIES.register("dna_extractor",
            () -> BlockEntityType.Builder.of(DNAExtractorBlockEntity::new, ModBlocks.DNA_EXTRACTOR.get()).build(null));

    private ModBlockEntities() {
    }
}
