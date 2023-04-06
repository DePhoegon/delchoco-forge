package com.dephoegon.delchoco.common.effects;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.commands.chocoboTeams;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dephoegon.delchoco.aid.tradeMaps.MOD_FARMER_TRADES;
import static com.dephoegon.delchoco.aid.tradeMaps.MOD_TRADE_LEVEL;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    public static void addCustomTrades(@NotNull VillagerTradesEvent event) {
        // No SubscribeEvent - Controlled loading order because items.
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            MOD_FARMER_TRADES.forEach((give, get) -> trades.get((int)MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)).add((trader, rand) -> new MerchantOffer(give, get, 10, 8, 0.02F)));
        }
    }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onRegisterCommandsEvent(@NotNull RegisterCommandsEvent event) { chocoboTeams.commands(event.getDispatcher()); }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onWorldLoad(WorldEvent.Load event) { composable.addToList(); }
}