package com.dephoegon.delchoco.client.keybind;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;

@Mod.EventBusSubscriber(modid = DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindManager {
    private KeyBindManager() { }
    public static final Lazy<KeyMapping> inWaterGlide = Lazy.of(() -> new KeyMapping("delchoco.key.in_water_glide", GLFW.GLFW_KEY_LEFT_ALT, "delchoco.key.category"));
    public static final Lazy<KeyMapping> stopAirGlide = Lazy.of(() -> new KeyMapping("delchoco.key.stop_in_air_glide", GLFW.GLFW_KEY_LEFT_SHIFT, "delchoco.key.category"));

    @SubscribeEvent
    public static void registerKeyBinding(@NotNull RegisterKeyMappingsEvent event)
    {
        event.register(inWaterGlide.get());
        event.register(stopAirGlide.get());
    }
}
