package com.dephoegon.delchoco.common.inventory;

import com.dephoegon.delchoco.common.items.ChocoboArmorItems;
import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import com.dephoegon.delchoco.common.items.ChocoboWeaponItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class chocoboEquipmentSlot extends SlotItemHandler {
    private boolean saddle = false;
    public static final int saddleType = 1;
    private boolean armor = false;
    public static final int armorType = 2;
    private boolean weapon = false;
    public static final int weaponType = 3;
    public chocoboEquipmentSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, int type) {
        super(itemHandler, index, xPosition, yPosition);
        switch (type) {
            case saddleType -> this.saddle = true;
            case armorType -> this.armor = true;
            case weaponType -> this.weapon = true;
            default -> this.saddle = false;
        }
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) { return false; } else {
            if (stack.getItem() instanceof ChocoboArmorItems && this.armor) { return true; }
            if (stack.getItem() instanceof ChocoboSaddleItem && this.saddle) { return true; }
            return stack.getItem() instanceof ChocoboWeaponItems && this.weapon;
        }
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
