package com.dephoegon.delchoco.common.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class chocoboEquipmentSlot extends SlotItemHandler {
    public chocoboEquipmentSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) { return false; }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
