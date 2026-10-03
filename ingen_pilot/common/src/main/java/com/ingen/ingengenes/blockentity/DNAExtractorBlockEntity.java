package com.ingen.ingengenes.blockentity;

import com.ingen.ingengenes.registry.ModBlockEntities;
import com.ingen.ingengenes.registry.ModItems;
import com.ingen.ingengenes.menu.DNAExtractorMenu;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Slots: 0 = fossil de entrada (dinosaur_fossil), 1 = reagente (extraction_buffer),
 * 2 = saida (raw_dna_sample). Logica de receita esta hardcoded neste v1 (piloto) -
 * um sistema de Recipe/RecipeType data-driven pode substituir isto depois que o
 * ciclo mecanico estiver validado em jogo.
 */
public class DNAExtractorBlockEntity extends BlockEntity implements Container, ExtendedMenuProvider {

    public static final int SLOT_FOSSIL = 0;
    public static final int SLOT_REAGENT = 1;
    public static final int SLOT_OUTPUT = 2;
    private static final int SLOT_COUNT = 3;

    public static final int MAX_PROGRESS = 200; // 10 segundos a 20 ticks/s

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private int progress = 0;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                progress = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public DNAExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DNA_EXTRACTOR.get(), pos, state);
    }

    // ---------------------------------------------------------------
    // Tick (server-only, chamado pelo Block.getTicker)
    // ---------------------------------------------------------------

    public static void serverTick(Level level, BlockPos pos, BlockState state, DNAExtractorBlockEntity be) {
        boolean canProcess = be.canProcess();

        if (canProcess) {
            be.progress++;
            if (be.progress >= MAX_PROGRESS) {
                be.progress = 0;
                be.processRecipe();
                be.setChanged();
            }
        } else if (be.progress > 0) {
            // sem insumo suficiente: regride o progresso em vez de travar parado
            be.progress = Math.max(0, be.progress - 2);
        }
    }

    private boolean canProcess() {
        ItemStack fossil = items.get(SLOT_FOSSIL);
        ItemStack reagent = items.get(SLOT_REAGENT);
        ItemStack output = items.get(SLOT_OUTPUT);

        boolean hasInputs = fossil.is(ModItems.DINOSAUR_FOSSIL.get()) && fossil.getCount() >= 1
            && reagent.is(ModItems.EXTRACTION_BUFFER.get()) && reagent.getCount() >= 1;
        if (!hasInputs) {
            return false;
        }

        if (output.isEmpty()) {
            return true;
        }
        return output.is(ModItems.RAW_DNA_SAMPLE.get()) && output.getCount() < output.getMaxStackSize();
    }

    private void processRecipe() {
        items.get(SLOT_FOSSIL).shrink(1);
        items.get(SLOT_REAGENT).shrink(1);

        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            items.set(SLOT_OUTPUT, new ItemStack(ModItems.RAW_DNA_SAMPLE.get(), 1));
        } else {
            output.grow(1);
        }
    }

    public void dropContents(Level level, BlockPos pos) {
        net.minecraft.world.Containers.dropContents(level, pos, items);
    }

    // ---------------------------------------------------------------
    // Container
    // ---------------------------------------------------------------

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_FOSSIL -> stack.is(ModItems.DINOSAUR_FOSSIL.get());
            case SLOT_REAGENT -> stack.is(ModItems.EXTRACTION_BUFFER.get());
            case SLOT_OUTPUT -> false; // saida nao aceita insercao manual
            default -> false;
        };
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    // ---------------------------------------------------------------
    // Persistencia
    // ---------------------------------------------------------------

    @Override
    protected void saveAdditional(net.minecraft.nbt.CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", progress);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(net.minecraft.nbt.CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        progress = tag.getInt("Progress");
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    // ---------------------------------------------------------------
    // Menu / GUI
    // ---------------------------------------------------------------

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ingen_genes.dna_extractor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new DNAExtractorMenu(containerId, playerInventory, this);
    }

    @Override
    public void saveExtraData(FriendlyByteBuf buf) {
        buf.writeBlockPos(worldPosition);
    }

    public ContainerData getContainerData() {
        return containerData;
    }
}
