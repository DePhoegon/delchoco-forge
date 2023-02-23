package com.dephoegon.delchoco.aid;

import net.minecraft.world.item.Items;
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
        add(f2, Items.ROTTEN_FLESH);
        add(f2, Items.SPIDER_EYE);
        add(f3, Items.COD);
        add(f3, Items.SALMON);
        add(f3, Items.TROPICAL_FISH);
        add(f3, Items.PUFFERFISH);
        add(f3, Items.BEEF);
        add(f3, Items.CHICKEN);
        add(f3, Items.RABBIT);
        add(f3, Items.MUTTON);
        add(f3, Items.COOKED_BEEF);
        add(f3, Items.COOKED_COD);
        add(f3, Items.COOKED_CHICKEN);
        add(f3, Items.COOKED_RABBIT);
        add(f3, Items.COOKED_MUTTON);
        add(f4, LOVELY_GYSAHL_GREEN.get());
        add(f4, STRAW_NEST_ITEM.get());
        add(f4, GYSAHL_CAKE.get());
        add(f4, CHOCO_DISGUISE_LEGGINGS.get());
        add(f4, CHOCO_DISGUISE_BOOTS.get());
        add(f4, CHOCO_DISGUISE_CHESTPLATE.get());
        add(f4, CHOCO_DISGUISE_HELMET.get());
    }
    private static void add(float pChance, @NotNull ItemLike pItem) { if (!COMPOSTABLES.containsKey(pItem.asItem())) { COMPOSTABLES.put(pItem.asItem(), pChance); } }
    public static void addToList(){ compost(); }
}
