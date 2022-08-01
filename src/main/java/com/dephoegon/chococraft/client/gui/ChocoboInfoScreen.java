package com.dephoegon.chococraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.chococraft.Chococraft;
import com.dephoegon.chococraft.common.ChocoConfig;
import com.dephoegon.chococraft.common.entities.ChocoboEntity;
import com.dephoegon.chococraft.common.init.ModAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ChocoboInfoScreen extends Screen {
    public final static ResourceLocation TEXTURE = new ResourceLocation(Chococraft.MOD_ID, "textures/gui/chocobo_stats.png");

    private final ChocoboEntity chocobo;
    private final Player player;

    private int xSize = 176;
    private int ySize = 89;
    private int guiLeft;
    private int guiTop;

    public ChocoboInfoScreen(ChocoboEntity chocobo, Player player) {
        super(new TranslatableComponent(player.getName().getString()));
        this.chocobo = chocobo;
        this.player = player;
    }

    public static void openScreen(ChocoboEntity chocobo, Player player) {
        Minecraft.getInstance().setScreen(new ChocoboInfoScreen(chocobo, player));
    }

    /*
	SKILL ID Numbers
	1	-	SPRINT
	2	-	GLIDE
	3	-	DIVE
	4	-	FLY
    */
    @SuppressWarnings("deprecated")
    @Override
    public void init() {
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;

        this.renderBackground(matrixStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        matrixStack.pushPose();
        matrixStack.translate(this.guiLeft, this.guiTop, 0);

        this.blit(matrixStack, 0, 0, 0, 0, this.xSize, this.ySize);

        String name = this.chocobo.getDisplayName().getString();
        int nameLength = this.font.width(name);
        this.font.drawShadow(matrixStack, name, (this.xSize / 2) - (nameLength / 2), 4, -1);

        String ownerText = I18n.get("gui.chocoinfo.text.not_tamed");
        if (chocobo.isTame()) {
            LivingEntity owner = chocobo.getOwner();
            if (owner == null)
                ownerText = I18n.get("gui.chocoinfo.text.unknown_owner");
            else
                ownerText = I18n.get("gui.chocoinfo.text.owner_format", owner.getDisplayName().getString());
        }
        int ownerTextLength = this.font.width(ownerText);
        this.font.drawShadow(matrixStack, ownerText, (this.xSize / 2) - (ownerTextLength / 2), 74, -1);

        this.drawGenderInfo(matrixStack);
        this.drawHealthInfo(matrixStack);
        this.drawSpeedInfo(matrixStack);
        this.drawStaminaInfo(matrixStack);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        this.drawHover(matrixStack, mouseX, mouseY);

        matrixStack.popPose();
    }

    private void updateButtonTextures() {}

    @Override
    public void tick() { }
    private int divideTwo(int iOne) {
        return (iOne/ 2);
    }

    private void drawGenderInfo(PoseStack matrixStack) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.pushPose();

            this.blit(matrixStack, 26, 18, 176, this.chocobo.isMale() ? 16: 0, 16, 16);

        String value = I18n.get(this.chocobo.isMale() ? "gui.chocoinfo.texture.male" : "gui.chocoinfo.texture.female");
        int width = this.font.width(value);
        this.font.drawShadow(matrixStack, value, 35 - divideTwo(width), 36, -1);
    }

    private void drawHealthInfo(PoseStack matrixStack) {
        String value = String.valueOf((int) this.chocobo.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
        int width = this.font.width(value);
        this.font.drawShadow(matrixStack, value, 70 - divideTwo(width), 36, -1);
    }

    private void drawSpeedInfo(PoseStack matrixStack) {
        String value = String.valueOf((int) Math.round(this.chocobo.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 100));
        int width = this.font.width(value);
        this.font.drawShadow(matrixStack, value, 106 - divideTwo(width), 36, -1);
    }

    private void drawStaminaInfo(PoseStack matrixStack) {
        String value = String.valueOf((int) this.chocobo.getAttribute(ModAttributes.MAX_STAMINA.get()).getBaseValue());
        int width = this.font.width(value);
        this.font.drawShadow(matrixStack, value, 142 - divideTwo(width), 36, -1);
    }

    private void drawHover(PoseStack poseStack, int mouseX, int mouseY) {
        if (mouseX >= 25 && mouseY >= 17 && mouseX < 25 + 18 && mouseY < 17 + 18) {
            List<Component> text = new ArrayList<>();
            text.add(new TranslatableComponent("gui.chocoinfo.texture.gender"));
            renderComponentTooltip(poseStack, text, mouseX, mouseY);
        }

        if (mouseX >= 61 && mouseY >= 17 && mouseX < 61 + 18 && mouseY < 17 + 18) {
            List<Component> text = new ArrayList<>();
            text.add(new TranslatableComponent("gui.chocoinfo.texture.health"));
            renderComponentTooltip(poseStack, text, mouseX, mouseY);
        }

        if (mouseX >= 97 && mouseY >= 17 && mouseX < 97 + 18 && mouseY < 17 + 18) {
            List<Component> text = new ArrayList<>();
            text.add(new TranslatableComponent("gui.chocoinfo.texture.speed"));
            renderComponentTooltip(poseStack, text, mouseX, mouseY);
        }

        if (mouseX >= 133 && mouseY >= 17 && mouseX < 133 + 18 && mouseY < 17 + 18) {
            List<Component> text = new ArrayList<>();
            text.add(new TranslatableComponent("gui.chocoinfo.texture.stamina"));
            renderComponentTooltip(poseStack, text, mouseX, mouseY);
        }
    }
}
