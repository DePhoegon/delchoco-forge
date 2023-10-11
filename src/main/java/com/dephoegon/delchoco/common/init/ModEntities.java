package com.dephoegon.delchoco.common.init;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.spawnerColors.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DelChoco.DELCHOCO_ID);
    public static final RegistryObject<EntityType<Chocobo>> CHOCOBO = ENTITIES.register("chocobo", () -> register("chocobo", EntityType.Builder.of(Chocobo::new, MobCategory.CREATURE).sized(1.2f, 2.8f).canSpawnFarFromPlayer().clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Yellow>> YELLOW_SPAWNER_CHOCOBO = ENTITIES.register("yellow_spawner_chocobo", () -> register("yellow_spawner_chocobo", EntityType.Builder.of(Yellow::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Green>> GREEN_SPAWNER_CHOCOBO = ENTITIES.register("green_spawner_chocobo", () -> register("green_spawner_chocobo", EntityType.Builder.of(Green::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Blue>> BLUE_SPAWNER_CHOCOBO = ENTITIES.register("blue_spawner_chocobo", () -> register("blue_spawner_chocobo", EntityType.Builder.of(Blue::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<White>> WHITE_SPAWNER_CHOCOBO = ENTITIES.register("white_spawner_chocobo", () -> register("white_spawner_chocobo", EntityType.Builder.of(White::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Black>> BLACK_SPAWNER_CHOCOBO = ENTITIES.register("black_spawner_chocobo", () -> register("black_spawner_chocobo", EntityType.Builder.of(Black::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Gold>> GOLD_SPAWNER_CHOCOBO = ENTITIES.register("gold_spawner_chocobo", () -> register("gold_spawner_chocobo", EntityType.Builder.of(Gold::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Pink>> PINK_SPAWNER_CHOCOBO = ENTITIES.register("pink_spawner_chocobo", () -> register("pink_spawner_chocobo", EntityType.Builder.of(Pink::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Red>> RED_SPAWNER_CHOCOBO = ENTITIES.register("red_spawner_chocobo", () -> register("red_spawner_chocobo", EntityType.Builder.of(Red::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Purple>> PURPLE_SPAWNER_CHOCOBO = ENTITIES.register("purple_spawner_chocobo", () -> register("purple_spawner_chocobo", EntityType.Builder.of(Purple::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));
    public static final RegistryObject<EntityType<Flame>> FLAME_SPAWNER_CHOCOBO = ENTITIES.register("flame_spawner_chocobo", () -> register("flame_spawner_chocobo", EntityType.Builder.of(Flame::new, MobCategory.CREATURE).canSpawnFarFromPlayer().sized(1.2f, 2.8f).clientTrackingRange(64)));

    public static <T extends Entity> @NotNull EntityType<T> register(String id, EntityType.@NotNull Builder<T> builder) { return builder.build(id); }
}