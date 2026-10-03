package com.ingen.ingengenes.client;

import com.ingen.ingengenes.InGenGenes;
import com.ingen.ingengenes.menu.DNAExtractorMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DNAExtractorScreen extends AbstractContainerScreen<DNAExtractorMenu> {

    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath(InGenGenes.MODID, "textures/gui/dna_extractor.png");

    // posicao/tamanho da barra de progresso dentro da textura - ver
    // dna_extractor.png gerado (176x166, seta de 24x17 comecando em 176,0 na folha)
    private static final int PROGRESS_ARROW_X = 79;
    private static final int PROGRESS_ARROW_Y = 34;
    private static final int PROGRESS_ARROW_WIDTH = 24;
    private static final int PROGRESS_ARROW_HEIGHT = 17;

    public DNAExtractorScreen(DNAExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int x = this.leftPos;
        int y = this.topPos;
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int progress = this.menu.getProgress();
        int maxProgress = this.menu.getMaxProgress();
        int filledWidth = (progress * PROGRESS_ARROW_WIDTH) / maxProgress;
        if (filledWidth > 0) {
            graphics.blit(TEXTURE, x + PROGRESS_ARROW_X, y + PROGRESS_ARROW_Y,
                176, 0, filledWidth, PROGRESS_ARROW_HEIGHT);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        graphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
