package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class LayerSaddle extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private final float hide;
	private final float show;
	private static final Map<Item, ResourceLocation> CHOCOBO_SADDLES = Util.make(Maps.newHashMap(), (map) ->{
		map.put(ModRegistry.CHOCOBO_SADDLE.get(), new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle.png"));
		map.put(ModRegistry.CHOCOBO_SADDLE_BAGS.get(), new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle_bag.png"));
		map.put(ModRegistry.CHOCOBO_SADDLE_PACK.get(),new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/pack_bag.png"));
	});

	public LayerSaddle(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn, float visibleAlpha, float invisibleAlpha) {
		super(rendererIn);
		this.hide = invisibleAlpha;
		this.show = visibleAlpha;
	}

	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!chocobo.isBaby()) {
			float alpha = chocobo.isInvisible() ? hide : show;
			if (chocobo.getSaddle().getItem() instanceof ChocoboSaddleItem saddleItem && alpha != 0F) {
				VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(CHOCOBO_SADDLES.get(saddleItem), saddleItem.hasOutline()));
				this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocobo, 0F), 1F, 1F, 1F, alpha);
			}
		}
	}
}
