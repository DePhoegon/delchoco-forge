package com.dephoegon.delchoco.aid.util;

import com.dephoegon.delchoco.common.events.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.events.ModCommonEvents;
import com.dephoegon.delchoco.common.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import static com.dephoegon.delchoco.aid.util.creativeTabAid.CREATIVE_MODE_TABS_CHOCO;

public class regList {
    public static void theEventBusSubscribe(Object passThrough) {
        MinecraftForge.EVENT_BUS.register(new ChocoboCombatEffects());
        MinecraftForge.EVENT_BUS.register(passThrough);
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::addCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::onServerStartAddCompostItems);
    }
    public static void attributes(IEventBus eventBus) {
        ModAttributes.ATTRIBUTES.register(eventBus);
        eventBus.addListener(ModCommonEvents::registerEntityAttributes);

    }
    public static void modBlocksItems(IEventBus eventBus) {
        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
    }
    public static void entityRelated(IEventBus eventBus) {
        ModContainers.CONTAINERS.register(eventBus);
        ModRegistry.BLOCK_ENTITIES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        CREATIVE_MODE_TABS_CHOCO.register(eventBus);

    }
}
