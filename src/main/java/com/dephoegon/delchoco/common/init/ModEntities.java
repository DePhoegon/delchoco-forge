package com.dephoegon.delchoco.common.init;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.ChocoConfig;
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

import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;
import static net.minecraft.world.level.biome.Biomes.WARM_OCEAN;
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
        ResourceKey<Biome> biomesKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

        String bob = biomesKey.toString();
        // Name Strings to extend towards BOP support
        if (!hasType(biomesKey, Type.OCEAN) || biomesKey == WARM_OCEAN){
            if (hasType(biomesKey, Type.NETHER) && COMMON.netherSpawns.get()) {
                event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                        COMMON.chocoboSpawnWeightNether.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
            }
            if (hasType(biomesKey, Type.MUSHROOM) && COMMON.overworldSpawns.get()) {
                event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                        COMMON.chocoboSpawnWeightMushroom.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
            } else if (hasType(biomesKey, Type.OVERWORLD) && COMMON.overworldSpawns.get()) {
                event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                        COMMON.chocoboSpawnWeight.get(), COMMON.chocoboPackSizeMin.get(), COMMON.chocoboPackSizeMax.get()));
            }
            if (hasType(biomesKey, Type.END) && COMMON.endSpawns.get()) {
                event.getSpawns().getSpawner(MobCategory.CREATURE).add(new SpawnerData(CHOCOBO.get(),
                        COMMON.chocoboSpawnWeightEnd.get(), COMMON.chocoboPackSizeMin.get()+1, COMMON.chocoboPackSizeMax.get()+2));

            }
        }
    }

    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) { event.put(CHOCOBO.get(), createAttributes().build()); }
}
