package com.dephoegon.delchoco.common.capabilities.chocoboowner;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.capabilities.CapabilityHandler;
import com.dephoegon.delchoco.common.capabilities.storedchocobo.IStoredChocobo;
import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class ChocoboOwner implements IChocoboOwner, ICapabilitySerializable<CompoundTag>, ICapabilityProvider
{

	private int chocoboNum = 0;
	private CompoundTag chocoboNBT = new CompoundTag();
	private String storageUUID = "";
	private ResourceKey<Level> lastSeenDim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld"));
	private Vec3 lastSeenPos = Vec3.ZERO;

	@Override
	public Chocobo createChocoboEntity(Level world)
	{
		Optional<EntityType<?>> type = EntityType.by(chocoboNBT);

		if (type.isPresent())
		{
			Entity entity = type.get().create(world);
			if (entity instanceof Chocobo)
			{
				entity.load(chocoboNBT);

				chocoboNum++;

				LazyOptional<IStoredChocobo> cap = entity.getCapability(CapabilityHandler.CHOCOBO_CAPABILITY, null);
				if (cap.isPresent())
				{
					cap.resolve().get().setChocoboNum(chocoboNum);

					entity.setUUID(UUID.randomUUID());
					entity.clearFire();
					((AbstractHorse) entity).hurtTime = 0;
				}

				return (AbstractHorse) entity;
			}

			DelChoco.log.error("The entity with NBT " + chocoboNBT.toString() + " wasn't a horse somehow?...");
		}
		return null;
	}

	@Override
	public CompoundTag getChocoboNBT()
	{
		return chocoboNBT;
	}

	@Override
	public void setChocobo(AbstractHorse chocobo, Player player)
	{
		storageUUID = UUID.randomUUID().toString();

		LazyOptional<IStoredChocobo> cap = chocobo.getCapability(CapabilityHandler.CHOCOBO_CAPABILITY, null);

		cap.ifPresent(storedHorse -> {
			storedHorse.setChocoboNum(chocoboNum);
			storedHorse.setOwned(true);
			storedHorse.setOwnerUUID(player.getUUID());
			storedHorse.setStorageUUID(storageUUID);

			chocoboNBT = chocobo.serializeNBT();
		});
	}

	@Override
	public void clearChocobo()
	{
		chocoboNum = 0;
		chocoboNBT = new CompoundTag();
		storageUUID = "";
		lastSeenDim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld"));
		lastSeenPos = Vec3.ZERO;
	}

	@Override
	public int getChocoboNum()
	{
		return chocoboNum;
	}

	@Override
	public void setChocoboNum(int num)
	{
		chocoboNum = num;
	}

	@Override
	public String getStorageUUID()
	{
		return storageUUID;
	}

	@Override
	public void setStorageUUID(String id)
	{
		this.storageUUID = id;
	}

	@Override
	public void setChocoboNBT(CompoundTag nbt)
	{
		this.chocoboNBT = nbt;
	}

	@Override
	public void setLastSeenPosition(Vec3 pos)
	{
		this.lastSeenPos = pos;
	}

	@Override
	public Vec3 getLastSeenPosition()
	{
		return this.lastSeenPos;
	}

	@Override
	public ResourceKey<Level> getLastSeenDim()
	{
		return this.lastSeenDim;
	}

	@Override
	public void setLastSeenDim(ResourceKey<Level> dim)
	{
		this.lastSeenDim = dim;
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		return CapabilityHandler.OWNER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
	}

	@Override
	public CompoundTag serializeNBT() {
		return writeNBT(this);
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		readNBT(this, tag);
	}

	public static CompoundTag writeNBT(IChocoboOwner instance) {
		if (instance == null) {
			return null;
		}
		CompoundTag tag = new CompoundTag();

		tag.put("horse", instance.getChocoboNBT());
		tag.putInt("horseNum", instance.getChocoboNum());
		tag.putString("uuid", instance.getStorageUUID());
		tag.put("lastSeenPos", NbtUtils.writeBlockPos(new BlockPos(instance.getLastSeenPosition())));
		tag.putString("lastSeenDim", instance.getLastSeenDim().location().toString());
		return tag;

	}

	public static void readNBT(IChocoboOwner instance, CompoundTag tag) {
		instance.setChocoboNBT(tag.getCompound("horse"));
		instance.setChocoboNum(tag.getInt("horseNum"));
		instance.setStorageUUID(tag.getString("uuid"));
		BlockPos temp = NbtUtils.readBlockPos(tag.getCompound("lastSeenPos"));
		instance.setLastSeenPosition(new Vec3(temp.getX(), temp.getY(), temp.getZ()));
		instance.setLastSeenDim(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag.getString("lastSeenDim"))));
	}
}
