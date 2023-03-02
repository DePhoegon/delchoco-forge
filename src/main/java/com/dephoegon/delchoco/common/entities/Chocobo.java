package com.dephoegon.delchoco.common.entities;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.ChocoConfig;
import com.dephoegon.delchoco.common.entities.breeding.ChocoboMateGoal;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.entities.properties.ChocoboGoals.*;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.entities.properties.MovementType;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModSounds;
import com.dephoegon.delchoco.common.inventory.SaddleBagContainer;
import com.dephoegon.delchoco.common.inventory.SaddleItemStackHandler;
import com.dephoegon.delchoco.common.items.ChocoboArmorItems;
import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import com.dephoegon.delchoco.common.items.ChocoboWeaponItems;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.common.network.packets.OpenChocoboGuiMessage;
import com.dephoegon.delchoco.utils.WorldUtils;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.dephoegon.delbase.item.shiftingDyes.*;
import static com.dephoegon.delchoco.aid.chocoKB.isAltDown;
import static com.dephoegon.delchoco.aid.dyeList.getDyeList;
import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.breeding.ChocoboSnap.setChocoScale;
import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.init.ModSounds.AMBIENT_SOUND;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnEggItem.*;
import static net.minecraft.world.entity.ai.attributes.Attributes.*;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.world.level.biome.Biome.getBiomeCategory;
import static net.minecraft.world.level.biome.Biomes.*;
import static net.minecraftforge.common.BiomeDictionary.hasType;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static net.minecraftforge.common.Tags.Biomes.*;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class Chocobo extends TamableAnimal implements NeutralMob {
    private static final String NBTKEY_CHOCOBO_COLOR = "Color";
    private static final String NBTKEY_CHOCOBO_IS_MALE = "Male";
    private static final String NBTKEY_CHOCOBO_FROM_EGG = "Egg";
    private static final String NBTKEY_MOVEMENT_TYPE = "MovementType";
    private static final String NBTKEY_SADDLE_ITEM = "Saddle";
    private static final String NBTKEY_WEAPON_ITEM = "Weapon";
    private static final String NBTKEY_ARMOR_ITEM = "Armor";
    private static final String NBTKEY_INVENTORY = "Inventory";
    private static final String NBTKEY_NEST_POSITION = "NestPos";
    private static final String NBTKEY_CHOCOBO_GENERATION = "Generation";
    private static final String NBTKEY_CHOCOBO_STAMINA = "Stamina";
    private static final String NBTKEY_CHOCOBO_FLAME_BLOOD = "FlameBlood";
    private static final String NBTKEY_CHOCOBO_WATER_BREATH = "WaterBreath";
    private static final String NBTKEY_CHOCOBO_COLLAR = "Collar";
    private static final String NBTKEY_CHOCOBO_WITHER_IMMUNE = "WitherImmune";
    private static final String NBTKEY_CHOCOBO_POISON_IMMUNE = "PoisonImmune";
    private static final String NBTKEY_CHOCOBO_SCALE = "Scale";
    private static final String NBTKEY_CHOCOBO_SCALE_MOD = "ScaleMod";
    private static final UUID CHOCOBO_CHEST_ARMOR_MOD_UUID = UUID.fromString("c03d8021-8839-4377-ac23-ed723ece6454");
    private static final UUID CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID = UUID.fromString("f7dcb185-7182-4a28-83ae-d1a2de9c022d");
    private static final UUID CHOCOBO_WEAPON_DAM_MOD_UUID = UUID.fromString("b9f0dc43-15a7-49f5-815c-915322c30402");
    private static final UUID CHOCOBO_WEAPON_SPD_MOD_UUID = UUID.fromString("46c84540-15f7-4f22-9da9-ebc23d2353af");
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable private UUID persistentAngerTarget;
    private int remainingPersistentAngerTime;
    private int ticksUntilNextAlert;
    private int timeToRecalculatePath;
    private final double followSpeedModifier = 2.0D;
    private AvoidEntityGoal chocoboAvoidPlayerGoal;
    private WaterAvoidingRandomStrollGoal roamAround;
    private Goal avoidBlocks;
    private float wingRotation;
    private float destPos;
    private boolean isChocoboJumping;
    private float wingRotDelta;
    private BlockPos nestPos;
    private boolean noRoam;
    public float followingMrHuman = 2;
    private final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);

    private static final EntityDataAccessor<ChocoboColor> PARAM_COLOR = SynchedEntityData.defineId(Chocobo.class, ModDataSerializers.CHOCOBO_COLOR);
    private static final EntityDataAccessor<Boolean> PARAM_IS_MALE = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARAM_FROM_EGG = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARAM_IS_FLAME_BLOOD = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARAM_IS_WATER_BREATH = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<MovementType> PARAM_MOVEMENT_TYPE = SynchedEntityData.defineId(Chocobo.class, ModDataSerializers.MOVEMENT_TYPE);
    private static final EntityDataAccessor<ItemStack> PARAM_SADDLE_ITEM = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> PARAM_WEAPON_ITEM = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> PARAM_ARMOR_ITEM = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> PARAM_COLLAR_COLOR = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Integer> PARAM_GENERATION = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Float> PARAM_STAMINA = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.FLOAT);
    private final static EntityDataAccessor<Byte> PARAM_ABILITY_MASK = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BYTE);
    private final static EntityDataAccessor<Boolean> PARAM_WITHER_IMMUNE = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private final static EntityDataAccessor<Boolean> PARAM_POISON_IMMUNE = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PARAM_SCALE = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> PARAM_SCALE_MOD = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.FLOAT);
    private static final UUID CHOCOBO_SPRINTING_BOOST_ID = UUID.fromString("03ba3167-393e-4362-92b8-909841047640");
    private final FollowOwnerGoal follow = new FollowOwnerGoal(this, followSpeedModifier, 4.0F, 30.0F, false);
    private static final AttributeModifier CHOCOBO_SPRINTING_SPEED_BOOST = (new AttributeModifier(CHOCOBO_SPRINTING_BOOST_ID, "Chocobo sprinting speed boost", 1, Operation.MULTIPLY_BASE));
    public static final int tier_one_chocobo_inv_slot_count = 15; // 3*5
    public static final int tier_two_chocobo_inv_slot_count = 45; //5*9
    private final int top_tier_chocobo_inv_slot_count = tier_two_chocobo_inv_slot_count;
    public final ItemStackHandler chocoboInventory = new ItemStackHandler(top_tier_chocobo_inv_slot_count){
        protected void onContentsChanged(int slot) {
            chocoboFeatherPick(chocoboInventory, tierOneItemStackHandler, slot);
            chocoboFeatherPick(chocoboInventory, tierTwoItemStackHandler, slot);
        }
    };
    public final ItemStackHandler tierOneItemStackHandler = new ItemStackHandler(tier_one_chocobo_inv_slot_count) {
        protected void onContentsChanged(int slot) { chocoboFeatherPick(tierOneItemStackHandler, chocoboInventory, slot); }
    };
    public final ItemStackHandler tierTwoItemStackHandler = new ItemStackHandler(tier_two_chocobo_inv_slot_count){
        protected void onContentsChanged(int slot) { chocoboFeatherPick(tierTwoItemStackHandler, chocoboInventory, slot); }
    };
    public final SaddleItemStackHandler chocoboArmorHandler = new SaddleItemStackHandler(){
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return stack.isEmpty() || stack.getItem() instanceof ChocoboArmorItems; }
        protected void onStackChanged() {
            Chocobo.this.setArmorType(this.itemStack);
            Chocobo.this.setChocoboArmorStats(this.itemStack);
        }
    };
    public final SaddleItemStackHandler chocoboWeaponHandler = new SaddleItemStackHandler() {
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return stack.isEmpty() || stack.getItem() instanceof ChocoboWeaponItems; }
        @Override
        protected void onStackChanged() {
            Chocobo.this.setWeaponType(this.itemStack);
            Chocobo.this.setChocoboWeaponStats(this.itemStack);
        }
    };
    public final SaddleItemStackHandler saddleItemStackHandler = new SaddleItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return stack.isEmpty() || stack.getItem() instanceof ChocoboSaddleItem; }

        @Override
        protected void onStackChanged() {
            SimpleContainer dropper = new SimpleContainer(chocoboInventory.getSlots());
            for (int i = 0; i < chocoboInventory.getSlots(); i++) {
                if (!(chocoboInventory.getStackInSlot(i).isEmpty())) {
                    dropper.setItem(i, chocoboInventory.getStackInSlot(i));
                    Containers.dropContents(level, Chocobo.this.getOnPos(), dropper);
                }
            }
            Chocobo.this.setSaddleType(this.itemStack);
        }
    };

    public Chocobo(EntityType<? extends Chocobo> type, Level world) { super(type, world); }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ChocoPanicGoal(this,1.5D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this,2F, true));
        // toggleable Goal 3, - Follow owner (whistle [tamed])
        this.goalSelector.addGoal(4, new ChocoboLavaEscape(this));
        // toggleable Goal 5, - Avoid Player Goal (non-tamed goal)
        // toggleable Goal 6, - Roam Around Goal (whistle toggle [tamed/non-tamed])
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.2D, Ingredient.of(GYSAHL_GREEN.get()), false));
        this.goalSelector.addGoal(8, new AvoidEntityGoal<>(this, Llama.class, 15F, 1.3F, 1.5F));
        this.goalSelector.addGoal(9, new ChocoboMateGoal(this, 1.0D));
        // toggleable Goal 10, - Avoid Blocks by Class<? extends Block>
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this)); // moved after Roam, a little too stationary
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, new ChocoboOwnerHurtByGoal(this));
        this.targetSelector.addGoal(2, new ChocoboOwnerHurtGoal(this));
        this.targetSelector.addGoal(3, (new ChocoboHurtByTargetGoal(this, Chocobo.class)).setAlertOthers(Chocobo.class));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Endermite.class, false));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Silverfish.class, false));
    }
    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(ModAttributes.MAX_STAMINA.get(),  ChocoConfig.COMMON.defaultStamina.get())
                .add(Attributes.MOVEMENT_SPEED, COMMON.defaultSpeed.get() / 100f)
                .add(MAX_HEALTH, ChocoConfig.COMMON.defaultHealth.get())
                .add(Attributes.ARMOR, COMMON.defaultArmor.get())
                .add(Attributes.ARMOR_TOUGHNESS, COMMON.defaultArmorToughness.get())
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.ATTACK_DAMAGE, COMMON.defaultAttackStrength.get())
                .add(Attributes.FOLLOW_RANGE, Attributes.FOLLOW_RANGE.getDefaultValue()*3);
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PARAM_IS_FLAME_BLOOD, false);
        this.entityData.define(PARAM_IS_WATER_BREATH, false);
        this.entityData.define(PARAM_WITHER_IMMUNE, false);
        this.entityData.define(PARAM_POISON_IMMUNE, false);
        this.entityData.define(PARAM_COLLAR_COLOR, 0);
        this.entityData.define(PARAM_COLOR, ChocoboColor.YELLOW);
        this.entityData.define(PARAM_IS_MALE, false);
        this.entityData.define(PARAM_FROM_EGG, false);
        this.entityData.define(PARAM_MOVEMENT_TYPE, MovementType.WANDER);
        this.entityData.define(PARAM_SADDLE_ITEM, ItemStack.EMPTY);
        this.entityData.define(PARAM_ARMOR_ITEM, ItemStack.EMPTY);
        this.entityData.define(PARAM_WEAPON_ITEM, ItemStack.EMPTY);
        this.entityData.define(PARAM_STAMINA, (float) COMMON.defaultStamina.get());
        this.entityData.define(PARAM_GENERATION, 0);
        this.entityData.define(PARAM_ABILITY_MASK, (byte) 0);
        this.entityData.define(PARAM_SCALE, 0);
        this.entityData.define(PARAM_SCALE_MOD, 1f);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setChocoboColor(ChocoboColor.values()[compound.getByte(NBTKEY_CHOCOBO_COLOR)]);
        this.setMale(compound.getBoolean(NBTKEY_CHOCOBO_IS_MALE));
        this.setFromEgg(compound.getBoolean(NBTKEY_CHOCOBO_FROM_EGG));
        this.setMovementType(MovementType.values()[compound.getByte(NBTKEY_MOVEMENT_TYPE)]);
        this.saddleItemStackHandler.deserializeNBT(compound.getCompound(NBTKEY_SADDLE_ITEM));
        this.chocoboWeaponHandler.deserializeNBT(compound.getCompound(NBTKEY_WEAPON_ITEM));
        this.chocoboArmorHandler.deserializeNBT(compound.getCompound(NBTKEY_ARMOR_ITEM));
        this.chocoboInventory.deserializeNBT(compound.getCompound(NBTKEY_INVENTORY));
        if (compound.contains(NBTKEY_NEST_POSITION)) { this.nestPos = NbtUtils.readBlockPos(compound.getCompound(NBTKEY_NEST_POSITION)); }
        this.setGeneration(compound.getInt(NBTKEY_CHOCOBO_GENERATION));
        this.setStamina(compound.getFloat(NBTKEY_CHOCOBO_STAMINA));
        this.setFlame(compound.getBoolean(NBTKEY_CHOCOBO_FLAME_BLOOD));
        this.setWaterBreath(compound.getBoolean(NBTKEY_CHOCOBO_WATER_BREATH));
        this.setWitherImmune(compound.getBoolean(NBTKEY_CHOCOBO_WITHER_IMMUNE));
        this.setPoisonImmune(compound.getBoolean(NBTKEY_CHOCOBO_POISON_IMMUNE));
        this.setChocoboScale(false, compound.getInt(NBTKEY_CHOCOBO_SCALE), true);
        this.setChocoboScaleMod(compound.getFloat(NBTKEY_CHOCOBO_SCALE_MOD));
        this.setCollarColor(compound.getInt(NBTKEY_CHOCOBO_COLLAR));
    }
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte(NBTKEY_CHOCOBO_COLOR, (byte) this.getChocoboColor().ordinal());
        compound.putBoolean(NBTKEY_CHOCOBO_IS_MALE, this.isMale());
        compound.putBoolean(NBTKEY_CHOCOBO_FROM_EGG, this.fromEgg());
        compound.putByte(NBTKEY_MOVEMENT_TYPE, (byte) this.getMovementType().ordinal());
        compound.put(NBTKEY_SADDLE_ITEM, this.saddleItemStackHandler.serializeNBT());
        compound.put(NBTKEY_ARMOR_ITEM, this.chocoboArmorHandler.serializeNBT());
        compound.put(NBTKEY_WEAPON_ITEM, this.chocoboWeaponHandler.serializeNBT());
        compound.put(NBTKEY_INVENTORY, this.chocoboInventory.serializeNBT());
        if (this.nestPos != null) { compound.put(NBTKEY_NEST_POSITION, NbtUtils.writeBlockPos(this.nestPos)); }
        compound.putInt(NBTKEY_CHOCOBO_GENERATION, this.getGeneration());
        compound.putBoolean(NBTKEY_CHOCOBO_FLAME_BLOOD, this.fireImmune());
        compound.putBoolean(NBTKEY_CHOCOBO_WATER_BREATH, this.isWaterBreather());
        compound.putBoolean(NBTKEY_CHOCOBO_WITHER_IMMUNE, this.isWitherImmune());
        compound.putBoolean(NBTKEY_CHOCOBO_POISON_IMMUNE, this.isPoisonImmune());
        compound.putInt(NBTKEY_CHOCOBO_SCALE, this.getChocoboScale());
        compound.putFloat(NBTKEY_CHOCOBO_SCALE_MOD, this.getChocoboScaleMod());
        compound.putFloat(NBTKEY_CHOCOBO_STAMINA, this.getStamina());
        compound.putInt(NBTKEY_CHOCOBO_COLLAR, this.getCollarColor());
    }
    private @NotNull ArrayList<ResourceKey<Biome>> whiteChocobo() {
        ArrayList<ResourceKey<Biome>> out = new ArrayList<>();
        out.add(OLD_GROWTH_BIRCH_FOREST);
        out.add(BIRCH_FOREST);
        return out;
    }
    private @NotNull ArrayList<ResourceKey<Biome>> blueChocobo() {
        ArrayList<ResourceKey<Biome>> out = new ArrayList<>();
        out.add(WARM_OCEAN);
        out.add(RIVER);
        return out;
    }
    private @NotNull ArrayList<ResourceKey<Biome>> greenChocobo() {
        ArrayList<ResourceKey<Biome>> out = new ArrayList<>();
        out.add(JUNGLE);
        out.add(BAMBOO_JUNGLE);
        out.add(SWAMP);
        out.add(LUSH_CAVES);
        out.add(DRIPSTONE_CAVES);
        return out;
    }
    public int ChocoboShaker(@NotNull String stat) {
        return switch (stat) {
            case "health" -> boundedRangeModifier(5, 10);
            case "attack", "toughness" -> boundedRangeModifier(1, 3);
            case "defense" -> boundedRangeModifier(2, 4);
            default -> 0;
        };
    }
    private void chocoboStatShake(Attribute attribute, String text) {
        int aValue = ChocoboShaker(text);
        this.getAttribute(attribute).addPermanentModifier(new AttributeModifier(text+" variance", aValue, Operation.ADDITION));
    }
    private int boundedRangeModifier(int lower, int upper) {
        int range = lower+upper;
        return random.nextInt(range)-lower;
    }
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setMale(this.level.random.nextBoolean());

        final Holder<Biome> currentBiomes = this.level.getBiome(blockPosition().below());
        BiomeCategory biomeCategory = getBiomeCategory(currentBiomes);
        //noinspection OptionalGetWithoutIsPresent
        final ResourceKey<Biome> BiomesKey = currentBiomes.unwrapKey().get();
        ChocoboColor color;
        if (!fromEgg()) {
            setChocoboSpawnCheck(ChocoboColor.YELLOW);
            if (!currentBiomes.containsTag(IS_END) && !currentBiomes.containsTag(IS_OVERWORLD)) { setChocoboSpawnCheck(ChocoboColor.FLAME); }
            if (currentBiomes.containsTag(IS_END)) { setChocoboSpawnCheck(ChocoboColor.PURPLE); }
            if (hasType(BiomesKey, BiomeDictionary.Type.MUSHROOM)) { setChocoboSpawnCheck(ChocoboColor.PINK); }
            if (currentBiomes.containsTag(IS_SNOWY) || whiteChocobo().contains(BiomesKey)) { setChocoboSpawnCheck(ChocoboColor.WHITE); }
            if (blueChocobo().contains(BiomesKey)) { setChocoboSpawnCheck(ChocoboColor.BLUE); }
            if (biomeCategory == BiomeCategory.FOREST || biomeCategory == BiomeCategory.MESA) { setChocoboSpawnCheck(ChocoboColor.RED); }
            if (greenChocobo().contains(BiomesKey)) { setChocoboSpawnCheck(ChocoboColor.GREEN); }
            if (currentBiomes.containsTag(IS_HOT_OVERWORLD) && !currentBiomes.containsTag(IS_SAVANNA)) { setChocoboSpawnCheck(ChocoboColor.BLACK); }
            this.setChocoboScale(this.isMale(), 0, false);
        }
        chocoboStatShake(MAX_HEALTH, "health");
        chocoboStatShake(ATTACK_DAMAGE, "attack");
        chocoboStatShake(ARMOR, "defense");
        chocoboStatShake(ARMOR_TOUGHNESS, "toughness");
        if (getChocoboColor() == ChocoboColor.PURPLE) {
            int chance = currentBiomes.containsTag(IS_END) ? 60 : 15;
            if (random.nextInt(100)+1 < chance) {
                this.chocoboInventory.setStackInSlot(random.nextInt(18), new ItemStack(ENDER_PEARL.getDefaultInstance().split(random.nextInt(3) + 1).getItem()));
            }
            if (random.nextInt(100)+1 < chance) {
                this.chocoboInventory.setStackInSlot(random.nextInt(9)+18, new ItemStack(ENDER_PEARL.getDefaultInstance().split(random.nextInt(3) + 1).getItem()));
            }
            if (random.nextInt(100)+1 < chance) {
                this.chocoboInventory.setStackInSlot(random.nextInt(18)+27, new ItemStack(ENDER_PEARL.getDefaultInstance().split(random.nextInt(3) + 1).getItem()));
            }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    private void setChocobo(ChocoboColor color, boolean flame, boolean water, boolean wither, boolean poison) {
        this.setChocoboColor(color);
        this.setFlame(flame);
        this.setWaterBreath(water);
        this.setWitherImmune(wither);
        this.setPoisonImmune(poison);
    }
    private void setChocoboSpawnCheck(ChocoboColor color) {
        ChocoboColor chocobo = this.getChocoboColor();
        if ((chocobo == ChocoboColor.YELLOW || color == ChocoboColor.YELLOW) && chocobo != color) { setChocobo(color, color == ChocoboColor.FLAME, wbChocobos().contains(color), wiChocobos().contains(color), piChocobos().contains(color)); }
    }
    @Override
    public boolean canBeControlledByRider() { return this.isTame(); }
    private void setArmor(ItemStack pStack) {
        this.setItemSlot(EquipmentSlot.CHEST, pStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }
    private void setWeapon(ItemStack pStack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, pStack);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }
    public boolean isChocoboArmor(@NotNull ItemStack pStack) { return pStack.getItem() instanceof ChocoboArmorItems; }
    public boolean isChocoWeapon(@NotNull ItemStack pStack) { return pStack.getItem() instanceof ChocoboWeaponItems; }
    public int chocoStatMod() { return COMMON.modifier.get(); }
    private void setChocoboArmorStats(ItemStack pStack) {
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(CHOCOBO_CHEST_ARMOR_MOD_UUID);
            this.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID);
            if (this.isChocoboArmor(pStack)) {
                this.setDropChance(((ChocoboArmorItems)pStack.getItem()).getSlot(), 0.0F);
                int p = ((ChocoboArmorItems)pStack.getItem()).getDefense()*chocoStatMod();
                float t = ((ChocoboArmorItems)pStack.getItem()).getToughness()*chocoStatMod();
                if (p != 0) { this.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(CHOCOBO_CHEST_ARMOR_MOD_UUID, "Chocobo Armor Bonus", p, Operation.ADDITION)); }
                if (t != 0) { this.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(new AttributeModifier(CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID, "Chocobo Armor Toughness", t, Operation.ADDITION)); }
                this.setArmor(pStack);
            }
        }
    }
    private void setChocoboWeaponStats(ItemStack pStack) {
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CHOCOBO_WEAPON_DAM_MOD_UUID);
            this.getAttribute(Attributes.ATTACK_SPEED).removeModifier(CHOCOBO_WEAPON_SPD_MOD_UUID);
            if (this.isChocoWeapon(pStack)) {
                double a = ((ChocoboWeaponItems)pStack.getItem()).getDamage()*chocoStatMod();
                float s = ((ChocoboWeaponItems)pStack.getItem()).getAttackSpeed()*chocoStatMod();
                if (a != 0) { this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(CHOCOBO_WEAPON_DAM_MOD_UUID, "Chocobo Attack Bonus", a, Operation.ADDITION)); }
                if (s != 0) { this.getAttribute(Attributes.ATTACK_SPEED).addPermanentModifier(new AttributeModifier(CHOCOBO_WEAPON_SPD_MOD_UUID, "Chocobo Attack Speed Bonus", s, Operation.ADDITION)); }
                this.setWeapon(pStack);
            }
        }
    }
    public ChocoboColor getChocoboColor() { return this.entityData.get(PARAM_COLOR); }
    public void setChocoboColor(ChocoboColor color) { this.entityData.set(PARAM_COLOR, color); }
    public void setCollarColor(Integer color) { this.entityData.set(PARAM_COLLAR_COLOR, color); }
    public Integer getCollarColor() { return this.entityData.get(PARAM_COLLAR_COLOR); }
    @Override
    public boolean fireImmune() { return this.entityData.get(PARAM_IS_FLAME_BLOOD); }
    public void setFlame(boolean flame) { this.entityData.set(PARAM_IS_FLAME_BLOOD, flame); }
    public void setWaterBreath(boolean waterBreath) { this.entityData.set(PARAM_IS_WATER_BREATH, waterBreath); }
    public void setWitherImmune(boolean witherImmune) { this.entityData.set(PARAM_WITHER_IMMUNE, witherImmune); }
    public void setPoisonImmune(boolean poisonImmune) { this.entityData.set(PARAM_POISON_IMMUNE, poisonImmune); }
    public void setChocoboScale(boolean isMale, int overrideValue, boolean override) {
        int scale;
        if (override) { scale = overrideValue; } else { scale = setChocoScale(isMale); }
        this.setChocoboScaleMod(ScaleMod(scale));
        this.entityData.set(PARAM_SCALE, scale);
    }
    public void setChocoboScaleMod(float value) {
        this.entityData.set(PARAM_SCALE_MOD, value);
    }
    public boolean nonFlameFireImmune() { return fireImmune() && ChocoboColor.FLAME != getChocoboColor(); }
    public boolean isWaterBreather() { return this.entityData.get(PARAM_IS_WATER_BREATH); }
    public boolean isWitherImmune() { return this.entityData.get(PARAM_WITHER_IMMUNE); }
    public boolean isPoisonImmune() { return this.entityData.get(PARAM_POISON_IMMUNE); }
    public int getChocoboScale() { return this.entityData.get(PARAM_SCALE); }
    public float getChocoboScaleMod() { return this.entityData.get(PARAM_SCALE_MOD); }
    public float ScaleMod(int scale) {
        return (scale == 0) ? 0 : ((scale < 0) ? (((float) ((scale * -1) - 100) / 100) * -1) : (1f + ((float) scale / 100)));
    }
    public boolean canBeAffected(@NotNull MobEffectInstance potionEffect) {
        if (potionEffect.getEffect() == MobEffects.WITHER) return !this.isWitherImmune();
        if (potionEffect.getEffect() == MobEffects.POISON) return !this.isPoisonImmune();
        return super.canBeAffected(potionEffect);
    }
    public boolean isMale() { return this.entityData.get(PARAM_IS_MALE); }
    public boolean fromEgg() { return this.entityData.get(PARAM_FROM_EGG); }
    public void setMale(boolean isMale) { this.entityData.set(PARAM_IS_MALE, isMale); }
    public void setFromEgg(boolean fromEgg) { this.entityData.set(PARAM_FROM_EGG, fromEgg); }
    public MovementType getMovementType() { return this.entityData.get(PARAM_MOVEMENT_TYPE); }
    public void setMovementType(MovementType type) { this.entityData.set(PARAM_MOVEMENT_TYPE, type); }
    public boolean isSaddled() { return !this.getSaddle().isEmpty(); }
    public boolean isArmored() { return !this.getArmor().isEmpty(); }
    public boolean isArmed() { return !this.getWeapon().isEmpty(); }
    public ItemStack getSaddle() { return this.entityData.get(PARAM_SADDLE_ITEM); }
    public ItemStack getWeapon() { return this.entityData.get(PARAM_WEAPON_ITEM); }
    public ItemStack getArmor() { return this.entityData.get(PARAM_ARMOR_ITEM); }
    private void setSaddleType(@NotNull ItemStack saddleStack) {
        ItemStack oldStack = getSaddle();
        if (oldStack.getItem() != saddleStack.getItem()) { this.entityData.set(PARAM_SADDLE_ITEM, saddleStack.copy()); }
    }
    private void setWeaponType(@NotNull ItemStack weaponType) {
        ItemStack oldStack = getWeapon();
        if (oldStack.getItem() != weaponType.getItem()) { this.entityData.set(PARAM_WEAPON_ITEM, weaponType.copy()); }
    }
    private void setArmorType(@NotNull ItemStack armorType) {
        ItemStack oldStack = getArmor();
        if (oldStack.getItem() != armorType.getItem()) { this.entityData.set(PARAM_ARMOR_ITEM, armorType.copy()); }
    }
    public boolean rideableUnderWater() { return this.canBreatheUnderwater(); }
    public boolean canBreatheUnderwater() { return this.isWaterBreather(); }
    public float getStamina() { return this.entityData.get(PARAM_STAMINA); }
    public void setStamina(float value) { this.entityData.set(PARAM_STAMINA, value); }
    public float getStaminaPercentage() { return (float) (this.getStamina() / this.getAttribute(ModAttributes.MAX_STAMINA.get()).getValue()); }
    public int getGeneration() { return this.entityData.get(PARAM_GENERATION); }
    public String getGenerationString() {
        int gen = this.getGeneration();
        return Integer.toString(gen);
    }
    public void setGeneration(int value) { this.entityData.set(PARAM_GENERATION, value); }
    private boolean useStamina(float value) {
        if (value == 0) return true;
        float curStamina = this.entityData.get(PARAM_STAMINA);
        if (curStamina < value) return false;

        float maxStamina = (float) this.getAttribute(ModAttributes.MAX_STAMINA.get()).getValue();
        float newStamina = Mth.clamp(curStamina - value, 0, maxStamina);
        this.entityData.set(PARAM_STAMINA, newStamina);
        return true;
    }
    @Override
    public double getPassengersRidingOffset() {
        double scaleZero = this.getChocoboScale() == 0 ? 1.65D : this.getChocoboScale() < 0 ? 1.55D : 1.75D;
        return this.getChocoboScale() == 0 ? scaleZero : scaleZero * this.getChocoboScaleMod();
    }
    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Nullable
    public Entity getControllingPassenger() { return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0); }
    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        this.fluidHeight.clear();
        this.updateInWaterStateAndDoWaterCurrentPushing();
        boolean flag = this.updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, 0.085D);
        return this.isInWater() || flag;
    }
    private void updateInWaterStateAndDoWaterCurrentPushing() {
        if (!this.isWaterBreather()) {
            if (this.getVehicle() instanceof Chocobo) { this.wasTouchingWater = false; }
            else if (this.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014D)) {
                if (!this.wasTouchingWater && !this.firstTick) { this.doWaterSplashEffect(); }
                this.fallDistance = 0.0F;
                this.wasTouchingWater = true;
                this.clearFire();
            } else { this.wasTouchingWater = false; }
        } else {
            if (this.isInWater()) {
                this.wasTouchingWater = false;
                this.clearFire();
                if (this.getVehicle() instanceof Chocobo) { if (this.getControllingPassenger() instanceof Player rider) { rider.clearFire(); } }
            }
        }
    }
    @Override
    public void travel(@NotNull Vec3 travelVector) {
        Vec3 newVector = travelVector;
        if (this.getControllingPassenger() instanceof Player rider) {
            this.yRotO = rider.getYRot();
            this.xRotO = rider.getXRot();
            this.setYRot(rider.getYRot());
            this.setXRot(rider.getXRot());
            this.setRot(this.getYRot(), this.getXRot());
            this.yHeadRot = this.getYRot();
            this.yBodyRot = this.getYRot();

            newVector = new Vec3(rider.xxa * 0.5F, newVector.y, rider.zza); //Strafe - Vertical - Forward

            // reduce movement speed by 75% if moving backwards
            if (newVector.z() <= 0.0D)
                newVector = new Vec3(newVector.x, newVector.y, newVector.z * 0.25F);

            if (this.onGround) { this.isChocoboJumping = false; }

            if (this.isControlledByLocalInstance()) {
                if (Minecraft.getInstance().options.keyJump.isDown()) {
                    // jump logic
                    if (!this.isChocoboJumping && this.onGround && this.useStamina(COMMON.jumpStaminaCost.get().floatValue())) {
                        Vec3 motion = getDeltaMovement();
                        setDeltaMovement(new Vec3(motion.x, .6f, motion.z));
                        this.isChocoboJumping = true;
                    }
                }
                if (rider.isInWater()) {
                    Vec3 motion = getDeltaMovement();
                    if (Minecraft.getInstance().options.keyJump.isDown()) { setDeltaMovement(new Vec3(motion.x, .5f, motion.z)); }
                    else if (this.getDeltaMovement().y < 0 && !this.isWaterBreather()) {
                        int distance = WorldUtils.getDistanceToSurface(this.blockPosition(), this.getCommandSenderWorld());
                        if (distance > 0) { setDeltaMovement(new Vec3(motion.x, .05f, motion.z)); }
                    } else if (this.isWaterBreather() && isAltDown()) {
                        Vec3 waterMotion = getDeltaMovement();
                        setDeltaMovement(new Vec3(waterMotion.x, waterMotion.y * 0.65F, waterMotion.z));
                    }
                }
                if (rider.isInLava()) {
                    Vec3 motion = getDeltaMovement();
                    if (Minecraft.getInstance().options.keyJump.isDown()) { setDeltaMovement(new Vec3(motion.x, .5f, motion.z)); }
                    else if (this.fireImmune() && this.getDeltaMovement().y < 0) {
                        int distance = WorldUtils.getDistanceToSurface(this.blockPosition(), this.getCommandSenderWorld());
                        if (distance > 0) { setDeltaMovement(new Vec3(motion.x, .05f, motion.z)); }
                    }
                }
                // Insert override for slow-fall Option on Chocobo
                if (!this.onGround && !this.isInWater() && !this.isInLava() && !rider.isShiftKeyDown() && this.getDeltaMovement().y < 0 &&
                    this.useStamina(COMMON.glideStaminaCost.get().floatValue())) {
                    if (Minecraft.getInstance().options.keyJump.isDown()) {
                        Vec3 motion = getDeltaMovement();
                        setDeltaMovement(new Vec3(motion.x, motion.y * 0.65F, motion.z));
                    }
                }
                if ((this.isSprinting() && !this.useStamina(COMMON.sprintStaminaCost.get().floatValue())) || (this.isSprinting() && this.isInWater() && this.useStamina(COMMON.sprintStaminaCost.get().floatValue()))) { this.setSprinting(false); }

                this.setSpeed((float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                super.travel(newVector);
            }
        } else {
            if (!this.onGround && !this.isInWater() && !this.isInLava() && this.getDeltaMovement().y < 0 && this.useStamina(COMMON.glideStaminaCost.get().floatValue())) {
                Vec3 motion = getDeltaMovement();
                setDeltaMovement(new Vec3(motion.x, motion.y * 0.65F, motion.z));
            }
            double y = newVector.y;
            if (y > 0) y = y * -1;
            Vec3 cappedNewVector = new Vec3(newVector.x, y, newVector.z);
            super.travel(cappedNewVector);
        }
    }
    @Override
    public void positionRider(@NotNull Entity passenger) {
        super.positionRider(passenger);
        if (passenger instanceof Mob && this.getControllingPassenger() == passenger) { this.yBodyRot = ((LivingEntity) passenger).yBodyRot; }
    }
    private int rideTickDelay = 0;
    public void tick() {
        super.tick();
        floatChocobo();
        LivingEntity owner = this.getOwner() != null ? this.getOwner() : null;
        if (this.rideTickDelay < 0) {
            Entity RidingPlayer = this.getControllingPassenger();
            if (RidingPlayer != null) {
                this.rideTickDelay = 5;
                if (RidingPlayer instanceof Player player) {
                    this.setInvulnerable(player.isCreative());
                    if (this.getHealth() != this.getMaxHealth() && player.getOffhandItem().getItem() == GYSAHL_GREEN_ITEM.get()) {
                        player.getOffhandItem().shrink(1);
                        heal(COMMON.defaultHealAmmount.get());
                    }
                } else { this.setInvulnerable(false); }
            } else {
                this.setInvulnerable(false);
                this.rideTickDelay = 30;
            }
        } else { this.rideTickDelay--; }
        if (owner != null) {
            if (followingMrHuman == 1) {
                if (--this.timeToRecalculatePath <= 0) {
                    this.getLookControl().setLookAt(owner, 10.0F, (float) this.getMaxHeadXRot());
                    if (--this.timeToRecalculatePath <= 0) {
                        this.timeToRecalculatePath = this.randomIntInclusive(10, 40);
                        if (this.distanceToSqr(owner) >= 144.0D) { this.teleportToOwner(owner);}
                        else { this.navigation.moveTo(owner, this.followSpeedModifier); }
        }   }   }   }
    }
    public double getFollowSpeedModifier() { return this.followSpeedModifier; }
    public int getRideTickDelay() { return this.rideTickDelay; }
    private void floatChocobo() {
        if (this.isInLava()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level.getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)) { this.onGround = true; }
            else { this.setDeltaMovement(this.getDeltaMovement().scale(.003D).add(0.0D, 0.05D, 0.0D)); }
        }
        if (this.isInWater() && !this.isWaterBreather()) {
            CollisionContext collisioncontext = CollisionContext.of(this);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) && !this.level.getFluidState(this.blockPosition().above()).is(FluidTags.WATER)) { this.onGround = true; }
            else { this.setDeltaMovement(this.getDeltaMovement().scale(.003D).add(0.0D, 0.05D, 0.0D)); }
        }
    }
    public float getWalkTargetValue(@NotNull BlockPos pPos, @NotNull LevelReader pLevel) {
        if (pLevel.getBlockState(pPos).getFluidState().is(FluidTags.LAVA)) { return (float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 10.0F);}
        else if (pLevel.getBlockState(pPos).getFluidState().is(FluidTags.WATER)) { return (float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 10.0F); }
        else { return (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue(); }
    }
    private boolean maybeTeleportTo(int pX, int pY, int pZ, @NotNull LivingEntity owner) {
        if (Math.abs((double)pX - owner.getX()) < 2.0D && Math.abs((double)pZ - owner.getZ()) < 2.0D) { return false; }
        else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) { return false; }
        else {
            this.moveTo((double)pX + 0.5D, pY, (double)pZ + 0.5D, this.getYRot(), this.getXRot());
            this.navigation.stop();
            return true;
        }
    }
    private void teleportToOwner(@NotNull LivingEntity owner) {
        BlockPos blockpos = owner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l, owner);
            if (flag) { return; }
        }
    }
    private boolean canTeleportTo(@NotNull BlockPos pPos) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pPos.mutable());
        if (blockpathtypes != BlockPathTypes.WALKABLE) { return false; }
        else {
            BlockPos blockpos = pPos.subtract(this.blockPosition());
            return this.level.noCollision(this, this.getBoundingBox().move(blockpos));
        }
    }
    private int randomIntInclusive(int pMin, int pMax) { return this.getRandom().nextInt(pMax - pMin + 1) + pMin; }
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob mate) { return null; }
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this || !(otherAnimal instanceof Chocobo otherChocobo)) return false;
        if (!this.isInLove() || !otherAnimal.isInLove()) return false;
        return otherChocobo.isMale() != this.isMale();
    }
    public void setSprinting(boolean sprinting) {
        this.setSharedFlag(3, sprinting);
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance.getModifier(CHOCOBO_SPRINTING_BOOST_ID) != null) { attributeInstance.removeModifier(CHOCOBO_SPRINTING_SPEED_BOOST); }
        if (sprinting) { attributeInstance.addTransientModifier(CHOCOBO_SPRINTING_SPEED_BOOST); }
    }
    public void dropFeather() {
        if (this.getCommandSenderWorld().isClientSide) { return; }
        if (this.isBaby()) { return; }
        this.spawnAtLocation(new ItemStack(CHOCOBO_FEATHER.get(), 1), 0.0F);
    }
    public int TimeSinceFeatherChance = 0;
    protected boolean canRide(@NotNull Entity entityIn) { return !this.getSaddle().isEmpty() && super.canRide(entityIn); }
    public void aiStep() {
        super.aiStep();
        this.setRot(this.getYRot(), this.getXRot());
        this.regenerateStamina();
        this.maxUpStep = 1f;
        this.fallDistance = 0f;

        if (this.TimeSinceFeatherChance == 3000) {
            this.TimeSinceFeatherChance = 0;
            if ((float) Math.random() < .25) { this.dropFeather(); }
        } else { this.TimeSinceFeatherChance++; }

        //Change effects to chocobo colors
        if (!this.getCommandSenderWorld().isClientSide) {
            if (this.tickCount % 60 == 0) {
                if (this.fireImmune()) {
                    this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
                    if (this.isVehicle()) {
                        Entity controller = this.getControllingPassenger();
                        if (controller instanceof Player) { ((Player) controller).addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false)); }
                    }
                }
                if (this.isWaterBreather()) {
                    this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, false));
                    if (this.isVehicle()) {
                        Entity controller = this.getControllingPassenger();
                        if (controller instanceof Player) { ((Player) controller).addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, false)); }
                    }
                }
            }
        } else {
            // Wing rotations, control packet, client side
            // Client side
            this.destPos += (double) (this.onGround ? -1 : 4) * 0.3D;
            this.destPos = Mth.clamp(destPos, 0f, 1f);

            if (!this.onGround) { this.wingRotDelta = Math.min(wingRotation, 1f); }
            this.wingRotDelta *= 0.9D;
            this.wingRotation += this.wingRotDelta * 2.0F;

            if (this.onGround) {
                this.animationSpeedOld = this.animationSpeed;
                double d1 = this.getX() - this.xo;
                double d0 = this.getZ() - this.zo;
                float f4 = ((float)Math.sqrt(d1 * d1 + d0 * d0)) * 4.0F;
                if (f4 > 1.0F) { f4 = 1.0F; }
                this.animationSpeed += (f4 - this.animationSpeed) * 0.4F;
                this.animationPosition += this.animationSpeed;
            } else {
                this.animationPosition = 0;
                this.animationSpeed = 0;
                this.animationSpeedOld = 0;
            }
        }
    }
    private void regenerateStamina() {
        if (!this.onGround && !this.isSprinting()) { return; }
        float regen = COMMON.staminaRegenRate.get().floatValue();

        // half the amount of regeneration while moving
        Vec3 motion = getDeltaMovement();
        if (motion.x != 0 || motion.z != 0) { regen *= 0.85; }

        // TODO: implement regen bonus (another IAttribute?)
        this.useStamina(-regen);
    }
    public boolean isFood(@NotNull ItemStack stack) { return false; }
    private final Map<Item, Integer> COLLAR_COLOR = Util.make(Maps.newHashMap(), (map) ->{
        map.put(CLEANSE_SHIFT_DYE.get().asItem(), 0);
        map.put(RED_SHIFT_DYE.get().asItem(), 16);
        map.put(RED_DYE.asItem(), 16);
        map.put(BLOOD_SHIFT_DYE.get().asItem(), 16);
        map.put(WHITE_SHIFT_DYE.get().asItem(), 15);
        map.put(WHITE_DYE.asItem(), 15);
        map.put(ORANGE_SHIFT_DYE.get().asItem(), 14);
        map.put(ORANGE_DYE.asItem(), 14);
        map.put(MAGENTA_SHIFT_DYE.get().asItem(), 13);
        map.put(MAGENTA_DYE.asItem(), 13);
        map.put(LIGHT_BLUE_SHIFT_DYE.get().asItem(), 12);
        map.put(LIGHT_BLUE_DYE.asItem(), 12);
        map.put(YELLOW_SHIFT_DYE.get().asItem(), 11);
        map.put(YELLOW_DYE.asItem(), 11);
        map.put(LIME_SHIFT_DYE.get().asItem(), 10);
        map.put(LIME_DYE.asItem(), 10);
        map.put(PINK_SHIFT_DYE.get().asItem(), 9);
        map.put(PINK_DYE.asItem(), 9);
        map.put(GRAY_SHIFT_DYE.get().asItem(), 8);
        map.put(GRAY_DYE.asItem(), 8);
        map.put(LIGHT_GRAY_SHIFT_DYE.get().asItem(), 7);
        map.put(LIGHT_GRAY_DYE.asItem(), 7);
        map.put(CYAN_SHIFT_DYE.get().asItem(), 6);
        map.put(CYAN_DYE.asItem(), 6);
        map.put(PURPLE_SHIFT_DYE.get().asItem(), 5);
        map.put(PURPLE_DYE.asItem(), 5);
        map.put(BLUE_SHIFT_DYE.get().asItem(), 4);
        map.put(BLUE_DYE.asItem(), 4);
        map.put(GREEN_SHIFT_DYE.get().asItem(), 3);
        map.put(GREEN_DYE.asItem(), 3);
        map.put(BROWN_SHIFT_DYE.get().asItem(), 2);
        map.put(BROWN_DYE.asItem(), 2);
        map.put(BLACK_SHIFT_DYE.get().asItem(), 1);
        map.put(BLACK_DYE.asItem(), 1);
    });
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        ItemStack heldItemStack = player.getItemInHand(hand);
        Item defaultHand = heldItemStack.getItem();
        if (this.isTame()) {
            if (getDyeList().contains(defaultHand)) {
                if (!Objects.equals(this.getCollarColor(), COLLAR_COLOR.get(defaultHand))) {
                    this.setCollarColor(COLLAR_COLOR.get(defaultHand));
                    this.usePlayerItem(player, hand, heldItemStack);
                }
            }
            if (defaultHand == GYSAHL_CAKE.get().asItem() && this.isBaby()) {
                this.usePlayerItem(player, hand, heldItemStack);
                ageBoundaryReached();
                return InteractionResult.SUCCESS;
            }
            if (player.isShiftKeyDown() && !this.isBaby()) {
                if (player instanceof ServerPlayer) { this.displayChocoboInventory((ServerPlayer) player); }
                return InteractionResult.SUCCESS;
            }
            if (heldItemStack.isEmpty() && !player.isShiftKeyDown() && !this.isBaby() && this.isSaddled()) {
                if (COMMON.ownerOnlyAccess.get()) {
                    if (isOwnedBy(player)) { player.startRiding(this); }
                    else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true); }
                } else { player.startRiding(this); }
                return InteractionResult.SUCCESS;
            }
            if (defaultHand == GYSAHL_GREEN_ITEM.get()) {
                if (getHealth() != getMaxHealth()) {
                    this.usePlayerItem(player, hand, player.getInventory().getSelected());
                    heal(COMMON.defaultHealAmmount.get());
                } else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.heal_fail"), true); }
            }
            if (defaultHand == CHOCOBO_WHISTLE.get() && !this.isBaby()) {
                if (isOwnedBy(player)) {
                    if (this.followingMrHuman == 3) {
                        this.playSound(ModSounds.WHISTLE_SOUND_FOLLOW.get(), 1.0F, 1.0F);
                        this.setNoAi(false);
                        if (noRoam) {
                            this.goalSelector.addGoal(6, this.roamAround);
                            this.goalSelector.addGoal(10, this.avoidBlocks);
                            noRoam = false;
                        }
                        this.goalSelector.addGoal(3, this.follow);
                        followingMrHuman = 1;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_follow_cmd"), true);
                    } else if (this.followingMrHuman == 1) {
                        this.playSound(ModSounds.WHISTLE_SOUND_WANDER.get(), 1.0F, 1.0F);
                        this.goalSelector.removeGoal(this.follow);
                        followingMrHuman = 2;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_wander_cmd"), true);
                    } else if (this.followingMrHuman == 2) {
                        this.playSound(ModSounds.WHISTLE_SOUND_STAY.get(), 1.0F, 1.0F);
                        // this.setNoAi(true);
                        if (!noRoam) {
                            this.goalSelector.removeGoal(roamAround);
                            this.goalSelector.removeGoal(avoidBlocks);
                            noRoam = true;
                        }
                        followingMrHuman = 3;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_stay_cmd"), true);
                    }
                } else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true); }
                return InteractionResult.SUCCESS;
            }
            if (!this.isInLove() && defaultHand == LOVELY_GYSAHL_GREEN.get() && !this.isBaby()) {
                this.usePlayerItem(player, hand, player.getInventory().getSelected());
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            }
            if (defaultHand instanceof ChocoboSaddleItem && !this.isSaddled() && !this.isBaby()) {
                this.setSaddleType(heldItemStack);
                this.saddleItemStackHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                this.usePlayerItem(player, hand, heldItemStack);
                return InteractionResult.SUCCESS;
            }
            if (defaultHand instanceof ChocoboArmorItems && !this.isArmored() && !this.isBaby()) {
                if (this.chocoboArmorHandler.getStackInSlot(0).isEmpty()) {
                    this.chocoboArmorHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                    this.usePlayerItem(player, hand, heldItemStack);
                }
                return InteractionResult.SUCCESS;
            }
            if (defaultHand instanceof ChocoboWeaponItems && !this.isArmed() && !this.isBaby()) {
                if (this.chocoboWeaponHandler.getStackInSlot(0).isEmpty()) {
                    this.chocoboWeaponHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                    this.usePlayerItem(player, hand, heldItemStack);
                }
                return InteractionResult.SUCCESS;
            }
            if (defaultHand == Items.NAME_TAG) {
                if (COMMON.ownerOnlyAccess.get()) {
                    if (isOwnedBy(player)) {
                        this.setCustomName(heldItemStack.getHoverName());
                        this.setCustomNameVisible(true);
                        this.usePlayerItem(player, hand, heldItemStack);
                    } else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true); }
                } else {
                    this.setCustomName(heldItemStack.getHoverName());
                    this.setCustomNameVisible(true);
                    this.usePlayerItem(player, hand, heldItemStack);
                }
                return InteractionResult.SUCCESS;
            }
            if (defaultHand == CHOCOBO_FEATHER.get().asItem()) {
                if (isOwnedBy(player)) { this.setCustomNameVisible(!this.isCustomNameVisible()); }
                else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true); }
                return InteractionResult.SUCCESS;
            }
        } else {
            if (defaultHand == GYSAHL_GREEN_ITEM.get()) {
                this.usePlayerItem(player, hand, player.getInventory().getSelected());
                if ((float) Math.random() < COMMON.tameChance.get().floatValue() || player.isCreative()) {
                    this.setOwnerUUID(player.getUUID());
                    this.setTame(true);
                    this.setCollarColor(16);
                    player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.tame_success"), true);
                } else { player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.tame_fail"), true); }
                return InteractionResult.SUCCESS;
            }
            if (defaultHand == Items.NAME_TAG) {
                this.setCustomName(heldItemStack.getHoverName());
                this.setCustomNameVisible(true);
                this.usePlayerItem(player, hand, heldItemStack);
                return InteractionResult.SUCCESS;
            }
        }
        if (this.getCommandSenderWorld().isClientSide) return InteractionResult.SUCCESS;
        return super.interactAt(player, vec, hand);
    }
    private void displayChocoboInventory(@NotNull ServerPlayer player) {
        if (player.containerMenu != player.inventoryMenu) { player.closeContainer(); }

        player.nextContainerCounter();
        PacketManager.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OpenChocoboGuiMessage(this, player.containerCounter));
        player.containerMenu = new SaddleBagContainer(player.containerCounter, player.getInventory(), this);
        player.initMenu(player.containerMenu);
        EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
    }
    private void chocoboFeatherPick(@NotNull ItemStackHandler sendingInv, @NotNull ItemStackHandler receivingInv, int slot) {
        boolean isReverseTierOne = sendingInv.getSlots() > receivingInv.getSlots();
        boolean isTierOne = sendingInv.getSlots() < receivingInv.getSlots();
        boolean pick = true;
        int slotAdjust = slot;
        if (isTierOne) {
            if (slot < 5) { slotAdjust = slot + 11; }
            if (slot > 4 && slot < 10) { slotAdjust = slot + 15; }
            if (slot > 9) { slotAdjust = slot + 19; }
        }
        if (isReverseTierOne) {
            if (slot > 10 && slot < 16) { slotAdjust = slot-11; }
            if (slot > 19 && slot < 25) { slotAdjust = slot-15; }
            if (slot > 28 && slot < 34) { slotAdjust = slot-19; }
            pick = slotAdjust != slot;
        }
        if (pick) { if (receivingInv.getStackInSlot(slotAdjust) != sendingInv.getStackInSlot(slot)) { receivingInv.setStackInSlot(slotAdjust, sendingInv.getStackInSlot(slot)); } }
    }
    protected void dropFromLootTable(@NotNull DamageSource damageSourceIn, boolean attackedRecently) {
        super.dropFromLootTable(damageSourceIn, attackedRecently);
        if (this.chocoboInventory != null && this.isSaddled()) {
            for (int i = 0; i < this.chocoboInventory.getSlots(); i++) { if (!this.chocoboInventory.getStackInSlot(i).isEmpty()) { this.spawnAtLocation(this.chocoboInventory.getStackInSlot(i), 0.0F); } }
            this.spawnAtLocation(this.saddleItemStackHandler.getStackInSlot(0), 0.0F);
        } else if (this.isSaddled()) { this.spawnAtLocation(this.saddleItemStackHandler.getStackInSlot(0), 0.0F); }
        if (this.isArmed()) { this.spawnAtLocation(this.chocoboWeaponHandler.getStackInSlot(0), 0.0F); }
        if (this.isArmored()) { this.spawnAtLocation(this.chocoboArmorHandler.getStackInSlot(0), 0.0F); }
    }
    protected SoundEvent getAmbientSound() { return AMBIENT_SOUND.get(); }
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) { return AMBIENT_SOUND.get(); }
    protected SoundEvent getDeathSound() { return AMBIENT_SOUND.get(); }
    protected float getSoundVolume() { return .6f; }
    public int getAmbientSoundInterval() { return (24 * (int) (Math.random() * 100)); }
    public boolean checkSpawnRules(@NotNull LevelAccessor worldIn, @NotNull MobSpawnType spawnReasonIn) {
        final Holder<Biome> currentBiome = this.level.getBiome(blockPosition().below());
        if (!currentBiome.containsTag(IS_OVERWORLD)) {
            if (currentBiome.containsTag(IS_END)) { return !this.level.getBlockState(blockPosition().below()).isAir();}
            else { return true; }
        }
        return super.checkSpawnRules(worldIn, spawnReasonIn);
    }
    protected void reassessTameGoals() {
        super.reassessTameGoals();
        if(chocoboAvoidPlayerGoal == null) { chocoboAvoidPlayerGoal = new ChocoboAvoidPlayer(this); }
        if (roamAround == null) { roamAround = new WaterAvoidingRandomStrollGoal(this, 1D); }
        if (avoidBlocks == null) { avoidBlocks = new ChocoboAvoidBlockGoal(this,  avoidBlocks()); }
        if(isTame()) { goalSelector.removeGoal(chocoboAvoidPlayerGoal);}
        else { goalSelector.addGoal(5, chocoboAvoidPlayerGoal); }
        goalSelector.addGoal(6, roamAround);
        goalSelector.addGoal(10, avoidBlocks);
        noRoam = false;
    }
    private static @NotNull ArrayList<Class<? extends Block>> avoidBlocks() {
        ArrayList<Class<? extends Block>> chk = new ArrayList<>();
        chk.add(Blocks.COBBLESTONE_WALL.getClass());
        chk.add(Blocks.WARPED_FENCE.getClass());
        chk.add(Blocks.WARPED_FENCE_GATE.getClass());
        return chk;
    }
    public int getRemainingPersistentAngerTime() { return this.remainingPersistentAngerTime; }
    public void setRemainingPersistentAngerTime(int pTime) { this.remainingPersistentAngerTime = pTime; }
    public @Nullable UUID getPersistentAngerTarget() { return this.persistentAngerTarget; }
    public void setPersistentAngerTarget(@Nullable UUID pTarget) { this.persistentAngerTarget = pTarget; }
    public void startPersistentAngerTimer() { this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random)); }
    protected void customServerAiStep() {
        this.updatePersistentAnger((ServerLevel)this.level, true);
        if (this.getTarget() != null) { this.maybeAlertOthers(); }
        if (this.isAngry()) { this.lastHurtByPlayerTime = this.tickCount; }
    }
    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) { --this.ticksUntilNextAlert; }
        else {
            if (this.getSensing().hasLineOfSight(this.getTarget())) { this.alertOthers(); }
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }
    }
    private void alertOthers() {
        double d0 = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(d0, 10.0D, d0);
        this.level.getEntitiesOfClass(Chocobo.class, aabb, EntitySelector.NO_SPECTATORS).stream()
                .filter((p_34463_) -> { return p_34463_ != this; })
                .filter((p_34461_) -> { return p_34461_.getTarget() == null; })
                .filter((p_34456_) -> { return !p_34456_.isAlliedTo(this.getTarget()); })
                .forEach((p_34440_) -> { p_34440_.setTarget(this.getTarget()); });
    }
    public boolean isPersistenceRequired() { return this.isTame(); }
    public boolean requiresCustomPersistence() { return this.isPassenger(); }
    protected boolean shouldDespawnInPeaceful() { return false; }
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) { this.discard(); }
        else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            Entity entity = this.level.getNearestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                noActionTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.discard();
                entity = null;
            }
            if (entity != null) {
                double d0 = entity.distanceToSqr(this);
                int i = this.getType().getCategory().getDespawnDistance();
                int j = i * i;
                if (d0 > (double)j && this.removeWhenFarAway(d0)) { this.discard(); }

                int k = this.getType().getCategory().getNoDespawnDistance();
                int l = k * k;
                if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && d0 > (double)l && this.removeWhenFarAway(d0)) { this.discard();}
                else if (d0 < (double)l) { this.noActionTime = 0; }
            }
        } else { this.noActionTime = 0; }
    }
}