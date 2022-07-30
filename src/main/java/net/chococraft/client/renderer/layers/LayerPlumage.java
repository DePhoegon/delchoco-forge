package net.chococraft.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.chococraft.Chococraft;
import net.chococraft.client.models.entities.AdultChocoboModel;
import net.chococraft.common.entities.ChocoboEntity;
import net.chococraft.common.entities.properties.ChocoboColor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerPlumage extends RenderLayer<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> {

	public LayerPlumage(RenderLayerParent<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> rendererIn) {
		super(rendererIn);
	}

	// Use logic to use different Plumage on tames opf different Colors
	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ChocoboEntity chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (chocoboEntity.isTame() && !chocoboEntity.isInvisible()) {
			String coloredPlumage = "textures/entities/chocobos/plumage.png";
			ChocoboColor color = chocoboEntity.getChocoboColor();
			switch (color) {
				case GREEN -> coloredPlumage = "textures/entities/chocobos/plumage_green.png";
				case BLUE -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case WHITE -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case BLACK -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case GOLD -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case PINK -> coloredPlumage = "textures/entities/chocobos/plumage_pink.png";
				case RED -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case PURPLE -> coloredPlumage = "textures/entities/chocobos/plumage.png";
				case FLAME -> coloredPlumage = "textures/entities/chocobos/plumage.png";
			}
			ResourceLocation PLUMAGE = new ResourceLocation(Chococraft.MOD_ID, coloredPlumage);
			renderColoredCutoutModel(this.getParentModel(), PLUMAGE, matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
		}
	}
}
