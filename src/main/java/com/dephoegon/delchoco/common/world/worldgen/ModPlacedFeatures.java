package com.dephoegon.delchoco.common.world.worldgen;

import com.dephoegon.delchoco.DelChoco;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dephoegon.delchoco.common.world.worldgen.ModFeatureConfig.GYSAHL_KEY;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> GYSAHL_PLACED_KEY = createKey("gysahl_placed");

    public static void bootstrap(@NotNull BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, GYSAHL_PLACED_KEY, configuredFeatures.getOrThrow(GYSAHL_KEY), CountPlacement.of(125), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, RarityFilter.onAverageOnceEvery(12), BiomeFilter.biome());
    }

    @SuppressWarnings("SameParameterValue")
    private static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(DelChoco.DELCHOCO_ID, name));
    }

    private static void register(@NotNull BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    @SuppressWarnings("SameParameterValue")
    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}
