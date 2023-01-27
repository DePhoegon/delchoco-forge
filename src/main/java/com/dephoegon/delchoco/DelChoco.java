package com.dephoegon.delchoco;

import com.dephoegon.delchoco.aid.dyeList;
import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.commands.ChocoboCommand;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModContainers;
import com.dephoegon.delchoco.common.init.ModEntities;
import com.dephoegon.delchoco.common.init.ModRegistry;
import com.dephoegon.delchoco.common.init.ModSounds;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.common.world.worldgen.ModWorldgen;
import com.dephoegon.delchoco.utils.Log4jFilter;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod(DelChoco.MOD_ID)
public class DelChoco {
    public static final String MOD_ID = "delchoco";
    public final static Logger log = LogManager.getLogger(MOD_ID);
    public final static Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab creativeTab = new CreativeModeTab(MOD_ID) {
          @Override
        public @NotNull ItemStack makeIcon() { return new ItemStack(ModRegistry.GYSAHL_GREEN.get()); }
    };

    public DelChoco() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChocoConfig.commonSpec);
        eventBus.register(ChocoConfig.class);

        eventBus.addListener(this::setup);

        ModRegistry.BLOCKS.register(eventBus);
        ModRegistry.ITEMS.register(eventBus);
        ModRegistry.BLOCK_ENTITIES.register(eventBus);
        ModEntities.ENTITIES.register(eventBus);
        ModSounds.SOUND_EVENTS.register(eventBus);
        ModContainers.CONTAINERS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new ModWorldgen());
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommandsEvent);
        MinecraftForge.EVENT_BUS.addListener(ModEntities::addSpawns);
        eventBus.addListener(ModEntities::registerEntityAttributes);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::onClientSetup);
            eventBus.addListener(ClientHandler::registerEntityRenders);
            eventBus.addListener(ClientHandler::registerLayerDefinitions);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();
    }

    public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
        ChocoboCommand.initializeCommands(event.getDispatcher());
    }
}
