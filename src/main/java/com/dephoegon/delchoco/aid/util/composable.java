package com.dephoegon.delchoco.aid.util;

import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static net.minecraft.world.level.block.ComposterBlock.COMPOSTABLES;

public class composable {
    private static void compost() {
        COMPOSTABLES.defaultReturnValue();
        float f = 0.3F;
        float f1 = 0.5F;
        float f2 = 0.65F;
        float f3 = 0.85F;
        float f4 = 1.0F;
        add(f, GYSAHL_GREEN_SEEDS.get());
        add(f1, CHOCOBO_FEATHER.get());
        add(f2, GYSAHL_GREEN_ITEM.get());
        add(f3, PICKLED_GYSAHL_COOKED.get());
        add(f3, PICKLED_GYSAHL_RAW.get());
        add(f3, CHOCOBO_DRUMSTICK_COOKED.get());
        add(f3, CHOCOBO_DRUMSTICK_RAW.get());
        add(f4, LOVELY_GYSAHL_GREEN.get());
        add(f4, PINK_GYSAHL_GREEN.get());
        add(f4, GOLDEN_GYSAHL_GREEN.get());
        add(f4, SPIKE_FRUIT.get());
        add(f4, DEAD_PEPPER.get());
        add(f4, GYSAHL_CAKE.get());
        add(f4, LEATHER_CHOCO_DISGUISE_LEGS.get());
        add(f4, LEATHER_CHOCO_DISGUISE_FEET.get());
        add(f4, LEATHER_CHOCO_DISGUISE_CHEST.get());
        add(f4, LEATHER_CHOCO_DISGUISE_HELMET.get());
    }
    private static void add(float pChance, @NotNull ItemLike pItem) { if (!COMPOSTABLES.containsKey(pItem.asItem())) { COMPOSTABLES.put(pItem.asItem(), pChance); } }
    public static void addToList(){ compost(); }
}