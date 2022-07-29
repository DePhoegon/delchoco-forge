package net.chococraft.common.init;

import net.chococraft.Chococraft;
import net.chococraft.common.ChocoConfig;
import net.chococraft.common.entities.ChocoboEntity;
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

import static net.chococraft.common.ChocoConfig.COMMON;
import static net.chococraft.common.entities.ChocoboEntity.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Chococraft.MOD_ID);

    public static final RegistryObject<EntityType<ChocoboEntity>> CHOCOBO = ENTITIES.register("chocobo", () ->
            register("chocobo", EntityType.Builder.of(ChocoboEntity::new, MobCategory.CREATURE).sized(1.2f, 2.8f).clientTrackingRange(64)));

    public static <T extends Entity> @NotNull EntityType<T> register(String id, EntityType.@NotNull Builder<T> builder) { return builder.build(id); }

    public static void addSpawns(@NotNull BiomeLoadingEvent event) {
        if (event.getName() == null) { return; }

        // Biome Spawning area
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());
        if(BiomeDictionary.hasType(biomeKey, Type.PLAINS) || BiomeDictionary.hasType(biomeKey, Type.HILLS) ||
                (BiomeDictionary.hasType(biomeKey, Type.HOT) && BiomeDictionary.hasType(biomeKey, Type.DRY)) ||
                BiomeDictionary.hasType(biomeKey, Type.SNOWY) || BiomeDictionary.hasType(biomeKey, Type.SWAMP) ||
                BiomeDictionary.hasType(biomeKey, Type.MESA)) {

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
