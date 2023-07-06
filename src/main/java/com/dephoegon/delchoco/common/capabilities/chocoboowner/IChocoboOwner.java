package com.dephoegon.delchoco.common.capabilities.chocoboowner;

import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface IChocoboOwner {

	public Chocobo createChocoboEntity(Level world);
	
	public CompoundTag getChocoboNBT();
	
	public void setChocoboNBT(CompoundTag nbt);

	public void setChocobo(AbstractHorse horse, Player player);
	
	public void clearChocobo();
	
	public int getChocoboNum();
	
	public void setChocoboNum(int num);
	
	public String getStorageUUID();
	
	public void setStorageUUID(String id);

	public void setLastSeenPosition(Vec3 pos);
	
	public Vec3 getLastSeenPosition();
	
	public ResourceKey<Level> getLastSeenDim();
	
	public void setLastSeenDim(ResourceKey<Level> dim);

}
