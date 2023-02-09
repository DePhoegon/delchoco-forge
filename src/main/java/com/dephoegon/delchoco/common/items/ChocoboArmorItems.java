package com.dephoegon.delchoco.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class ChocoboArmorItems extends Item implements Vanishable {
    private static final UUID CHOCO_ARMOR_SLOT = UUID.fromString("02a4a813-7afd-4073-bf47-6dcffdf18fca");
    protected final EquipmentSlot slot;
    private final int defense;
    private final float toughness;
    protected final float knockBackResistance;
    protected final ArmorMaterial material;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ChocoboArmorItems(ArmorMaterial pMaterial, EquipmentSlot pSlot, Item.Properties pProperties) {
        super(pProperties.defaultDurability(pMaterial.getDurabilityForSlot(pSlot)));
        this.material = pMaterial;
        this.slot = pSlot;
        this.defense = pMaterial.getDefenseForSlot(pSlot);
        this.toughness = pMaterial.getToughness();
        this.knockBackResistance = pMaterial.getKnockbackResistance();
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = CHOCO_ARMOR_SLOT;
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", (double)this.defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("Armor toughness", (double)this.toughness, AttributeModifier.Operation.ADDITION));
        if (this.knockBackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", (double)this.knockBackResistance, AttributeModifier.Operation.ADDITION));
        }

        this.defaultModifiers = builder.build();
    }
    public EquipmentSlot getSlot() {
        return this.slot;
    }
    public int getEnchantmentValue() { return this.material.getEnchantmentValue(); }
    public ArmorMaterial getMaterial() {
        return this.material;
    }
    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return this.material.getRepairIngredient().test(pRepair) || super.isValidRepairItem(pToRepair, pRepair);
    }
    // Set failed here for player.
    public InteractionResultHolder<ItemStack> use(Level pLevel, @NotNull Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
            return InteractionResultHolder.fail(itemstack);
    }
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
    public int getProtection() { return this.defense; }

    public float getToughness() {
        return this.toughness;
    }

    @Nullable
    public SoundEvent getEquipSound() {
        return this.getMaterial().getEquipSound();
    }
}
