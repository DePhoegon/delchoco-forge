package com.dephoegon.delchoco.common.world.worldgen;

import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.blocks.GysahlGreenBlock;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class ModFeatureConfigs {
	protected static final BlockState GYSAHL_GREEN = ModRegistry.GYSAHL_GREEN.get().defaultBlockState().setValue(GysahlGreenBlock.AGE, GysahlGreenBlock.MAX_AGE);

	public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_GYSAHL_GRASS = FeatureUtils.register("patch_gysahl_grass", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(GYSAHL_GREEN)), List.of(Blocks.GRASS_BLOCK, Blocks.NETHERRACK, Blocks.CLAY, Blocks.MOSS_BLOCK, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.TUFF, Blocks.DRIPSTONE_BLOCK), ChocoConfig.COMMON.gysahlGreenPatchSize.get()));
}
