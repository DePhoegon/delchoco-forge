package com.dephoegon.delchoco.aid;

import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.configs.ChocoConfig;
import com.dephoegon.delchoco.common.effects.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.effects.ModCommonEvents;
import com.dephoegon.delchoco.common.init.*;
import com.dephoegon.delchoco.common.world.worldgen.ModWorldgen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class chocoList {
    public static void chocoOrder(@NotNull IEventBus eventBus) {
        eventBus.register(ChocoConfig.class);
        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModRegistry.BLOCK_ENTITIES.register(eventBus);
        ModEntities.ENTITIES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);
        eventBus.addListener(ModEntities::registerEntityAttributes);
    }
    public static void forgeEventBus(@NotNull IEventBus eventBus) {
        eventBus.register(new ModWorldgen());
        eventBus.register(new ChocoboCombatEffects());
        eventBus.addListener(ModCommonEvents::onRegisterCommandsEvent);
        eventBus.addListener(ModEntities::addSpawns);
        eventBus.addListener(ModCommonEvents::onWorldLoad);
        eventBus.addListener(ModCommonEvents::addCustomTrades);
    }
    @OnlyIn(Dist.CLIENT)
    public static void clientOnly(@NotNull IEventBus eventBus) {
        eventBus.addListener(ClientHandler::onClientSetup);
        eventBus.addListener(ClientHandler::registerEntityRenders);
        eventBus.addListener(ClientHandler::registerLayerDefinitions);
    }
}