package com.dephoegon.delchoco.client.renderer.entities;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.client.models.entities.AdultChocoboModel;
import com.dephoegon.delchoco.client.models.entities.ChicoboModel;
import com.dephoegon.delchoco.client.renderer.layers.*;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.dephoegon.delchoco.aid.util.chocoKB.hideChocoboMountInFirstPerson;
import static com.dephoegon.delchoco.aid.util.fallbackValues.*;
import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;

public class ChocoboRenderer extends MobRenderer<Chocobo, EntityModel<Chocobo>> {
    private static final Map<ChocoboColor, ResourceLocation> CHOCOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/yellow.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/green.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/blue.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/white.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/black.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/gold.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/pink.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/red.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/base/purple.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chocobos/flame/flame.png"));
    });
    private static final Map<ChocoboColor, ResourceLocation> CHICOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/yellow.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/green.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/blue.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/white.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/black.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/gold.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/pink.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/red.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/base/purple.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.DELCHOCO_ID, "textures/entities/chicobos/flame/flame.png"));
    });
    private final EntityModel<Chocobo> chicoboModel;
    private final EntityModel<Chocobo> chocoboModel = this.getModel();
    public static final float armorAlpha = 0.75F;
    public static final float weaponAlpha = 1;
    public static final float collarAlpha = 1;
    public static final float collarTellAlpha = .45F;
    public static final float saddleAlpha = 1;

    public ChocoboRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultChocoboModel<>(context.bakeLayer(ClientHandler.CHOCOBO)), .75f);
        this.chicoboModel = new ChicoboModel<>(context.bakeLayer(ClientHandler.CHICOBO));
        // Bottom is the Top Layer (from the outside)

        this.addLayer(new LayerCollar(this, collarAlpha, ChocoConfigGet(COMMON.collarInvisibility.get(), dCollarAlpha).floatValue()));
        this.addLayer(new LayerCollarTells(this, collarTellAlpha, ChocoConfigGet(COMMON.collarInvisibility.get(), dCollarAlpha).floatValue()));
        this.addLayer(new LayerMaleTrims(this, .85F));
        this.addLayer(new LayerChocoboTrims(this, .75F, .85F));
        this.addLayer(new LayerBeakClaws(this, true));
        this.addLayer(new LayerArmor(this, armorAlpha, ChocoConfigGet(COMMON.armorInvisibility.get(), dArmorAlpha).floatValue()));
        this.addLayer(new LayerSaddle(this, saddleAlpha, ChocoConfigGet(COMMON.saddleInvisibility.get(), dSaddleAlpha).floatValue()));
        this.addLayer(new LayerWeapon(this, weaponAlpha, ChocoConfigGet(COMMON.weaponInvisibility.get(), dWeaponAlpha).floatValue()));
    }
    public void render(@NotNull Chocobo chocobo, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = chocobo.isBaby() ? chicoboModel : chocoboModel;
        if (hideChocoboMountInFirstPerson(chocobo)) { return; }
        float factor = chocobo.getChocoboScale() == 0 ? 1 : chocobo.getChocoboScaleMod();
        poseStack.scale(factor, factor, factor);
        super.render(chocobo, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
    public @NotNull ResourceLocation getTextureLocation(@NotNull Chocobo chocobo) {
        ChocoboColor color = chocobo.getChocoboColor();
        return chocobo.isBaby() ? CHICOBO_PER_COLOR.get(color) : CHOCOBO_PER_COLOR.get(color);
    }
}