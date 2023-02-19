package com.dephoegon.delchoco.common.items;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class ChocoboSaddleItem extends Item {
    private final int inventorySize;
    private final boolean renderOutline;

    public ChocoboSaddleItem(@NotNull Properties properties, int inventorySize, boolean renderOutline) {
        super(properties.stacksTo(4));
        this.inventorySize = inventorySize;
        this.renderOutline = renderOutline;
    }
    public boolean hasOutline() {
        return this.renderOutline;
    }
    public int getInventorySize() {
        return inventorySize;
    }
}
