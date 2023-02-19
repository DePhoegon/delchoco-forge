package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    public int generation;
    public float health;
    public float speed;
    public float stamina;
    public boolean flameBlood;
    public boolean waterBreath;
    public ChocoboColor color;
    public double attack;
    public double defense;
    public double toughness;

    static {
        DEFAULT = new ChocoboStatSnapshot();
        DEFAULT.generation = 1;
        DEFAULT.health = ChocoConfig.COMMON.defaultHealth.get();
        DEFAULT.stamina = ChocoConfig.COMMON.defaultStamina.get();
        DEFAULT.speed = ChocoConfig.COMMON.defaultSpeed.get() / 100f;
        DEFAULT.attack = ChocoConfig.COMMON.defaultAttackStrength.get();
        DEFAULT.defense = ChocoConfig.COMMON.defaultArmor.get();
        DEFAULT.toughness = ChocoConfig.COMMON.defaultArmorToughness.get();
        DEFAULT.flameBlood = false;
        DEFAULT.waterBreath = false;
        DEFAULT.color = ChocoboColor.YELLOW;
    }
    public ChocoboStatSnapshot() { }
    public ChocoboStatSnapshot(@NotNull Chocobo chocobo) {
        this.generation = chocobo.getGeneration();
        this.health = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();
        this.speed = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MOVEMENT_SPEED)).getBaseValue();
        this.stamina = (float) Objects.requireNonNull(chocobo.getAttribute(ModAttributes.MAX_STAMINA.get())).getBaseValue();
        this.attack = Objects.requireNonNull(chocobo.getAttribute(Attributes.ATTACK_DAMAGE)).getBaseValue();
        this.defense = Objects.requireNonNull(chocobo.getAttribute(Attributes.ARMOR)).getBaseValue();
        this.toughness = Objects.requireNonNull(chocobo.getAttribute(Attributes.ARMOR_TOUGHNESS)).getBaseValue();
        this.flameBlood = chocobo.fireImmune();
        this.waterBreath = chocobo.isWaterBreather();
        this.color = chocobo.getChocoboColor();
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
        nbt.putByte(NBTKEY_COLOR, (byte) this.color.ordinal());
        return nbt;
    }
}
