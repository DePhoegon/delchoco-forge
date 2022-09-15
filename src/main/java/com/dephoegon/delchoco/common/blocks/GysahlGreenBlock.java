package com.dephoegon.delchoco.common.blocks;

import com.dephoegon.delbase.aid.block.colorshift.grav.sandBlock;
import com.dephoegon.delbase.aid.block.colorshift.grav.solidSandBlock;
import com.dephoegon.delbase.aid.block.colorshift.slab.sandSlab;
import com.dephoegon.delbase.aid.block.colorshift.slab.sandSlabEnergy;
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
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return (ableSurvive(worldIn, pos, 3)) && ((state.getBlock() == this || state.getValue(AGE) == MAX_AGE) &&
                super.canSurvive(state, worldIn, pos));
    }
    private boolean ableSurvive(@NotNull LevelReader worldIn, BlockPos pos, int lightLevel) {
        if (worldIn.getRawBrightness(pos, 0) >= lightLevel) return true;
        if (worldIn.canSeeSky(pos)) return true;
        BlockState block = worldIn.getBlockState(pos.below());
        return block.getBlock().defaultBlockState() == Blocks.FARMLAND.defaultBlockState() || ablePlace(block, worldIn, pos.below(), lightLevel);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return super.mayPlaceOn(state, worldIn, pos) || ablePlace(state, worldIn, pos, 7);
    }
    private boolean ablePlace(@NotNull BlockState state, BlockGetter worldIn, BlockPos pos, int light) {
        // Sand Slabs explictedly allowed because of desert looks & beaches
        if (state.getBlock() instanceof sandSlab || state.getBlock() instanceof sandSlabEnergy) {
            SlabType s_type = state.getValue(TYPE);
            boolean w_logged = state.getValue(SlabBlock.WATERLOGGED);
            if (s_type != SlabType.BOTTOM) { if (w_logged || checkDirection(worldIn, pos)) { return true; } }
        }
        // Sand & Solid stand stated, for the obvious reasons, & Array list used for randomish sorted group of blocks selectively choosen
        if (state.getBlock() instanceof sandBlock || state.getBlock() instanceof solidSandBlock || blockPlaceableOnList().contains(state.getBlock().defaultBlockState())) { return checkDirection(worldIn, pos); }
        // phrased this way to keep the old & admittedly personally interesting of planting in grass in daylight of 7 & allowing canSurvive to choose if it pops off or not.
        return worldIn.getLightEmission(pos) > light && state.is(Blocks.GRASS_BLOCK);
    }
    private boolean checkDirection(BlockGetter worldIn , BlockPos pos) {
        int start = 1;
        int limit = 4;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockState blockState = worldIn.getBlockState(pos.relative(direction));
            FluidState fluidState = worldIn.getFluidState(pos.relative(direction));
            if (fluidState.is(FluidTags.WATER) || blockState.is(Blocks.FROSTED_ICE)) { return true; }
            if (direction == Direction.SOUTH || direction == Direction.NORTH) {
                return directionExtension(worldIn, pos, start, limit, direction, null);
            } else {
                return directionExtension(worldIn, pos, start, limit, direction, Direction.NORTH) || directionExtension(worldIn, pos, start, limit, direction, Direction.SOUTH);
            }
        }
        return false;
    }
    private boolean directionExtension(BlockGetter worldIn, BlockPos pos, int current, int limit, Direction direction, Direction stretch) {
        BlockPos shiftDirection = pos.relative(direction);
        if (current <= limit) {
            BlockState blockState = worldIn.getBlockState(shiftDirection.relative(direction));
            FluidState fluidState = worldIn.getFluidState(shiftDirection.relative(direction));
            if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                return directionExtension(worldIn, shiftDirection, current+1, limit, direction, null) || fluidState.is(FluidTags.WATER) || blockState.is(Blocks.FROSTED_ICE);
            } else {
                return directionExtension(worldIn, shiftDirection, current+1, limit, direction, stretch) || directionExtension(worldIn, shiftDirection, 1, limit, stretch, null);
            }
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }
    private @NotNull ArrayList<Object> blockPlaceableOnList() {
        ArrayList<Object> block_set = new ArrayList();
        block_set.add(Blocks.SAND.defaultBlockState());
        block_set.add(Blocks.RED_SAND.defaultBlockState());
        block_set.add(Blocks.DIRT.defaultBlockState());
        block_set.add(Blocks.COARSE_DIRT.defaultBlockState());
        block_set.add(Blocks.FARMLAND.defaultBlockState());
        return block_set;
    }
}
