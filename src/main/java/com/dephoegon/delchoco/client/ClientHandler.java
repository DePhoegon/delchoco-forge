package com.dephoegon.delchoco.client;

import com.dephoegon.delchoco.DelChoco;
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

public class ClientHandler {
    public static final ModelLayerLocation CHOCOBO = new ModelLayerLocation(new ResourceLocation(DelChoco.MOD_ID, "main"), "chocobo");
    public static final ModelLayerLocation CHICOBO = new ModelLayerLocation(new ResourceLocation(DelChoco.MOD_ID, "main"), "chicobo");
    public static final ModelLayerLocation CHOCO_DISGUISE = new ModelLayerLocation(new ResourceLocation(DelChoco.MOD_ID, "main"), "choco_disguise");

    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GYSAHL_GREEN.get(), RenderType.cutout());
    }

    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CHOCOBO.get(), ChocoboRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHOCOBO, () -> AdultChocoboModel.createBodyLayer());
        event.registerLayerDefinition(CHICOBO, () -> ChicoboModel.createBodyLayer());
        event.registerLayerDefinition(CHOCO_DISGUISE, () -> ChocoDisguiseModel.createArmorDefinition());
    }
}