package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.dephoegon.delchoco.aid.util.fallbackValues.*;
import static com.dephoegon.delchoco.common.configs.ChocoConfig.COMMON;

public class ChocoboStatSnapshot {
    public static final ChocoboStatSnapshot DEFAULT;
    public static final String NBTKEY_GENERATION = "Generation";
    public static final String NBTKEY_HEALTH = "Health";
    public static final String NBTKEY_SPEED = "Speed";
    public static final String NBTKEY_STAMINA = "Stamina";
    public static final String NBTKEY_COLOR = "Color";
    public static final String NBTKEY_FLAME_BLOOD = "FlameBlood";
    public static final String NBTKEY_WATER_BREATH = "WaterBreath";
    public static final String NBTKEY_ATTACK = "Damage";
    public static final String NBTKEY_ARMOR = "Armor";
    public static final String NBTKEY_ARMOR_TOUGHNESS = "Toughness";
    private static final String NBTKEY_CHOCOBO_WITHER_IMMUNE = "WitherImmune";
    private static final String NBTKEY_CHOCOBO_POISON_IMMUNE = "PoisonImmune";
    private static final String NBTKEY_CHOCOBO_SCALE = "Scale";

    public int generation;
    public int scale;
    public float health;
    public float speed;
    public float stamina;
    public boolean flameBlood;
    public boolean waterBreath;
    public boolean witherImmune;
    public boolean poisonImmune;
    public ChocoboColor color;
    public double attack;
    public double defense;
    public double toughness;

    static {
        DEFAULT = new ChocoboStatSnapshot();
        DEFAULT.generation = 1;
        DEFAULT.health = ChocoConfigGet(COMMON.defaultHealth.get(), dHealth);
        DEFAULT.stamina = ChocoConfigGet(COMMON.defaultStamina.get(),dStamina);
        DEFAULT.speed = ChocoConfigGet(COMMON.defaultSpeed.get(), dSpeed) / 100f;
        DEFAULT.attack = ChocoConfigGet(COMMON.defaultAttackStrength.get(), dAttack);
        DEFAULT.defense = ChocoConfigGet(COMMON.defaultArmor.get(), dArmor);
        DEFAULT.toughness = ChocoConfigGet(COMMON.defaultArmorToughness.get(), dArmorTough);
        DEFAULT.flameBlood = false;
        DEFAULT.waterBreath = false;
        DEFAULT.color = ChocoboColor.YELLOW;
        DEFAULT.witherImmune = false;
        DEFAULT.poisonImmune = false;
    }
    public ChocoboStatSnapshot() { }
    public ChocoboStatSnapshot(@NotNull Chocobo chocobo) {
        ItemStack weapon = chocobo.getWeapon();
        if (!weapon.isEmpty()) { chocobo.setChocoboWeaponStats(ItemStack.EMPTY); }
        ItemStack armor = chocobo.getArmor();
        if (!armor.isEmpty()) { chocobo.setChocoboArmorStats(ItemStack.EMPTY); }
        this.generation = chocobo.getGeneration();
        this.health = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MAX_HEALTH)).getValue();
        this.speed = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MOVEMENT_SPEED)).getValue();
        this.stamina = (float) Objects.requireNonNull(chocobo.getAttribute(ModAttributes.MAX_STAMINA.get())).getValue();
        this.attack = Objects.requireNonNull(chocobo.getAttribute(Attributes.ATTACK_DAMAGE)).getValue();
        this.defense = Objects.requireNonNull(chocobo.getAttribute(Attributes.ARMOR)).getValue();
        this.toughness = Objects.requireNonNull(chocobo.getAttribute(Attributes.ARMOR_TOUGHNESS)).getValue();
        this.flameBlood = chocobo.fireImmune();
        this.waterBreath = chocobo.isWaterBreather();
        this.witherImmune = chocobo.isWitherImmune();
        this.poisonImmune = chocobo.isPoisonImmune();
        this.scale = 0;
        this.color = chocobo.getChocoboColor();
        if (!weapon.isEmpty()) { chocobo.setChocoboWeaponStats(weapon); }
        if (!armor.isEmpty()) { chocobo.setChocoboArmorStats(armor); }
    }
    public ChocoboStatSnapshot(@NotNull CompoundTag nbt) {
        this.generation = nbt.getInt(NBTKEY_GENERATION);
        this.health = nbt.getFloat(NBTKEY_HEALTH);
        this.speed = nbt.getFloat(NBTKEY_SPEED);
        this.stamina = nbt.getFloat(NBTKEY_STAMINA);
        this.attack = nbt.getDouble(NBTKEY_ATTACK);
        this.attack = nbt.getDouble(NBTKEY_ARMOR);
        this.attack = nbt.getDouble(NBTKEY_ARMOR_TOUGHNESS);
        this.flameBlood = nbt.getBoolean(NBTKEY_FLAME_BLOOD);
        this.waterBreath = nbt.getBoolean(NBTKEY_WATER_BREATH);
        this.witherImmune = nbt.getBoolean(NBTKEY_CHOCOBO_WITHER_IMMUNE);
        this.poisonImmune = nbt.getBoolean(NBTKEY_CHOCOBO_POISON_IMMUNE);
        this.scale = nbt.getInt(NBTKEY_CHOCOBO_SCALE);
        this.color = ChocoboColor.values()[nbt.getByte(NBTKEY_COLOR)];
    }
    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBTKEY_GENERATION, this.generation);
        nbt.putFloat(NBTKEY_HEALTH, this.health);
        nbt.putFloat(NBTKEY_SPEED, this.speed);
        nbt.putFloat(NBTKEY_STAMINA, this.stamina);
        nbt.putDouble(NBTKEY_ATTACK, this.attack);
        nbt.putDouble(NBTKEY_ARMOR, this.defense);
        nbt.putDouble(NBTKEY_ARMOR_TOUGHNESS, this.toughness);
        nbt.putBoolean(NBTKEY_FLAME_BLOOD, this.flameBlood);
        nbt.putBoolean(NBTKEY_WATER_BREATH, this.waterBreath);
        nbt.putBoolean(NBTKEY_CHOCOBO_WITHER_IMMUNE, this.witherImmune);
        nbt.putBoolean(NBTKEY_CHOCOBO_POISON_IMMUNE, this.poisonImmune);
        nbt.putInt(NBTKEY_CHOCOBO_SCALE, this.scale);
        nbt.putByte(NBTKEY_COLOR, (byte) this.color.ordinal());
        return nbt;
    }
}