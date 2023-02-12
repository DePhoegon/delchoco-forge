package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;

public class LayerArmor extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private static final Map<String, ResourceLocation> CHOCOBO_ARMORS = Util.make(Maps.newHashMap(), (map) -> {
        map.put(CHAIN_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_armor_chain.png"));
        map.put(IRON_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_armor_iron.png"));
        map.put(DIAMOND_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_armor_diamond.png"));
        map.put(NETHERITE_CHOCO_CHEST.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_armor_netherite.png"));
    });
    public LayerArmor(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer) {
        super(pRenderer);
    }

    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        String armor = chocoboEntity.isArmored() ? chocoboEntity.getArmor().getDescriptionId() : null;

        if (armor != null && !chocoboEntity.isBaby() && !chocoboEntity.isInvisible()) {
            renderColoredCutoutModel(this.getParentModel(), CHOCOBO_ARMORS.get(armor), matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
        }
    }
}
