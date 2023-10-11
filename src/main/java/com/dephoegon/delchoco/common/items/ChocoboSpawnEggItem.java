package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.dephoegon.delchoco.common.init.ModEntities.CHOCOBO;
import static java.lang.Math.random;

public class ChocoboSpawnEggItem extends Item {
    private final ChocoboColor color;

    public ChocoboSpawnEggItem(Properties pProperties, ChocoboColor color) {
        super(pProperties);
        this.color = color;
    }
    private @NotNull Component name(@NotNull ItemStack egg) { return egg.getHoverName(); }
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level worldIn = context.getLevel();
        if (worldIn.isClientSide) { return InteractionResult.SUCCESS; }

        final Chocobo chocobo = CHOCOBO.get().create(worldIn);
        if (chocobo != null) {
            final BlockPos pos = context.getClickedPos();
            final Player player = context.getPlayer();
            BlockState blockState = worldIn.getBlockState(pos);
            if (blockState.is(Blocks.SPAWNER)) {
                BlockEntity blockEntity = worldIn.getBlockEntity(pos);
                if (blockEntity instanceof SpawnerBlockEntity) {
                    BaseSpawner baseSpawner = ((SpawnerBlockEntity)blockEntity).getSpawner();
                    baseSpawner.setEntityId(this.color.getTypeByColor());
                    blockEntity.setChanged();
                    worldIn.sendBlockUpdated(pos, blockState, blockState, 3);
                }
            } else {
                if (player != null) { if (player.isCrouching()) { chocobo.setAge(-7500); } }

                chocobo.moveTo(pos.getX() + .5, pos.getY() + 1.5F, pos.getZ() + .5, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                chocobo.yHeadRot = chocobo.getYRot();
                chocobo.yBodyRot = chocobo.getYRot();
                chocobo.setMale(.50f > (float) random());
                chocobo.setChocoboColor(color);
                Component nameCheck = name(context.getItemInHand());
                if (context.getItemInHand().hasCustomHoverName()) { chocobo.setCustomName(nameCheck); }
                chocobo.setFlame(color == ChocoboColor.FLAME);
                chocobo.setWaterBreath(wbChocobos().contains(color));
                chocobo.setWitherImmune(wiChocobos().contains(color));
                chocobo.setPoisonImmune(piChocobos().contains(color));
                chocobo.setFromEgg(true);
                chocobo.setChocoboScale(chocobo.isMale(), 0, false);
                chocobo.finalizeSpawn((ServerLevel) worldIn, worldIn.getCurrentDifficultyAt(chocobo.blockPosition()), MobSpawnType.SPAWN_EGG, null, null);
                worldIn.addFreshEntity(chocobo);
                chocobo.playAmbientSound();
            }
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
    public static @NotNull ArrayList<ChocoboColor> wbChocobos() {
        ArrayList<ChocoboColor> out = new ArrayList<>();
        out.add(ChocoboColor.BLUE);
        out.add(ChocoboColor.GOLD);
        return out;
    }
    public static @NotNull ArrayList<ChocoboColor> wiChocobos() {
        ArrayList<ChocoboColor> out = new ArrayList<>();
        out.add(ChocoboColor.BLACK);
        out.add(ChocoboColor.GOLD);
        return out;
    }
    public static @NotNull ArrayList<ChocoboColor> piChocobos() {
        ArrayList<ChocoboColor> out = new ArrayList<>();
        out.add(ChocoboColor.GREEN);
        out.add(ChocoboColor.GOLD);
        return out;
    }
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> toolTip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, toolTip, flag); //if shifting, show tip1 (if not empty)
    }
}
