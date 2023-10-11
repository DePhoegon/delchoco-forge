package com.dephoegon.delchoco.client;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.keybind.KeyBindManager;
import com.dephoegon.delchoco.client.models.armor.ChocoDisguiseModel;
import com.dephoegon.delchoco.client.models.entities.AdultChocoboModel;
import com.dephoegon.delchoco.client.models.entities.ChicoboModel;
import com.dephoegon.delchoco.client.renderer.entities.ChocoboRenderer;
import com.dephoegon.delchoco.common.init.ModEntities;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

public class ClientHandler {
    public static final ModelLayerLocation CHOCOBO = new ModelLayerLocation(new ResourceLocation(DelChoco.DELCHOCO_ID, "main"), "chocobo");
    public static final ModelLayerLocation CHICOBO = new ModelLayerLocation(new ResourceLocation(DelChoco.DELCHOCO_ID, "main"), "chicobo");
    public static final ModelLayerLocation CHOCO_DISGUISE = new ModelLayerLocation(new ResourceLocation(DelChoco.DELCHOCO_ID, "main"), "choco_disguise");

    public static void onClientSetup(final FMLClientSetupEvent ignoredEvent) {
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GYSAHL_GREEN.get(), RenderType.cutout());
        KeyBindManager.mapKeys();
    }
    public static void registerEntityRenders(EntityRenderersEvent.@NotNull RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.YELLOW_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.GREEN_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.BLUE_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.WHITE_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.BLACK_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLD_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.PINK_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.RED_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.PURPLE_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
        event.registerEntityRenderer(ModEntities.FLAME_SPAWNER_CHOCOBO.get(), ChocoboRenderer::new);
    }
    public static void registerLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHOCOBO, AdultChocoboModel::createBodyLayer);
        event.registerLayerDefinition(CHICOBO, ChicoboModel::createBodyLayer);
        event.registerLayerDefinition(CHOCO_DISGUISE, ChocoDisguiseModel::createArmorDefinition);
    }
}