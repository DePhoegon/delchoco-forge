package com.dephoegon.delchoco.common.blocks;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.blockentities.ChocoboEggBlockEntity;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.breeding.ChocoboBreedInfo;
import com.dephoegon.delchoco.common.entities.breeding.ChocoboStatSnapshot;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

import static com.dephoegon.delchoco.common.entities.breeding.ChocoboBreedInfo.getFromNbtOrDefault;

public class ChocoboEggBlock extends BaseEntityBlock {
    public final static String NBTKEY_HATCHINGSTATE_TIME = "Time";
    public final static String NBTKEY_HATCHINGSTATE = "HatchingState";
    public final static String NBTKEY_BREEDINFO = "BreedInfo";

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(4, 1, 4, 12, 6, 12),
            Block.box(5, 6, 5, 11, 8, 11),
            Block.box(6, 8, 6, 10, 10, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ChocoboEggBlock(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.MODEL; }
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) { return SHAPE; }
    public static boolean isChocoboEgg(@NotNull ItemStack itemStack) { return itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof ChocoboEggBlock; }
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) { return new ChocoboEggBlockEntity(pos, state); }
    @Override
    public void setPlacedBy(@NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        if (!worldIn.isClientSide) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (!(tile instanceof ChocoboEggBlockEntity)) { return; }
            ChocoboBreedInfo breedInfo = getFromNbtOrDefault(stack.getTagElement(NBTKEY_BREEDINFO));
            ((ChocoboEggBlockEntity) tile).setBreedInfo(breedInfo);
        }
        super.setPlacedBy(worldIn, pos, state, placer, stack);
    }
    @Override
    public void playerDestroy(@NotNull Level worldIn, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
        if (worldIn.isClientSide) { return; }
        if (te instanceof ChocoboEggBlockEntity) {
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            ItemStack itemStack = new ItemStack(ModRegistry.CHOCOBO_EGG.get());
            ChocoboBreedInfo breedInfo = ((ChocoboEggBlockEntity) te).getBreedInfo();
            if (breedInfo == null) { breedInfo = getFromNbtOrDefault(null); }
            itemStack.addTagElement(NBTKEY_BREEDINFO, breedInfo.serialize());
            popResource(worldIn, pos, itemStack);
            return;
        }
        super.playerDestroy(worldIn, player, pos, state, te, stack);
    }
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag nbtBreedInfo = stack.getTagElement(NBTKEY_BREEDINFO);
        if (nbtBreedInfo != null) {
            final ChocoboBreedInfo info = new ChocoboBreedInfo(nbtBreedInfo);
            final ChocoboStatSnapshot mother = info.getMother();
            final ChocoboStatSnapshot father = info.getFather();
            tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".chocobo_egg.tooltip.mother_info", (int) mother.health, (int) (mother.speed * 100), (int) mother.stamina, mother.color.getEggText()));
            tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".chocobo_egg.tooltip.father_info", (int) father.health, (int) (father.speed * 100), (int) father.stamina, father.color.getEggText()));
        } else { tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".chocobo_egg.tooltip.invalid_egg")); }
    }
}