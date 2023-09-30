package com.dephoegon.delchoco.common.effects;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.commands.chocoboTeams;
import com.dephoegon.delchoco.common.entities.Chocobo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.dephoegon.delchoco.aid.tradeMaps.MOD_FARMER_TRADES;
import static com.dephoegon.delchoco.aid.tradeMaps.MOD_TRADE_LEVEL;
import static com.dephoegon.delchoco.common.entities.Chocobo.blueChocobo;
import static net.minecraft.tags.BiomeTags.IS_NETHER;
import static net.minecraft.tags.BiomeTags.IS_OCEAN;
import static net.minecraft.world.level.biome.Biomes.*;
import static net.minecraftforge.common.Tags.Biomes.IS_END;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    public static void addCustomTrades(@NotNull VillagerTradesEvent event) {
        // No SubscribeEvent - Controlled loading order because items.
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            MOD_FARMER_TRADES.forEach((give, get) -> trades.get((int)MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)).add((trader, rand) -> new MerchantOffer(give, get, 10, MOD_TRADE_LEVEL.get(give.getItem() == Items.EMERALD.asItem() ? get : give)*8, 0.02F)));
        }
    }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onRegisterCommandsEvent(@NotNull RegisterCommandsEvent event) { chocoboTeams.commands(event.getDispatcher()); }
    // No SubscribeEvent - Controlled loading order because items.
    public static void onWorldLoad(WorldEvent.Load ignoredEvent) { composable.addToList(); }
    public static void onCheckSpawn(LivingSpawnEvent.@NotNull CheckSpawn event) {
        if (event.getEntity() instanceof Chocobo) {
            LevelAccessor level1 = event.getWorld();
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