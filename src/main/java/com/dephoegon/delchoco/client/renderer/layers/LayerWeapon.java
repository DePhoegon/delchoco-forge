package com.dephoegon.delchoco.client.renderer.layers;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
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

public class LayerWeapon extends RenderLayer<Chocobo, EntityModel<Chocobo>> {
    private static final Map<String, ResourceLocation> CHOCOBO_WEAPONS = Util.make(Maps.newHashMap(), (map) -> {
        map.put(STONE_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_stone.png"));
        map.put(IRON_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_iron.png"));
        map.put(DIAMOND_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_diamond.png"));
        map.put(NETHERITE_CHOCO_WEAPON.get().getDescriptionId(), new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/chocobo_weapon_netherite.png"));
    });
    public LayerWeapon(RenderLayerParent<Chocobo, EntityModel<Chocobo>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull Chocobo chocoboEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        String weapon = chocoboEntity.isArmed() ? chocoboEntity.getWeapon().getDescriptionId() : null;
        // !chocoboEntity.isInvisible() - left out for now, to see how it looks.
        if (weapon != null && !chocoboEntity.isBaby()) {
            renderColoredCutoutModel(this.getParentModel(), CHOCOBO_WEAPONS.get(weapon), matrixStackIn, bufferIn, packedLightIn, chocoboEntity, 1.0F, 1.0F, 1.0F);
        }
    }
}
