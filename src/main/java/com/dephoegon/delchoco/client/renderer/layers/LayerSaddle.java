package com.dephoegon.delchoco.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LayerSaddle extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
	private ResourceLocation SADDLE = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle_alt.png");
	private ResourceLocation SADDLE_BAG = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/saddle_bag_alt.png");
	private ResourceLocation PACK_BAG = new ResourceLocation(DelChoco.MOD_ID,"textures/entities/chocobos/pack_bag_alt.png");
	
	public LayerSaddle(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn) {
		super(rendererIn);
	}

	@Override
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(!chocobo.isInvisible() && chocobo.isSaddled() && !chocobo.isBaby()) {
			ResourceLocation saddleTexture = null;
			ItemStack saddleStack = chocobo.getSaddle();
			if(!saddleStack.isEmpty()){
				Item item = saddleStack.getItem();
				if(item == ModRegistry.CHOCOBO_SADDLE.get()) {
					saddleTexture = SADDLE;
				} else if(item == ModRegistry.CHOCOBO_SADDLE_BAGS.get()) {
					saddleTexture = SADDLE_BAG;
				} else if(item == ModRegistry.CHOCOBO_SADDLE_PACK.get()) {
					saddleTexture = PACK_BAG;
				}
			}

			renderColoredCutoutModel(this.getParentModel(), saddleTexture, matrixStackIn, bufferIn, packedLightIn, chocobo, 1.0F, 1.0F, 1.0F);
		}
	}
}
