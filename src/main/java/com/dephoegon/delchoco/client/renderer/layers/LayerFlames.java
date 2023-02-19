package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class LayerFlames extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private static final ResourceLocation CHOCOBO_FLAME_EYES = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame/eyes.png");
    private static final ResourceLocation CHICOBO_FLAME_EYES = new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/flame/eyes.png");

    public LayerFlames(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Chocobo pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isFlame() && !pLivingEntity.isInvisible()) {
            ResourceLocation FLAME_EYES = pLivingEntity.isBaby() ? CHICOBO_FLAME_EYES : CHOCOBO_FLAME_EYES;
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(FLAME_EYES, false));
            this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0F), 1F, 1F, 1F, 1F);

        }
    }
}
