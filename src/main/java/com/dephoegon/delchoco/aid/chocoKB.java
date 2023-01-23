package com.dephoegon.delchoco.aid;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import static com.mojang.blaze3d.platform.InputConstants.isKeyDown;
public class chocoKB {
private static boolean LAlt() {
    return isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT);
}
private static boolean RAlt() {
    return isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_ALT);
}
public static boolean isAltDown() {
    return (RAlt() || LAlt());
}
}
