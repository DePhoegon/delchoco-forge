package com.dephoegon.delchoco.common.world.worldgen;

import com.dephoegon.delchoco.common.ChocoConfig;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public class ModPlacements {
	public static final List<PlacementModifier> GYSAHL_PLACEMENT_NO_BIOME = List.of(CountPlacement.of(UniformInt.of(0, 5)), RarityFilter.onAverageOnceEvery((int) ChocoConfig.COMMON.gysahlGreenSpawnChance.get().doubleValue() * 10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE);
	public static final RarityFilter GYSAHL_RARITY_UNDERGROUND = RarityFilter.onAverageOnceEvery((int) ChocoConfig.COMMON.gysahlGreenSpawnChance.get().doubleValue() * 10*6);

	public static final Holder<PlacedFeature> PATCH_GYSAHL_NO_BIOME = PlacementUtils.register("patch_gysahl_no_biome", ModFeatureConfigs.PATCH_GYSAHL_GRASS, GYSAHL_PLACEMENT_NO_BIOME);
	public static final Holder<PlacedFeature> PATCH_GYSAHL_UNDERGROUND = PlacementUtils.register("patch_gysahl_underground", ModFeatureConfigs.PATCH_GYSAHL_GRASS, GYSAHL_RARITY_UNDERGROUND, InSquarePlacement.spread(), PlacementUtils.FULL_RANGE);
}