package com.dephoegon.delchoco.aid;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.init.ModRegistry.SPIKE_FRUIT;

public class tradeMaps {
    private static final ItemStack gysahl_green = new ItemStack(GYSAHL_GREEN_ITEM.get(), 8);
    private static final ItemStack lovely_gysahl = new ItemStack(LOVELY_GYSAHL_GREEN.get(), 2);
    private static final ItemStack dead_pepper = new ItemStack(DEAD_PEPPER.get(), 1);
    private static final ItemStack golden_green = new ItemStack(GOLDEN_GYSAHL_GREEN.get(), 1);
    private static final ItemStack pink_green = new ItemStack(PINK_GYSAHL_GREEN.get(), 1);
    private static final ItemStack spike_fruit = new ItemStack(SPIKE_FRUIT.get(), 1);
    public static Map<ItemStack, ItemStack> MOD_FARMER_TRADES = Util.make(Maps.newHashMap(), (map) -> {
        map.put(gysahl_green, new ItemStack(Items.EMERALD, 1));
        map.put(new ItemStack(Items.EMERALD, 2), gysahl_green);
        map.put(lovely_gysahl, new ItemStack(Items.EMERALD, 1));
        map.put(new ItemStack(Items.EMERALD, 2), lovely_gysahl);
        map.put(pink_green, new ItemStack(Items.EMERALD, 4));
        map.put(new ItemStack(Items.EMERALD, 8), pink_green);
        map.put(dead_pepper, new ItemStack(Items.EMERALD, 6));
        map.put(new ItemStack(Items.EMERALD, 12), dead_pepper);
        map.put(golden_green, new ItemStack(Items.EMERALD, 18));
        map.put(new ItemStack(Items.EMERALD, 32), golden_green);
        map.put(spike_fruit, new ItemStack(Items.EMERALD, 6));
        map.put(new ItemStack(Items.EMERALD, 12), spike_fruit);
    });
    public static Map<ItemStack, Integer> MOD_TRADE_LEVEL = Util.make(Maps.newHashMap(), (map) -> {
        map.put(gysahl_green, 1);
        map.put(lovely_gysahl, 2);
        map.put(pink_green, 3);
        map.put(dead_pepper, 4);
        map.put(spike_fruit, 4);
        map.put(golden_green, 5);
    });
}
