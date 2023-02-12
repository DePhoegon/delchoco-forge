package com.dephoegon.delchoco.client.renderer.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.dephoegon.delchoco.common.init.ModRegistry.STONE_CHOCO_WEAPON;

public class LayerPlumage extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private static final Map<ChocoboColor, ResourceLocation> CHOCOBO_PLUMES = Util.make(Maps.newHashMap(), (map) -> {
		map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_yellow.png"));
		map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_green.png"));
		map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_blue.png"));
		map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_black.png"));
		map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_white.png"));
		map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_gold.png"));
		map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_pink.png"));
		map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_red.png"));
		map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_purple.png"));
		map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/plumage_flame.png"));
	});
	public LayerPlumage(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn) { super(rendererIn); }

	// Use logic to use different Plumage on tames opf different Colors
	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (chocobo.isTame() && !chocobo.isInvisible() && !chocobo.isBaby()) {
			ChocoboColor color = chocobo.getChocoboColor();
			renderColoredCutoutModel(this.getParentModel(), CHOCOBO_PLUMES.get(color), matrixStackIn, bufferIn, packedLightIn, chocobo, 1.0F, 1.0F, 1.0F);
		}
	}
}
