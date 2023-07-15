package com.dephoegon.delchoco.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.jetbrains.annotations.NotNull;

public final class KeyBindManager {
    private KeyBindManager() { }
    public static KeyMapping inWaterGlide;
    public static KeyMapping stopAirGlide;
    public static void mapKeys() {
        inWaterGlide = registerKey("delchoco.key.in_water_glide", InputConstants.KEY_LALT);
        stopAirGlide = registerKey("delchoco.key.stop_in_air_glide", InputConstants.KEY_LSHIFT);
    }

    private static @NotNull KeyMapping registerKey(String name, int keyCode) {
        final var key = new KeyMapping(name, keyCode, "delchoco.key.category");
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
