package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.ChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.storedchocobo.IStoredChocobo;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.handler.ChocoboHelper;
import com.dephoegon.delchoco.common.handler.ChocoboManager;
import com.dephoegon.delchoco.common.world.data.StoredChocoboWorldData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Objects;

import static com.dephoegon.delchoco.aid.fallbackValues.ChocoConfigGet;
import static com.dephoegon.delchoco.aid.fallbackValues.dDeathIsPermanent;
import static com.dephoegon.delchoco.common.world.config.ChocoConfig.COMMON;

@EventBusSubscriber(modid = DelChoco.DELCHOCO_ID)
public class EntityEvents
{

	@SubscribeEvent
	public static void onAttachCaps(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof Player)
			event.addCapability(new ResourceLocation(DelChoco.DELCHOCO_ID, "horse_owner"), new ChocoboOwner());

	}

	// Remove horses with lower num when they are loaded
	// Notify player of offline horse death
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event)
	{
		Entity joiningEntity = event.getEntity();
		Level world = event.getLevel();
		if (!world.isClientSide)
		{
			if(joiningEntity instanceof Player player)
			{
				IChocoboOwner owner = ChocoboHelper.getOwnerCap(player);

				String ownedChocobo = Objects.requireNonNull(owner).getStorageUUID();

				if (!ownedChocobo.isEmpty())
				{
					StoredChocoboWorldData data = ChocoboHelper.getWorldData((ServerLevel) world);
					if (data.wasKilled(ownedChocobo))
					{
						data.clearKilled(ownedChocobo);

						if(ChocoConfigGet(COMMON.deathIsPermanent.get(), dDeathIsPermanent))
						{
							owner.clearChocobo();
							player.displayClientMessage(Component.translatable("delchoco.alert.offline_death").withStyle(ChatFormatting.RED), false);
						}
						else
						{
							Chocobo deadChocobo = owner.createChocoboEntity(world);
							ChocoboManager.prepDeadChocoboForRespawning(deadChocobo);
							owner.setChocoboNBT(deadChocobo.serializeNBT());
							owner.setLastSeenPosition(Vec3.ZERO);
						}
					}

					if (data.wasOfflineSaved(ownedChocobo))
					{
						CompoundTag newNBT = data.getOfflineSavedHorse(ownedChocobo);
						owner.setChocoboNBT(newNBT);
						data.clearOfflineSavedHorse(ownedChocobo);
					}
				}
			}
			else if (joiningEntity instanceof Chocobo)
			{
				IStoredChocobo chocobo = ChocoboHelper.getChocoboCap(joiningEntity);
				if (Objects.requireNonNull(chocobo).isOwned())
				{
					StoredChocoboWorldData data = ChocoboHelper.getWorldData((ServerLevel) world);
					int globalNum = ChocoboHelper.getHorseNum((ServerLevel) joiningEntity.level, chocobo.getStorageUUID());
					if (globalNum > chocobo.getChocoboNum())
					{
						event.setCanceled(true);
						DelChoco.log.debug(joiningEntity + " was instantly despawned because its number is " + chocobo.getChocoboNum() + " and the global num is " + globalNum);
					}
					else if (data.isDisbanded(chocobo.getStorageUUID()))
					{
						ChocoboManager.clearHorse(chocobo);
						data.clearDisbanded(chocobo.getStorageUUID());

					}
				}
			}
		}
	}

	// Remove horses with lower num when they are loaded
	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
