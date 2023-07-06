package com.dephoegon.delchoco.proxy;

import com.dephoegon.delchoco.client.gui.GuiStatViewer;
import com.dephoegon.delchoco.client.keybinds.KeybindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientProxy implements IProxy
{
    @SubscribeEvent
    public static void setupKeyMappings(RegisterKeyMappingsEvent event)
    {
        KeybindManager.registerKeyBinding(event);
    }

    @Override
    public Level getWorld()
    {
        return Minecraft.getInstance().level;
    }

    @Override
    public Player getPlayer()
    {
        return Minecraft.getInstance().player;
    }

    @Override
    public void displayStatViewer()
    {
        Minecraft.getInstance().setScreen(new GuiStatViewer(Minecraft.getInstance().player));
    }

}

