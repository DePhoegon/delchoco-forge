package com.dephoegon.delchoco.client.gui;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.PLAYER_HEALTH;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, value = Dist.CLIENT)
public class RenderChocoboOverlay {
    private static final ResourceLocation ICONS = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/icons.png");

    @SubscribeEvent
    public static void onGuiInGameOverlayRender(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != PLAYER_HEALTH.type()) { return; }
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack matrixStack = event.getPoseStack();
        assert minecraft.player != null;
        Entity mountedEntity = minecraft.player.getVehicle();
        if (!(mountedEntity instanceof Chocobo chocobo)) { return; }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, ICONS);

        final int width = event.getWindow().getGuiScaledWidth();
        final int height = event.getWindow().getGuiScaledHeight();
        int left_align = width / 2 + 91;
        int top = height - 39; //right_height = 39
        top -= Math.ceil(chocobo.getHealth() / 20) * 10; //Offset it based on the amount of health rendered
        float staminaPercentage = chocobo.getStaminaPercentage() * 10;

        for (int i = 0; i < 10; ++i) {
            int x = left_align - i * 8 - 9;
            if (i >= staminaPercentage) {
                // render empty
                GuiComponent.blit(matrixStack, x, top, 0, 0, 9, 9, 32, 32);
            } else {
                if (i == ((int) staminaPercentage)) {
                    // draw partial
                    GuiComponent.blit(matrixStack, x, top, 0, 0, 9, 9, 32, 32);
                    int iconHeight = (int) (9 * (staminaPercentage - ((int) staminaPercentage)));
                    GuiComponent.blit(matrixStack, x, top + (9 - iconHeight), 0, 18 + (9 - iconHeight), 9, iconHeight, 32, 32);
                } else {
                    // draw full
                    GuiComponent.blit(matrixStack, x, top, 0, 18, 9, 9, 32, 32);
                }
            }
        }
    }
}
