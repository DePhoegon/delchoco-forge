package com.dephoegon.delchoco.common.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class WorldConfig {
    public static class WCommon {
        public final ForgeConfigSpec.IntValue gysahlGreenPatchSize;
        public final ForgeConfigSpec.DoubleValue gysahlGreenSpawnChance;
        public final ForgeConfigSpec.IntValue chocoboSpawnWeight;
        public final ForgeConfigSpec.IntValue chocoboSpawnWeightMushroom;
        public final ForgeConfigSpec.IntValue chocoboSpawnWeightNether;
        public final ForgeConfigSpec.IntValue chocoboSpawnWeightEnd;
        public final ForgeConfigSpec.IntValue chocoboPackSizeMin;
        public final ForgeConfigSpec.IntValue chocoboPackSizeMax;
        public final ForgeConfigSpec.BooleanValue overworldSpawns;
        public final ForgeConfigSpec.BooleanValue netherSpawns;
        public final ForgeConfigSpec.BooleanValue endSpawns;
        public final ForgeConfigSpec.BooleanValue summonSpawns;
        public final ForgeConfigSpec.BooleanValue chocoboSpawnEnabler;
        WCommon(ForgeConfigSpec.@NotNull Builder builder) {
            builder.comment("World generation related configuration").push("World");
            builder.comment("Gysahl Greens").push("gysahl");
            /*
            gysahlGreenSpawnWeight = builder
                    .worldRestart()
                    .comment("Controls the weight compared to other world gen [Default: 3]")
                    .defineInRange("gysahlGreenSpawnWeight", 3, 0, 20);
             */

            gysahlGreenPatchSize = builder
                    .worldRestart()
                    .comment("Controls the Patch Size compared to other world gen [Default: 64]")
                    .defineInRange("gysahlGreenPatchSize", 64, 0, 128);

            gysahlGreenSpawnChance = builder
                    .worldRestart()
                    .comment("Controls the Spawn Chance compared to other world gen [Default: 0.1]")
                    .defineInRange("gysahlGreenSpawnChance", 0.1, 0, 1);

            builder.pop();
            builder.comment("Chocobo World Related Settings").push("chocobo");

            chocoboSpawnEnabler = builder
                    .comment("Enables/Disables Chocobo Natural Spawning")
                    .define("naturalSpawns", true);

            overworldSpawns = builder
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

            chocoboSpawnWeight = builder
                    .comment("Controls Chocobo Spawn Weight [Default: 8]\nSpawn weight Lowered to allow for natural spawns & Chocobo Alters Usage [ Defaults for normal spawns w/o alter usage : 4]")
                    .defineInRange("chocoboSpawnWeight", 8, 0, 20);

            chocoboSpawnWeightMushroom = builder
                    .comment("Controls Chocobo Spawn Weight in Mushroom Fields [Default: 1]\nMooshrooms are easily pushed out (spawn wise)\nSpawn weight Lowered to allow for natural spawns & Chocobo Alters Usage [ Defaults for normal spawns w/o alter usage : 1]")
                    .defineInRange("chocoboSpawnWeightMushrooms", 2, 0, 4);

            chocoboSpawnWeightNether = builder
                    .comment("Controls Chocobo Spawn Weight in the Nether. [Default: 100]\nThe Nether seems to be a bit more aggressive & requires a higher number.\nSpawn weight Lowered to allow for natural spawns & Chocobo Alters Usage [ Defaults for normal spawns w/o alter usage : 250]")
                    .defineInRange("chocoboSpawnWeightNether", 100, 75, 200);

            chocoboSpawnWeightEnd = builder
                    .comment("Controls Chocobo Spawn Weight in the End. [Default: 100]\nEnderman Spawns are super aggressive.\nTested at old default spawn weight (10)*25 to be reasonable\nSpawn weight Lowered to allow for natural spawns & Chocobo Alters Usage [ Defaults for normal spawns w/o alter usage : 250]")
                    .defineInRange("chocoboSpawnWeightEnd", 100, 75, 200);

            chocoboPackSizeMin = builder
                    .comment("Controls Chocobo Pack Size Min [Default: 1]")
                    .defineInRange("chocoboPackSizeMin", 1, 1, 4);

            chocoboPackSizeMax = builder
                    .comment("Controls Chocobo Pack Size Max [Default: 4]")
                    .defineInRange("chocoboPackSizeMax", 4, 2, 10);

            builder.pop(2);
        }
    }
    public static final ForgeConfigSpec commonSpec;
    public static final WCommon COMMON;

    static {
        final Pair<WCommon, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(WCommon::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}
