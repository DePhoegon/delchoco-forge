package com.dephoegon.delchoco.common.blocks;

import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static net.minecraft.world.level.block.SlabBlock.TYPE;

public class GysahlGreenBlock extends CropBlock {
    public static final int MAX_AGE = 4;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    @SuppressWarnings("unused") // used by class factory
    public GysahlGreenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModRegistry.GYSAHL_GREEN_SEEDS::get;
    }

    @Override
    public boolean canSurvive(BlockState state, @NotNull LevelReader worldIn, BlockPos pos) {
        boolean light = worldIn.getRawBrightness(pos, 0) >= 5;
        boolean sky = worldIn.canSeeSky(pos);
        BlockState block = worldIn.getBlockState(pos);
        boolean blocks = blockPlaceableOnList().contains(block.getBlock().defaultBlockState());
        return (light || sky || blocks) && ((state.getBlock() == this || state.getValue(AGE) == MAX_AGE) &&
                super.canSurvive(state, worldIn, pos));
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, BlockGetter worldIn, BlockPos pos) {
        boolean miscs = blockPlaceableOnList().contains(state.getBlock().defaultBlockState());
        return super.mayPlaceOn(state, worldIn, pos) || miscs;
    }
    private boolean directionExtension(BlockGetter worldIn, @NotNull BlockPos pos, int current, int limit, Direction direction, Direction stretch) {
        BlockPos shiftDirection = pos.relative(direction);
        if (current <= limit) {
            BlockState blockState = worldIn.getBlockState(shiftDirection.relative(direction));
            FluidState fluidState = worldIn.getFluidState(shiftDirection.relative(direction));
            if (stretch == null) {
                return fluidState.is(FluidTags.WATER) || blockState.is(Blocks.FROSTED_ICE) || directionExtension(worldIn, shiftDirection, current+1, limit, direction, null);
            } else {
                return directionExtension(worldIn, shiftDirection, current+1, limit, direction, stretch) || directionExtension(worldIn, shiftDirection, 1, limit, stretch, null);
            }
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(AGE); }

    @Override
    public IntegerProperty getAgeProperty() { return AGE; }

    @Override
    public int getMaxAge() { return MAX_AGE; }
    private @NotNull ArrayList<BlockState> blockPlaceableOnList() {
        ArrayList<BlockState> block_set = new ArrayList<>();
        block_set.add(0, Blocks.END_STONE.defaultBlockState());
        block_set.add(1, Blocks.NETHERRACK.defaultBlockState());
        block_set.add(2, Blocks.DIRT.defaultBlockState());
        block_set.add(3, Blocks.COARSE_DIRT.defaultBlockState());
        block_set.add(4, Blocks.FARMLAND.defaultBlockState());
        block_set.add(5, Blocks.GRASS_BLOCK.defaultBlockState());
        return block_set;
    }
}
