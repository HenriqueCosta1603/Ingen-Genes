package com.ingen.ingengenes.registry;

import com.ingen.ingengenes.InGenGenes;
import com.ingen.ingengenes.blockentity.DNAExtractorBlockEntity;
import com.ingen.ingengenes.menu.DNAExtractorMenu;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * MenuRegistry.ofExtended() e o jeito Architectury de abrir uma GUI que
 * precisa de dado extra (aqui, a BlockPos da maquina) sem escrever
 * CustomPacketPayload manual por loader - o Architectury cuida do envio
 * dessa BlockPos nos tres loaders por baixo.
 *
 * Se o build local reclamar da assinatura de ofExtended(...), essa e a
 * classe mais provavel de precisar de ajuste (API do Architectury pode
 * ter mudado o nome do metodo entre versoes 3.4.x).
 */
public final class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(InGenGenes.MODID, Registries.MENU);

    public static final RegistrySupplier<MenuType<DNAExtractorMenu>> DNA_EXTRACTOR =
        MENUS.register("dna_extractor", () -> MenuRegistry.ofExtended((syncId, inventory, buf) -> {
            BlockPos pos = buf.readBlockPos();
            BlockEntity be = inventory.player.level().getBlockEntity(pos);
            return new DNAExtractorMenu(syncId, inventory, (DNAExtractorBlockEntity) be);
        }));

    private ModMenuTypes() {
    }
}
