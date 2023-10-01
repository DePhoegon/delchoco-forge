package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.common.commands.chocoboTeams;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

public class forgeCommonEvents {
    public static void onRegisterCommands(@NotNull RegisterCommandsEvent event) {
        chocoboTeams.commands(event.getDispatcher());
    }
}