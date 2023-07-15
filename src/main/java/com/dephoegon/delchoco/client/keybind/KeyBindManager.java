package com.dephoegon.delchoco.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;

@Mod.EventBusSubscriber(modid = DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindManager {
    private KeyBindManager() { }
    public static KeyMapping inWaterGlide;
    public static KeyMapping stopAirGlide;

    @SubscribeEvent
    public static void registerKeyBinding(@NotNull RegisterKeyMappingsEvent event)
    {
        inWaterGlide = new KeyMapping("delchoco.key.in_water_glide", GLFW.GLFW_KEY_LEFT_ALT, "delchoco.key.category");
        stopAirGlide = new KeyMapping("delchoco.key.stop_in_air_glide",GLFW.GLFW_KEY_LEFT_SHIFT, "delchoco.key.category");

        event.register(inWaterGlide);
        event.register(stopAirGlide);
    }
}
