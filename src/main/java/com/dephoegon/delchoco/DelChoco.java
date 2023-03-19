package com.dephoegon.delchoco;

import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.events.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.init.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.aid.creativeTabAid.CHOCO_TAB;
import static com.dephoegon.delchoco.aid.creativeTabArrayLists.*;

@Mod(DelChoco.DELCHOCO_ID)
public class DelChoco {
    public static final String DELCHOCO_ID = "delchoco";
    public final static Logger log = LogManager.getLogger(DELCHOCO_ID);
    private void addCreative(CreativeModeTabEvent.@NotNull BuildContents event) {
        if(event.getTab() == CHOCO_TAB) { getChocoBlocks().forEach(event::accept); }
        if(event.getTab() == CHOCO_TAB) { getAllChocoboItems().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.COMBAT) { getChocoboArmors().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.COMBAT) { getChocoboWeapons().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.COMBAT) { getChocoboDisguiseItems().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS) { getChocoboFood().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.INGREDIENTS) { getChocoboMiscItems().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.SPAWN_EGGS) { getChocoboSpawnEggs().forEach(event::accept); }
        if(event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) { getChocoboSaddles().forEach(event::accept); }
    }

    public DelChoco() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChocoConfig.commonSpec);
        eventBus.register(ChocoConfig.class);

        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModRegistry.BLOCK_ENTITIES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new ChocoboCombatEffects());
        eventBus.addListener(this::addCreative);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerEntityRenders);
            eventBus.addListener(ClientHandler::registerLayerDefinitions);
        });
    }


}