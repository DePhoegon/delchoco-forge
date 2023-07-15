package com.dephoegon.delchoco.common.configs;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class SpawnControl {
    public static class Common {
        public final ForgeConfigSpec.BooleanValue overworldSpawns;
        public final ForgeConfigSpec.BooleanValue netherSpawns;
        public final ForgeConfigSpec.BooleanValue endSpawns;
        public final ForgeConfigSpec.BooleanValue summonSpawns;
        public final ForgeConfigSpec.BooleanValue chocoboSpawnEnabler;

        Common(ForgeConfigSpec.@NotNull Builder builder) {
            builder.comment("World generation related configuration").push("World");
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

            builder.pop(2);
        }
    }
    public static final ForgeConfigSpec commonWorld;
    public static final Common COMMON_WORLD;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonWorld = specPair.getRight();
        COMMON_WORLD = specPair.getLeft();
    }
}