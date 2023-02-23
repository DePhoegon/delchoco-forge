package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.handler.ChocoboSummoning;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static net.minecraft.world.level.block.Blocks.*;

public class ChocoboSpawnerItemHelper extends Item {
    public ChocoboSpawnerItemHelper(Properties pProperties) { super(pProperties); }
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        if (ChocoConfig.COMMON.summonSpawns.get()) {
            Level worldIn = context.getLevel();
            BlockPos alter = context.getClickedPos();
            Player player = context.getPlayer();
            ItemStack summonObject = context.getItemInHand();
            new ChocoboSummoning(worldIn, alter, player, summonObject);
        }
        return InteractionResult.PASS;
    }
    public static final Map<BlockState, ChocoboColor> TARGETED_ALTERS = Util.make(Maps.newHashMap(), (map) ->{
        map.put(MAGMA_BLOCK.defaultBlockState(), ChocoboColor.FLAME);
        map.put(PURPUR_BLOCK.defaultBlockState(), ChocoboColor.PURPLE);
        map.put(END_STONE_BRICKS.defaultBlockState(), ChocoboColor.PURPLE);
        map.put(MYCELIUM.defaultBlockState(), ChocoboColor.PINK);
        map.put(MUSHROOM_STEM.defaultBlockState(), ChocoboColor.PINK);
        map.put(RED_MUSHROOM_BLOCK.defaultBlockState(), ChocoboColor.PINK);
        map.put(BROWN_MUSHROOM_BLOCK.defaultBlockState(), ChocoboColor.PINK);
        map.put(ICE.defaultBlockState(), ChocoboColor.WHITE);
        map.put(BLUE_ICE.defaultBlockState(), ChocoboColor.WHITE);
        map.put(PACKED_ICE.defaultBlockState(), ChocoboColor.WHITE);
        map.put(BIRCH_WOOD.defaultBlockState(), ChocoboColor.WHITE);
        map.put(BRAIN_CORAL.defaultBlockState(), ChocoboColor.BLUE);
        map.put(BRAIN_CORAL_FAN.defaultBlockState(), ChocoboColor.BLUE);
        map.put(BRAIN_CORAL_BLOCK.defaultBlockState(), ChocoboColor.BLUE);
        map.put(TUBE_CORAL.defaultBlockState(), ChocoboColor.BLUE);
        map.put(TUBE_CORAL_FAN.defaultBlockState(), ChocoboColor.BLUE);
        map.put(TUBE_CORAL_BLOCK.defaultBlockState(), ChocoboColor.BLUE);
        map.put(BUBBLE_CORAL.defaultBlockState(), ChocoboColor.BLUE);
        map.put(BUBBLE_CORAL_FAN.defaultBlockState(), ChocoboColor.BLUE);
        map.put(BUBBLE_CORAL_BLOCK.defaultBlockState(), ChocoboColor.BLUE);
        map.put(FIRE_CORAL.defaultBlockState(), ChocoboColor.BLUE);
        map.put(FIRE_CORAL_FAN.defaultBlockState(), ChocoboColor.BLUE);
        map.put(FIRE_CORAL_BLOCK.defaultBlockState(), ChocoboColor.BLUE);
        map.put(HORN_CORAL.defaultBlockState(), ChocoboColor.BLUE);
        map.put(HORN_CORAL_FAN.defaultBlockState(), ChocoboColor.BLUE);
        map.put(HORN_CORAL_BLOCK.defaultBlockState(), ChocoboColor.BLUE);
        map.put(RED_SAND.defaultBlockState(), ChocoboColor.RED);
        map.put(RED_SANDSTONE.defaultBlockState(), ChocoboColor.RED);
        map.put(CUT_RED_SANDSTONE.defaultBlockState(), ChocoboColor.RED);
        map.put(CHISELED_RED_SANDSTONE.defaultBlockState(), ChocoboColor.RED);
        map.put(SMOOTH_RED_SANDSTONE.defaultBlockState(), ChocoboColor.RED);
        map.put(TERRACOTTA.defaultBlockState(), ChocoboColor.RED);
        map.put(MOSS_BLOCK.defaultBlockState(), ChocoboColor.GREEN);
        map.put(CLAY.defaultBlockState(), ChocoboColor.GREEN);
        map.put(JUNGLE_WOOD.defaultBlockState(), ChocoboColor.GREEN);
        map.put(SAND.defaultBlockState(), ChocoboColor.BLACK);
        map.put(SANDSTONE.defaultBlockState(), ChocoboColor.BLACK);
        map.put(SMOOTH_SANDSTONE.defaultBlockState(), ChocoboColor.BLACK);
        map.put(CUT_SANDSTONE.defaultBlockState(), ChocoboColor.BLACK);
        map.put(CHISELED_SANDSTONE.defaultBlockState(), ChocoboColor.BLACK);
        map.put(GOLD_BLOCK.defaultBlockState(), ChocoboColor.GOLD);
        map.put(GRASS_BLOCK.defaultBlockState(), ChocoboColor.YELLOW);
    });
    public static boolean alterBlocks(BlockState blockState) {
        ArrayList<BlockState> approvedList = new ArrayList<>();
        approvedList.add(HAY_BLOCK.defaultBlockState());
        approvedList.add(OAK_WOOD.defaultBlockState());
        approvedList.add(SPRUCE_WOOD.defaultBlockState());
        approvedList.add(BIRCH_WOOD.defaultBlockState());
        approvedList.add(ACACIA_WOOD.defaultBlockState());
        approvedList.add(DARK_OAK_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_OAK_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_SPRUCE_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_BIRCH_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_JUNGLE_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_DARK_OAK_WOOD.defaultBlockState());
        approvedList.add(STRIPPED_ACACIA_WOOD.defaultBlockState());
        approvedList.add(OAK_LEAVES.defaultBlockState());
        approvedList.add(SPRUCE_LEAVES.defaultBlockState());
        approvedList.add(BIRCH_LEAVES.defaultBlockState());
        approvedList.add(JUNGLE_LEAVES.defaultBlockState());
        approvedList.add(ACACIA_LEAVES.defaultBlockState());
        approvedList.add(DARK_OAK_LEAVES.defaultBlockState());
        approvedList.add(FLOWERING_AZALEA_LEAVES.defaultBlockState());
        approvedList.add(AZALEA_LEAVES.defaultBlockState());
        approvedList.add(CLAY.defaultBlockState());
        approvedList.add(MOSS_BLOCK.defaultBlockState());
        approvedList.add(END_STONE.defaultBlockState());
        approvedList.add(WARPED_NYLIUM.defaultBlockState());
        approvedList.add(CRIMSON_NYLIUM.defaultBlockState());
        approvedList.add(SAND.defaultBlockState());
        approvedList.add(RED_SAND.defaultBlockState());
        return approvedList.contains(blockState);
    }
    public static boolean randomAlter(BlockState blockState) {
        ArrayList<BlockState> listOfRandomAlters = new ArrayList<>();
        listOfRandomAlters.add(DIRT.defaultBlockState());
        return listOfRandomAlters.contains(blockState);
    }
}
