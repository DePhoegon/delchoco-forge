package com.dephoegon.chococraft.common.entities.breeding;

import com.dephoegon.chococraft.common.ChocoConfig;
import com.dephoegon.chococraft.common.entities.ChocoboEntity;
import com.dephoegon.chococraft.common.entities.properties.ChocoboColor;
import com.dephoegon.chococraft.common.init.ModAttributes;
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

    public int generation;
    public float health;
    public float speed;
    public float stamina;
    public boolean flameBlood;
    public ChocoboColor color;

    static {
        DEFAULT = new ChocoboStatSnapshot();
        DEFAULT.generation = 1;
        DEFAULT.health = ChocoConfig.COMMON.defaultHealth.get();
        DEFAULT.stamina = ChocoConfig.COMMON.defaultStamina.get();
        DEFAULT.speed = ChocoConfig.COMMON.defaultSpeed.get() / 100f;
        DEFAULT.flameBlood = false;
        DEFAULT.color = ChocoboColor.YELLOW;
    }

    private ChocoboStatSnapshot() {
    }

    public ChocoboStatSnapshot(@NotNull ChocoboEntity chocobo) {
        this.generation = chocobo.getGeneration();
        this.health = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();
        this.speed = (float) Objects.requireNonNull(chocobo.getAttribute(Attributes.MOVEMENT_SPEED)).getBaseValue();
        this.stamina = (float) Objects.requireNonNull(chocobo.getAttribute(ModAttributes.MAX_STAMINA.get())).getBaseValue();
        this.flameBlood = chocobo.isFlame();
        this.color = chocobo.getChocoboColor();
    }

    public ChocoboStatSnapshot(@NotNull CompoundTag nbt) {
        this.generation = nbt.getInt(NBTKEY_GENERATION);
        this.health = nbt.getFloat(NBTKEY_HEALTH);
        this.speed = nbt.getFloat(NBTKEY_SPEED);
        this.stamina = nbt.getFloat(NBTKEY_STAMINA);
        this.flameBlood = nbt.getBoolean(NBTKEY_FLAME_BLOOD);
        this.color = ChocoboColor.values()[nbt.getByte(NBTKEY_COLOR)];
    }

    public CompoundTag serialize() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(NBTKEY_GENERATION, this.generation);
        nbt.putFloat(NBTKEY_HEALTH, this.health);
        nbt.putFloat(NBTKEY_SPEED, this.speed);
        nbt.putFloat(NBTKEY_STAMINA, this.stamina);
        nbt.putBoolean(NBTKEY_FLAME_BLOOD, this.flameBlood);
        nbt.putByte(NBTKEY_COLOR, (byte) this.color.ordinal());
        return nbt;
    }
}
