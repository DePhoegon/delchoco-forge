package com.dephoegon.delchoco.aid.util;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.dephoegon.delbase.aid.util.kb.*;
import static com.dephoegon.delbase.aid.util.kb.isKeyBindDefault;
import static com.dephoegon.delchoco.client.keybind.KeyBindManager.inWaterGlide;
import static com.dephoegon.delchoco.client.keybind.KeyBindManager.stopAirGlide;
import static com.mojang.blaze3d.platform.InputConstants.isKeyDown;
public class chocoKB {
    private static boolean LAlt() { return isKB_KeyBindDown(inWaterGlide); }
    private static boolean RAlt() { return keyCheck(InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_ALT); }
    private static boolean ChocoLShift() { return isKB_KeyBindDown(stopAirGlide); }
    private static boolean ChocoRShift() { return keyCheck(InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT); }
    public static boolean isChocoboWaterGlide() { if (isKeyBindDefault(inWaterGlide)) { return (RAlt() || LAlt()); } else { return LAlt(); } }
    public static boolean isChocoShiftDown() { if (isKeyBindDefault(stopAirGlide)) { return (ChocoLShift() || ChocoRShift()); } else  { return ChocoLShift(); } }
    public static boolean hideChocoboMountInFirstPerson(@NotNull Chocobo chocobo) { return chocobo.isControlledByLocalInstance() && !Minecraft.getInstance().gameRenderer.getMainCamera().isDetached(); }
}