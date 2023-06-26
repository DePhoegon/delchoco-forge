package com.dephoegon.delchoco.aid;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.minecraft.world.level.biome.Biomes.*;

public class SpawnBiomesChecks {
    private static @NotNull ArrayList<ResourceKey<Biome>> netherBiomes() {
        ArrayList<ResourceKey<Biome>> netherList = new ArrayList<>();
        netherList.add(NETHER_WASTES);
        netherList.add(WARPED_FOREST);
        netherList.add(SOUL_SAND_VALLEY);
        netherList.add(BASALT_DELTAS);
        return netherList;
    }
    private static @NotNull ArrayList<ResourceKey<Biome>> overWorldBiomes() {
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
        return overWorldList;
    }
    private static ArrayList<ResourceKey<Biome>> endBiomes() {
        ArrayList<ResourceKey<Biome>> endList = new ArrayList<>();
        endList.add(THE_END);
        endList.add(END_HIGHLANDS);
        endList.add(END_MIDLANDS);
        endList.add(SMALL_END_ISLANDS);
        endList.add(END_BARRENS);
        return  endList;
    }
    public static boolean allBiomes(ResourceKey<Biome> key) {
        ArrayList<ResourceKey<Biome>> out = new ArrayList<>();
        out.addAll(endBiomes());
        out.addAll(netherBiomes());
        out.addAll(overWorldBiomes());
        return out.contains(key);
    }
}