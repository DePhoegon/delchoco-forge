package com.dephoegon.delchoco.common.inventory;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.inventory.chocoboEquipmentSlot.*;

public class SaddleBagContainer extends AbstractContainerMenu {
    private final Chocobo chocobo;

    public SaddleBagContainer(int id, Inventory player, Chocobo chocobo) {
        super(null, id);
        this.chocobo = chocobo;
        this.refreshSlots(chocobo, player);
    }
    public Chocobo getChocobo() { return chocobo; }
    public void refreshSlots(@NotNull Chocobo chocobo, Inventory player) {
        this.slots.clear();
        bindPlayerInventory(player);
        ItemStack saddleStack = chocobo.getSaddle();
        int slotOneX = -16;
        int slotOneY = 18-20;
        this.addSlot(new chocoboEquipmentSlot(chocobo.saddleItemStackHandler, 0, slotOneX, slotOneY, saddleType));
        this.addSlot(new chocoboEquipmentSlot(chocobo.chocoboArmorHandler, 0, 2*18+slotOneX, slotOneY, armorType));
        this.addSlot(new chocoboEquipmentSlot(chocobo.chocoboWeaponHandler, 0, 4*18+slotOneX, slotOneY, weaponType));
        if(!saddleStack.isEmpty() && saddleStack.getItem() instanceof ChocoboSaddleItem saddleItem) {
            int saddleSize = saddleItem.getInventorySize();
            switch (saddleSize) {
                case 15 -> bindInventorySmall(saddleStack, chocobo.tierOneItemStackHandler);
                case 45 -> bindInventoryBig(saddleStack, chocobo.tierTwoItemStackHandler);
            }
        }

    }
    private void bindInventorySmall(@NotNull ItemStack saddle, IItemHandler inventory) {
        if (!(saddle.isEmpty())) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 5; col++) {
                    this.addSlot(new SlotItemHandler(inventory, row * 5 + col, 44 + col * 18, 36 + row * 18));
                    if (row * 5 + col < 5) { this.chocobo.tierOneItemStackHandler.setStackInSlot(row * 5 + col, this.chocobo.chocoboInventory.getStackInSlot(row * 5 + col+11)); }
                    if (row * 5 + col > 4 && row * 5 + col < 10) { this.chocobo.tierOneItemStackHandler.setStackInSlot(row * 5 + col, this.chocobo.chocoboInventory.getStackInSlot(row * 5 + col+15)); }
                    if (row * 5 + col > 9) { this.chocobo.tierOneItemStackHandler.setStackInSlot(row * 5 + col, this.chocobo.chocoboInventory.getStackInSlot(row * 5 + col+19)); }
                }
            }
        }
    }
    private void bindInventoryBig(@NotNull ItemStack saddle, IItemHandler inventory) {
        if (!(saddle.isEmpty())) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 9; col++) {
                    this.addSlot(new SlotItemHandler(inventory, row * 9 + col, 8 + col * 18, 18 + row * 18));
                    this.chocobo.tierTwoItemStackHandler.setStackInSlot(row * 9 + col, this.chocobo.chocoboInventory.getStackInSlot(row * 9 + col));
                }
            }
        }
    }
    private void bindPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) { for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 122 + row * 18));
        } }
        for (int i = 0; i < 9; ++i) { this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 180)); }
    }
    public boolean stillValid(@NotNull Player playerIn) { return this.chocobo.isAlive() && this.chocobo.distanceTo(playerIn) < 8.0F; }
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        ItemStack saddleStack = chocobo.getSaddle();
        boolean notEmpty;
        int slotSize;
        if (!saddleStack.isEmpty() && saddleStack.getItem() instanceof ChocoboSaddleItem saddleItem) { slotSize = saddleItem.getInventorySize(); }
        else { slotSize = 0; }
        notEmpty = !(slot instanceof chocoboEquipmentSlot);
        if (notEmpty){
            if (slot.hasItem()) {
                ItemStack itemstack1 = slot.getItem();
                itemstack = itemstack1.copy();

                if (index < slotSize) {
                    if (!this.moveItemStackTo(itemstack1, slotSize, this.slots.size(), true)) { return ItemStack.EMPTY; }
                } else if (!this.moveItemStackTo(itemstack1, 0, slotSize, false)) { return ItemStack.EMPTY; }
                if (itemstack1.isEmpty()) { slot.set(ItemStack.EMPTY);} else { slot.setChanged(); }
            }
        }
        if (notEmpty) { return itemstack; } else { return ItemStack.EMPTY; }
    }
}