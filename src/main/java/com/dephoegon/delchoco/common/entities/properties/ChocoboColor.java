package com.dephoegon.delchoco.common.entities.properties;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.Random;

public enum ChocoboColor {
    YELLOW(null,1),
    GREEN(null,2),
    BLUE(null,3),
    WHITE(null,4),
    BLACK(null,5),
    GOLD(null,6),
    PINK(null,7),
    RED(null,8),
    PURPLE(null,9),
    FLAME(null,10);

    private final static Random rand = new Random();
    private final TagKey<Item> colorTag;
    private final TranslatableComponent eggText;
    private final int customModelData;

    ChocoboColor(TagKey<Item> colorIngredient, int CustomModelData) {
        this.colorTag = colorIngredient;
        this.customModelData = CustomModelData;
        this.eggText = new TranslatableComponent("item." + DelChoco.DELCHOCO_ID + ".chocobo_egg.tooltip." + this.name().toLowerCase());
    }
    public int getCustomModelData() { return this.customModelData; }
    public static ChocoboColor getRandomColor() { return values()[rand.nextInt(values().length)]; }
    public static Optional<ChocoboColor> getColorForItemstack(ItemStack stack) {
        for (ChocoboColor color : values()) { if(color.colorTag != null && stack.is(color.colorTag)) { return Optional.of(color); } }
        return Optional.empty();
    }
    public TranslatableComponent getEggText() { return eggText; }
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