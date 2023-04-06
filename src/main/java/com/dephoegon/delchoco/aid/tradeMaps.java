package com.dephoegon.delchoco.aid;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

import static com.dephoegon.delchoco.common.init.ModRegistry.GYSAHL_GREEN_ITEM;
import static com.dephoegon.delchoco.common.init.ModRegistry.LOVELY_GYSAHL_GREEN;

public class tradeMaps {
    private static final ItemStack gysahl_green = new ItemStack(GYSAHL_GREEN_ITEM.get(), 8);
    private static final ItemStack lovely_gysahl = new ItemStack(LOVELY_GYSAHL_GREEN.get(), 2);
    public static Map<ItemStack, ItemStack> MOD_FARMER_TRADES = Util.make(Maps.newHashMap(), (map) -> {
        map.put(gysahl_green, new ItemStack(Items.EMERALD, 1));
        map.put(new ItemStack(Items.EMERALD, 2), gysahl_green);
        map.put(lovely_gysahl, new ItemStack(Items.EMERALD, 1));
        map.put(new ItemStack(Items.EMERALD, 2), lovely_gysahl);
    });
    public static Map<ItemStack, Integer> MOD_TRADE_LEVEL = Util.make(Maps.newHashMap(), (map) -> {
        map.put(gysahl_green, 1);
        map.put(lovely_gysahl, 2);
    });
}
