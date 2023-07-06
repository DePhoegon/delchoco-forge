package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.storedchocobo.IStoredChocobo;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.network.OwnerSyncShowStatsPacket;
import com.dephoegon.delchoco.common.network.PlayWhistlePacket;
import com.dephoegon.delchoco.common.world.data.StoredChocoboWorldData;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;

import static com.dephoegon.delchoco.common.world.config.ChocoConfig.SERVER;

public class ChocoboManager
{

	public static boolean callChocobo(Player player)
	{
		if (player != null)
		{
			IChocoboOwner chocoboOwner = ChocoboHelper.getOwnerCap(player);
			if (chocoboOwner != null)
			{
				if (chocoboOwner.getChocoboNBT().isEmpty())
				{
					player.displayClientMessage(Component.translatable("delchoco.error.nochoco").withStyle(ChatFormatting.RED), true);
					return false;
				}

				if (!canCallHorse(player))
					return false;
				Random rand = new Random();
				player.level.playSound(player, player.blockPosition(), WhistleSounds.WHISTLE.get(), SoundSource.PLAYERS, 1f, (float) (1.4 + rand.nextGaussian() / 3));
				DelChoco.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new PlayWhistlePacket());

				Chocobo e = findChocoboWithStorageID(chocoboOwner.getStorageUUID(), player.level);
				if (e != null)
				{
					IStoredChocobo horse = ChocoboHelper.getChocoboCap(e);
					if (horse.getStorageUUID().equals(chocoboOwner.getStorageUUID()))
					{
						if (e.level.dimensionType() == player.level.dimensionType())
						{
							e.ejectPassengers();
							
							ChocoboHelper.setHorseLastSeen(player);
							ChocoboHelper.sendChocoboUpdateInRange(e);
							return true;
						}
						else
						{
							// Removing any loaded horses in other dims when a
							// new one is spawned
							ChocoboManager.saveHorse(e);
							e.setPos(e.getX(), -200, e.getZ());
							e.discard();
						}

					}
				}

				// Spawning a new horse with a new num
				Chocobo newHorse = chocoboOwner.createChocoboEntity(player.level);
				newHorse.setPos(player.getX(), player.getY(), player.getZ());
				player.level.addFreshEntity(newHorse);
				IStoredChocobo h = ChocoboHelper.getChocoboCap(newHorse);
				ChocoboHelper.setHorseNum((ServerLevel) newHorse.level, h.getStorageUUID(), h.getChocoboNum());
				ChocoboHelper.sendChocoboUpdateInRange(newHorse);
				ChocoboHelper.setHorseLastSeen(player);
				return true;

			}

		}

