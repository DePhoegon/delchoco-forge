package com.dephoegon.delchoco.aid;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.dephoegon.delbase.item.shiftingDyes.*;
import static net.minecraft.world.item.Items.*;

public abstract class dyeList {
    private static @NotNull ArrayList<Item> setListOfDyes() {
        ArrayList<Item> dyes = new ArrayList<>();
        dyes.add(RED_DYE.asItem());
        dyes.add(BLACK_DYE.asItem());
        dyes.add(BROWN_DYE.asItem());
        dyes.add(GREEN_DYE.asItem());
        dyes.add(BLUE_DYE.asItem());
        dyes.add(PURPLE_DYE.asItem());
        dyes.add(CYAN_DYE.asItem());
        dyes.add(LIGHT_GRAY_DYE.asItem());
        dyes.add(GRAY_DYE.asItem());
        dyes.add(PINK_DYE.asItem());
        dyes.add(LIME_DYE.asItem());
        dyes.add(YELLOW_DYE.asItem());
        dyes.add(LIGHT_BLUE_DYE.asItem());
        dyes.add(MAGENTA_DYE.asItem());
        dyes.add(ORANGE_DYE.asItem());
        dyes.add(WHITE_DYE.asItem());
        return dyes;
    }
    private static @NotNull ArrayList<Item> setListOfModDyes() {
        ArrayList<Item> dyes = new ArrayList<>();
        dyes.add(CLEANSE_SHIFT_DYE.get().asItem());
        dyes.add(RED_SHIFT_DYE.get().asItem());
        dyes.add(BLOOD_SHIFT_DYE.get().asItem());
        dyes.add(BLACK_SHIFT_DYE.get().asItem());
        dyes.add(BROWN_SHIFT_DYE.get().asItem());
        dyes.add(GREEN_SHIFT_DYE.get().asItem());
        dyes.add(BLUE_SHIFT_DYE.get().asItem());
        dyes.add(PURPLE_SHIFT_DYE.get().asItem());
        dyes.add(CYAN_SHIFT_DYE.get().asItem());
        dyes.add(LIGHT_GRAY_SHIFT_DYE.get().asItem());
        dyes.add(GRAY_SHIFT_DYE.get().asItem());
        dyes.add(PINK_SHIFT_DYE.get().asItem());
        dyes.add(LIME_SHIFT_DYE.get().asItem());
        dyes.add(YELLOW_SHIFT_DYE.get().asItem());
        dyes.add(LIGHT_BLUE_SHIFT_DYE.get().asItem());
        dyes.add(MAGENTA_SHIFT_DYE.get().asItem());
        dyes.add(ORANGE_SHIFT_DYE.get().asItem());
        dyes.add(WHITE_SHIFT_DYE.get().asItem());
        return dyes;
    }
    public static @NotNull ArrayList<Item> getDyeList() {
        ArrayList<Item> out = new ArrayList<>();
        out.addAll(setListOfDyes());
        out.addAll(setListOfModDyes());
        return out;
    }
}
