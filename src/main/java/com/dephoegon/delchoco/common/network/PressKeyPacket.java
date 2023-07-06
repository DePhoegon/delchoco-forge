package com.dephoegon.delchoco.common.network;

import com.dephoegon.delchoco.common.handler.ChocoboManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PressKeyPacket
{
	private int key;

	public PressKeyPacket()
	{
	}

	public PressKeyPacket(int key)
	{
		this.key = key;
	}

	public PressKeyPacket(FriendlyByteBuf buf)
	{
		this.key = buf.readInt();
	}

	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeInt(key);
	}

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isServer())
			{
				ServerPlayer player = ctx.getSender();

				if (player != null)
				{
					switch (key) {
						case 0 -> ChocoboManager.callChocobo(player);
						case 1 -> ChocoboManager.setHorse(player);
						case 2 -> ChocoboManager.showHorseStats(player);
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}

}
