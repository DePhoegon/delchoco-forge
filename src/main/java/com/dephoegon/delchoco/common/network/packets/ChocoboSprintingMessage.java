package com.dephoegon.delchoco.common.network.packets;

import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ChocoboSprintingMessage {
    private final boolean sprinting;

    public ChocoboSprintingMessage(boolean sprinting) { this.sprinting = sprinting; }

    public void encode(@NotNull FriendlyByteBuf buf) { buf.writeBoolean(sprinting); }

    @Contract("_ -> new")
    public static @NotNull ChocoboSprintingMessage decode(final @NotNull FriendlyByteBuf buffer) { return new ChocoboSprintingMessage(buffer.readBoolean()); }

    public void handle(@NotNull Supplier<Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if(ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
                Player player = ctx.getSender();
                if (player != null) {
                    if (player.getVehicle() == null) { return; }

                    Entity mount = player.getVehicle();
                    if (!(mount instanceof Chocobo)) { return; }

                    mount.setSprinting(sprinting);
                }
            }
        });
        ctx.setPacketHandled(true);
    }

}
