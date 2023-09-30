package com.dephoegon.delchoco.common.init;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.blocks.GysahlGreenBlock;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.items.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dephoegon.delchoco.DelChoco.CHOCO_TAB;
import static com.dephoegon.delchoco.common.entities.Chocobo.tier_one_chocobo_inv_slot_count;
import static com.dephoegon.delchoco.common.entities.Chocobo.tier_two_chocobo_inv_slot_count;
import static com.dephoegon.delchoco.common.items.ChocoboArmorItems.CHOCOBO_ARMOR_MATERIALS;
import static com.dephoegon.delchoco.common.items.ChocoboWeaponItems.CHOCOBO_WEAPON_TIERS;

@SuppressWarnings("unused")
public class ModRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DelChoco.DELCHOCO_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DelChoco.DELCHOCO_ID);
    public static final RegistryObject<Block> GYSAHL_GREEN = BLOCKS.register("gysahl_green", () -> new GysahlGreenBlock(Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)));
    public static final RegistryObject<Item> GYSAHL_GREEN_SEEDS = ITEMS.register("gysahl_green_seeds", () -> new CustomBlockNamedItem(GYSAHL_GREEN, itemBuilder(false)));
    public static final RegistryObject<Item> GYSAHL_GREEN_ITEM = ITEMS.register("gysahl_green", () -> new Item(itemBuilder(false).food(ModFoods.GYSAHL_GREEN)));
    public static final RegistryObject<Item> LOVELY_GYSAHL_GREEN = ITEMS.register("lovely_gysahl_green", () -> new Item(itemBuilder(false)));
    public static final RegistryObject<Item> GOLDEN_GYSAHL_GREEN = ITEMS.register("golden_gysahl_green", () -> new Item(itemBuilder(false)));
    public static final RegistryObject<Item> PINK_GYSAHL_GREEN = ITEMS.register("pink_gysahl_green", () -> new Item(itemBuilder(false)));
    public static final RegistryObject<Item> DEAD_PEPPER = ITEMS.register("dead_pepper", () -> new Item(itemBuilder(false)));
    public static final RegistryObject<Item> SPIKE_FRUIT = ITEMS.register("spike_fruit", () -> new Item(itemBuilder(false)));

    //Chocobo Items
    public static final RegistryObject<Item> STONE_CHOCO_WEAPON = ITEMS.register("chocobo_weapon_stone", () -> new ChocoboWeaponItems(CHOCOBO_WEAPON_TIERS.get(1), -2.4f, (itemBuilder(false))));
    public static final RegistryObject<Item> IRON_CHOCO_WEAPON = ITEMS.register("chocobo_weapon_iron", () -> new ChocoboWeaponItems(CHOCOBO_WEAPON_TIERS.get(2), -2.4f, (itemBuilder(false))));
    public static final RegistryObject<Item> DIAMOND_CHOCO_WEAPON = ITEMS.register("chocobo_weapon_diamond", () -> new ChocoboWeaponItems(CHOCOBO_WEAPON_TIERS.get(3), -2.4f, (itemBuilder(false))));
    public static final RegistryObject<Item> NETHERITE_CHOCO_WEAPON = ITEMS.register("chocobo_weapon_netherite", () -> new ChocoboWeaponItems(CHOCOBO_WEAPON_TIERS.get(4), -2.4f, (itemBuilder(true))));
    public static final RegistryObject<Item> CHAIN_CHOCO_CHEST = ITEMS.register("chocobo_armor_chain", () -> new ChocoboArmorItems(CHOCOBO_ARMOR_MATERIALS.get(1), EquipmentSlot.CHEST, (itemBuilder(false))));
    public static final RegistryObject<Item> IRON_CHOCO_CHEST = ITEMS.register("chocobo_armor_iron", () -> new ChocoboArmorItems(CHOCOBO_ARMOR_MATERIALS.get(2), EquipmentSlot.CHEST, (itemBuilder(false))));
    public static final RegistryObject<Item> DIAMOND_CHOCO_CHEST = ITEMS.register("chocobo_armor_diamond", () -> new ChocoboArmorItems(CHOCOBO_ARMOR_MATERIALS.get(3), EquipmentSlot.CHEST, (itemBuilder(false))));
    public static final RegistryObject<Item> NETHERITE_CHOCO_CHEST = ITEMS.register("chocobo_armor_netherite", () -> new ChocoboArmorItems(CHOCOBO_ARMOR_MATERIALS.get(4), EquipmentSlot.CHEST, (itemBuilder(true))));
    public static final RegistryObject<Item> CHOCOBO_SADDLE = ITEMS.register("chocobo_saddle", () -> new ChocoboSaddleItem(itemBuilder(false), 0, false));
    public static final RegistryObject<Item> CHOCOBO_SADDLE_BAGS = ITEMS.register("chocobo_saddle_bags", () -> new ChocoboSaddleItem(itemBuilder(false), tier_one_chocobo_inv_slot_count, false));
    public static final RegistryObject<Item> CHOCOBO_SADDLE_PACK = ITEMS.register("chocobo_saddle_pack", () -> new ChocoboSaddleItem(itemBuilder(false), tier_two_chocobo_inv_slot_count, true));
    public static final RegistryObject<Item> YELLOW_CHOCOBO_SPAWN_EGG = ITEMS.register("yellow_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.YELLOW));
    public static final RegistryObject<Item> GREEN_CHOCOBO_SPAWN_EGG = ITEMS.register("green_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.GREEN));
    public static final RegistryObject<Item> BLUE_CHOCOBO_SPAWN_EGG = ITEMS.register("blue_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.BLUE));
    public static final RegistryObject<Item> WHITE_CHOCOBO_SPAWN_EGG = ITEMS.register("white_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.WHITE));
    public static final RegistryObject<Item> BLACK_CHOCOBO_SPAWN_EGG = ITEMS.register("black_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.BLACK));
    public static final RegistryObject<Item> GOLD_CHOCOBO_SPAWN_EGG = ITEMS.register("gold_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.GOLD));
    public static final RegistryObject<Item> PINK_CHOCOBO_SPAWN_EGG = ITEMS.register("pink_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.PINK));
    public static final RegistryObject<Item> RED_CHOCOBO_SPAWN_EGG = ITEMS.register("red_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.RED));
    public static final RegistryObject<Item> PURPLE_CHOCOBO_SPAWN_EGG = ITEMS.register("purple_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(false), ChocoboColor.PURPLE));
    public static final RegistryObject<Item> FLAME_CHOCOBO_SPAWN_EGG = ITEMS.register("flame_chocobo_spawn_egg", () -> new ChocoboSpawnEggItem(itemBuilder(true), ChocoboColor.FLAME));
    public static final RegistryObject<Item> CHOCOBO_WHISTLE = ITEMS.register("chocobo_whistle", () -> new Item(itemBuilder(false)));
    public static final RegistryObject<Item> CHOCOBO_FEATHER = ITEMS.register("chocobo_feather", () -> new ChocoboSpawnerItemHelper(itemBuilder(false)));
    public static final RegistryObject<Item> GYSAHL_CAKE = ITEMS.register("gysahl_cake", () -> new Item(itemBuilder(false).stacksTo(8)));

    public static final RegistryObject<Item> CHOCOBO_DRUMSTICK_RAW = ITEMS.register("chocobo_drumstick_raw", () -> new Item(itemBuilder(false).food(ModFoods.CHOCOBO_DRUMSTICK_RAW)));
    public static final RegistryObject<Item> CHOCOBO_DRUMSTICK_COOKED = ITEMS.register("chocobo_drumstick_cooked", () -> new Item(itemBuilder(false).food(ModFoods.CHOCOBO_DRUMSTICK_COOKED)));
    public static final RegistryObject<Item> PICKLED_GYSAHL_RAW = ITEMS.register("pickled_gysahl_raw", () -> new Item(itemBuilder(false).food(ModFoods.PICKLED_GYSAHL_RAW)));
    public static final RegistryObject<Item> PICKLED_GYSAHL_COOKED = ITEMS.register("pickled_gysahl_cooked", () -> new Item(itemBuilder(false).food(ModFoods.PICKLED_GYSAHL_COOKED)));
    public static final RegistryObject<Item> CHOCOBO_LEASH_STICK = ITEMS.register("chocobo_leash_stick", () -> new ChocoboLeashPointer(itemBuilder(false)));

    public static final RegistryObject<Item> LEATHER_CHOCO_DISGUISE_HELMET = ITEMS.register("choco_disguise_leather_helmet", () -> new ChocoDisguiseItem(ModArmorMaterial.LEATHER_CHOCO_DISGUISE, EquipmentSlot.HEAD, itemBuilder(false)));
    public static final RegistryObject<Item> LEATHER_CHOCO_DISGUISE_CHEST = ITEMS.register("choco_disguise_leather_chestplate", () -> new ChocoDisguiseItem(ModArmorMaterial.LEATHER_CHOCO_DISGUISE, EquipmentSlot.CHEST, itemBuilder(false)));
    public static final RegistryObject<Item> LEATHER_CHOCO_DISGUISE_LEGS = ITEMS.register("choco_disguise_leather_leggings", () -> new ChocoDisguiseItem(ModArmorMaterial.LEATHER_CHOCO_DISGUISE, EquipmentSlot.LEGS, itemBuilder(false)));
    public static final RegistryObject<Item> LEATHER_CHOCO_DISGUISE_FEET = ITEMS.register("choco_disguise_leather_boots", () -> new ChocoDisguiseItem(ModArmorMaterial.LEATHER_CHOCO_DISGUISE, EquipmentSlot.FEET, itemBuilder(false)));
    public static final RegistryObject<Item> IRON_CHOCO_DISGUISE_HELMET = ITEMS.register("choco_disguise_iron_helmet", () -> new ChocoDisguiseItem(ModArmorMaterial.IRON_CHOCO_DISGUISE, EquipmentSlot.HEAD, itemBuilder(false)));
    public static final RegistryObject<Item> IRON_CHOCO_DISGUISE_CHEST = ITEMS.register("choco_disguise_iron_chestplate", () -> new ChocoDisguiseItem(ModArmorMaterial.IRON_CHOCO_DISGUISE, EquipmentSlot.CHEST, itemBuilder(false)));
    public static final RegistryObject<Item> IRON_CHOCO_DISGUISE_LEGS = ITEMS.register("choco_disguise_iron_leggings", () -> new ChocoDisguiseItem(ModArmorMaterial.IRON_CHOCO_DISGUISE, EquipmentSlot.LEGS, itemBuilder(false)));
    public static final RegistryObject<Item> IRON_CHOCO_DISGUISE_FEET = ITEMS.register("choco_disguise_iron_boots", () -> new ChocoDisguiseItem(ModArmorMaterial.IRON_CHOCO_DISGUISE, EquipmentSlot.FEET, itemBuilder(false)));
    public static final RegistryObject<Item> DIAMOND_CHOCO_DISGUISE_HELMET = ITEMS.register("choco_disguise_diamond_helmet", () -> new ChocoDisguiseItem(ModArmorMaterial.DIAMOND_CHOCO_DISGUISE, EquipmentSlot.HEAD, itemBuilder(false)));
    public static final RegistryObject<Item> DIAMOND_CHOCO_DISGUISE_CHEST = ITEMS.register("choco_disguise_diamond_chestplate", () -> new ChocoDisguiseItem(ModArmorMaterial.DIAMOND_CHOCO_DISGUISE, EquipmentSlot.CHEST, itemBuilder(false)));
    public static final RegistryObject<Item> DIAMOND_CHOCO_DISGUISE_LEGS = ITEMS.register("choco_disguise_diamond_leggings", () -> new ChocoDisguiseItem(ModArmorMaterial.DIAMOND_CHOCO_DISGUISE, EquipmentSlot.LEGS, itemBuilder(false)));
    public static final RegistryObject<Item> DIAMOND_CHOCO_DISGUISE_FEET = ITEMS.register("choco_disguise_diamond_boots", () -> new ChocoDisguiseItem(ModArmorMaterial.DIAMOND_CHOCO_DISGUISE, EquipmentSlot.FEET, itemBuilder(false)));
    public static final RegistryObject<Item> NETHERITE_CHOCO_DISGUISE_HELMET = ITEMS.register("choco_disguise_netherite_helmet", () -> new ChocoDisguiseItem(ModArmorMaterial.NETHERITE_CHOCO_DISGUISE, EquipmentSlot.HEAD, itemBuilder(true)));
    public static final RegistryObject<Item> NETHERITE_CHOCO_DISGUISE_CHEST = ITEMS.register("choco_disguise_netherite_chestplate", () -> new ChocoDisguiseItem(ModArmorMaterial.NETHERITE_CHOCO_DISGUISE, EquipmentSlot.CHEST, itemBuilder(true)));
    public static final RegistryObject<Item> NETHERITE_CHOCO_DISGUISE_LEGS = ITEMS.register("choco_disguise_netherite_leggings", () -> new ChocoDisguiseItem(ModArmorMaterial.NETHERITE_CHOCO_DISGUISE, EquipmentSlot.LEGS, itemBuilder(true)));
    public static final RegistryObject<Item> NETHERITE_CHOCO_DISGUISE_FEET = ITEMS.register("choco_disguise_netherite_boots", () -> new ChocoDisguiseItem(ModArmorMaterial.NETHERITE_CHOCO_DISGUISE, EquipmentSlot.FEET, itemBuilder(true)));

    private static Item.Properties itemBuilder(boolean fireImmune) { if (fireImmune) { return new Item.Properties().tab(CHOCO_TAB).fireResistant();} else { return new Item.Properties().tab(CHOCO_TAB); } }
}