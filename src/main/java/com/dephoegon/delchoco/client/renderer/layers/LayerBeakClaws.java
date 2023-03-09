package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LayerBeakClaws extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private final boolean boxedBeak;
    private static final String curved = "curved";
    private static final String boxed = "boxed";
    private static final Map<String, ResourceLocation> CHOCOBO_BEAKS = Util.make(Maps.newHashMap(), (map) -> {
        map.put(curved, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame/curved.png"));
        map.put(boxed, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame/boxed.png"));
    });
    public LayerBeakClaws(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer, boolean isBoxedBeak) {
        super(pRenderer);
        this.boxedBeak = isBoxedBeak;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!chocoboEntity.isBaby()) {
            String beak = boxedBeak ? boxed : curved;
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityCutout(CHOCOBO_BEAKS.get(beak)));
                this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocoboEntity, 0F), 1F, 1F, 1F, 1F);
        }
    }
}
