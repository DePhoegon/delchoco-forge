package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
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

import static com.dephoegon.delchoco.common.init.ModRegistry.STONE_CHOCO_WEAPON;

public class LayerCollar extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private float hide;
	private float show;
	private static final Map<Integer, ResourceLocation> FEMALE_CHOCOBOS = Util.make(Maps.newHashMap(), (map) ->{
		map.put(1, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_black_collar.png"));
		map.put(2, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_brown_collar.png"));
		map.put(3, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_green_collar.png"));
		map.put(4, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_blue_collar.png"));
		map.put(5, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_purple_collar.png"));
		map.put(6, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_cyan_collar.png"));
		map.put(7, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_light_gray_collar.png"));
		map.put(8, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_gray_collar.png"));
		map.put(9, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_pink_collar.png"));
		map.put(10, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_lime_collar.png"));
		map.put(11, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_yellow_collar.png"));
		map.put(12, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_light_blue_collar.png"));
		map.put(13, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_magenta_collar.png"));
		map.put(14, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_orange_collar.png"));
		map.put(15, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_white_collar.png"));
		map.put(16, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_red_collar.png"));
	});
	private static final Map<Integer, ResourceLocation> MALE_CHOCOBOS = Util.make(Maps.newHashMap(), (map) ->{
		map.put(1, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_black_collar.png"));
		map.put(2, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_brown_collar.png"));
		map.put(3, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_green_collar.png"));
		map.put(4, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_blue_collar.png"));
		map.put(5, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_purple_collar.png"));
		map.put(6, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_cyan_collar.png"));
		map.put(7, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_light_gray_collar.png"));
		map.put(8, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_gray_collar.png"));
		map.put(9, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_pink_collar.png"));
		map.put(10, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_lime_collar.png"));
		map.put(11, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_yellow_collar.png"));
		map.put(12, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_light_blue_collar.png"));
		map.put(13, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_magenta_collar.png"));
		map.put(14, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_orange_collar.png"));
		map.put(15, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_white_collar.png"));
		map.put(16, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_red_collar.png"));
	});
	private static final Map<Integer, ResourceLocation> CHICOBOS = Util.make(Maps.newHashMap(), (map) ->{
		map.put(1, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/black_collar.png"));
		map.put(2, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/brown_collar.png"));
		map.put(3, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/green_collar.png"));
		map.put(4, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/blue_collar.png"));
		map.put(5, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/purple_collar.png"));
		map.put(6, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/cyan_collar.png"));
		map.put(7, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/light_gray_collar.png"));
		map.put(8, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/gray_collar.png"));
		map.put(9, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/pink_collar.png"));
		map.put(10, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/lime_collar.png"));
		map.put(11, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/yellow_collar.png"));
		map.put(12, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/light_blue_collar.png"));
		map.put(13, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/magenta_collar.png"));
		map.put(14, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/orange_collar.png"));
		map.put(15, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/white_collar.png"));
		map.put(16, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/red_collar.png"));
	});

	public LayerCollar(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn, float visibleAlpha, float invisibleAlpha) {
		super(rendererIn);
		this.hide = invisibleAlpha;
		this.show = visibleAlpha;
	}

	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

		int color = chocoboEntity.getCollarColor();
		ResourceLocation COLLAR = color != 0 ? chocoboEntity.isBaby() ? CHICOBOS.get(color) : chocoboEntity.isMale() ? MALE_CHOCOBOS.get(color) : FEMALE_CHOCOBOS.get(color) : null;
		float alpha = chocoboEntity.isInvisible() ? hide : show;
		if (chocoboEntity.isTame() && (COLLAR != null) && alpha != 0F) {
			VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(COLLAR, false));
			this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocoboEntity, 0F), 1F, 1F, 1F, alpha);
		}
	}
}
