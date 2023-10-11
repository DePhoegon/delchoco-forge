package com.dephoegon.delchoco.common.entities.properties;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

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
    public EntityType<?> getTypeByColor() {
        return switch (this) {
            case RED -> ModEntities.RED_SPAWNER_CHOCOBO.get();
            case BLUE -> ModEntities.BLUE_SPAWNER_CHOCOBO.get();
            case GOLD -> ModEntities.GOLD_SPAWNER_CHOCOBO.get();
            case PINK -> ModEntities.PINK_SPAWNER_CHOCOBO.get();
            case BLACK -> ModEntities.BLACK_SPAWNER_CHOCOBO.get();
            case FLAME -> ModEntities.FLAME_SPAWNER_CHOCOBO.get();
            case GREEN -> ModEntities.GREEN_SPAWNER_CHOCOBO.get();
            case WHITE -> ModEntities.WHITE_SPAWNER_CHOCOBO.get();
            case PURPLE -> ModEntities.PURPLE_SPAWNER_CHOCOBO.get();
            case YELLOW -> ModEntities.YELLOW_SPAWNER_CHOCOBO.get();
        };
    }
}