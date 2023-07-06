package com.dephoegon.delchoco.client.event;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.keybinds.KeybindManager;
import com.dephoegon.delchoco.common.network.PressKeyPacket;
import com.dephoegon.delchoco.common.world.config.ChocoCaller;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = DelChoco.DELCHOCO_ID)
public class KeybindEvents
{
	private static long lastPressTime = 0;

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onPlayerTick(PlayerTickEvent event)
	{
		Player player = event.player;

		if (player != null && event.side == LogicalSide.CLIENT)
		{
			boolean callHorse = KeybindManager.callHorse.isDown();
			boolean setHorse = KeybindManager.setHorse.isDown();
			boolean showStats = ChocoCaller.choco_call.enableStatsViewer.get() && KeybindManager.showStats.isDown();

			if (callHorse)
			{
				if (System.currentTimeMillis() - lastPressTime > 500)
				{
					lastPressTime = System.currentTimeMillis();
					DelChoco.network.sendToServer(new PressKeyPacket(0));
				}
			}

			if (setHorse)
			{
				if (System.currentTimeMillis() - lastPressTime > 500)
				{
					lastPressTime = System.currentTimeMillis();
					DelChoco.network.sendToServer(new PressKeyPacket(1));
				}
			}
			
			if (showStats)
			{
				if (System.currentTimeMillis() - lastPressTime > 500)
				{
					lastPressTime = System.currentTimeMillis();
					DelChoco.network.sendToServer(new PressKeyPacket(2));
				}
			}
		}
	}

}
