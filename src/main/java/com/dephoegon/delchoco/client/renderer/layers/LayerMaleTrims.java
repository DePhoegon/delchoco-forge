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
import org.jetbrains.annotations.NotNull;

public class LayerMaleTrims extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private final float alpha;

    private final ResourceLocation tamed = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/tamed_plumes/male.png");
    private final ResourceLocation notTamed = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/untamed_plumes/male.png");
    public LayerMaleTrims(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer, float layerAlpha) {
        super(pRenderer);
        this.alpha = layerAlpha;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocobo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!chocobo.isInvisible() && chocobo.isMale()) {
            if (!chocobo.isBaby()) {
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(chocobo.isTame() ? tamed : notTamed, false));
                this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocobo, 0F), 1F, 1F, 1F, alpha);
            }
        }
    }
}
