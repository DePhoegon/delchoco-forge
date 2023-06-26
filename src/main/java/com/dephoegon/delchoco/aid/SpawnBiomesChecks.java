package com.dephoegon.delchoco.aid;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;
import static net.minecraft.world.level.biome.Biomes.*;
import static net.minecraftforge.common.BiomeDictionary.hasType;

public class SpawnBiomesChecks {
    public static final String overWorld = "OverWorld";
    public static final String nether = "Nether";
    public static final String theEnd = "TheEnd";
    public static boolean typeCheck(ResourceKey<Biome> biomesKey, BiomeDictionary.Type type, String OptionalDimension) {
        boolean biomes = hasType(biomesKey, type);
        if (OptionalDimension == null) { OptionalDimension = "default"; }
        boolean dimensions = switch (OptionalDimension) {
            default -> true;
            case overWorld -> overWorldCheck(biomesKey, false);
            case nether -> netherCheck(biomesKey, false);
            case theEnd -> theEndCheck(biomesKey, false);
        };
        return biomes && dimensions;
    }
    public static boolean netherCheck(ResourceKey<Biome> biomesKey, boolean includeBiomesCheck) {
        boolean nether = hasType(biomesKey, BiomeDictionary.Type.NETHER);
        boolean biomesCheck = !includeBiomesCheck || netherBiomes(biomesKey);
        return (biomesCheck || nether) && COMMON.netherSpawns.get();
    }
    private static boolean netherBiomes(ResourceKey<Biome> Key) {
        ArrayList<ResourceKey<Biome>> netherList = new ArrayList<>();
        netherList.add(NETHER_WASTES);
        netherList.add(WARPED_FOREST);
        netherList.add(SOUL_SAND_VALLEY);
        netherList.add(BASALT_DELTAS);
        return netherList.contains(Key);
    }
    public static boolean overWorldCheck(ResourceKey<Biome> biomesKey, boolean includeBiomesCheck) {
        boolean oceans = typeCheck(biomesKey, BiomeDictionary.Type.OCEAN, null);
        boolean overWorld = hasType(biomesKey, BiomeDictionary.Type.OVERWORLD) && !oceans;
        boolean biomesCheck = !includeBiomesCheck || overWorldBiomes(biomesKey);
        return (biomesCheck || overWorld) && COMMON.overworldSpawns.get();
    }
    private static boolean overWorldBiomes(ResourceKey<Biome> Key) {
        ArrayList<ResourceKey<Biome>> overWorldList = new ArrayList<>();
        overWorldList.add(PLAINS);
        overWorldList.add(SUNFLOWER_PLAINS);
        overWorldList.add(SNOWY_BEACH);
        overWorldList.add(ICE_SPIKES);
        overWorldList.add(DESERT);
        overWorldList.add(SWAMP);
        overWorldList.add(FOREST);
        overWorldList.add(BIRCH_FOREST);
        overWorldList.add(FLOWER_FOREST);
        overWorldList.add(DARK_FOREST);
        overWorldList.add(OLD_GROWTH_BIRCH_FOREST);
        overWorldList.add(OLD_GROWTH_PINE_TAIGA);
        overWorldList.add(OLD_GROWTH_SPRUCE_TAIGA);
        overWorldList.add(TAIGA);
        overWorldList.add(SNOWY_TAIGA);
        overWorldList.add(SAVANNA);
        overWorldList.add(SAVANNA_PLATEAU);
        overWorldList.add(WINDSWEPT_HILLS);
        overWorldList.add(WINDSWEPT_GRAVELLY_HILLS);
        overWorldList.add(WINDSWEPT_FOREST);
        overWorldList.add(WINDSWEPT_SAVANNA);
        overWorldList.add(JUNGLE);
        overWorldList.add(SPARSE_JUNGLE);
        overWorldList.add(BAMBOO_JUNGLE);
        overWorldList.add(BADLANDS);
        overWorldList.add(ERODED_BADLANDS);
        overWorldList.add(WOODED_BADLANDS);
        overWorldList.add(MEADOW);
        overWorldList.add(GROVE);
        overWorldList.add(SNOWY_SLOPES);
        overWorldList.add(RIVER);
        overWorldList.add(FROZEN_RIVER);
        overWorldList.add(BEACH);
        overWorldList.add(SNOWY_BEACH);
        overWorldList.add(STONY_SHORE);
        overWorldList.add(WARM_OCEAN);
        overWorldList.add(LUKEWARM_OCEAN);
        overWorldList.add(MUSHROOM_FIELDS);
        overWorldList.add(DRIPSTONE_CAVES);
        overWorldList.add(LUSH_CAVES);
        return overWorldList.contains(Key);
    }
    public static boolean theEndCheck(ResourceKey<Biome> biomesKey, boolean includeBiomesCheck) {
        boolean theEnd = hasType(biomesKey, BiomeDictionary.Type.END);
        boolean biomesCheck = !includeBiomesCheck || endBiomes(biomesKey);
        return (biomesCheck || theEnd) && COMMON.endSpawns.get();
    }
    private static boolean endBiomes(ResourceKey<Biome> Key) {
        ArrayList<ResourceKey<Biome>> endList = new ArrayList<>();
        endList.add(THE_END);
        endList.add(END_HIGHLANDS);
        endList.add(END_MIDLANDS);
        endList.add(SMALL_END_ISLANDS);
        endList.add(END_BARRENS);
        return  endList.contains(Key);
    }
    public static boolean biomesCheck(ResourceKey<Biome> biomesKey, ResourceKey<Biome> biomes) { return biomesKey == biomes; }
}