//		LevelAccessor world = event.getWorld();
//		if (!world.isClientSide())
//		{
//
//			ChunkAccess chk = event.getChunk();
//
//			if (chk instanceof LevelChunk)
//			{
//				ClassInstanceMultiMap<Entity>[] entitylists = ((LevelChunk) chk).getEntitySections();
//
//				for (ClassInstanceMultiMap<Entity> list : entitylists)
//				{
//					for (Entity e : list)
//					{
//						if (e instanceof AbstractHorse)
//						{
//							IStoredHorse horse = HorseHelper.getHorseCap(e);
//							if (horse.isOwned())
//							{
//								StoredHorsesWorldData data = HorseHelper.getWorldData((ServerLevel) world);
//								if (data.isDisbanded(horse.getStorageUUID()))
//								{
//									HorseManager.clearHorse(horse);
//									data.clearDisbanded(horse.getStorageUUID());
//
//								}
//								else
//								{
//									int globalNum = HorseHelper.getHorseNum((ServerLevel) e.level, horse.getStorageUUID());
//									if (globalNum > horse.getHorseNum())
//									{
////										e.setPosition(e.getPosX(), -200, e.getPosZ());
//										e.discard();
//										CallableHorses.LOGGER.debug(e + " was instantly despawned because its number is " + horse.getHorseNum() + " and the global num is " + globalNum);
//									}
//								}
//
//							}
//						}
//					}
//				}
//			}
//		}

	}

	// Clone player cap on teleport/respawn
	@SubscribeEvent
	public static void onClone(Clone event)
	{
		Player original = event.getOriginal();
		Player newPlayer = event.getEntity();

		original.reviveCaps();
		IChocoboOwner oldHorse = ChocoboHelper.getOwnerCap(original);
		IChocoboOwner newHorse = ChocoboHelper.getOwnerCap(newPlayer);
		original.invalidateCaps();

		Objects.requireNonNull(newHorse).setChocoboNBT(Objects.requireNonNull(oldHorse).getChocoboNBT());
		newHorse.setChocoboNum(oldHorse.getChocoboNum());
		newHorse.setStorageUUID(oldHorse.getStorageUUID());

	}

	// Save Horse to player cap when it unloads
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
//		LevelAccessor world = event.getWorld();
//		if (!world.isClientSide())
//		{
//			ChunkAccess chk = event.getChunk();
//
//			if (chk instanceof LevelChunk)
//			{
//				ClassInstanceMultiMap<Entity>[] entitylists = ((LevelChunk) chk).getEntitySections();
//
//				for (ClassInstanceMultiMap<Entity> list : entitylists)
//				{
//					for (Entity e : list)
//					{
//						HorseManager.saveHorse(e);
//					}
//				}
//			}
//		}

	}

	// Save Horse to player cap when it unloads
	@SubscribeEvent
	public static void onEntityLeaveWorld(EntityLeaveLevelEvent event)
	{
		Level world = event.getLevel();
		if (!world.isClientSide())
		{
			ChocoboManager.saveHorse(event.getEntity());
		}
	}

	// Save Horse to player cap when it unloads
	@SubscribeEvent
	public static void onStopTracking(PlayerEvent.StopTracking event)
	{
		Player player = event.getEntity();
		Level world = player.level;
		Entity e = event.getTarget();

		if (!world.isClientSide && e.isAlive())
		{
			ChocoboManager.saveHorse(e);
		}

	}

	// Debug
	@SubscribeEvent
	public static void onLivingUpdate(LivingTickEvent event)
	{

		Entity e = event.getEntity();
		if (e instanceof AbstractHorse && !e.level.isClientSide)
		{
			if (SERVER.enableDebug.get() || SERVER.continuousAntiDupeChecking.get())
			{
				IStoredChocobo horse = ChocoboHelper.getChocoboCap(e);
				if (SERVER.enableDebug.get())
					e.setCustomName(Component.literal("Is Owned: " + Objects.requireNonNull(horse).isOwned() + ", Storage UUID: " + horse.getStorageUUID() + ", Horse Number: " + horse.getChocoboNum() + ", Horse UUID: " + e.getUUID()));

				if (SERVER.continuousAntiDupeChecking.get())
				{
					int thisNum = Objects.requireNonNull(horse).getChocoboNum();
					int globalNum = ChocoboHelper.getHorseNum((ServerLevel) e.level, horse.getStorageUUID());
					if (globalNum > thisNum)
					{
//						e.setPosition(e.getPosX(), -200, e.getPosZ());
						e.discard();
					}
				}

			}
		}
	}

	// Notify player of horse death
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		Entity e = event.getEntity();

		if (!e.level.isClientSide && e instanceof AbstractHorse)
		{
			IStoredChocobo horse = ChocoboHelper.getChocoboCap(e);
			if (Objects.requireNonNull(horse).isOwned())
			{
				Player owner = ChocoboHelper.getPlayerFromUUID(horse.getOwnerUUID(), e.level);
				if (owner != null)
				{
					IChocoboOwner horseOwner = ChocoboHelper.getOwnerCap(owner);
					if (SERVER.deathIsPermanent.get())
					{
						Objects.requireNonNull(horseOwner).clearChocobo();
						owner.displayClientMessage(Component.translatable("delchoco.alert.death").withStyle(ChatFormatting.RED), false);
					}
					else
					{
						ChocoboManager.saveHorse(e);
						Chocobo deadHorse = Objects.requireNonNull(horseOwner).createChocoboEntity(owner.level);
						ChocoboManager.prepDeadChocoboForRespawning(deadHorse);
						horseOwner.setChocoboNBT(deadHorse.serializeNBT());
						horseOwner.setLastSeenPosition(Vec3.ZERO);
					}

				}
				else
				{
					DelChoco.log.debug(e + " was marked as killed.");
					Objects.requireNonNull(e.level.getServer()).getAllLevels().forEach(serverworld -> ChocoboHelper.getWorldData(serverworld).markKilled(horse.getStorageUUID()));
				}
			}
		}
	}
}
