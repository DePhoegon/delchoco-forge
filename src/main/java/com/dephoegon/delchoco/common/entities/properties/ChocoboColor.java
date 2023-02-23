package com.dephoegon.delchoco.common.entities.properties;

import com.dephoegon.delchoco.DelChoco;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.Random;

public enum ChocoboColor {
    YELLOW(null),
    GREEN(null),
    BLUE(null),
    WHITE(null),
    BLACK(null),
    GOLD(null),
    PINK(null),
    RED(null),
    PURPLE(null),
    FLAME(null);

    private final static Random rand = new Random();
    private final TagKey<Item> colorTag;
    private final TranslatableComponent eggText;

    ChocoboColor(TagKey<Item> colorIngredient) {
        this.colorTag = colorIngredient;
        this.eggText = new TranslatableComponent("item." + DelChoco.MOD_ID + ".chocobo_egg.tooltip." + this.name().toLowerCase());
    }
    public static ChocoboColor getRandomColor() { return values()[rand.nextInt(values().length)]; }
    public static Optional<ChocoboColor> getColorForItemstack(ItemStack stack) {
        for (ChocoboColor color : values()) { if(color.colorTag != null && stack.is(color.colorTag)) { return Optional.of(color); } }
        return Optional.empty();
    }
    public TranslatableComponent getEggText() { return eggText; }
}
