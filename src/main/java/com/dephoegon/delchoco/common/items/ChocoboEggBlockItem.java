package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.blocks.ChocoboEggBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ChocoboEggBlockItem extends BlockItem {
    public ChocoboEggBlockItem(Block block, Item.Properties builder) { super(block, builder); }
    public boolean isBarVisible(@NotNull ItemStack stack) {
        if (!ChocoboEggBlock.isChocoboEgg(stack)) { return super.isBarVisible(stack); }
        if (!stack.hasTag()) { return false; }
        CompoundTag nbtHatchingState = stack.getTagElement(ChocoboEggBlock.NBTKEY_HATCHING_STATE);
        return nbtHatchingState != null;
    }
    public int getBarWidth(@NotNull ItemStack stack) {
        if (!ChocoboEggBlock.isChocoboEgg(stack)) { return super.getBarWidth(stack); }
        if (!stack.hasTag()) { return 0; }

        CompoundTag nbtHatchingState = stack.getTagElement(ChocoboEggBlock.NBTKEY_HATCHING_STATE);
        if (nbtHatchingState != null) {
            int time = nbtHatchingState.getInt(ChocoboEggBlock.NBTKEY_HATCHING_STATE_TIME);
            return Math.round(time * 13.0F / (float)ChocoConfig.COMMON.eggHatchTimeTicks.get());
        }
        return 1;
    }
    public int getBarColor(@NotNull ItemStack stack) {
        if (!ChocoboEggBlock.isChocoboEgg(stack)) { return super.getBarColor(stack); }
        return 0x0000FF00;
    }
}
