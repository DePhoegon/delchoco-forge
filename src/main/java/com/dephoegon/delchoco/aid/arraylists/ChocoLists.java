package com.dephoegon.delchoco.aid.arraylists;

import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.events.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.events.ModCommonEvents;
import com.dephoegon.delchoco.common.events.forgeCommonEvents;
import com.dephoegon.delchoco.common.handler.loot.modLootModifiers;
import com.dephoegon.delchoco.common.init.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.aid.util.creativeTabAid.CREATIVE_MODE_TABS_CHOCO;

public class ChocoLists {
    public static void chocoOrder(@NotNull IEventBus eventBus) {
        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModEntities.ENTITIES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);
        modLootModifiers.register(eventBus);
        eventBus.addListener(ModCommonEvents::registerEntityAttributes);
        eventBus.addListener(ModCommonEvents::addCreative);
        CREATIVE_MODE_TABS_CHOCO.register(eventBus);
    }
    public static void forgeEventBus(@NotNull IEventBus forgeBus) {
        forgeBus.register(new ChocoboCombatEffects());
        forgeBus.addListener(forgeCommonEvents::onRegisterCommands);
        forgeBus.addListener(ModCommonEvents::addCustomTrades);
        forgeBus.addListener(ModCommonEvents::onServerStartAddCompostItems);
    }
    @OnlyIn(Dist.CLIENT)
    public static void clientOnly(@NotNull IEventBus eventBus) {
        eventBus.addListener(ClientHandler::registerEntityRenders);
        eventBus.addListener(ClientHandler::registerLayerDefinitions);
    }
}