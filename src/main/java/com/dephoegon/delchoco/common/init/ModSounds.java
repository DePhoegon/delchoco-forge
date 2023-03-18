package com.dephoegon.delchoco.common.init;

import com.dephoegon.delchoco.DelChoco;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.minecraft.sounds.SoundEvent.createVariableRangeEvent;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DelChoco.MOD_ID);
    public static final RegistryObject<SoundEvent> AMBIENT_SOUND = SOUND_EVENTS.register("entity.chocobo.kweh", () -> createVariableRangeEvent(new ResourceLocation(DelChoco.MOD_ID, "entity.chocobo.kweh")));
    public static final RegistryObject<SoundEvent> WHISTLE_SOUND_FOLLOW = SOUND_EVENTS.register("entity.chocobo.kwehwhistlefollow", () -> createVariableRangeEvent(new ResourceLocation(DelChoco.MOD_ID, "entity.chocobo.kwehwhistlefollow")));
    public static final RegistryObject<SoundEvent> WHISTLE_SOUND_STAY = SOUND_EVENTS.register("entity.chocobo.kwehwhistlestay", () -> createVariableRangeEvent(new ResourceLocation(DelChoco.MOD_ID, "entity.chocobo.kwehwhistlestay")));
    public static final net.minecraftforge.registries.RegistryObject<SoundEvent> WHISTLE_SOUND_WANDER = SOUND_EVENTS.register("entity.chocobo.kwehwhistlewander", () -> createVariableRangeEvent(new ResourceLocation(DelChoco.MOD_ID, "entity.chocobo.kwehwhistlewander")));
}