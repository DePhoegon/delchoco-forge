package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnEggItem.wbChocobos;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnerItemHelper.*;
import static com.dephoegon.delchoco.utils.RandomHelper.random;
import static net.minecraft.world.level.block.Blocks.*;

public class ChocoboSummoning {
    public final boolean isRandomAlter;
    private final ChocoboColor color;
    private final ItemStack summonItem;
    private int damage;

    public ChocoboSummoning(@NotNull Level worldIn, @NotNull BlockPos alterBlock, Player player, ItemStack summonItem) {
        BlockState Alter = worldIn.getBlockState(alterBlock).getBlock().defaultBlockState();
        this.color = TARGETED_ALTERS.get(Alter);
        this.isRandomAlter = randomAlter(Alter);
        this.summonItem = summonItem;
        if (isRandomAlter || color != null) alterSwitch(color, alterBlock, worldIn, player);
    }
    private void alterSwitch(ChocoboColor validAlterBlock, BlockPos pos, Level worldIn, Player player) {
        BlockState pillar;
        if(validAlterBlock != null)  {
            pillar = switch (validAlterBlock) {
                case RED -> RED_TERRACOTTA.defaultBlockState();
                case PURPLE -> PURPUR_PILLAR.defaultBlockState();
                case PINK -> MUSHROOM_STEM.defaultBlockState();
                case BLUE -> DRIED_KELP_BLOCK.defaultBlockState();
                case GOLD -> GOLD_BLOCK.defaultBlockState();
                case BLACK -> CACTUS.defaultBlockState();
                case FLAME -> MAGMA_BLOCK.defaultBlockState();
                case GREEN -> MOSS_BLOCK.defaultBlockState();
                case WHITE -> SNOW_BLOCK.defaultBlockState();
                default -> HAY_BLOCK.defaultBlockState();
            };
        } else { pillar = null; }
        boolean summon = pillarCheck(pos, worldIn, pillar) && baseCheck(pos, worldIn);
        if (summon) { cost(player, pos, worldIn); summonChocobo(worldIn, pos, player); }
    }
    private boolean pillarCheck(BlockPos alterPOS, Level worldIn, BlockState pillar) {
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -2; y < 2; y++) {
                    BlockState blockState = worldIn.getBlockState(new BlockPos(alterPOS.getX()+x, alterPOS.getY()+y, alterPOS.getZ()+z));
                    if ((x == -3 || x == 3) && (z == -3 || z == 3)) {
                        if (blockState != pillar && pillar != null) return false;
                        continue;
                    }
                    if (x == 0 && y == 0 && z ==0) { continue; }
                    if (y < 0) { if (x < 2 && x > -2 && z < 2 && z > -2) { continue; } }
                    if (y < -1) { if (x < 3 && x > -3 && z < 3 && z > -3) { continue; } }
                    if (!blockState.isAir()) return false;
                }
            }
        }
        return true;
    }
    private boolean baseCheck(BlockPos alterPOS, Level worldIn) {
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -3; y < 0; y++) {
                    BlockState baseCheck = worldIn.getBlockState(new BlockPos(alterPOS.getX()+x, alterPOS.getY()+y, alterPOS.getZ()+z));
                    if (x == 0 && z == 0) { if (!baseCheck.getFluidState().isSource()) { return false; } continue; }
                    else if (y > -3 && (x < -2 || x > 2 || z < -2 || z > 2)) { continue; }
                    else if (y > -2 && (x < -1 || x > 1 || z < -1 || z > 1)) { continue; }
                    if (!alterBlocks(baseCheck.getBlock().defaultBlockState())) { return false; }
                }
            }
        }
        return true;
    }
    private @NotNull Component customName() { return summonItem.getHoverName(); }
    private void summonChocobo(@NotNull Level worldIn, BlockPos pos, Player player) {
        if (!worldIn.isClientSide) {
            final Chocobo chocobo = ModEntities.CHOCOBO.get().create(worldIn);
            if (chocobo != null) {
                if (player != null) { if (player.isCrouching()) { chocobo.setAge(-7500); } }
                chocobo.moveTo(pos.getX() + placeRange(random.nextInt(100) + 1), pos.getY() + 1.5F, pos.getZ() + placeRange(random.nextInt(100) + 1), Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                chocobo.yHeadRot = chocobo.getYRot();
                chocobo.yBodyRot = chocobo.getYRot();
                if (!isRandomAlter) {
                    chocobo.setChocoboColor(color);
                    chocobo.setFromEgg(true);
                    chocobo.setFlame(color == ChocoboColor.FLAME);
                    chocobo.setWaterBreath(wbChocobos().contains(color));
                }
                if (summonItem.hasCustomHoverName()) { chocobo.setCustomName(customName()); }
                chocobo.finalizeSpawn((ServerLevel)worldIn, worldIn.getCurrentDifficultyAt(chocobo.blockPosition()), MobSpawnType.SPAWN_EGG, null, null);
                worldIn.addFreshEntity(chocobo);
                chocobo.playAmbientSound();
                summonItem.shrink(1);
            }
        }
    }
    private int placeRange(int chanceOf100) {
        int negPos = chanceOf100 > 50 ? -1 : 1;
        return random.nextInt(2)+1 + negPos;
    }
    private void cost(Player player, BlockPos pos, Level worldIn) {
        boolean eatAlter = eatAlter(player);
        if (eatAlter) { worldIn.setBlockAndUpdate(pos, AIR.defaultBlockState()); }
        else {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(CHOCO_DISGUISE_CHESTPLATE.get())) {
                player.getItemBySlot(EquipmentSlot.CHEST).hurtAndBreak(this.damage, player, (event) -> {
                    event.broadcastBreakEvent(EquipmentSlot.CHEST);
                });
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(CHOCO_DISGUISE_HELMET.get())) {
                player.getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak(this.damage, player, (event) -> {
                    event.broadcastBreakEvent(EquipmentSlot.HEAD);
                });
            }
            if (player.getItemBySlot(EquipmentSlot.LEGS).getItem().equals(CHOCO_DISGUISE_LEGGINGS.get())) {
                player.getItemBySlot(EquipmentSlot.LEGS).hurtAndBreak(this.damage, player, (event) -> {
                    event.broadcastBreakEvent(EquipmentSlot.LEGS);
                });
            }
            if (player.getItemBySlot(EquipmentSlot.FEET).getItem().equals(CHOCO_DISGUISE_BOOTS.get())) {
                player.getItemBySlot(EquipmentSlot.FEET).hurtAndBreak(this.damage, player, (event) -> {
                    event.broadcastBreakEvent(EquipmentSlot.FEET);
                });
            }
        }
    }
    private boolean eatAlter(@NotNull Player player){
        int alterEatChance = 100;
        boolean eatAlter;
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem().equals(CHOCO_DISGUISE_CHESTPLATE.get())) { alterEatChance = alterEatChance-25; }
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem().equals(CHOCO_DISGUISE_HELMET.get())) { alterEatChance = alterEatChance-25; }
        if (player.getItemBySlot(EquipmentSlot.LEGS).getItem().equals(CHOCO_DISGUISE_LEGGINGS.get())) { alterEatChance = alterEatChance-25; }
        if (player.getItemBySlot(EquipmentSlot.FEET).getItem().equals(CHOCO_DISGUISE_BOOTS.get())) { alterEatChance = alterEatChance-25; }
        eatAlter = random.nextInt(100) + 1 <= alterEatChance;
        this.damage = switch (alterEatChance) {
            case 75 -> 4;
            case 50 -> 3;
            case 25 -> 2;
            case 0 -> 1;
            default -> 0;
        };
        return eatAlter;
    }
}