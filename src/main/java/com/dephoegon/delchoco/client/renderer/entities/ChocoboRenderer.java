package com.dephoegon.delchoco.client.renderer.entities;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.client.models.entities.AdultChocoboModel;
import com.dephoegon.delchoco.client.models.entities.ChicoboModel;
import com.dephoegon.delchoco.client.renderer.layers.LayerCollar;
import com.dephoegon.delchoco.client.renderer.layers.LayerPlumage;
import com.dephoegon.delchoco.client.renderer.layers.LayerSaddle;
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

public class ChocoboRenderer extends MobRenderer<Chocobo, EntityModel<Chocobo>> {
    private static final Map<ChocoboColor, ResourceLocation> CHOCOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/yellow_chocobo.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/green_chocobo.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/blue_chocobo.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/white_chocobo.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/black_chocobo.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/gold_chocobo.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/pink_chocobo.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/red_chocobo.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/purple_chocobo.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame_chocobo.png"));
    });
    private static final Map<ChocoboColor, ResourceLocation> FLAME_CHOCOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/yellow_flame.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/green_flame.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/blue_flame.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/white_flame.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/black_flame.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/gold_flame.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/pink_flame.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/red_flame.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/purple_flame.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chocobos/flame_chocobo.png"));
    });
    private static final Map<ChocoboColor, ResourceLocation> CHICOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/yellow_chocobo.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/green_chocobo.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/blue_chocobo.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/white_chocobo.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/black_chocobo.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/gold_chocobo.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/pink_chocobo.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/red_chocobo.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/purple_chocobo.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/flame_chocobo.png"));
    });
    private static final Map<ChocoboColor, ResourceLocation> FLAME_CHICOBO_PER_COLOR = Util.make(Maps.newHashMap(), (map) -> {
        map.put(ChocoboColor.YELLOW, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/yellow_flame.png"));
        map.put(ChocoboColor.GREEN, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/green_flame.png"));
        map.put(ChocoboColor.BLUE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/blue_flame.png"));
        map.put(ChocoboColor.WHITE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/white_flame.png"));
        map.put(ChocoboColor.BLACK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/black_flame.png"));
        map.put(ChocoboColor.GOLD, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/gold_flame.png"));
        map.put(ChocoboColor.PINK, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/pink_flame.png"));
        map.put(ChocoboColor.RED, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/red_flame.png"));
        map.put(ChocoboColor.PURPLE, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/purple_flame.png"));
        map.put(ChocoboColor.FLAME, new ResourceLocation(DelChoco.MOD_ID, "textures/entities/chicobos/flame_chocobo.png"));
    });
    private final EntityModel<Chocobo> chicoboModel;
    private final EntityModel<Chocobo> chocoboModel = this.getModel();

    public ChocoboRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultChocoboModel<>(context.bakeLayer(ClientHandler.CHOCOBO)), 1.0f);
        this.chicoboModel = new ChicoboModel<>(context.bakeLayer(ClientHandler.CHICOBO));

        this.addLayer(new LayerCollar(this));
        this.addLayer(new LayerPlumage(this));
        this.addLayer(new LayerSaddle(this));
    }

    @Override
    public void render(@NotNull Chocobo chocobo, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        this.model = chocobo.isBaby() ? chicoboModel : chocoboModel;
        super.render(chocobo, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Chocobo chocobo) {
        ChocoboColor color = chocobo.getChocoboColor();
        boolean fire = chocobo.fireImmune();
        if (fire) { return chocobo.isBaby() ? FLAME_CHICOBO_PER_COLOR.get(color) : FLAME_CHOCOBO_PER_COLOR.get(color); } else
        { return chocobo.isBaby() ? CHICOBO_PER_COLOR.get(color) : CHOCOBO_PER_COLOR.get(color); }
    }
}