package com.dephoegon.delchoco.common.network;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.ChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.handler.ChocoboHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class OwnerSyncShowStatsPacket
{
	private CompoundTag ownerNBT = null;

	public OwnerSyncShowStatsPacket()
	{
	}

	public OwnerSyncShowStatsPacket(IChocoboOwner owner)
	{
		this.ownerNBT = (CompoundTag) ChocoboOwner.writeNBT(owner);
	}

	public OwnerSyncShowStatsPacket(FriendlyByteBuf buf)
	{
		this.ownerNBT = buf.readNbt();
	}

	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeNbt(ownerNBT);
	}

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient())
			{
				Player player = DelChoco.proxy.getPlayer();

				if (player != null)
				{
					IChocoboOwner owner = ChocoboHelper.getOwnerCap(player);
					ChocoboOwner.readNBT(Objects.requireNonNull(owner), ownerNBT);

					DelChoco.proxy.displayStatViewer();
				}
			}
		});
		ctx.setPacketHandled(true);
	}

}
