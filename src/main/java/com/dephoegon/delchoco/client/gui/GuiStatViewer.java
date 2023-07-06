package com.dephoegon.delchoco.client.gui;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.handler.ChocoboHelper;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

public class GuiStatViewer extends Screen
{

	private int xSize = 176;
	private int ySize = 138;

	private static final ResourceLocation TEXTURE = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/gui/horse_stat_viewer.png");
	private IChocoboOwner owner;
	private Chocobo chocobo;

	private float speed;
	private float jumpHeight;
	private float health;
	private float maxHealth;
	private Vec3 lastPos;
	private final ResourceKey<Level> lastDim;

	private static final Method setColor = ObfuscationReflectionHelper.findMethod(Llama.class, "setSwag", DyeColor.class);

	private Minecraft mc = Minecraft.getInstance();

	public GuiStatViewer(Player player)
	{
		super(Component.literal("Horse Stat Viewer"));
		this.owner = ChocoboHelper.getOwnerCap(player);
		this.chocobo = Objects.requireNonNull(owner).createChocoboEntity(player.level);
		chocobo.getAttributes().load(owner.getChocoboNBT().getList("Attributes", 10)); // Read
					
		// attributes		
		this.chocobo.load(owner.getChocoboNBT());

		this.health = (float) (Math.floor(chocobo.getHealth()));
		this.maxHealth = (float) (Math.floor(chocobo.getMaxHealth() * 10) / 10);
		this.speed = (float) (Math.floor(chocobo.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 100) / 10);
		this.jumpHeight = (float) (Math.floor(chocobo.getJumpBoostPower() * 100) / 10);
		this.lastPos = owner.getLastSeenPosition();
		this.lastDim = owner.getLastSeenDim();
	}

	@Override
	public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(stack);

		// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		GuiComponent.blit(stack, i, j, 0, 0, this.xSize, this.ySize, 256, 256);

		super.render(stack, mouseX, mouseY, partialTicks);

		InventoryScreen.renderEntityInInventory(i + 43, j + 68, 25, (float) (i + 51) - mouseX, (float) (j + 75 - 50) - mouseY, this.chocobo);

		GuiComponent.drawString(stack, mc.font, this.chocobo.getName(), i + 84, j + 10, DyeColor.WHITE.getTextColor());

		GuiComponent.drawString(stack, mc.font, "Health:", i + 84, j + 30, DyeColor.LIGHT_GRAY.getTextColor());
		GuiComponent.drawString(stack, mc.font, health + "/" + maxHealth, i + 120, j + 30, DyeColor.WHITE.getTextColor());

		GuiComponent.drawString(stack, mc.font, "Speed:", i + 84, j + 45, DyeColor.LIGHT_GRAY.getTextColor());
		GuiComponent.drawString(stack, mc.font, speed + "", i + 120, j + 45, DyeColor.WHITE.getTextColor());

		GuiComponent.drawString(stack, mc.font, "Jump Height:", i + 84, j + 60, DyeColor.LIGHT_GRAY.getTextColor());
		GuiComponent.drawString(stack, mc.font, jumpHeight + "", i + 148, j + 60, DyeColor.WHITE.getTextColor());

		GuiComponent.drawString(stack, mc.font, "Last known position:" + "", i + 8, j + 84, DyeColor.LIGHT_GRAY.getTextColor());
		GuiComponent.drawString(stack, mc.font, lastPos.equals(Vec3.ZERO) ? "Unknown" : "xyz = " + lastPos.x() + " " + lastPos.y() + " " + lastPos.z(), i + 8, j + 94, DyeColor.WHITE.getTextColor());

		GuiComponent.drawString(stack, mc.font, "Last known dimension:" + "", i + 8, j + 110, DyeColor.LIGHT_GRAY.getTextColor());
		GuiComponent.drawString(stack, mc.font, this.lastDim.location().toString(), i + 8, j + 120, DyeColor.WHITE.getTextColor());

	}
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	@Override
	public boolean shouldCloseOnEsc()
	{
		return true;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{		
		if (this.mc.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, modifiers)))
		{
			assert this.mc.player != null;
			this.mc.player.closeContainer();
			return true;
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

}
