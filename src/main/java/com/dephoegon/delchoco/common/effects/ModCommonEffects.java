package com.dephoegon.delchoco.common.effects;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.commands.chocoboTeams;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.utils.Log4jFilter;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEffects {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) { composable.addToList(); }
    @SubscribeEvent
    public void onRegisterCommandsEvent(@NotNull RegisterCommandsEvent event) { chocoboTeams.commands(event.getDispatcher()); }
}
