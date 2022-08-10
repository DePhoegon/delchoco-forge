package com.dephoegon.delchoco.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.dephoegon.delchoco.common.inventory.SaddleBagContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ChocoboInventoryScreen extends AbstractContainerScreen<SaddleBagContainer> {
    private static final ResourceLocation INV_TEXTURE_NULL = new ResourceLocation(DelChoco.MOD_ID, "textures/gui/chocobo_inventory_null.png");
    private static final ResourceLocation INV_TEXTURE_SMALL = new ResourceLocation(DelChoco.MOD_ID, "textures/gui/chocobo_inventory_small.png");
    private static final ResourceLocation INV_TEXTURE_LARGE = new ResourceLocation(DelChoco.MOD_ID, "textures/gui/chocobo_inventory_large.png");

    private Chocobo chocobo;

    public ChocoboInventoryScreen(SaddleBagContainer container, Inventory playerInventory, Chocobo chocobo) {
        super(container, playerInventory, chocobo.getDisplayName());

        this.imageWidth = 176;
        this.imageHeight = 204;
        this.chocobo = chocobo;
    }

    public static void openInventory(int windowId, Chocobo chocobo) {
        Player player = Minecraft.getInstance().player;
        SaddleBagContainer saddleContainer = new SaddleBagContainer(windowId, player.getInventory(), chocobo);
        player.containerMenu = saddleContainer;
        Minecraft.getInstance().setScreen(new ChocoboInventoryScreen(saddleContainer, player.getInventory(), chocobo));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        ItemStack saddleStack = chocobo.getSaddle();
        if(!saddleStack.isEmpty()){
            Item item = saddleStack.getItem();
            if(item == ModRegistry.CHOCOBO_SADDLE.get()) {
                RenderSystem.setShaderTexture(0, INV_TEXTURE_NULL);
            } else if(item == ModRegistry.CHOCOBO_SADDLE_BAGS.get()) {
                RenderSystem.setShaderTexture(0, INV_TEXTURE_SMALL);
            } else if(item == ModRegistry.CHOCOBO_SADDLE_PACK.get()) {
                RenderSystem.setShaderTexture(0, INV_TEXTURE_LARGE);
            }
        } else {
            RenderSystem.setShaderTexture(0, INV_TEXTURE_NULL);
        }

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(matrixStack, i - 24, j + 10, 0, 204, 27, 33);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.chocobo.getDisplayName().getString(), 8, 6, 0x888888);
        this.font.draw(matrixStack, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 0x888888);
    }
}