		return false;
	}

	public static void setHorse(Player player)
	{
		if (player != null)
		{
			if (player.getVehicle() == null)
			{
				player.displayClientMessage(Component.translatable("delchoco.error.notriding").withStyle(ChatFormatting.RED), true);
				return;
			}

			Entity e = player.getVehicle();
			if (e instanceof AbstractHorse)
			{
				if (!canSetHorse(player, e))
					return;

				IStoredChocobo storedHorse = ChocoboHelper.getChocoboCap(e);

				UUID owner = storedHorse.getOwnerUUID();
				String playerID = player.getGameProfile().getId().toString();
				boolean owned = storedHorse.isOwned();

				if (owned && !owner.equals(playerID))
				{
					player.displayClientMessage(Component.translatable("delchoco.error.alreadyowned").withStyle(ChatFormatting.RED), true);
					return;
				}

				if (owned && owner.equals(playerID))
				{
					player.displayClientMessage(Component.translatable("delchoco.error.alreadypersonal").withStyle(ChatFormatting.RED), true);
					return;
				}

				IChocoboOwner horseOwner = ChocoboHelper.getOwnerCap(player);
				String ownedID = horseOwner.getStorageUUID();

				// Marking any old horses as disbanded
				if (!ownedID.isEmpty())
				{
					Entity ent = findChocoboWithStorageID(horseOwner.getStorageUUID(), player.level);
					if (ent != null)
					{
						clearHorse(ChocoboHelper.getChocoboCap(ent));
					}
					else
					{
						player.level.getServer().getAllLevels().forEach(serverworld -> {
							StoredChocoboWorldData data = ChocoboHelper.getWorldData(serverworld);
							data.disbandHorse(ownedID);
						});
					}

				}
				horseOwner.clearChocobo();

				// Setting the new horse
				horseOwner.setChocobo((AbstractHorse) e, player);
				ChocoboHelper.setHorseLastSeen(player);
				ChocoboHelper.setHorseNum((ServerLevel) e.level, storedHorse.getStorageUUID(), storedHorse.getChocoboNum());
				player.displayClientMessage(Component.translatable("delchoco.success"), true);
				ChocoboHelper.sendChocoboUpdateInRange(e);

			}
		}
	}

	public static void showHorseStats(ServerPlayer player)
	{
		IChocoboOwner owner = ChocoboHelper.getOwnerCap(player);

		if (owner.getChocoboNBT().isEmpty())
		{
			player.displayClientMessage(Component.translatable("delchoco.error.nochoco").withStyle(ChatFormatting.RED), true);
			return;
		}

		Entity e = findChocoboWithStorageID(owner.getStorageUUID(), player.level);
		if (e != null)
		{
			ChocoboManager.saveHorse(e);
		}

		DelChoco.network.send(PacketDistributor.PLAYER.with(() -> player), new OwnerSyncShowStatsPacket(owner));
	}

	public static void clearHorse(IStoredChocobo horse)
	{
		horse.setOwned(false);
		horse.setChocoboNum(0);
		horse.setOwnerUUID(null);
		horse.setStorageUUID("");
	}

	@Nullable
	public static Chocobo findChocoboWithStorageID(String id, Level world)
	{
		MinecraftServer server = world.getServer();
		List<Entity> entities = new ArrayList<Entity>();

		for (ServerLevel w : server.getAllLevels())
			entities.addAll(ImmutableList.copyOf(w.getAllEntities()));

		for (Entity e : entities)
		{
			if (e instanceof Chocobo)
			{
				IStoredChocobo chocobo = ChocoboHelper.getChocoboCap(e);
				if (chocobo.getStorageUUID().equals(id))
					return (Chocobo) e;

			}
		}

		return null;
	}

	// Clear armor, saddle, and any chest items
	public static void prepDeadChocoboForRespawning(Chocobo e)
	{
		ChocoboColor color =

		e.clearFire();
		((LivingEntity) e).setHealth(((LivingEntity) e).getMaxHealth());

	}

	@SuppressWarnings("deprecation")
	public static boolean canCallHorse(Player player)
	{
		if (isAreaProtected(player, null))
		{
			player.displayClientMessage(Component.translatable("delchoco.error.area").withStyle(ChatFormatting.RED), true);
			return false;
		}

		if (player.getVehicle() != null)
		{
			player.displayClientMessage(Component.translatable("delchoco.error.riding").withStyle(ChatFormatting.RED), true);
			return false;
		}

		if (SERVER.checkForSpace.get())
		{
			double startX, startY, startZ;
			double endX, endY, endZ;

			startX = player.getX() - 1;
			startY = player.getY();
			startZ = player.getZ() - 1;

			endX = player.getX() + 1;
			endY = player.getY() + 2;
			endZ = player.getZ() + 1;

			Level world = player.level;

			for (double x = startX; x <= endX; x++)
			{
				for (double y = startY; y <= endY; y++)
				{
					for (double z = startZ; z <= endZ; z++)
					{
						BlockPos pos = new BlockPos(x, y, z);
						BlockState state = world.getBlockState(pos);
						if (state.getBlock().getCollisionShape(state, world, pos, null) != Shapes.empty())
						{
							player.displayClientMessage(Component.translatable("delchoco.error.nospace").withStyle(ChatFormatting.RED), true);
							return false;
						}
					}
				}
			}
		}

		if (!SERVER.callableInEveryDimension.get())
		{
			List<? extends String> allowedDims = SERVER.callableDimsWhitelist.get();
			ResourceKey<Level> playerDim = player.level.dimension();

			for (int i = 0; i < allowedDims.size(); i++)
			{
				if (allowedDims.get(i).equals(playerDim.location().toString()))
					return true;
			}
			player.displayClientMessage(Component.translatable("delchoco.error.dim").withStyle(ChatFormatting.RED), true);
			return false;
		}

		int maxDistance = SERVER.maxCallingDistance.get();
		if (maxDistance != -1)
		{
			IChocoboOwner owner = ChocoboHelper.getOwnerCap(player);
			Vec3 lastSeenPos = owner.getLastSeenPosition();
			ResourceKey<Level> lastSeenDim = owner.getLastSeenDim();

			if (lastSeenPos.equals(Vec3.ZERO))
				return true;

			MinecraftServer server = player.level.getServer();

			Entity livingHorse = findChocoboWithStorageID(owner.getStorageUUID(), player.level);
			if (livingHorse != null)
			{
				lastSeenPos = livingHorse.position();
				lastSeenDim = livingHorse.level.dimension(); // Dimension
																	// registry
																	// key
			}

			double movementFactorHorse = server.getLevel(lastSeenDim).dimensionType().coordinateScale(); // getDimensionType,
																										// getMovementFactor
			double movementFactorOwner = player.level.dimensionType().coordinateScale();

			double movementFactorTotal = movementFactorHorse > movementFactorOwner ? movementFactorHorse / movementFactorOwner : movementFactorOwner / movementFactorHorse;

			double distance = lastSeenPos.distanceTo(player.position()) / movementFactorTotal;
			if (distance <= maxDistance)
				return true;

			player.displayClientMessage(Component.translatable("delchoco.error.range").withStyle(ChatFormatting.RED), true);
			return false;
		}

		return true;
	}

	public static boolean canSetHorse(Player player, Entity entity)
	{
		if (isAreaProtected(player, entity))
		{
			player.displayClientMessage(Component.translatable("delchoco.error.setarea").withStyle(ChatFormatting.RED), true);
			return false;
		}

		return true;
	}

	public static void saveHorse(Entity e)
	{
		if (e instanceof AbstractHorse abstractHorse)
		{
			if (abstractHorse.isDeadOrDying())
				return;

			Level world = e.level;
			IStoredChocobo horse = ChocoboHelper.getChocoboCap(e);
			if (horse != null && horse.isOwned())
			{
				UUID ownerid = horse.getOwnerUUID();
				Player owner = ChocoboHelper.getPlayerFromUUID(ownerid, world);

				if (owner != null)
				{
					// Owner is online
					IChocoboOwner horseOwner = ChocoboHelper.getOwnerCap(owner);
					if (horseOwner != null)
					{
						CompoundTag nbt = e.serializeNBT();
						horseOwner.setChocoboNBT(nbt);
						horseOwner.setLastSeenDim(e.level.dimension());
						horseOwner.setLastSeenPosition(e.position());
					}
					else
					{
						Objects.requireNonNull(world.getServer()).getAllLevels().forEach(serverworld -> {
							StoredChocoboWorldData data = ChocoboHelper.getWorldData(serverworld);
							data.addOfflineSavedHorse(horse.getStorageUUID(), e.serializeNBT());
						});
					}
				}
				else
				{
					StoredChocoboWorldData data = ChocoboHelper.getWorldData((ServerLevel) world);
					data.addOfflineSavedHorse(horse.getStorageUUID(), e.serializeNBT());
				}
			}
		}
	}

	private static boolean isAreaProtected(Player player, @Nullable Entity fakeChocobo)
	{
		IChocoboOwner owner = ChocoboHelper.getOwnerCap(player);
		if (fakeChocobo == null)
			fakeChocobo = Objects.requireNonNull(owner).createChocoboEntity(player.level);
		fakeChocobo.setPos(player.getX(), player.getY(), player.getZ());
		EntityInteract interactEvent = new EntityInteract(player, InteractionHand.MAIN_HAND, fakeChocobo);
		AttackEntityEvent attackEvent = new AttackEntityEvent(player, fakeChocobo);

		MinecraftForge.EVENT_BUS.post(interactEvent);
		MinecraftForge.EVENT_BUS.post(attackEvent);

		return interactEvent.isCanceled() || attackEvent.isCanceled();
	}

}
