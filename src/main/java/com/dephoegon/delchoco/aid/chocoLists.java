package com.dephoegon.delchoco.aid;

import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.events.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.events.ModCommonEvents;
import com.dephoegon.delchoco.common.events.forgeCommonEvents;
import com.dephoegon.delchoco.common.handler.loot.ModLootModifiers;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModEntities;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.dephoegon.delchoco.common.init.ModSounds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class chocoLists {
    public static void chocoOrder(@NotNull IEventBus eventBus) {
        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModEntities.ENTITIES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);
        ModLootModifiers.register(eventBus);
        eventBus.addListener(ModCommonEvents::registerEntityAttributes);
    }
    public static void forgeEventBus(@NotNull IEventBus forgeBus) {
        forgeBus.register(new ChocoboCombatEffects());
        forgeBus.addListener(forgeCommonEvents::onRegisterCommands);
        forgeBus.addListener(ModCommonEvents::addCustomTrades);
        forgeBus.addListener(ModCommonEvents::onServerStartAddCompostItems);
        forgeBus.addListener(ModCommonEvents::onCheckSpawn);
    }
    @OnlyIn(Dist.CLIENT)
    public static void clientOnly(@NotNull IEventBus eventBus) {
        eventBus.addListener(ClientHandler::registerEntityRenders);
        eventBus.addListener(ClientHandler::registerLayerDefinitions);
    }
}