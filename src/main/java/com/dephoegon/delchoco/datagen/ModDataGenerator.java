package com.dephoegon.delchoco.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.blocks.GysahlGreenBlock;
import com.dephoegon.delchoco.common.init.ModEntities;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.init.ModEntities.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
	@SubscribeEvent
	public static void gatherData(@NotNull GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) { generator.addProvider(new ModLoot(generator)); }
		if (event.includeClient()) { /* generator.addProvider(new FarmingItemModels(generator, helper)); */ }
	}

	private static class ModLoot extends LootTableProvider {
		public ModLoot(DataGenerator gen) { super(gen); }
		@Override
		protected @NotNull @Unmodifiable List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() { return ImmutableList.of(Pair.of(ModBlockTables::new, LootContextParamSets.BLOCK), Pair.of(ModEntityTables::new, LootContextParamSets.ENTITY)); }

		private static class ModBlockTables extends BlockLoot {
			@Override
			protected void addTables() {
				LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(GYSAHL_GREEN.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(GysahlGreenBlock.AGE, GysahlGreenBlock.MAX_AGE));
				this.add(GYSAHL_GREEN.get(), applyExplosionDecay(GYSAHL_GREEN.get(), LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(GYSAHL_GREEN.get()))).withPool(LootPool.lootPool().when(condition)
						.add(LootItem.lootTableItem(GYSAHL_GREEN_ITEM.get())
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))).withPool(LootPool.lootPool().when(condition)
						.add(LootItem.lootTableItem(LOVELY_GYSAHL_GREEN.get()).when(LootItemRandomChanceCondition.randomChance(0.02F))))));
			}

			@Override
			protected @NotNull Iterable<Block> getKnownBlocks() { return ModRegistry.BLOCKS.getEntries().stream().map(net.minecraftforge.registries.RegistryObject::get)::iterator; }
		}

		private static class ModEntityTables extends EntityLoot {
			private @NotNull List<EntityType<?>> getChocobos() {
				List<EntityType<?>> Chocobos = new ArrayList<>();
				Chocobos.add(CHOCOBO.get());
				Chocobos.add(YELLOW_SPAWNER_CHOCOBO.get());
				Chocobos.add(GREEN_SPAWNER_CHOCOBO.get());
				Chocobos.add(BLUE_SPAWNER_CHOCOBO.get());
				Chocobos.add(WHITE_SPAWNER_CHOCOBO.get());
				Chocobos.add(BLACK_SPAWNER_CHOCOBO.get());
				Chocobos.add(GOLD_SPAWNER_CHOCOBO.get());
				Chocobos.add(PINK_SPAWNER_CHOCOBO.get());
				Chocobos.add(RED_SPAWNER_CHOCOBO.get());
				Chocobos.add(PURPLE_SPAWNER_CHOCOBO.get());
				Chocobos.add(FLAME_SPAWNER_CHOCOBO.get());
				return Chocobos;
			}
			@Override
			protected void addTables() {
				getChocobos().forEach(chocobo -> this.add(chocobo, LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CHOCOBO_FEATHER.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.LEATHER).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(CHOCOBO_DRUMSTICK_RAW.get()).apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, ENTITY_ON_FIRE))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))));
			}

			@Override
			protected @NotNull Iterable<EntityType<?>> getKnownEntities() {
				Stream<EntityType<?>> entityTypeStream = ModEntities.ENTITIES.getEntries().stream().map(net.minecraftforge.registries.RegistryObject::get);
				return entityTypeStream::iterator;
			}
		}

		@Override
		protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationtracker) { map.forEach((name, table) -> LootTables.validate(validationtracker, name, table)); }
	}

	private static class FarmingItemModels extends ItemModelProvider {
		public FarmingItemModels(DataGenerator gen, ExistingFileHelper helper) { super(gen, DelChoco.DELCHOCO_ID, helper); }
		protected void registerModels() {
			ModRegistry.ITEMS.getEntries().stream()
				.map(RegistryObject::get)
				.forEach(item -> {
					String path = Objects.requireNonNull(item.getRegistryName()).getPath();
					 this.singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
				});
		}

		@Contract(pure = true)
		public @NotNull String getName() { return "Item Models"; }
	}
}