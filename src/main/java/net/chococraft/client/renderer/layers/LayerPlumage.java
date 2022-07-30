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
import org.jetbrains.annotations.NotNull;

public class LayerPlumage extends RenderLayer<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> {

	public LayerPlumage(RenderLayerParent<ChocoboEntity, AdultChocoboModel<ChocoboEntity>> rendererIn) { super(rendererIn); }

	// Use logic to use different Plumage on tames opf different Colors
	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull ChocoboEntity chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (chocoboEntity.isTame() && !chocoboEntity.isInvisible()) {
			ChocoboColor color = chocoboEntity.getChocoboColor();
			String coloredPlumage = switch (color) {
				case YELLOW -> "textures/entities/chocobos/plumage_yellow.png";
				case GREEN -> "textures/entities/chocobos/plumage_green.png";
				case BLUE -> "textures/entities/chocobos/plumage_blue.png";
				case WHITE -> "textures/entities/chocobos/plumage_white.png";
				case BLACK -> "textures/entities/chocobos/plumage_black.png";
				case GOLD -> "textures/entities/chocobos/plumage.png";
				case PINK -> "textures/entities/chocobos/plumage_pink.png";
				case RED -> "textures/entities/chocobos/plumage_red.png";
				case PURPLE -> "textures/entities/chocobos/plumage_purple.png";
				case FLAME -> "textures/entities/chocobos/plumage_flame.png";
			};
			ResourceLocation PLUMAGE = new ResourceLocation(Chococraft.MOD_ID, coloredPlumage);
			renderColoredCutoutModel(this.getParentModel(), PLUMAGE, matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
		}
	}
}
