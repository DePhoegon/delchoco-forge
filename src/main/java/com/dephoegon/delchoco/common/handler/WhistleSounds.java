package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.DelChoco;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WhistleSounds
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DelChoco.DELCHOCO_ID);

	public static final RegistryObject<SoundEvent> WHISTLE = SOUND_EVENTS.register("whistle", () ->
			SoundEvent.createVariableRangeEvent(new ResourceLocation(DelChoco.DELCHOCO_ID, "whistle")));
}
