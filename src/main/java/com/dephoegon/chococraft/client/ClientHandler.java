package com.dephoegon.chococraft.client;

import com.dephoegon.chococraft.Chococraft;
import com.dephoegon.chococraft.client.gui.NestScreen;
import com.dephoegon.chococraft.client.models.armor.ChocoDisguiseModel;
import com.dephoegon.chococraft.client.models.entities.AdultChocoboModel;
import com.dephoegon.chococraft.client.renderer.entities.ChocoboRenderer;
import com.dephoegon.chococraft.common.init.ModContainers;
import com.dephoegon.chococraft.common.init.ModEntities;
import com.dephoegon.chococraft.common.init.ModRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static final ModelLayerLocation CHOCOBO = new ModelLayerLocation(new ResourceLocation(Chococraft.MOD_ID, "main"), "chocobo");
    public static final ModelLayerLocation CHOCO_DISGUISE = new ModelLayerLocation(new ResourceLocation(Chococraft.MOD_ID, "main"), "choco_disguise");

    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GYSAHL_GREEN.get(), RenderType.cutout());

        MenuScreens.register(ModContainers.NEST.get(), NestScreen::new);
    }

    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CHOCOBO.get(), ChocoboRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHOCOBO, () -> AdultChocoboModel.createBodyLayer());
        event.registerLayerDefinition(CHOCO_DISGUISE, () -> ChocoDisguiseModel.createArmorDefinition());
    }

//    public void openChocoboInfoGui(EntityChocobo chocobo, PlayerEntity player) {
//        super.openChocoboInfoGui(chocobo, player);
//        Minecraft.getMinecraft().displayGuiScreen(new GuiChocoboInfo(chocobo, player));
//    }
//
//    public void openChocoBook(PlayerEntity player) {
//        super.openChocoBook(player);
//        Minecraft.getMinecraft().displayGuiScreen(new GuiChocoboBook(player));
//    }
}