package com.dephoegon.delchoco.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class LayerSaddle extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private float hide;
	private float show;
	private ResourceLocation SADDLE = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle_alt.png");
	private ResourceLocation SADDLE_BAG = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle_bag_alt.png");
	private ResourceLocation PACK_BAG = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/pack_bag_alt.png");

	public LayerSaddle(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn, float visibleAlpha, float invisibleAlpha) {
		super(rendererIn);
		this.hide = invisibleAlpha;
		this.show = visibleAlpha;
	}

	@Override
	public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		float alpha = chocobo.isInvisible() ? hide : show;
		if(chocobo.isSaddled() && !chocobo.isBaby() && alpha != 0F) {
			ItemStack saddleStack = chocobo.getSaddle();
			ResourceLocation saddleTexture = null;
			if(!saddleStack.isEmpty()){
				Item item = saddleStack.getItem();
				if(item == ModRegistry.CHOCOBO_SADDLE.get()) { saddleTexture = SADDLE; }
				if(item == ModRegistry.CHOCOBO_SADDLE_BAGS.get()) { saddleTexture = SADDLE_BAG; }
				if(item == ModRegistry.CHOCOBO_SADDLE_PACK.get()) { saddleTexture = PACK_BAG; }
			}
			assert saddleTexture != null;
			VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(saddleTexture, saddleTexture != SADDLE));
			this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocobo, 0F), 1F, 1F, 1F, alpha);
		}
	}
}
