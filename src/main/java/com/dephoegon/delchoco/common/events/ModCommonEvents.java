package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.entities.Chocobo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.dephoegon.delchoco.aid.tradeMaps.MOD_FARMER_TRADES;
import static com.dephoegon.delchoco.aid.tradeMaps.MOD_TRADE_LEVEL;
import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;
import static com.dephoegon.delchoco.common.init.ModEntities.CHOCOBO;

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
            MOD_FARMER_TRADES.forEach((give, get) -> trades.get((int) MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)).add((trader, rand) -> new MerchantOffer(give, get, 10, 3, 0.02F)));
        }
    }
    // No SubscribeEvent - Controlled loading order because items.
    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) { event.put(CHOCOBO.get(), createAttributes().build()); }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onServerStartAddCompostItems(ServerStartedEvent ignoredEvent) { composable.addToList(); }
}