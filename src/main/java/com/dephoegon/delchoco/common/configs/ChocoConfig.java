package com.dephoegon.delchoco.common.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.client.renderer.entities.ChocoboRenderer.*;

public class ChocoConfig {
    public static class Common {
        public final DoubleValue tameChance;
        public final DoubleValue sprintStaminaCost;
        public final DoubleValue glideStaminaCost;
        public final DoubleValue jumpStaminaCost;
        public final DoubleValue staminaRegenRate;
        public final IntValue defaultHealAmount;
        public final IntValue defaultStamina;
        public final IntValue defaultSpeed;
        public final IntValue defaultHealth;
        public final IntValue modifier;
        public final IntValue defaultArmor;
        public final IntValue defaultArmorToughness;
        public final IntValue defaultAttackStrength;
        public final IntValue maxHealth;
        public final IntValue maxSpeed;
        public final DoubleValue maxStamina;
        public final DoubleValue maxStrength;
        public final DoubleValue maxArmor;
        public final DoubleValue maxToughness;
        public final DoubleValue posgainHealth;
        public final DoubleValue posgainSpeed;
        public final DoubleValue posgainStamina;
        public final DoubleValue poslossHealth;
        public final DoubleValue poslossSpeed;
        public final DoubleValue poslossStamina;
        public final IntValue fruitEatTimer;
        public final BooleanValue ownerOnlyAccess;
        public final DoubleValue collarInvisibility;
        public final DoubleValue armorInvisibility;
        public final DoubleValue saddleInvisibility;
        public final DoubleValue weaponInvisibility;
        public final BooleanValue chocoboResourcesOnHit;
        public final BooleanValue chocoboResourcesOnKill;
        public final BooleanValue extraChocoboEffects;

        public final BooleanValue ownChocoboHittable;
        public final BooleanValue tamedChocoboHittable;
        public final BooleanValue shiftBypassAllowed;

