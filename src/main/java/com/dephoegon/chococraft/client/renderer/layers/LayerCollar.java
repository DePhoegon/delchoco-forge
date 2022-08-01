package com.dephoegon.chococraft.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.chococraft.Chococraft;
import com.dephoegon.chococraft.client.models.entities.AdultChocoboModel;
import com.dephoegon.chococraft.common.entities.ChocoboEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerCollar extends RenderLayer<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> {
	private ResourceLocation COLLAR_CHOCOBO = new ResourceLocation(Chococraft.MOD_ID,"textures/entities/chocobos/collar.png");
	private ResourceLocation COLLAR_CHICOBO = new ResourceLocation(Chococraft.MOD_ID,"textures/entities/chicobos/collar.png");
	
	public LayerCollar(RenderLayerParent<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> rendererIn) {
		super(rendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ChocoboEntity chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (chocoboEntity.isTame() && !chocoboEntity.isInvisible()) {
			renderColoredCutoutModel(this.getParentModel(), chocoboEntity.isBaby()? COLLAR_CHICOBO : COLLAR_CHOCOBO, matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
		}
	}
}
