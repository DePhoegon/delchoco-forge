package com.dephoegon.delchoco.client.renderer.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LayerChocoboTrims extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private final float tamedShow;
	private final float untamedShow;
	private static final Map<ChocoboColor, ResourceLocation> TAMED_CHOCOBO_PLUMES = Util.make(Maps.newHashMap(), (map) -> {
		map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_yellow.png"));
		map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_green.png"));
		map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_blue.png"));
		map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_black.png"));
		map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_white.png"));
		map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_gold.png"));
		map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_pink.png"));
		map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_red.png"));
		map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_purple.png"));
		map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/tamed_plumes/tamed_flame.png"));
	});
	private static final Map<ChocoboColor, ResourceLocation> UNTAMED_CHOCOBO_PLUMES = Util.make(Maps.newHashMap(), (map) -> {
		map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/yellow.png"));
		map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/green.png"));
		map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/blue.png"));
		map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/black.png"));
		map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/white.png"));
		map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/gold.png"));
		map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/pink.png"));
		map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/red.png"));
		map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/purple.png"));
		map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/flame.png"));

	});
	private static final ResourceLocation CHOCOBO_FLAME_EYES = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame/eyes.png");
	private static final ResourceLocation CHICOBO_FLAME_EYES = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/flame/eyes.png");
	public LayerChocoboTrims(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn, float tamedAlpha, float untamedAlpha) {
		super(rendererIn);
		this.tamedShow = tamedAlpha;
		this.untamedShow = untamedAlpha;
	}

	// Use logic to use different Plumage on tames of different Colors
	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!chocobo.isInvisible()) {
			if (!chocobo.isBaby()) {
				rSwitch(matrixStackIn, bufferIn, packedLightIn, chocobo, untamedShow, UNTAMED_CHOCOBO_PLUMES.get(chocobo.getChocoboColor()), true);
				if (chocobo.isTame()) { rSwitch(matrixStackIn, bufferIn, packedLightIn, chocobo, tamedShow, TAMED_CHOCOBO_PLUMES.get(chocobo.getChocoboColor()), false); }
			}
			if (chocobo.nonFlameFireImmune()) { rSwitch(matrixStackIn, bufferIn, packedLightIn, chocobo, 1F, chocobo.isBaby() ? CHICOBO_FLAME_EYES : CHOCOBO_FLAME_EYES, false); }
		}
	}
	private void rSwitch(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocobo, float alpha, ResourceLocation layer, boolean outline) {
		VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(layer, outline));
		this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocobo, 0F), 1F, 1F, 1F, alpha);
	}
}
