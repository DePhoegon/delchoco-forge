package com.dephoegon.delchoco;

import com.dephoegon.delchoco.common.configs.ChocoConfig;
import com.dephoegon.delchoco.common.configs.SpawnControl;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.init.ModRegistry;
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

import static com.dephoegon.delchoco.aid.chocoLists.*;

@Mod(DelChoco.DELCHOCO_ID)
public class DelChoco {
    public static final String DELCHOCO_ID = "delchoco";
    public final static Logger log = LogManager.getLogger(DELCHOCO_ID);
    public static final CreativeModeTab CHOCO_TAB = new CreativeModeTab(DELCHOCO_ID) { public @NotNull ItemStack makeIcon() { return new ItemStack(ModRegistry.GYSAHL_GREEN.get()); } };

    public DelChoco() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChocoConfig.commonSpec, "delchoco-chocobo_settings-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpawnControl.commonWorld, "delchoco-chocobo_spawning-common.toml");
        eventBus.addListener(this::commonSetup);
        chocoOrder(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
        forgeEventBus(MinecraftForge.EVENT_BUS);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> clientOnly(eventBus));
    }
    public void commonSetup(@NotNull FMLCommonSetupEvent event) {
        ModDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();
    }
}