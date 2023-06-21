package com.dephoegon.delchoco.aid.arraylists;

import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;

public class creativeTabArrayLists {
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoBlocks() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(GYSAHL_GREEN);
        out.add(STRAW_NEST);
        out.add(CHOCOBO_EGG);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getAllChocoboItems() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.addAll(getChocoboFood());
        out.addAll(getChocoboWeapons());
        out.addAll(getChocoboArmors());
        out.addAll(getChocoboSaddles());
        out.addAll(getChocoboDisguiseItems());
        out.addAll(getChocoboMiscItems());
        out.addAll(getChocoboSpawnEggs());
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboWeapons() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(STONE_CHOCO_WEAPON);
        out.add(IRON_CHOCO_WEAPON);
        out.add(DIAMOND_CHOCO_WEAPON);
        out.add(NETHERITE_CHOCO_WEAPON);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboArmors() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(CHAIN_CHOCO_CHEST);
        out.add(IRON_CHOCO_CHEST);
        out.add(DIAMOND_CHOCO_CHEST);
        out.add(NETHERITE_CHOCO_CHEST);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboSaddles() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(CHOCOBO_SADDLE);
        out.add(CHOCOBO_SADDLE_BAGS);
        out.add(CHOCOBO_SADDLE_PACK);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboSpawnEggs() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(YELLOW_CHOCOBO_SPAWN_EGG);
        out.add(BLUE_CHOCOBO_SPAWN_EGG);
        out.add(GREEN_CHOCOBO_SPAWN_EGG);
        out.add(WHITE_CHOCOBO_SPAWN_EGG);
        out.add(BLACK_CHOCOBO_SPAWN_EGG);
        out.add(GOLD_CHOCOBO_SPAWN_EGG);
        out.add(PINK_CHOCOBO_SPAWN_EGG);
        out.add(RED_CHOCOBO_SPAWN_EGG);
        out.add(PURPLE_CHOCOBO_SPAWN_EGG);
        out.add(FLAME_CHOCOBO_SPAWN_EGG);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboMiscItems() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(CHOCOBO_WHISTLE);
        out.add(CHOCOBO_FEATHER);
        out.add(LOVELY_GYSAHL_GREEN);
        out.add(GYSAHL_CAKE);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboDisguiseItems() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(LEATHER_CHOCO_DISGUISE_HELMET);
        out.add(LEATHER_CHOCO_DISGUISE_CHEST);
        out.add(LEATHER_CHOCO_DISGUISE_LEGS);
        out.add(LEATHER_CHOCO_DISGUISE_FEET);
        out.add(IRON_CHOCO_DISGUISE_HELMET);
        out.add(IRON_CHOCO_DISGUISE_CHEST);
        out.add(IRON_CHOCO_DISGUISE_LEGS);
        out.add(IRON_CHOCO_DISGUISE_FEET);
        out.add(DIAMOND_CHOCO_DISGUISE_HELMET);
        out.add(DIAMOND_CHOCO_DISGUISE_CHEST);
        out.add(DIAMOND_CHOCO_DISGUISE_LEGS);
        out.add(DIAMOND_CHOCO_DISGUISE_FEET);
        out.add(NETHERITE_CHOCO_DISGUISE_HELMET);
        out.add(NETHERITE_CHOCO_DISGUISE_CHEST);
        out.add(NETHERITE_CHOCO_DISGUISE_LEGS);
        out.add(NETHERITE_CHOCO_DISGUISE_FEET);
        return out;
    }
    public static @NotNull ArrayList<RegistryObject<? extends ItemLike>> getChocoboFood() {
        ArrayList<RegistryObject<? extends ItemLike>> out = new ArrayList<>();
        out.add(CHOCOBO_DRUMSTICK_RAW);
        out.add(CHOCOBO_DRUMSTICK_COOKED);
        out.add(PICKLED_GYSAHL_RAW);
        out.add(PICKLED_GYSAHL_COOKED);
        out.add(GYSAHL_GREEN_ITEM);
        out.add(GYSAHL_GREEN_SEEDS);
        return out;
    }
}
