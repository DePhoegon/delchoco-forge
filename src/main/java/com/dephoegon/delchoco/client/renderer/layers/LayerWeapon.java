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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;

public class LayerWeapon extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private float hide;
    private float show;
    private static final Map<String, ResourceLocation> CHOCOBO_WEAPONS = Util.make(Maps.newHashMap(), (map) -> {
        map.put(STONE_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_stone.png"));
        map.put(IRON_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_iron.png"));
        map.put(DIAMOND_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_diamond.png"));
        map.put(NETHERITE_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_netherite.png"));
    });
    public LayerWeapon(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer, float visibleAlpha, float invisibleAlpha) {
        super(pRenderer);
        this.hide = invisibleAlpha;
        this.show = visibleAlpha;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        String weapon = chocoboEntity.isArmed() ? chocoboEntity.getWeapon().getDescriptionId() : null;
        float alpha = chocoboEntity.isInvisible() ? hide : show;
        if (weapon != null && !chocoboEntity.isBaby() && alpha != 0F) {
            VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(CHOCOBO_WEAPONS.get(weapon), false));
            this.getParentModel().renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(chocoboEntity, 0F), 1F, 1F, 1F, alpha);
        }
    }
}
