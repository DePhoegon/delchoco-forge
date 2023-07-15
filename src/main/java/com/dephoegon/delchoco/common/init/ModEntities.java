package com.dephoegon.delchoco.common.init;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.aid.SpawnBiomesChecks.*;
import static com.dephoegon.delchoco.aid.fallbackValues.*;
import static com.dephoegon.delchoco.common.configs.WorldConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, DelChoco.DELCHOCO_ID);
    public static final RegistryObject<EntityType<Chocobo>> CHOCOBO = ENTITIES.register("chocobo", () -> register("chocobo", EntityType.Builder.of(Chocobo::new, MobCategory.CREATURE).sized(1.2f, 2.8f).clientTrackingRange(64)));

    public static <T extends Entity> @NotNull EntityType<T> register(String id, EntityType.@NotNull Builder<T> builder) { return builder.build(id); }
    public static void addSpawns(@NotNull BiomeLoadingEvent event) {
        if (event.getName() == null) { return; }
        ResourceKey<Biome> biomesKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

         if (ChocoConfigGet(COMMON.chocoboSpawnEnabler.get(), dCanSpawn)) {
            if (netherCheck(biomesKey, true)) { event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(), ChocoConfigGet(COMMON.chocoboSpawnWeightNether.get(), dNetherSpawnWeight), getEffectiveMinPack(), getEffectiveMaxPack())); }
            if (theEndCheck(biomesKey, true)) { event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(), ChocoConfigGet(COMMON.chocoboSpawnWeightEnd.get(), dEndSpawnWeight), getEffectiveMinPack() + 1, getEffectiveMaxPack() + 2)); }
            if (typeCheck(biomesKey, Type.MUSHROOM, overWorld)) { event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(), ChocoConfigGet(COMMON.chocoboSpawnWeightMushroom.get(), dMushroomSpawnWeight), getEffectiveMinPack(), getEffectiveMaxPack())); }
            else if (overWorldCheck(biomesKey, true)) { event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(), ChocoConfigGet(COMMON.chocoboSpawnWeight.get(), dOverWorldSpawnWeight), getEffectiveMinPack(), getEffectiveMaxPack())); }
        }
    }
    private static int getMaxPack() { return ChocoConfigGet(COMMON.chocoboPackSizeMax.get(), dChocoboMaxPack); }
    private static int getMinPack() { return ChocoConfigGet(COMMON.chocoboPackSizeMin.get(), dChocoboMinPack); }
    private static int getEffectiveMinPack() { return Math.min(getMinPack(), getMaxPack()); }
    private static int getEffectiveMaxPack() { return Math.max(getMaxPack(), getMinPack()); }
    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) { event.put(CHOCOBO.get(), createAttributes().build()); }
}