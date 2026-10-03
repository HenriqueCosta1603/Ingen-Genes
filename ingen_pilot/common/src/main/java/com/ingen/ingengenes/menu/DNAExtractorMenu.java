package com.ingen.ingengenes.menu;

import com.ingen.ingengenes.blockentity.DNAExtractorBlockEntity;
import com.ingen.ingengenes.registry.ModItems;
import com.ingen.ingengenes.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DNAExtractorMenu extends AbstractContainerMenu {

    private static final int MACHINE_SLOT_COUNT = 3;

    private final DNAExtractorBlockEntity blockEntity;
    private final ContainerData containerData;

    public DNAExtractorMenu(int containerId, Inventory playerInventory, DNAExtractorBlockEntity blockEntity) {
        super(ModMenuTypes.DNA_EXTRACTOR.get(), containerId);
        this.blockEntity = blockEntity;
        this.containerData = blockEntity.getContainerData();

        // slots da maquina - coordenadas pensadas para uma textura 176x166 padrao
        this.addSlot(new Slot(blockEntity, DNAExtractorBlockEntity.SLOT_FOSSIL, 44, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.DINOSAUR_FOSSIL.get());
            }
        });
        this.addSlot(new Slot(blockEntity, DNAExtractorBlockEntity.SLOT_REAGENT, 44, 61) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.EXTRACTION_BUFFER.get());
            }
        });
        this.addSlot(new Slot(blockEntity, DNAExtractorBlockEntity.SLOT_OUTPUT, 116, 48) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // inventario do jogador (3 linhas + hotbar), padrao vanilla
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.addDataSlots(containerData);
    }

    public int getProgress() {
        return containerData.get(0);
    }

    public int getMaxProgress() {
        return Math.max(1, containerData.get(1));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return result;
        }

        ItemStack slotStack = slot.getItem();
        result = slotStack.copy();

        if (index < MACHINE_SLOT_COUNT) {
            // saindo da maquina -> tenta mandar pro inventario do jogador
            if (!this.moveItemStackTo(slotStack, MACHINE_SLOT_COUNT, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // saindo do inventario do jogador -> tenta encaixar no slot certo da maquina
            if (slotStack.is(ModItems.DINOSAUR_FOSSIL.get())) {
                if (!this.moveItemStackTo(slotStack, DNAExtractorBlockEntity.SLOT_FOSSIL, DNAExtractorBlockEntity.SLOT_FOSSIL + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotStack.is(ModItems.EXTRACTION_BUFFER.get())) {
                if (!this.moveItemStackTo(slotStack, DNAExtractorBlockEntity.SLOT_REAGENT, DNAExtractorBlockEntity.SLOT_REAGENT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (slotStack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, slotStack);
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }
}
