package com.dephoegon.delchoco.common.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("ALL")
public enum ModArmorMaterial implements ArmorMaterial {
	LEATHER_CHOCO_DISGUISE("delchoco:leather_choco_disguise", 10, new int[] { 3, 4, 5, 3 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5F, 0.0F, () -> Ingredient.of(ModRegistry.CHOCOBO_FEATHER.get(), Items.LEATHER)),
	IRON_CHOCO_DISGUISE("delchoco:iron_choco_disguise", 30, new int[] { 4, 7, 8, 4 }, 15, SoundEvents.ARMOR_EQUIP_IRON, 0.5F, 0.0F, () -> Ingredient.of(ModRegistry.CHOCOBO_FEATHER.get(), Items.IRON_INGOT)),
	DIAMOND_CHOCO_DISGUISE("delchoco:diamond_choco_disguise", 66, new int[] { 5, 8, 10, 5 }, 15, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.0F, () -> Ingredient.of(ModRegistry.CHOCOBO_FEATHER.get(), Items.DIAMOND)),
	NETHERITE_CHOCO_DISGUISE("delchoco:netherite_choco_disguise", 68, new int[] { 5, 8, 10, 5 }, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5F, 0.1F, () -> Ingredient.of(ModRegistry.CHOCOBO_FEATHER.get(), Items.NETHERITE_INGOT));

	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockBackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ModArmorMaterial(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent soundEvent, float pToughness, float pKnockBackResistance, Supplier<Ingredient> pRepairIngredient) {
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtections = pSlotProtections;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = soundEvent;
		this.toughness = pToughness;
		this.knockBackResistance = pKnockBackResistance;
		this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
	}
	public int getDurabilityForSlot(@NotNull EquipmentSlot p_40484_) { return HEALTH_PER_SLOT[p_40484_.getIndex()] * this.durabilityMultiplier; }
	public int getDefenseForSlot(@NotNull EquipmentSlot p_40487_) { return this.slotProtections[p_40487_.getIndex()]; }
	public int getEnchantmentValue() { return this.enchantmentValue; }
	public @NotNull SoundEvent getEquipSound() { return this.sound; }
	public @NotNull Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
	public @NotNull String getName() { return this.name; }
	public float getToughness() { return this.toughness; }
	public float getKnockbackResistance() { return this.knockBackResistance; }
}