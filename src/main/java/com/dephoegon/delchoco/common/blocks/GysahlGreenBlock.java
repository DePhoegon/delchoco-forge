package com.dephoegon.delchoco.common.blocks;

import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GysahlGreenBlock extends CropBlock {
    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    @SuppressWarnings("unused") // used by class factory
    public GysahlGreenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
    }
    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModRegistry.GYSAHL_GREEN_SEEDS::get;
    }
    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, @NotNull BlockPos pos) {
        BlockState block = worldIn.getBlockState(pos.below());
        if (state.getBlock() == this) { return blockPlaceableOnList().contains(block.getBlock().defaultBlockState()); }
        return mayPlaceOn(worldIn.getBlockState(pos), worldIn, pos.below());
    }
    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) { return blockPlaceableOnList().contains(state.getBlock().defaultBlockState()); }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) { builder.add(AGE); }
    @Override
    public @NotNull IntegerProperty getAgeProperty() { return AGE; }
    @Override
    public int getMaxAge() { return MAX_AGE; }
    private @NotNull ArrayList<BlockState> blockPlaceableOnList() {
        ArrayList<BlockState> block_set = new ArrayList<>();
        block_set.add(0, Blocks.END_STONE.defaultBlockState());
        block_set.add(1, Blocks.DIRT.defaultBlockState());
        block_set.add(2, Blocks.COARSE_DIRT.defaultBlockState());
        block_set.add(3, Blocks.PODZOL.defaultBlockState());
        block_set.add(4, Blocks.GRASS_BLOCK.defaultBlockState());
        block_set.add(5, Blocks.NETHERRACK.defaultBlockState());
        block_set.add(6, Blocks.WARPED_WART_BLOCK.defaultBlockState());
        block_set.add(7, Blocks.NETHER_WART_BLOCK.defaultBlockState());
        block_set.add(8, Blocks.CRIMSON_NYLIUM.defaultBlockState());
        block_set.add(9, Blocks.WARPED_NYLIUM.defaultBlockState());
        block_set.add(10, Blocks.CLAY.defaultBlockState());
        block_set.add(11, Blocks.MOSS_BLOCK.defaultBlockState());
        block_set.add(12, Blocks.DRIPSTONE_BLOCK.defaultBlockState());
        block_set.add(13, Blocks.TUFF.defaultBlockState());
        return block_set;
    }
}