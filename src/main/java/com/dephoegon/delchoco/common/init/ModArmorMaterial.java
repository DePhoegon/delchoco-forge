package com.dephoegon.delchoco.common.init;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {
	CHOCO_DISGUISE("delchoco:choco_disguise", 200, new int[] { 3, 7, 6, 3 }, 10, SoundEvents.ARMOR_EQUIP_LEATHER,
			0.0F, 0.0F, () -> {
		return Ingredient.of(ModRegistry.CHOCOBO_FEATHER.get());
	});

	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	ModArmorMaterial(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent soundEvent, float pToughness, float pKnockbackResistance, Supplier<Ingredient> pRepairIngredient) {
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtections = pSlotProtections;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = soundEvent;
		this.toughness = pToughness;
		this.knockbackResistance = pKnockbackResistance;
		this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
	}

	public int getDurabilityForSlot(EquipmentSlot p_40484_) {
		return HEALTH_PER_SLOT[p_40484_.getIndex()] * this.durabilityMultiplier;
	}

	public int getDefenseForSlot(EquipmentSlot p_40487_) {
		return this.slotProtections[p_40487_.getIndex()];
	}

	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	public @NotNull SoundEvent getEquipSound() {
		return this.sound;
	}

	public @NotNull Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	public @NotNull String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}
