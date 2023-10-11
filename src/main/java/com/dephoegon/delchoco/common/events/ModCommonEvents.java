package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.entities.Chocobo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.dephoegon.delchoco.aid.tradeMaps.MOD_FARMER_TRADES;
import static com.dephoegon.delchoco.aid.tradeMaps.MOD_TRADE_LEVEL;
import static com.dephoegon.delchoco.common.entities.Chocobo.blueChocobo;
import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;
import static com.dephoegon.delchoco.common.init.ModEntities.*;
import static net.minecraft.tags.BiomeTags.*;
import static net.minecraft.world.level.biome.Biomes.*;

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
    public static void onCheckSpawn(LivingSpawnEvent.@NotNull CheckSpawn event) {
        if (event.getEntity() instanceof Chocobo) {
            LevelAccessor level1 = event.getLevel();
            assert level1 != null;
            BlockPos spot = new BlockPos(event.getX(), event.getY(), event.getZ());
            List<Chocobo> bob = level1.getEntitiesOfClass(Chocobo.class, new AABB(spot).inflate(48, 32, 48));
            int sizeCtrl = 15;
            final Holder<Biome> BiomesKey = level1.getBiome(spot.below());
            //noinspection OptionalGetWithoutIsPresent
            ResourceKey<Biome> shift = BiomesKey.unwrapKey().get();
            if (BiomesKey.containsTag(IS_END)) {
                if (level1.getBlockState(spot).isAir() && !level1.getBlockState(spot.below()).isAir()) { event.setResult(Event.Result.ALLOW); } else { event.setResult(Event.Result.DENY); }
            } else if (BiomesKey.containsTag(IS_NETHER)) {
                if (level1.getBlockState(spot).isAir() && !level1.getBlockState(spot.below()).isAir() && level1.getBlockState(spot.below()).getFluidState().isEmpty()) { event.setResult(Event.Result.ALLOW); } else { event.setResult(Event.Result.DENY); }
            } else {
                if (BiomesKey.containsTag(IS_OCEAN)) {
                    if (blueChocobo().contains(shift)) {
                        if (bob.size() > 5) { event.setResult(Event.Result.DENY); }
                        event.setResult(Event.Result.DEFAULT);
                    } else { event.setResult(Event.Result.DENY); }
                } else { event.setResult(Event.Result.DEFAULT); }
            }
            if (IS_SPARSE().contains(shift)) { sizeCtrl = 5; }
            if (bob.size() > sizeCtrl) { event.setResult(Event.Result.DENY); } else { event.setResult(Event.Result.DEFAULT); }
        }
    }
    private static @NotNull ArrayList<ResourceKey<Biome>> IS_SPARSE() {
        ArrayList<ResourceKey<Biome>> out = new ArrayList<>();
        out.add(DESERT);
        out.add(RIVER);
        out.add(BADLANDS);
        out.add(ERODED_BADLANDS);
        out.add(STONY_PEAKS);
        return out;
    }
}