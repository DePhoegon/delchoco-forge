package com.dephoegon.delchoco.common.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class SaddleItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    protected ItemStack itemStack = ItemStack.EMPTY;
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        ItemStack oldStack = this.itemStack;
        this.itemStack = stack;
        // don't update if we change from empty to empty
        if (!(oldStack.isEmpty() && stack.isEmpty())) { this.onStackChanged(); }
    }
    public int getSlots() { return 1; }
    public @NotNull ItemStack getStackInSlot(int slot) { return this.itemStack; }
    public @NotNull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) { return ItemStack.EMPTY; }
        if (this.itemStack.isEmpty()) {
            if (simulate) { return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1); }
            this.itemStack = stack.split(1);
        }
        this.onStackChanged();
        return stack;
    }
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) { return ItemStack.EMPTY; }
        if (simulate) { return ItemHandlerHelper.copyStackWithSize(this.itemStack, amount); }
        ItemStack outStack = this.itemStack.split(amount);
        this.onStackChanged();
        return outStack;
    }
    public int getSlotLimit(int slot) { return 1; }
    public CompoundTag serializeNBT() { return this.itemStack.save(new CompoundTag()); }
    public void deserializeNBT(CompoundTag nbt) {
        this.itemStack = ItemStack.of(nbt);
        this.onStackChanged();
    }
    protected abstract void onStackChanged();
}