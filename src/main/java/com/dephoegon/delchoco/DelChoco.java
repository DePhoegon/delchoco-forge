package com.dephoegon.delchoco;

import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.events.ChocoboCombatEffects;
import com.dephoegon.delchoco.common.events.ModCommonEvents;
import com.dephoegon.delchoco.common.init.*;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.utils.Log4jFilter;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(DelChoco.DELCHOCO_ID)
public class DelChoco {
    public static final String DELCHOCO_ID = "delchoco";
    public final static Logger log = LogManager.getLogger(DELCHOCO_ID);
    public static final CreativeModeTab CHOCO_TAB = new CreativeModeTab(DELCHOCO_ID) { public @NotNull ItemStack makeIcon() { return new ItemStack(ModRegistry.GYSAHL_GREEN.get()); } };

    public DelChoco() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChocoConfig.commonSpec);
        eventBus.register(ChocoConfig.class);
        eventBus.addListener(this::commonSetup);

        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModRegistry.BLOCK_ENTITIES.register(eventBus);
        ModEntities.ENTITY_TYPES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new ChocoboCombatEffects());
        MinecraftForge.EVENT_BUS.register(this);
        eventBus.addListener(ModCommonEvents::registerEntityAttributes);
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::addCustomTrades);
        MinecraftForge.EVENT_BUS.addListener(ModCommonEvents::onServerStartAddCompostItems);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerEntityRenders);
            eventBus.addListener(ClientHandler::registerLayerDefinitions);
        });
    }
    public void commonSetup(@NotNull FMLCommonSetupEvent event) {
        ModDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();
    }
}