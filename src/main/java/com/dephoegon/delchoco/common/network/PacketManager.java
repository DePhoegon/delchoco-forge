package com.dephoegon.delchoco.common.network;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.network.packets.ChocoboSprintingMessage;
import com.dephoegon.delchoco.common.network.packets.OpenChocoboGuiMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketManager {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel( new ResourceLocation(DelChoco.DELCHOCO_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int id = 0;

    public static void init() {
        CHANNEL.registerMessage(id++, OpenChocoboGuiMessage.class, OpenChocoboGuiMessage::encode, OpenChocoboGuiMessage::decode, OpenChocoboGuiMessage::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(id++, ChocoboSprintingMessage.class, ChocoboSprintingMessage::encode, ChocoboSprintingMessage::decode, ChocoboSprintingMessage::handle, Optional.of(net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER));
    }
}