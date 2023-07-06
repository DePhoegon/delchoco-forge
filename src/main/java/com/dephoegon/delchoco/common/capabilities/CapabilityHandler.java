package com.dephoegon.delchoco.common.capabilities;

import com.dephoegon.delchoco.common.capabilities.chocoboowner.IChocoboOwner;
import com.dephoegon.delchoco.common.capabilities.storedchocobo.IStoredChocobo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CapabilityHandler {
	public static final Capability<IChocoboOwner> OWNER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});
	public static final Capability<IStoredChocobo> CHOCOBO_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IChocoboOwner.class);
		event.register(IStoredChocobo.class);
	}
}
