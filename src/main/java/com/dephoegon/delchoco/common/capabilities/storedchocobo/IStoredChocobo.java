package com.dephoegon.delchoco.common.capabilities.storedchocobo;

import java.util.UUID;

public interface IStoredChocobo {

	public String getStorageUUID();
	
	public void setStorageUUID(String uuid);
	
	public UUID getOwnerUUID();
	
	public void setOwnerUUID(UUID uuid);
	
	public void setChocoboNum(int num);
	
	public int getChocoboNum();
	
	public void setOwned(boolean bool);
	
	public boolean isOwned();

}
