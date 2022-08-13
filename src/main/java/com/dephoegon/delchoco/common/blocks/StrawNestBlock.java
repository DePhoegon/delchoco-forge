package com.dephoegon.delchoco.common.blocks;

import com.dephoegon.delchoco.common.blockentities.ChocoboNestBlockEntity;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class StrawNestBlock extends BaseEntityBlock {
    public final static BooleanProperty HAS_EGG = BooleanProperty.create("egg");
    protected static final VoxelShape EMPTY_SHAPE = Stream.of(
            Block.box(1, 0, 1, 15, 1, 15),
            Block.box(0, 1, 0, 16, 3, 2),
            Block.box(0, 1, 14, 16, 3, 16),
            Block.box(0, 1, 2, 2, 3, 14),
            Block.box(14, 1, 2, 16, 3, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(4, 1, 4, 12, 6, 12),
            Block.box(5, 6, 5, 11, 8, 11),
            Block.box(6, 8, 6, 10, 10, 10),
            Block.box(14, 1, 2, 16, 3, 14),
            Block.box(1, 0, 1, 15, 1, 15),
            Block.box(0, 1, 0, 16, 3, 2),
            Block.box(0, 1, 14, 16, 3, 16),
            Block.box(0, 1, 2, 2, 3, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public StrawNestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_EGG, false));
    }
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(HAS_EGG) ? SHAPE : EMPTY_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HAS_EGG);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player playerIn, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if(!(tile instanceof ChocoboNestBlockEntity nest))
            return InteractionResult.FAIL;

        ItemStack heldItem = playerIn.getItemInHand(handIn);
        if (ChocoboEggBlock.isChocoboEgg(heldItem)) {
            if (!nest.getEggItemStack().isEmpty()) return InteractionResult.FAIL;
            if (worldIn.isClientSide) return InteractionResult.SUCCESS;
            nest.setEggItemStack(playerIn.getItemInHand(handIn).copy());
            playerIn.getItemInHand(handIn).shrink(1);
            return InteractionResult.SUCCESS;
        } else {
            if(!worldIn.isClientSide) {
                NetworkHooks.openGui((ServerPlayer) playerIn, nest, pos);
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createNestTicker(level, blockEntityType, ModRegistry.STRAW_NEST_TILE.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createNestTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends ChocoboNestBlockEntity> nestBlockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, nestBlockEntityType, ChocoboNestBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ChocoboNestBlockEntity(pos, state);
    }
}
