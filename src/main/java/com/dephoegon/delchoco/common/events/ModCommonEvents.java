package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.util.composable;
import com.dephoegon.delchoco.common.entities.Chocobo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dephoegon.delchoco.aid.arraylists.creativeTabArrayLists.*;
import static com.dephoegon.delchoco.aid.util.creativeTabAid.CHOCO_TAB;
import static com.dephoegon.delchoco.aid.util.tradeMaps.MOD_FARMER_TRADES;
import static com.dephoegon.delchoco.aid.util.tradeMaps.MOD_TRADE_LEVEL;
import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;
import static com.dephoegon.delchoco.common.init.ModEntities.*;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    @SubscribeEvent
    public static void entitySpawnRestriction(@NotNull SpawnPlacementRegisterEvent event) {
        event.register(CHOCOBO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Chocobo::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
    public static void addCustomTrades(@NotNull VillagerTradesEvent event) {
        // No Subscribe event to control loading order.
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            MOD_FARMER_TRADES.forEach((give, get) -> trades.get((int) MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)).add((trader, rand) -> new MerchantOffer(give, get, 10, MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)*8, 0.02F)));
        }
    }
    public static void addCreative(@NotNull BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CHOCO_TAB.getKey()) { getAllChocoboItems().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.COMBAT) { getChocoboArmors().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.COMBAT) { getChocoboWeapons().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.COMBAT) { getChocoboDisguiseItems().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) { getChocoboFood().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) { getChocoboMiscItems().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) { getChocoboSpawnEggs().forEach(event::accept); }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) { getChocoboSaddles().forEach(event::accept); }
    }
    // No SubscribeEvent - Controlled loading order because items.
    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) {
        event.put(CHOCOBO.get(), createAttributes().build());
        event.put(YELLOW_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(GREEN_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(BLUE_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(WHITE_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(BLACK_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(GOLD_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(PINK_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(RED_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(PURPLE_SPAWNER_CHOCOBO.get(), createAttributes().build());
        event.put(FLAME_SPAWNER_CHOCOBO.get(), createAttributes().build());
    }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onServerStartAddCompostItems(ServerStartedEvent ignoredEvent) { composable.addToList(); }
}