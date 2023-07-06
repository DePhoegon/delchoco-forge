package com.dephoegon.delchoco.client.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeybindManager
{

	public static KeyMapping setHorse;
	public static KeyMapping callHorse;
	public static KeyMapping showStats;

	@OnlyIn(Dist.CLIENT)
	public static void registerKeyBinding(RegisterKeyMappingsEvent event)
	{
		setHorse = new KeyMapping("key.set_chocobo.desc", GLFW.GLFW_KEY_P, "key.callable_chocobos.category");
		callHorse = new KeyMapping("key.call_chocobo.desc",GLFW.GLFW_KEY_V, "key.callable_chocobos.category");
		showStats = new KeyMapping("key.show_stats.desc", GLFW.GLFW_KEY_K, "key.callable_chocobos.category");

		event.register(setHorse);
		event.register(callHorse);
		event.register(showStats);
	}

}