        Common(ForgeConfigSpec.@NotNull Builder builder) {
            builder.comment("General Chocobo Configuration").push("chocobo");
            builder.comment("Tamed Chocobo Protections").push("tamed");

            ownChocoboHittable = builder
                    .comment("Enables/Disable Hitting of Your Own Tamed/TeamTamed Chocobo")
                    .define("allowOwnTamedChocoboHit", false);

            tamedChocoboHittable = builder
                    .comment("Enables/Disable Hitting of Tamed Chocobos")
                    .define("allowTamedChocoboHit", false);

            shiftBypassAllowed = builder
                    .comment("Enables/Disable Use of Shift to Hit Tamed Chocobo\n-AKA, Allows you to hit Chocobo while using Shift[Intended as an intentional bypass]")
                    .define("shiftToHitChocobo", true);

            tameChance = builder
                    .comment("This multiplier controls the tame chance per gysahl used, so .15 results in 15% chance to tame [Default: 0.15]")
                    .defineInRange("tameChance", 0.15, 0, 1);

            builder.pop();
            builder.comment("Chocobo Combat Extras").push("combat");

            chocoboResourcesOnHit = builder
                    .comment("Controls if the Chocobos will produce extra resources on combat hits. (Includes Disarming Targets on hit chance)")
                    .define("choco_hit_resource", true);

            chocoboResourcesOnKill = builder
                    .comment("Controls if the Chocobos will produce extra resources on Kills")
                    .define("choco_kill_resource", true);

            extraChocoboEffects = builder
                    .comment("Controls if the chocobos get extra effects outside traits that can be passed through breeding & outside of no fall damage.\n-Also Controls if the player receives stats/buff bonuses from wearing the a full color set of the ChocoGuise Gear.")
                    .define("chocobo_effects", true);

            builder.pop();
            builder.comment("Combat Stats").push("stats");

            modifier = builder
                    .comment("Armor/Weapon Value Modifier {Weapons & Armor For Chocobo are More Effective} [Default:1]")
                    .defineInRange("modifier", 1, 1, 3);

            defaultArmor = builder
                    .comment("Default Amount of 'Armor' Points [Default:4]")
                    .defineInRange("defaultArmor", 4, 0, 20);

            defaultArmorToughness = builder
                    .comment("Default Amount of 'Armor Toughness' Value [Default:1]")
                    .defineInRange("defaultArmorToughness", 1,0, 10);

            defaultAttackStrength = builder
                    .comment("Default Attack Value [Default: 2]")
                    .defineInRange("defaultAttackStrength", 2, 1, 10);

            builder.pop();
            builder.comment("Access").push("access");

            ownerOnlyAccess = builder
                    .comment("Chocobos Rideable & Custom Nameable by Owners (Player that tamed it) Only. [Default: False]")
                    .define("ownerOnlyAccess", false);

            builder.pop();
            builder.comment("Stamina Costs").push("stamina_costs");

            sprintStaminaCost = builder
                    .comment("Controls the Sprint Stamina cost [Default: 0.06]")
                    .defineInRange("sprintStaminaCost", 0.06, 0, 1);

            glideStaminaCost = builder
                    .comment("Controls the Glide Stamina cost [Default: 0.005]")
                    .defineInRange("glideStaminaCost", 0.005D, 0, 1);

            jumpStaminaCost = builder
                    .comment("Controls the Jump Stamina cost [Default: 0.00]")
                    .defineInRange("jumpStaminaCost", 0.00D, 0, 1);

            staminaRegenRate = builder
                    .comment("Controls the amount of Stamina recharged per tick [Default: 0.025]")
                    .defineInRange("staminaRegenRate", 0.025, 0, 1);

            builder.pop();
            builder.comment("Chocobo Defaults").push("defaults");

            defaultHealAmount = builder
                    .comment("Amount of HP Restored per Green { 1HP = 1/2 Heart } [Default: 5]")
                    .defineInRange("defaultHealAmount", 5, 2, 10);

            defaultStamina = builder
                    .comment("Controls the default Stamina [Default: 10]")
                    .defineInRange("defaultStamina", 10, 5, 60);

            defaultSpeed = builder
                    .comment("Controls the default Speed [Default: 20]")
                    .defineInRange("defaultSpeed", 20, 0, 80);

            defaultHealth = builder
                    .comment("Controls the default Health [Default: 20]")
                    .defineInRange("defaultHealth", 20, 0, 1000);

            builder.pop();
            builder.comment("Chocobo Transparencies with 'invisibility' effect - (0 is invisible)").push("visibility");

            collarInvisibility = builder
                    .comment("Collar - Default [0.2]")
                    .defineInRange("collarInvisibility", 0.2, 0, collarAlpha);

            armorInvisibility = builder
                    .comment("Armor - Default [0.1]")
                    .defineInRange("armorInvisibility", 0.1, 0, armorAlpha);

            weaponInvisibility = builder
                    .comment("Weapon - Default [0.1]")
                    .defineInRange("weaponInvisibility", 0.1, 0, weaponAlpha);

            saddleInvisibility = builder
                    .comment("Saddles - Default [0.1]")
                    .defineInRange("saddleInvisibility", 0.1, 0, saddleAlpha);

            builder.pop();
            builder.comment("Breeding configuration").push("breeding");
            builder.comment("Egg Configuration").push("egg");

            fruitEatTimer = builder
                    .comment("Controls the amount of ticks until a Chocobo can eat another fruit. This value isn't super accurate [Default: 60]")
                    .defineInRange("fruitEatTimer", 60, 40, 600);

            builder.pop();
            builder.comment("Max Stats").push("max");

            maxHealth = builder
                    .comment("Controls the Max Health a Chocobo can have [Default: 60]")
                    .defineInRange("maxHealth", 60, 25, 400);

            maxSpeed = builder
                    .comment("Controls the Max Speed a Chocobo can have [Default: 40]")
                    .defineInRange("maxSpeed", 40, 30, 160);

            maxStamina = builder
                    .comment("Controls the Max Stamina a Chocobo can have [Default: 200]")
                    .defineInRange("maxStamina", 200, 20D, 1024D);

            maxArmor = builder
                    .comment("Controls the max Natural Armor of a Chocobo")
                    .defineInRange("maxArmor", 200, 20D, 300D);

            maxStrength = builder
                    .comment("Controls the Max amount of damage a Chocobo can do without weapons")
                    .defineInRange("maxStrength", 60, 8D, 100D);

            maxToughness = builder
                    .comment("Controls The Max amount 'Armor Toughness' a Chocobo can have naturally")
                    .defineInRange("maxToughness", 20, 8D, 60D);

            builder.pop();
            builder.comment("Gain Stats").push("gain_stats");

            posgainHealth = builder
                    .comment("Controls the multiplier the Health stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                    .defineInRange("posgainHealth", .1D, 0, 1);

            posgainSpeed = builder
                    .comment("Controls the multiplier the Speed stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                    .defineInRange("posgainSpeed", .1D, 0, 1);

            posgainStamina = builder
                    .comment("Controls the multiplier the Stamina stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                    .defineInRange("posgainStamina", .1D, 0, 1);

            builder.pop();
            builder.comment("Loss Stats").push("loss_stats");

            poslossHealth = builder
                    .comment("Controls the multiplier the Health stat loss (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                    .defineInRange("poslossHealth", 1D, 0, 1);

            poslossSpeed = builder
                    .comment("Controls the multiplier the Speed stat gains (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                    .defineInRange("poslossSpeed", 1D, 0, 1);

            poslossStamina = builder
                    .comment("Controls the multiplier the Stamina stat gains (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                    .defineInRange("poslossStamina", 1D, 0, 1);

            builder.pop(3);

        }
    }
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}