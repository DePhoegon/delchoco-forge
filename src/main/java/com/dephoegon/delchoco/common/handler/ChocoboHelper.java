package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.common.capabilities.CapabilityHandler;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.storedchocobo.IStoredChocobo;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.world.data.StoredChocoboWorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ChocoboHelper
{
	private static final Map<Entity, LazyOptional<IStoredChocobo>> cachedChocobos = new HashMap<Entity, LazyOptional<IStoredChocobo>>();

	public static IChocoboOwner getOwnerCap(Player player)
	{
		LazyOptional<IChocoboOwner> cap = player.getCapability(CapabilityHandler.OWNER_CAPABILITY, null);
		if (cap.isPresent())
			return cap.resolve().get();

		return null;
	}
	public static IStoredChocobo getChocobo(Chocobo choco) {
		return null;
	}

	public static IStoredChocobo getChocoboCap(Entity horse)
	{
		LazyOptional<IStoredChocobo> cap = cachedChocobos.get(horse);
		if (cap == null)
		{
			cap = horse.getCapability(CapabilityHandler.CHOCOBO_CAPABILITY, null);
			cachedChocobos.put(horse, cap);
			cap.addListener(optional -> {
				cachedChocobos.remove(horse);
			});
		}
		if (cap.isPresent())
			return cap.resolve().get();

		return null;
	}

	@Nullable
	public static Player getPlayerFromUUID(UUID uuid, Level world)
	{
		MinecraftServer server = world.getServer();

		return Objects.requireNonNull(server).getPlayerList().getPlayer(uuid);
	}

	public static void setHorseNum(ServerLevel world, String storageId, int num)
	{
		world.getServer().getAllLevels().forEach(serverworld -> {
			StoredChocoboWorldData storedHorses = StoredChocoboWorldData.getInstance(serverworld);
			storedHorses.addHorseNum(storageId, num);
		});
	}

	public static int getHorseNum(ServerLevel world, String storageId)
	{
		StoredChocoboWorldData storedHorses = StoredChocoboWorldData.getInstance(world);
		return storedHorses.getHorseNum(storageId);
	}

	public static void setHorseLastSeen(Player player)
	{
		IChocoboOwner owner = getOwnerCap(player);
		Objects.requireNonNull(owner).setLastSeenPosition(player.position());
		owner.setLastSeenDim(player.level.dimension());
	}

	public static StoredChocoboWorldData getWorldData(ServerLevel world)
	{
		return StoredChocoboWorldData.getInstance(world);
	}
}
