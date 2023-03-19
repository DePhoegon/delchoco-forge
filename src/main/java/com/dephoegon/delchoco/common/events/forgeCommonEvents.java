package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.common.commands.chocoboTeams;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class forgeCommonEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        chocoboTeams.commands(event.getDispatcher());
    }
}
