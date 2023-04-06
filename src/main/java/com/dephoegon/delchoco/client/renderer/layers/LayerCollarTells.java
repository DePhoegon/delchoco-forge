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

public class LayerCollarTells extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private final float hide;
    private final float show;
    private final ResourceLocation FEMALE_CHOCOBO = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/collars/f_tell.png");
    private final ResourceLocation MALE_CHOCOBO = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/collars/m_tell.png");
    private final ResourceLocation MALE_CHICOBO = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/collars/m_tell.png");
    private final ResourceLocation FEMALE_CHICOBO = new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/collars/f_tell.png");

    public LayerCollarTells(RenderLayerParent<Chocobo, EntityModel<Chocobo>> rendererIn, float visibleAlpha, float invisibleAlpha) {
        super(rendererIn);
        this.hide = invisibleAlpha;
        this.show = visibleAlpha;
    }

    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (chocoboEntity.isTame()) {
            ResourceLocation COLLAR = chocoboEntity.isBaby() ? chocoboEntity.isMale() ? MALE_CHICOBO : FEMALE_CHICOBO : chocoboEntity.isMale() ? MALE_CHOCOBO : FEMALE_CHOCOBO;
            float alpha = chocoboEntity.isInvisible() ? hide : show;
            if (alpha != 0F) {
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(COLLAR, false));
                this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocoboEntity, 0F), 1F, 1F, 1F, alpha);
            }
        }
    }
}
