package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
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

import java.util.Map;

public class LayerUntamedPlumage extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private float show;
    private static final Map<ChocoboColor, ResourceLocation> CHOCOBO_PLUMES = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/yellow.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/green.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/blue.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/black.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/white.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/gold.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/pink.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/red.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/purple.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/untamed_plumes/flame.png"));

    });
    public LayerUntamedPlumage(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer, float visibleAlpha) {
        super(pRenderer);
        this.show = visibleAlpha;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Chocobo chocobo, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!chocobo.isBaby() && !chocobo.isInvisible()) {
            ChocoboColor color = chocobo.getChocoboColor();
            float alpha = show;
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(CHOCOBO_PLUMES.get(color), true));
            this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(chocobo, 0F), 1F, 1F, 1F, alpha);
        }
    }
}
