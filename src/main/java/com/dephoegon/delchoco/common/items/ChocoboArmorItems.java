package com.dephoegon.delchoco.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class ChocoboArmorItems extends Item implements Wearable {
    private static final UUID CHOCO_ARMOR_SLOT = UUID.fromString("02a4a813-7afd-4073-bf47-6dcffdf18fca");
    protected final EquipmentSlot slot;
    private final int defense;
    private final float toughness;
    protected final float knockbackResistance;
    protected final ArmorMaterial material;

    private final int enchantmentValue;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public static final Map<Integer, ArmorMaterial> CHOCOBO_ARMOR_MATERIALS = Util.make(Maps.newHashMap(), (map) ->{
        map.put(1, ArmorMaterials.CHAIN);
        map.put(2, ArmorMaterials.IRON);
        map.put(3, ArmorMaterials.DIAMOND);
        map.put(4, ArmorMaterials.NETHERITE);
    });
    private static final Map<ArmorMaterial, Integer> CHOCOBO_ARMOR_MATERIAL = Util.make(Maps.newHashMap(), (map) ->{
        for (int i = 0; !(i > CHOCOBO_ARMOR_MATERIALS.size()); i++) { map.put(CHOCOBO_ARMOR_MATERIALS.get(i), i); }
    });
    private static final float setMod = 2.5F;
    private static int totalArmorMaterialDefense(ArmorMaterial armor, EquipmentSlot slot, int additive, boolean initialMaterial) {
        int out = initialMaterial ? armor.getDefenseForSlot(slot) + additive : (armor.getDefenseForSlot(slot) /2) + additive;
        int nextLowestArmor = CHOCOBO_ARMOR_MATERIAL.get(armor)-1;
        return nextLowestArmor > 0 ? totalArmorMaterialDefense(CHOCOBO_ARMOR_MATERIALS.get(nextLowestArmor), slot, out, false) : out;
    }
    private static float totalArmorMaterialToughness(ArmorMaterial armor, float additive, boolean initalMaterial) {
        float out = initalMaterial ? armor.getToughness()*setMod + additive : ((armor.getToughness()/2)*setMod)+ additive;
        int nextLowestArmor = CHOCOBO_ARMOR_MATERIAL.get(armor)-1;
        return nextLowestArmor > 0 ? totalArmorMaterialToughness(CHOCOBO_ARMOR_MATERIALS.get(nextLowestArmor), out, false) : out;
    }
    private static float totalArmorMaterialKnockBackResistance(@NotNull ArmorMaterial armor, float additive) {
        float out = armor.getKnockbackResistance() > 0 ? armor.getKnockbackResistance()*setMod + additive : additive;
        int nextLowestArmor = CHOCOBO_ARMOR_MATERIAL.get(armor)-1;
        return nextLowestArmor > 0 ? totalArmorMaterialKnockBackResistance(CHOCOBO_ARMOR_MATERIALS.get(nextLowestArmor), out) : out;
    }
    public ChocoboArmorItems(@NotNull ArmorMaterial pMaterial, EquipmentSlot pSlot, Item.@NotNull Properties pProperties) {
        super(pProperties.defaultDurability(pMaterial.getDurabilityForSlot(pSlot)));
        this.material = pMaterial;
        this.slot = pSlot;
        this.defense = totalArmorMaterialDefense(pMaterial, pSlot, 0, true);
        this.toughness = totalArmorMaterialToughness(pMaterial, 0, true);
        this.enchantmentValue = pMaterial.getEnchantmentValue();
        this.knockbackResistance = totalArmorMaterialKnockBackResistance(pMaterial, 0);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = CHOCO_ARMOR_SLOT;
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) { builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION)); }
        this.defaultModifiers = builder.build();
    }
    public EquipmentSlot getSlot() { return this.slot; }
    public int getEnchantmentValue() { return this.enchantmentValue; }
    public ArmorMaterial getMaterial() { return this.material; }
    public boolean isValidRepairItem(@NotNull ItemStack pToRepair, @NotNull ItemStack pRepair) { return this.material.getRepairIngredient().test(pRepair) || super.isValidRepairItem(pToRepair, pRepair); }
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        return InteractionResultHolder.fail(itemstack);
    }
    @SuppressWarnings("deprecation")
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) { return pEquipmentSlot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot); }

    public int getDefense() { return this.defense; }
    public float getToughness() { return this.toughness; }
    @Nullable
    public SoundEvent getEquipSound() { return this.getMaterial().getEquipSound(); }
}
