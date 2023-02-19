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

import static com.dephoegon.delchoco.common.init.ModRegistry.*;

public class LayerArmor extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private final float hide;
    private final float show;
    private static final Map<String, ResourceLocation> CHOCOBO_ARMORS = Util.make(Maps.newHashMap(), (map) -> {
        map.put(CHAIN_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/armor/chocobo_chain.png"));
        map.put(IRON_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/armor/chocobo_iron.png"));
        map.put(DIAMOND_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/armor/chocobo_diamond.png"));
        map.put(NETHERITE_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/armor/chocobo_netherite.png"));
    });
    public LayerArmor(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer, float visibleAlpha, float invisibleAlpha) {
        super(pRenderer);
        this.hide = invisibleAlpha;
        this.show = visibleAlpha;
    }

    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!chocoboEntity.isBaby()) {
            String armorID = chocoboEntity.isArmored() ? chocoboEntity.getArmor().getDescriptionId() : null;
            float alpha = chocoboEntity.isInvisible() ? hide : show;
            if (armorID != null && alpha != 0F) {
                VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(CHOCOBO_ARMORS.get(armorID), false));
                this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocoboEntity, 0F), 1F, 1F, 1F, alpha);
            }
        }
    }
}
