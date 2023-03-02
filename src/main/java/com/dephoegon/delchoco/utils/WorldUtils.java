package com.dephoegon.delchoco.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WorldUtils {
    public static int getDistanceToSurface(BlockPos startPos, @NotNull Level world) {
        BlockPos lastLiquidPos = null;

        for (BlockPos pos = startPos; pos.getY() < world.getMaxBuildHeight(); pos = pos.above()) {
            BlockState state = world.getBlockState(pos);
            if (!state.getMaterial().isLiquid()) { break; }
            lastLiquidPos = pos;
        }
        return lastLiquidPos == null ? -1 : lastLiquidPos.getY() - startPos.getY();
    }
}