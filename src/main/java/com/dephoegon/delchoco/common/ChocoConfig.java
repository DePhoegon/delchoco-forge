package com.dephoegon.delchoco.common;

import com.dephoegon.delchoco.DelChoco;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
        public final IntValue eggHatchTimeTicks;
        public final BooleanValue ownerOnlyAccess;
        public final DoubleValue collarInvisibility;
        public final DoubleValue armorInvisibility;
        public final DoubleValue saddleInvisibility;
        public final DoubleValue weaponInvisibility;
        public final BooleanValue overWorldSpawns;
        public final BooleanValue netherSpawns;
        public final BooleanValue endSpawns;
        public final BooleanValue summonSpawns;
        public final BooleanValue chocoboResourcesOnHit;
        public final BooleanValue chocoboResourcesOnKill;
        public final BooleanValue extraChocoboEffects;
        public final BooleanValue chocoboSpawnEnabler;

        Common(ForgeConfigSpec.@NotNull Builder builder) {
            builder.comment("Chocobo Spawn Configuration")
                    .push("spawning");

            chocoboSpawnEnabler = builder
                    .comment("Enables/Disables Chocobo Natural Spawning")
                    .define("naturalSpawns", true);

            overWorldSpawns = builder
                    .comment("Allows Chocobo to spawn in the OverWorld [Default: true]")
                    .define("overworldSpawns", true);

            netherSpawns = builder
                    .comment("Allows Chocobo to spawn in the Nether [Default: true]")
                    .define("netherSpawns", true);

            endSpawns = builder
                    .comment("Allows Chocobo to spawn in the End [Default: true]")
                    .define("endSpawns", true);

            summonSpawns = builder
                    .comment("Allows Chocobo to spawned in by the player with spawning [Default: true]\nSpawn weights lowered, if summonSpawns is disabled raise weights to commented Defaults")
                    .define("summonSpawns", true);

            builder.pop();
            builder.comment("Chocobo Extras")
                    .push("extras");

            chocoboResourcesOnHit = builder
                    .comment("Controls if the Chocobos will produce extra resources on combat hits. (Includes Disarming Targets on hit chance)")
                    .define("choco_hit_resource", true);

            chocoboResourcesOnKill = builder
                    .comment("Controls if the Chocobos will produce extra resources on Kills")
                    .define("choco_kill_resource", true);

            extraChocoboEffects = builder
                    .comment("Controls if the chocobos get extra effects outside traits that can be passed through breeding & outside of no fall damage.\n-Also Controls if the player receives stats/buff bonuses from wearing the a full color set of the ChocoGuise Gear.")
                    .define("effects", true);

            builder.pop();
            builder.comment("Chocobo configuration")
                    .push("chocobo");

            ownerOnlyAccess = builder
                    .comment("Chocobos Rideable & Custom Nameable by Owners (Player that tamed it) Only. [Default: False]")
                    .define("ownerOnlyAccess", false);

            builder.comment("Stamina Costs")
                    .push("stamina_costs");

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
            builder.comment("Defaults")
                    .push("defaults");

            defaultHealAmount = builder
                    .comment("Amount of HP Restored per Green { 1HP = 1/2 Heart } [Default: 5]")
                    .defineInRange("defaultHealAmount", 5, 2, 10);

            defaultStamina = builder
                    .comment("Controls the default Stamina [Default: 10]")
                    .defineInRange("defaultStamina", 10, 0, Integer.MAX_VALUE);

            defaultSpeed = builder
                    .comment("Controls the default Speed [Default: 20]")
                    .defineInRange("defaultSpeed", 20, 0, Integer.MAX_VALUE);

            defaultHealth = builder
                    .comment("Controls the default Health [Default: 20]")
                    .defineInRange("defaultHealth", 20, 0, Integer.MAX_VALUE);

            builder.pop();
            builder.comment("Chocobo Transparencies - 0 is invisible\nControls how visible the Chocobo Part is when the Chocobo is invisible")
                            .push("visibility");

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
            builder.comment("Combat Stats")
                    .push("stats");

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

            tameChance = builder
                    .comment("This multiplier controls the tame chance per gysahl used, so .15 results in 15% chance to tame [Default: 0.15]")
                    .defineInRange("tameChance", 0.15, 0, 1);

            builder.pop(2);
            builder.comment("Breeding configuration")
                    .push("breeding");

                builder.comment("Max Stats")
                        .push("max");

                maxHealth = builder
                        .comment("Controls the Max Health a Chocobo can have [Default: 50]")
                        .defineInRange("maxHealth", 60, 25, 400);

                maxSpeed = builder
                        .comment("Controls the Max Speed a Chocobo can have [Default: 40]")
                        .defineInRange("maxSpeed", 40, 30, 160);

                maxStamina = builder
                        .comment("Controls the Max Stamina a Chocobo can have [Default: 25]")
                        .defineInRange("maxStamina", 25D, 20D, 80D);

                maxArmor = builder
                        .comment("Controls the max Natural Armor of a Chocobo")
                        .defineInRange("maxArmor", 20D, 10D, 30D);

                maxStrength = builder
                        .comment("Controls the Max amount of damage a Chocobo can do without weapons")
                        .defineInRange("maxStrength", 10D, 8D, 30D);

                maxToughness = builder
                        .comment("Controls The Max amount 'Armor Toughness' a Chocobo can have naturally")
                        .defineInRange("maxToughness", 10D, 8D, 30D);

                builder.pop();
                builder.comment("Gain Stats")
                        .push("gain_stats");

                posgainHealth = builder
                        .comment("Controls the multiplier the Health stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                        .defineInRange("posgainHealth", .1D, 0, Integer.MAX_VALUE);

                posgainSpeed = builder
                        .comment("Controls the multiplier the Speed stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                        .defineInRange("posgainSpeed", .1D, 0, Integer.MAX_VALUE);

                posgainStamina = builder
                        .comment("Controls the multiplier the Stamina stat gains (for example 0.05 would result in a max gain of 5% so 20 to 21) [Default: .1]")
                        .defineInRange("posgainStamina", .1D, 0, Integer.MAX_VALUE);

                builder.pop();
                builder.comment("Loss Stats")
                        .push("loss_stats");

                poslossHealth = builder
                        .comment("Controls the multiplier the Health stat loss (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                        .defineInRange("poslossHealth", 1D, 0, Integer.MAX_VALUE);

                poslossSpeed = builder
                        .comment("Controls the multiplier the Speed stat gains (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                        .defineInRange("poslossSpeed", 1D, 0, Integer.MAX_VALUE);

                poslossStamina = builder
                        .comment("Controls the multiplier the Stamina stat gains (for example 0.95 would result in a max loss of 5% so 20 to 19) [Default: 1]")
                        .defineInRange("poslossStamina", 1D, 0, Integer.MAX_VALUE);

                builder.pop();

            eggHatchTimeTicks = builder
                    .comment("Controls the amount of ticks / time till an egg hatches. This value isn't super accurate [Default: 500-1000]")
                    .defineInRange("eggHatchTimeTicks", 500, 0, Integer.MAX_VALUE);

            builder.pop();
        }
    }
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.@NotNull Loading configEvent) { DelChoco.log.debug("Loaded delchoco' config file {}", configEvent.getConfig().getFileName()); }
    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.@NotNull Reloading configEvent) {
        DelChoco.log.debug("delchoco' config just got changed on the file system!");
        if(configEvent.getConfig().getModId().equals(DelChoco.DELCHOCO_ID)) {
            configEvent.getConfig().save();
        }
    }
}