package com.dephoegon.delchoco.common.world.worldgen;

import com.dephoegon.delchoco.common.blocks.GysahlGreenBlock;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;


public class ModFeatureConfig {
	protected static final BlockState GYSAHL_GREEN = ModRegistry.GYSAHL_GREEN.get().defaultBlockState().setValue(GysahlGreenBlock.AGE, GysahlGreenBlock.MAX_AGE);
	public static final ResourceKey<ConfiguredFeature<?, ?>> GYSAHL_KEY = registerKey("gysahl_green");

	@SuppressWarnings("unused")
	public static <T> void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		featureRegister(context, GYSAHL_KEY, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(GYSAHL_GREEN)), List.of(Blocks.GRASS_BLOCK, Blocks.NETHERRACK, Blocks.CLAY, Blocks.MOSS_BLOCK, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.TUFF, Blocks.DRIPSTONE_BLOCK, Blocks.END_STONE), 64));
	}
	public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(DELCHOCO_ID, name));
	}

	@SuppressWarnings("SameParameterValue")
	private static <FC extends FeatureConfiguration, F extends Feature<FC>> void featureRegister(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}
}
