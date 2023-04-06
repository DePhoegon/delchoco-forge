package com.dephoegon.delchoco.common.entities.properties;

import com.dephoegon.delchoco.DelChoco;
import net.minecraft.network.chat.Component;

import java.util.Random;

public enum ChocoboColor {
    YELLOW(1),
    GREEN(2),
    BLUE(3),
    WHITE(4),
    BLACK(5),
    GOLD(6),
    PINK(7),
    RED(8),
    PURPLE(9),
    FLAME(10);

    private final static Random rand = new Random();
    private final Component eggText;
    private final int customModelData;

    ChocoboColor(int CustomModelData) {
        this.customModelData = CustomModelData;
        this.eggText = Component.translatable("item." + DelChoco.DELCHOCO_ID + ".chocobo_egg.tooltip." + this.name().toLowerCase());
    }
    public int getCustomModelData() { return this.customModelData; }
    public static ChocoboColor getRandomColor() { return values()[rand.nextInt(values().length)]; }
    public Component getEggText() { return eggText; }
}