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
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.Chocobo.*;
import static net.minecraftforge.common.BiomeDictionary.hasType;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, DelChoco.MOD_ID);

    public static final RegistryObject<EntityType<Chocobo>> CHOCOBO = ENTITIES.register("chocobo", () ->
            register("chocobo", EntityType.Builder.of(Chocobo::new, MobCategory.CREATURE).sized(1.2f, 2.8f).clientTrackingRange(64)));

    public static <T extends Entity> @NotNull EntityType<T> register(String id, EntityType.@NotNull Builder<T> builder) { return builder.build(id); }

    public static void addSpawns(@NotNull BiomeLoadingEvent event) {
        if (event.getName() == null) { return; }

        // Biome Spawning area
        // Overrides Spawn Eggs
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

        String bob = biomeKey.toString();
        // Name Strings to extend towards BOP support
        if (BiomeDictionary.hasType(biomeKey, Type.NETHER)) {
            event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                    COMMON.chocoboSpawnWeightNether.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
        }
        if(BiomeDictionary.hasType(biomeKey, Type.PLAINS) || BiomeDictionary.hasType(biomeKey, Type.HILLS) || (BiomeDictionary.hasType(biomeKey, Type.HOT) && BiomeDictionary.hasType(biomeKey, Type.DRY) && !(BiomeDictionary.hasType(biomeKey, Type.NETHER))) || BiomeDictionary.hasType(biomeKey, Type.SNOWY) || BiomeDictionary.hasType(biomeKey, Type.SWAMP) || BiomeDictionary.hasType(biomeKey, Type.MESA) || bob.contains("mystic") || bob.contains("blossom") || bob.contains("lavender") || bob.contains("tropics") || hasType(biomeKey, Type.FOREST)) {

            event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                    COMMON.chocoboSpawnWeight.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
        }
        if (BiomeDictionary.hasType(biomeKey, Type.MUSHROOM)) {
            event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                    COMMON.chocoboSpawnWeightMushroom.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
        }
    }

    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) { event.put(CHOCOBO.get(), createAttributes().build()); }
}
