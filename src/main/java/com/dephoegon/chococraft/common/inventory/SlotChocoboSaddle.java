package com.dephoegon.chococraft.common.inventory;

import com.dephoegon.chococraft.common.items.ChocoboSaddleItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class SlotChocoboSaddle extends SlotItemHandler {
    public SlotChocoboSaddle(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) { return false; }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
