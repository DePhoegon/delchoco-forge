package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerCollar extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private final ResourceLocation COLLAR_CHOCOBO_F_BLACK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_black_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_BROWN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_brown_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_GREEN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_green_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_blue_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_PURPLE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_purple_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_CYAN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_cyan_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_LIGHT_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_light_gray_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_gray_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_PINK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_pink_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_LIME = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_lime_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_YELLOW = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_yellow_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_LIGHT_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_light_blue_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_MAGENTA = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_magenta_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_ORANGE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_orange_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_WHITE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_white_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_F_RED = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/f_red_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_BLACK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_black_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_BROWN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_brown_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_GREEN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_green_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_blue_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_PURPLE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_purple_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_CYAN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_cyan_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_LIGHT_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_light_gray_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_gray_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_PINK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_pink_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_LIME = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_lime_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_YELLOW = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_yellow_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_LIGHT_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_light_blue_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_MAGENTA = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_magenta_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_ORANGE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_orange_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_WHITE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_white_collar.png");
	private final ResourceLocation COLLAR_CHOCOBO_M_RED = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/m_red_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_BLACK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/black_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_BROWN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/brown_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_GREEN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/green_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/blue_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_PURPLE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/purple_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_CYAN = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/cyan_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_LIGHT_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/light_gray_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_GRAY = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/gray_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_PINK = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/pink_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_LIME = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/lime_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_YELLOW = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/yellow_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_LIGHT_BLUE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/light_blue_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_MAGENTA = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/magenta_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_ORANGE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/orange_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_WHITE = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/white_collar.png");
	private final ResourceLocation COLLAR_CHICOBO_RED = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/red_collar.png");

	public LayerCollar(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn) {
		super(rendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ResourceLocation COLLAR_CHICOBO;
		ResourceLocation COLLAR_CHOCOBO;
		switch (chocoboEntity.getCollarColor()) {
			case 15 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_WHITE;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_WHITE;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_WHITE; }
			}
			case 14 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_ORANGE;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_ORANGE;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_ORANGE; }
			}
			case 13 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_MAGENTA;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_MAGENTA;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_MAGENTA; }
			}
			case 12 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_LIGHT_BLUE;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_LIGHT_BLUE;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_LIGHT_BLUE; }
			}
			case 11 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_YELLOW;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_YELLOW;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_YELLOW; }
			}
			case 10 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_LIME;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_LIME;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_LIME; }
			}
			case 9 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_PINK;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_PINK;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_PINK; }
			}
			case 8 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_GRAY;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_GRAY;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_GRAY; }
			}
			case 7 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_LIGHT_GRAY;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_LIGHT_GRAY;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_LIGHT_GRAY; }
			}
			case 6 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_CYAN;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_CYAN;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_CYAN; }
			}
			case 5 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_PURPLE;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_PURPLE;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_PURPLE; }
			}
			case 4 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_BLUE;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_BLUE;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_BLUE; }
			}
			case 3 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_GREEN;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_GREEN;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_GREEN; }
			}
			case 2 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_BROWN;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_BROWN;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_BROWN; }
			}
			case 1 -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_BLACK;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_BLACK;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_BLACK; }
			}
			default -> {
				COLLAR_CHICOBO = COLLAR_CHICOBO_RED;
				if (chocoboEntity.isMale()) {
					COLLAR_CHOCOBO = COLLAR_CHOCOBO_M_RED;
				} else { COLLAR_CHOCOBO = COLLAR_CHOCOBO_F_RED; }
			}
		}

		if (chocoboEntity.isTame() && !chocoboEntity.isInvisible()) {
			renderColoredCutoutModel(this.getParentModel(), chocoboEntity.isBaby() ? COLLAR_CHICOBO : COLLAR_CHOCOBO, matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
		}
	}
}
