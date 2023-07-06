package com.dephoegon.delchoco.aid;

import java.util.List;

public class fallbackValues {
    public static final Integer dStamina = 10;
    public static final Integer dSpeed = 20;
    public static final Integer dHealth = 20;
    public static final Integer dArmor = 4;
    public static final Integer dArmorTough = 1;
    public static final Integer dAttack = 2;
    public static final Integer dWeaponModifier = 1;
    public static final Integer dHealAmount = 5;
    public static final Double dStaminaRegen = 0.025;
    public static final Double dTame = 0.15;
    public static final Double dStaminaCost = 0.06;
    public static final Double dStaminaGlide = 0.005;
    public static final Double dStaminaJump = 0D;
    public static final Boolean dCanSpawn = true;
    public static final Boolean dExtraChocoboEffects = true;
    public static final Boolean dExtraChocoboResourcesOnHit = true;
    public static final Boolean dExtraChocoboResourcesOnKill = true;
    public static final Boolean dShiftHitBypass = true;
    public static final Boolean dOwnChocoboHittable = false;
    public static final Boolean dTamedChocoboHittable = false;
    public static final Integer dEggHatchTimeTicks = 500;
    public static final Double dPossLoss = 1D;
    public static final Double dPossGain = .1D;
    public static final Integer dMaxHealth = 60;
    public static final Integer dMaxSpeed = 40;
    public static final Double dMaxStamina = 35D;
    public static final Double dMaxStrength = 60D;
    public static final Double dMaxArmor = 200D;
    public static final Double dMaxArmorToughness = 20D;
    public static final Double dArmorAlpha = 0.1;
    public static final Double dWeaponAlpha = 0.1;
    public static final Double dCollarAlpha = 0.2;
    public static final Double dSaddleAlpha = 0.1;
    public static final Boolean dDeathIsPermanent = true;
    public static final Boolean dCallableInEveryDimension = true;
    public static final List<? extends String> dCallableDimsWhiteList = List.of("minecraft:overworld");
    public static final Integer dMaxCallDistance = -1;
    public static final Boolean dEnableStatViewer = true;
    public static final Double dChocoboWalkingRange = 30d;
    public static final Boolean dContinuousAntiDupeChecking = false;
    public static final Boolean dCheckForFreeSpace = true;

    public static Integer ChocoConfigGet(Integer value, Integer FallBack) {
        return value == null ? FallBack : value;
    }
    public static Double ChocoConfigGet(Double value, Double FallBack) {
        return value == null ? FallBack : value;
    }
    public static Boolean ChocoConfigGet(Boolean config, Boolean FallBack) {
        return config == null ? FallBack : config;
    }
}
