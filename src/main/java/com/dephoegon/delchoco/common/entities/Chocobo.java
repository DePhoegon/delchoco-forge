package com.dephoegon.delchoco.common.entities;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.breeding.ChocoboMateGoal;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.entities.properties.MovementType;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModSounds;
import com.dephoegon.delchoco.common.inventory.SaddleBagContainer;
import com.dephoegon.delchoco.common.inventory.SaddleItemStackHandler;
import com.dephoegon.delchoco.common.items.ChocoDisguiseItem;
import com.dephoegon.delchoco.common.items.ChocoboArmorItems;
import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import com.dephoegon.delchoco.common.items.ChocoboWeaponItems;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.common.network.packets.OpenChocoboGuiMessage;
import com.dephoegon.delchoco.utils.RandomHelper;
import com.dephoegon.delchoco.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

import static com.dephoegon.delbase.item.shiftingDyes.*;
import static com.dephoegon.delchoco.aid.chocoKB.isAltDown;
import static com.dephoegon.delchoco.aid.dyeList.getDyeList;
import static com.dephoegon.delchoco.aid.dyeList.setLists;
import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.init.ModSounds.AMBIENT_SOUND;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.world.level.biome.Biome.getBiomeCategory;
import static net.minecraftforge.common.BiomeDictionary.hasType;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@SuppressWarnings({"rawtypes", "ConstantConditions"})
public class Chocobo extends TamableAnimal implements NeutralMob {
    private static final String NBTKEY_CHOCOBO_COLOR = "Color";
    private static final String NBTKEY_CHOCOBO_IS_MALE = "Male";
    private static final String NBTKEY_MOVEMENTTYPE = "MovementType";
    private static final String NBTKEY_SADDLE_ITEM = "Saddle";
    private static final String NBTKEY_INVENTORY = "Inventory";
    private static final String NBTKEY_NEST_POSITION = "NestPos";
    private static final String NBTKEY_CHOCOBO_GENERATION = "Generation";
    private static final String NBTKEY_CHOCOBO_STAMINA = "Stamina";
    private static final String NBTKEY_CHOCOBO_FLAME_BLOOD = "FlameBlood";
    private static final String NBTKEY_CHOCOBO_WATER_BREATH = "WaterBreath";
    private static final String NBTKEY_CHOCOBO_COLLAR = "Collar";
    private static final UUID CHOCOBO_CHEST_ARMOR_MOD_UUID = UUID.fromString("c03d8021-8839-4377-ac23-ed723ece6454");
    private static final UUID CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID = UUID.fromString("f7dcb185-7182-4a28-83ae-d1a2de9c022d");
    private static final UUID CHOCOBO_WEAPON_DAM_MOD_UUID = UUID.fromString("b9f0dc43-15a7-49f5-815c-915322c30402");
    private static final UUID CHOCOBO_WEAPON_SPD_MOD_UUID = UUID.fromString("46c84540-15f7-4f22-9da9-ebc23d2353af");
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private UUID persistentAngerTarget;

    private static final EntityDataAccessor<ChocoboColor> PARAM_COLOR = SynchedEntityData.defineId(Chocobo.class, ModDataSerializers.CHOCOBO_COLOR);
    private static final EntityDataAccessor<Boolean> PARAM_IS_MALE = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARAM_IS_FLAME_BLOOD = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PARAM_IS_WATER_BREATH = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<MovementType> PARAM_MOVEMENT_TYPE = SynchedEntityData.defineId(Chocobo.class, ModDataSerializers.MOVEMENT_TYPE);
    private static final EntityDataAccessor<ItemStack> PARAM_SADDLE_ITEM = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> PARAM_COLLAR_COLOR = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Integer> PARAM_GENERATION = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Float> PARAM_STAMINA = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.FLOAT);
    private final static EntityDataAccessor<Byte> PARAM_ABILITY_MASK = SynchedEntityData.defineId(Chocobo.class, EntityDataSerializers.BYTE);

    private static final UUID CHOCOBO_SPRINTING_BOOST_ID = UUID.fromString("03ba3167-393e-4362-92b8-909841047640");
    private static final AttributeModifier CHOCOBO_SPRINTING_SPEED_BOOST = (new AttributeModifier(CHOCOBO_SPRINTING_BOOST_ID, "Chocobo sprinting speed boost", 1, Operation.MULTIPLY_BASE));

    private AvoidEntityGoal chocoboAvoidPlayerGoal;
    private WaterAvoidingRandomStrollGoal roamAround;
    public static final int tier_one_chocobo_inv_slot_count = 15; // 3*5
    public static final int tier_two_chocobo_inv_slot_count = 45; //5*9
    public static final int chocobo_equip_slot_count = 2; // Chest/Armor - 0, Weapon(claw) - 1
    private final int top_tier_chocobo_inv_slot_count = tier_two_chocobo_inv_slot_count;
    public final ItemStackHandler chocoboInventory = new ItemStackHandler(top_tier_chocobo_inv_slot_count){
        // will be treated as the backbone
        protected void onContentsChanged(int slot) {
            if (slot > 10 && slot <16) {
                if (chocoboInventory.getStackInSlot(slot) != tierOneItemStackHandler.getStackInSlot(slot - 11)) {
                    tierOneItemStackHandler.setStackInSlot(slot - 11, chocoboInventory.getStackInSlot(slot));
                }
            }
            if (slot > 19 && slot <25) {
                if (chocoboInventory.getStackInSlot(slot) != tierOneItemStackHandler.getStackInSlot(slot - 15)) {
                    tierOneItemStackHandler.setStackInSlot(slot - 15, chocoboInventory.getStackInSlot(slot));
                }
            }
            if (slot > 28 && slot <34) {
                if (chocoboInventory.getStackInSlot(slot) != tierOneItemStackHandler.getStackInSlot(slot - 19)) {
                    tierOneItemStackHandler.setStackInSlot(slot - 19, chocoboInventory.getStackInSlot(slot));
                }
            }
            if (chocoboInventory.getStackInSlot(slot) != tierTwoItemStackHandler.getStackInSlot(slot)) {
                tierTwoItemStackHandler.setStackInSlot(slot, chocoboInventory.getStackInSlot(slot));
            }
        }
    };
    public final ItemStackHandler tierOneItemStackHandler = new ItemStackHandler(tier_one_chocobo_inv_slot_count) {
        protected void onContentsChanged(int slot) {
            int slotAdjust;
            if (slot < 5) { slotAdjust = slot + 11; }
            else if(slot < 10) { slotAdjust = slot + 15; }
            else { slotAdjust = slot + 19; }
            if (tierOneItemStackHandler.getStackInSlot(slot) != chocoboInventory.getStackInSlot(slotAdjust)) {
                chocoboInventory.setStackInSlot(slotAdjust, tierOneItemStackHandler.getStackInSlot(slot));
            }
        }
    };
    public final ItemStackHandler tierTwoItemStackHandler = new ItemStackHandler(tier_two_chocobo_inv_slot_count){
        protected void onContentsChanged(int slot) {
            if (tierTwoItemStackHandler.getStackInSlot(slot) != chocoboInventory.getStackInSlot(slot)) {
                chocoboInventory.setStackInSlot(slot, tierTwoItemStackHandler.getStackInSlot(slot));
            }
        }
    };
    public final ItemStackHandler chocoboEquipmentHandler = new ItemStackHandler(chocobo_equip_slot_count) {
        protected void onContentsChanged(int slot) {
            if (slot == 0) {
                if (chocoboEquipmentHandler.getStackInSlot(slot) != chocoboArmorHandler.getStackInSlot(0)) {
                    chocoboArmorHandler.setStackInSlot(0, chocoboEquipmentHandler.getStackInSlot(slot));
                }
                setChocoboArmorHandler(chocoboEquipmentHandler.getStackInSlot(slot));
            }
            if (slot == 1) {
                if (chocoboEquipmentHandler.getStackInSlot(slot) != chocoboWeaponHandler.getStackInSlot(0)) {
                    chocoboWeaponHandler.setStackInSlot(0, chocoboEquipmentHandler.getStackInSlot(slot));
                }
                setChocoboWeaponHandler(chocoboEquipmentHandler.getStackInSlot(slot));
            }
        }
    };
    public final ItemStackHandler chocoboArmorHandler = new ItemStackHandler(1){
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.isEmpty() || stack.getItem() instanceof ChocoboArmorItems;
        }
        protected void onContentsChanged(int slot) {
            if (chocoboEquipmentHandler.getStackInSlot(0) != chocoboArmorHandler.getStackInSlot(slot)) {
                chocoboEquipmentHandler.setStackInSlot(0, chocoboArmorHandler.getStackInSlot(slot));
            }
        }
    };
    public final ItemStackHandler chocoboWeaponHandler = new ItemStackHandler(1) {
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.isEmpty() || stack.getItem() instanceof ChocoboWeaponItems;
        }
        @Override
        protected void onContentsChanged(int slot) {
            if (chocoboEquipmentHandler.getStackInSlot(1) != chocoboWeaponHandler.getStackInSlot(slot)) {
                chocoboEquipmentHandler.setStackInSlot(1, chocoboWeaponHandler.getStackInSlot(slot));
            }
        }
    };

    public final SaddleItemStackHandler saddleItemStackHandler = new SaddleItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
            //return stack.isEmpty() || stack.getItem() instanceof ChocoboSaddleItem;
        }

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
    private final LazyOptional<IItemHandler> tierOneContainer = LazyOptional.of(() -> tierOneItemStackHandler);
    private final LazyOptional<IItemHandler> tierTwoContainer = LazyOptional.of(() -> tierTwoItemStackHandler);
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (saddleItemStackHandler.getStackInSlot(0).getItem() instanceof ChocoboSaddleItem saddleItem) {
                int saddleSize = saddleItem.getInventorySize();
                return switch (saddleSize) {
                    case 15 -> tierOneContainer.cast();
                    case 45 -> tierTwoContainer.cast();
                    default -> super.getCapability(cap, side);
                };
            }
        }
        return super.getCapability(cap, side);
    }

    private float wingRotation;
    private float destPos;
    private boolean isChocoboJumping;
    private float wingRotDelta;
    private BlockPos nestPos;

    public Chocobo(EntityType<? extends Chocobo> type, Level world) {
        super(type, world);
    }
    @Override
    protected void registerGoals() {
        // togglable Goal 0, - Follow owner (whistle [tamed])
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ChocoPanicGoal(1.5D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(GYSAHL_GREEN.get()), false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Llama.class, 15F, 1.3F, 1.5F));
        // togglable Goal 5, - Avoid Player Goal (non-tamed goal)
        this.goalSelector.addGoal(6, new LeapAtTargetGoal(this, 0.5F));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new ChocoboMateGoal(this, 1.0D));
        // togglable Goal 9, - Roam Around Goal (whistle toggle [tamed/non-tamed])
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this)); // moved after Roam, alittle too stationary
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, true));
    }
    class ChocoPanicGoal extends PanicGoal {
        public ChocoPanicGoal(double pSpeed) {
            super(Chocobo.this, pSpeed);
        }
        protected boolean shouldPanic() { return this.mob.isFreezing() || this.mob.isOnFire(); }
    }
    private final FollowOwnerGoal follow = new FollowOwnerGoal(this, 2.0D, 3.0F, 10.0F, false);
    private boolean noroam;
    public float followingmrhuman = 2;

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(ModAttributes.MAX_STAMINA.get(), COMMON.defaultStamina.get())
                .add(Attributes.MOVEMENT_SPEED, COMMON.defaultSpeed.get() / 100f)
                .add(Attributes.MAX_HEALTH, COMMON.defaultHealth.get())
                .add(Attributes.ARMOR, COMMON.armor.get())
                .add(Attributes.ARMOR_TOUGHNESS, COMMON.armorToughness.get())
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.ATTACK_DAMAGE, COMMON.attackStrength.get());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PARAM_IS_FLAME_BLOOD, false);
        this.entityData.define(PARAM_IS_WATER_BREATH, false);
        this.entityData.define(PARAM_COLLAR_COLOR, 16);
        this.entityData.define(PARAM_COLOR, ChocoboColor.YELLOW);
        this.entityData.define(PARAM_IS_MALE, false);
        this.entityData.define(PARAM_MOVEMENT_TYPE, MovementType.WANDER);
        this.entityData.define(PARAM_SADDLE_ITEM, ItemStack.EMPTY);
        this.entityData.define(PARAM_STAMINA, (float) COMMON.defaultStamina.get());
        this.entityData.define(PARAM_GENERATION, 0);
        this.entityData.define(PARAM_ABILITY_MASK, (byte) 0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setMale(this.level.random.nextBoolean());

        final Holder<Biome> currentBiome = this.level.getBiome(blockPosition().below());
        Biome.BiomeCategory biomeCategory = getBiomeCategory(currentBiome);
        //noinspection OptionalGetWithoutIsPresent
        final ResourceKey<Biome> biomeKey = currentBiome.unwrapKey().get();
        if (biomeCategory == Biome.BiomeCategory.NETHER) { setChocobo(ChocoboColor.FLAME, true, false); }
        else {
            if (biomeCategory == Biome.BiomeCategory.FOREST && !(nameCheck(currentBiome, "mystic") || nameCheck(currentBiome, "blossom")  || nameCheck(currentBiome, "tropics") || nameCheck(currentBiome, "lavender"))) { setChocobo(ChocoboColor.RED, false, false); }
            else if (biomeCategory == Biome.BiomeCategory.MESA) { setChocobo(ChocoboColor.RED, false, false); }
            else if (hasType(biomeKey, Type.HOT) && hasType(biomeKey, Type.DRY)) { setChocobo(ChocoboColor.BLACK, false, true); }
            if (biomeCategory == Biome.BiomeCategory.MUSHROOM) { setChocobo(ChocoboColor.PINK, false, false); }
            if (hasType(biomeKey, Type.SNOWY)) { setChocobo(ChocoboColor.WHITE, false, false); }
            if (biomeCategory == Biome.BiomeCategory.SWAMP) { setChocobo(ChocoboColor.GREEN, false, false); }
            if (nameCheck(currentBiome, "mystic")) { setChocobo(ChocoboColor.BLUE, false, true); }
            if (nameCheck(currentBiome, "blossom")) { setChocobo(ChocoboColor.PINK, false, false); }
            if (nameCheck(currentBiome, "lavender")) { setChocobo(ChocoboColor.PURPLE, false, true); }
            if (nameCheck(currentBiome, "tropics")) { setChocobo(ChocoboColor.GOLD, false, true); }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    private boolean nameCheck(@NotNull Holder<Biome> biomeHolder, String name) {
        String tName = biomeHolder.toString();
        return tName.contains(name);
    }
    private void setChocobo(ChocoboColor color, boolean flame, boolean water) {
        this.setFlame(flame);
        this.setWaterBreath(water);
        this.setChocoboColor(color);
    }

    @Override
    public boolean canBeControlledByRider() { return this.isTame(); }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setChocoboColor(ChocoboColor.values()[compound.getByte(NBTKEY_CHOCOBO_COLOR)]);
        this.setMale(compound.getBoolean(NBTKEY_CHOCOBO_IS_MALE));
        this.setMovementType(MovementType.values()[compound.getByte(NBTKEY_MOVEMENTTYPE)]);
        this.saddleItemStackHandler.deserializeNBT(compound.getCompound(NBTKEY_SADDLE_ITEM));
        this.chocoboInventory.deserializeNBT(compound.getCompound(NBTKEY_INVENTORY));
        if (compound.contains(NBTKEY_NEST_POSITION)) { this.nestPos = NbtUtils.readBlockPos(compound.getCompound(NBTKEY_NEST_POSITION)); }
        this.setGeneration(compound.getInt(NBTKEY_CHOCOBO_GENERATION));
        this.setStamina(compound.getFloat(NBTKEY_CHOCOBO_STAMINA));
        this.setFlame(compound.getBoolean(NBTKEY_CHOCOBO_FLAME_BLOOD));
        this.setWaterBreath(compound.getBoolean(NBTKEY_CHOCOBO_WATER_BREATH));
        this.setCollarColor(compound.getInt(NBTKEY_CHOCOBO_COLLAR));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte(NBTKEY_CHOCOBO_COLOR, (byte) this.getChocoboColor().ordinal());
        compound.putBoolean(NBTKEY_CHOCOBO_IS_MALE, this.isMale());
        compound.putByte(NBTKEY_MOVEMENTTYPE, (byte) this.getMovementType().ordinal());
        compound.put(NBTKEY_SADDLE_ITEM, this.saddleItemStackHandler.serializeNBT());
        compound.put(NBTKEY_INVENTORY, this.chocoboInventory.serializeNBT());
        if (this.nestPos != null) { compound.put(NBTKEY_NEST_POSITION, NbtUtils.writeBlockPos(this.nestPos)); }
        compound.putInt(NBTKEY_CHOCOBO_GENERATION, this.getGeneration());
        compound.putBoolean(NBTKEY_CHOCOBO_FLAME_BLOOD, this.isFlame());
        compound.putBoolean(NBTKEY_CHOCOBO_WATER_BREATH, this.isWBreather());
        compound.putFloat(NBTKEY_CHOCOBO_STAMINA, this.getStamina());
        compound.putInt(NBTKEY_CHOCOBO_COLLAR, this.getCollarColor());
    }

    private void setArmor(ItemStack pStack) {
        this.setItemSlot(EquipmentSlot.CHEST, pStack);
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }
    public boolean isChocoboArmor(ItemStack pStack) {
        return pStack.getItem() instanceof ChocoboArmorItems;
    }
    public boolean isChocoWeapon(ItemStack pStack) {
        return pStack.getItem() instanceof ChocoboWeaponItems;
    }
    private void setChocoboArmorHandler(ItemStack pStack) {
        this.setArmor(pStack);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(CHOCOBO_CHEST_ARMOR_MOD_UUID);
            this.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID);
            if (this.isChocoboArmor(pStack)) {
                this.setDropChance(((ChocoboArmorItems)pStack.getItem()).getSlot(), 0.0F);
                int p = ((ChocoboArmorItems)pStack.getItem()).getProtection();
                float t = ((ChocoboArmorItems)pStack.getItem()).getToughness();
                if (p != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(CHOCOBO_CHEST_ARMOR_MOD_UUID, "Chocobo Armor Bonus", (double)p, Operation.ADDITION));
                }
                if (t != 0) {
                    this.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(new AttributeModifier(CHOCOBO_CHEST_ARMOR_TOUGH_MOD_UUID, "Chocobo Armor Toughness", (float)t, Operation.ADDITION));
                }
            }
        }
    }
    private void setChocoboWeaponHandler(ItemStack pStack) {
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CHOCOBO_WEAPON_DAM_MOD_UUID);
            this.getAttribute(Attributes.ATTACK_SPEED).removeModifier(CHOCOBO_WEAPON_SPD_MOD_UUID);
            if (this.isChocoWeapon(pStack)) {
                double a = ((ChocoboWeaponItems)pStack.getItem()).getDamage();
                float s = ((ChocoboWeaponItems)pStack.getItem()).getAttackSpeed();
                if (a != 0) {
                    this.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(CHOCOBO_WEAPON_DAM_MOD_UUID, "Chocobo Attack Bonus", a, Operation.ADDITION));
                }
                if (s != 0) {
                    this.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(CHOCOBO_WEAPON_SPD_MOD_UUID, "Chocobo Attack Speed Bonus", s, Operation.ADDITION));
                }
            }
        }
    }

    public ChocoboColor getChocoboColor() { return this.entityData.get(PARAM_COLOR); }
    public void setChocoboColor(ChocoboColor color) { this.entityData.set(PARAM_COLOR, color); }
    public void setCollarColor(Integer color) { this.entityData.set(PARAM_COLLAR_COLOR, color); }
    public Integer getCollarColor() { return this.entityData.get(PARAM_COLLAR_COLOR); }
    @Override
    public boolean fireImmune() { return isFlame(); }
    public void setFlame(boolean flame) { this.entityData.set(PARAM_IS_FLAME_BLOOD, flame); }
    public void setWaterBreath(boolean waterBreath) { this.entityData.set(PARAM_IS_WATER_BREATH, waterBreath); }
    public boolean isFlame() { return this.entityData.get(PARAM_IS_FLAME_BLOOD); }
    public boolean isWBreather() { return this.entityData.get(PARAM_IS_WATER_BREATH); }
    public boolean isMale() { return this.entityData.get(PARAM_IS_MALE); }
    public void setMale(boolean isMale) { this.entityData.set(PARAM_IS_MALE, isMale); }
    public MovementType getMovementType() { return this.entityData.get(PARAM_MOVEMENT_TYPE); }
    public void setMovementType(MovementType type) { this.entityData.set(PARAM_MOVEMENT_TYPE, type); }
    public boolean isSaddled() { return !this.getSaddle().isEmpty(); }
    public ItemStack getSaddle() { return this.entityData.get(PARAM_SADDLE_ITEM); }
    private void setSaddleType(@NotNull ItemStack saddleStack) {
        ItemStack oldStack = getSaddle();
        if (oldStack.getItem() != saddleStack.getItem()) {
            this.entityData.set(PARAM_SADDLE_ITEM, saddleStack.copy());
        }
    }
    public boolean rideableUnderWater() { return canBreatheUnderwater(); }
    public boolean canBreatheUnderwater() { return this.isWBreather(); }

    @Nullable
    public BlockPos getNestPosition() { return this.nestPos; }

    @SuppressWarnings("unused")
    public void setNestPosition(@Nullable BlockPos nestPos) { this.nestPos = nestPos; }

    //region Chocobo statistics getter/setter
    public float getStamina() { return this.entityData.get(PARAM_STAMINA); }
    public void setStamina(float value) { this.entityData.set(PARAM_STAMINA, value); }
    public float getStaminaPercentage() { return (float) (this.getStamina() / this.getAttribute(ModAttributes.MAX_STAMINA.get()).getValue()); }
    public int getGeneration() { return this.entityData.get(PARAM_GENERATION); }
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
    //endregion

    @Override
    public double getPassengersRidingOffset() { return 1.65D; }
    @Override
    public boolean canBeRiddenInWater(Entity rider) { return true; }
    @Nullable
    public Entity getControllingPassenger() { return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0); }
    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
            this.fluidHeight.clear();
            this.updateInWaterStateAndDoWaterCurrentPushing();
            double d0 = this.level.dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
            boolean flag;
            if (this.isFlame()) { flag = false; } else {
                flag = this.updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, d0);
            }
            return this.isInWater() || flag;
    }
    private void updateInWaterStateAndDoWaterCurrentPushing() {
        if (!this.isWBreather()) {
            if (this.getVehicle() instanceof Chocobo) {
                this.wasTouchingWater = false;
            } else if (this.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0.014D)) {
                if (!this.wasTouchingWater && !this.firstTick) {
                    this.doWaterSplashEffect();
                }

                this.fallDistance = 0.0F;
                this.wasTouchingWater = true;
                this.clearFire();
            } else {
                this.wasTouchingWater = false;
            }
        } else {
            if (this.isInWater()) {
                this.wasTouchingWater = false;
                this.clearFire();
                if (this.getVehicle() instanceof Chocobo) {
                    if (this.getControllingPassenger() instanceof Player rider) {
                        rider.clearFire();
                    }
                }
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

            if (this.onGround)
                this.isChocoboJumping = false;

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
                    if (Minecraft.getInstance().options.keyJump.isDown()) {
                        setDeltaMovement(new Vec3(motion.x, .5f, motion.z));
                    } else if (this.getDeltaMovement().y < 0 && !this.isWBreather()) {
                        int distance = WorldUtils.getDistanceToSurface(this.blockPosition(), this.getCommandSenderWorld());
                        if (distance > 0)
                            setDeltaMovement(new Vec3(motion.x, .05f, motion.z));
                    } else if (this.isWBreather() && isAltDown()) {
                        Vec3 waterMotion = getDeltaMovement();
                        setDeltaMovement(new Vec3(waterMotion.x, waterMotion.y * 0.65F, waterMotion.z));
                    }
                }
                // Insert override for slowfall Option on Chocobo
                if (!this.onGround && !this.isInWater() && !rider.isShiftKeyDown() && this.getDeltaMovement().y < 0 &&
                    this.useStamina(COMMON.glideStaminaCost.get().floatValue())) {
                    if (Minecraft.getInstance().options.keyJump.isDown())
                    {
                        Vec3 motion = getDeltaMovement();
                        setDeltaMovement(new Vec3(motion.x, motion.y * 0.65F, motion.z));
                    }
                }

                if ((this.isSprinting() && !this.useStamina(COMMON.sprintStaminaCost.get().floatValue())) || (this.isSprinting() &&
                        this.isInWater() && this.useStamina(COMMON.sprintStaminaCost.get().floatValue()))) { this.setSprinting(false); }

                this.setSpeed((float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                super.travel(newVector);
            }
        } else {
            if (!this.onGround && !this.isInWater() && this.getDeltaMovement().y < 0 &&
                    this.useStamina(COMMON.glideStaminaCost.get().floatValue())) {
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
        if (passenger instanceof Mob && this.getControllingPassenger() == passenger) {
            this.yBodyRot = ((LivingEntity) passenger).yBodyRot;
        }
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob mate) { return null; }
    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this || !(otherAnimal instanceof Chocobo otherChocobo)) return false;
        if (!this.isInLove() || !otherAnimal.isInLove()) return false;
        return otherChocobo.isMale() != this.isMale();
    }
    @Override
    public void setSprinting(boolean sprinting) {
        this.setSharedFlag(3, sprinting);
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);

        if (attributeInstance.getModifier(CHOCOBO_SPRINTING_BOOST_ID) != null) {
            attributeInstance.removeModifier(CHOCOBO_SPRINTING_SPEED_BOOST);
        }

        if (sprinting) {
            attributeInstance.addTransientModifier(CHOCOBO_SPRINTING_SPEED_BOOST);
        }
    }
    public void dropFeather() {
        if (this.getCommandSenderWorld().isClientSide)
            return;

        if (this.isBaby())
            return;

        this.spawnAtLocation(new ItemStack(CHOCOBO_FEATHER.get(), 1), 0.0F);
    }
    public int TimeSinceFeatherChance = 0;
    @Override
    protected boolean canRide(@NotNull Entity entityIn) { return !this.getSaddle().isEmpty() && super.canRide(entityIn); }
    @Override
    public void aiStep() {
        super.aiStep();
        this.setRot(this.getYRot(), this.getXRot());
        this.regenerateStamina();
        this.maxUpStep = 1f;
        this.fallDistance = 0f;

        if (this.TimeSinceFeatherChance == 3000) {
            this.TimeSinceFeatherChance = 0;

            if ((float) Math.random() < .25) {
                this.dropFeather();
            }
        } else { this.TimeSinceFeatherChance++; }

        //Change effects to chocobo colors
        if (!this.getCommandSenderWorld().isClientSide) {
            if (this.tickCount % 60 == 0)
            {
                if (this.isFlame()) {
                    this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
                    if (this.isVehicle()) {
                        Entity controller = this.getControllingPassenger();
                        if (controller instanceof Player) {
                            ((Player) controller).addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false));
                        }
                    }
                }
                if (this.isWBreather()) {
                    this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, false));
                    if (this.isVehicle()) {
                        Entity controller = this.getControllingPassenger();
                        if (controller instanceof Player) {
                            ((Player) controller).addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, false));
                        }
                    }
                }
            }
        } else {
            // Wing rotations, control packet, client side
            // Client side
            this.destPos += (double) (this.onGround ? -1 : 4) * 0.3D;
            this.destPos = Mth.clamp(destPos, 0f, 1f);

            if (!this.onGround)
                this.wingRotDelta = Math.min(wingRotation, 1f);
            this.wingRotDelta *= 0.9D;
            this.wingRotation += this.wingRotDelta * 2.0F;

            if (this.onGround) {
                this.animationSpeedOld = this.animationSpeed;
                double d1 = this.getX() - this.xo;
                double d0 = this.getZ() - this.zo;
                float f4 = ((float)Math.sqrt(d1 * d1 + d0 * d0)) * 4.0F;

                if (f4 > 1.0F) {
                    f4 = 1.0F;
                }

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
        // ... yes, we also allow regeneration while in lava :P
        // this effectively limits regeneration to only work while on the ground
        if (!this.onGround && !this.isInWater() && !this.isInLava() && !this.isSprinting())
            return;

        float regen = COMMON.staminaRegenRate.get().floatValue();

        // half the amount of regeneration while moving
        Vec3 motion = getDeltaMovement();
        if (motion.x != 0 || motion.z != 0)
            regen *= 0.85;

        // TODO: implement regen bonus (another IAttribute?)
        this.useStamina(-regen);
    }
    @Override
    public boolean isFood(@NotNull ItemStack stack) { return false; }
    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        ItemStack heldItemStack = player.getItemInHand(hand);
        setLists();
        Item defaultHand = heldItemStack.getItem();
        if (this.isTame()) {
            if (getDyeList().contains(defaultHand)) {
                int control = 0;
                if (defaultHand == RED_DYE.asItem() || defaultHand == RED_SHIFT_DYE.get().asItem()
                        || defaultHand == BLOOD_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 16) {
                        this.setCollarColor(16);
                        control += 1;
                    }
                }
                if (defaultHand == WHITE_DYE.asItem() || defaultHand == WHITE_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 15) {
                        this.setCollarColor(15);
                        control += 1;
                    }
                }
                if (defaultHand == ORANGE_DYE.asItem() || defaultHand == ORANGE_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 14) {
                        this.setCollarColor(14);
                        control += 1;
                    }
                }
                if (defaultHand == MAGENTA_DYE.asItem() || defaultHand == MAGENTA_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 13) {
                        this.setCollarColor(13);
                        control += 1;
                    }
                }
                if (defaultHand == LIGHT_BLUE_DYE.asItem() || defaultHand == LIGHT_BLUE_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 12) {
                        this.setCollarColor(12);
                        control += 1;
                    }
                }
                if (defaultHand == YELLOW_DYE.asItem() || defaultHand == YELLOW_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 11) {
                        this.setCollarColor(11);
                        control += 1;
                    }
                }
                if (defaultHand == LIME_DYE.asItem() || defaultHand == LIME_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 10) {
                        this.setCollarColor(10);
                        control += 1;
                    }
                }
                if (defaultHand == PINK_DYE.asItem() || defaultHand == PINK_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 9) {
                        this.setCollarColor(9);
                        control += 1;
                    }
                }
                if (defaultHand == GRAY_DYE.asItem() || defaultHand == GRAY_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 8) {
                        this.setCollarColor(8);
                        control += 1;
                    }
                }
                if (defaultHand == LIGHT_GRAY_DYE.asItem() || defaultHand == LIGHT_GRAY_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 7) {
                        this.setCollarColor(7);
                        control += 1;
                    }
                }
                if (defaultHand == CYAN_DYE.asItem() || defaultHand == CYAN_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 6) {
                        this.setCollarColor(6);
                        control += 1;
                    }
                }
                if (defaultHand == PURPLE_DYE.asItem() || defaultHand == PURPLE_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 5) {
                        this.setCollarColor(5);
                        control += 1;
                    }
                }
                if (defaultHand == BLUE_DYE.asItem() || defaultHand == BLUE_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 4) {
                        this.setCollarColor(4);
                        control += 1;
                    }
                }
                if (defaultHand == GREEN_DYE.asItem() || defaultHand == GREEN_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 3) {
                        this.setCollarColor(3);
                        control += 1;
                    }
                }
                if (defaultHand == BROWN_DYE.asItem() || defaultHand == BROWN_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 2) {
                        this.setCollarColor(2);
                        control += 1;
                    }
                }
                if (defaultHand == BLACK_DYE.asItem() || defaultHand == BLACK_SHIFT_DYE.get().asItem()) {
                    if (this.getCollarColor() != 1) {
                        this.setCollarColor(1);
                        control += 1;
                    }
                }
                if (control > 0) { this.usePlayerItem(player, hand, heldItemStack); }}

            if (heldItemStack.getItem() == GYSAHL_CAKE.get().asItem()) {
                this.usePlayerItem(player, hand, heldItemStack);
                ageBoundaryReached();
                return InteractionResult.SUCCESS;
            }

            if (player.isShiftKeyDown() && !this.isBaby()) {
                if (player instanceof ServerPlayer)
                    this.displayChocoboInventory((ServerPlayer) player);
                return InteractionResult.SUCCESS;
            }

            if (heldItemStack.isEmpty() && !player.isShiftKeyDown() && !this.isBaby()) {
                if (COMMON.ownerOnlyRider.get()) {
                    if (isOwnedBy(player)) {
                        player.startRiding(this);
                        return InteractionResult.SUCCESS;
                    } else {
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true);
                    }
                } else {
                    player.startRiding(this);
                    return InteractionResult.SUCCESS;
                }
            }

            if (heldItemStack.getItem() == GYSAHL_GREEN_ITEM.get()) {
                if (getHealth() != getMaxHealth()) {
                    this.usePlayerItem(player, hand, player.getInventory().getSelected());
                    heal(5);
                } else {
                    player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.heal_fail"), true);
                }
            }

            if (heldItemStack.getItem() == CHOCOBO_WHISTLE.get() && !this.isBaby()) {
                if (isOwnedBy(player)) {
                    if (this.followingmrhuman == 3) {
                        this.playSound(ModSounds.WHISTLE_SOUND_FOLLOW.get(), 1.0F, 1.0F);
                        this.setNoAi(false);
                        if (noroam) {
                            this.goalSelector.addGoal(9, this.roamAround);
                            noroam = false;
                        }
                        this.goalSelector.addGoal(0, this.follow);
                        followingmrhuman = 1;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_followcmd"), true);
                    } else if (this.followingmrhuman == 1) {
                        this.playSound(ModSounds.WHISTLE_SOUND_WANDER.get(), 1.0F, 1.0F);
                        this.goalSelector.removeGoal(this.follow);
                        followingmrhuman = 2;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_wandercmd"), true);
                    } else if (this.followingmrhuman == 2) {
                        this.playSound(ModSounds.WHISTLE_SOUND_STAY.get(), 1.0F, 1.0F);
                        // this.setNoAi(true);
                        if (!noroam) {
                            this.goalSelector.removeGoal(roamAround);
                            noroam = true;
                        }
                        followingmrhuman = 3;
                        player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.chocobo_staycmd"), true);
                    }
                } else {
                    player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true);
                }
                return InteractionResult.SUCCESS;
            }

            if (!this.isInLove() && heldItemStack.getItem() == LOVELY_GYSAHL_GREEN.get() && !this.isBaby()) {
                this.usePlayerItem(player, hand, player.getInventory().getSelected());
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            }

            if (heldItemStack.getItem() instanceof ChocoboSaddleItem && !this.isSaddled() && !this.isBaby()) {
                this.saddleItemStackHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                this.setSaddleType(heldItemStack);
                this.usePlayerItem(player, hand, heldItemStack);
                return InteractionResult.SUCCESS;
            }

            if (heldItemStack.getItem() instanceof ChocoboArmorItems armorItems && !this.isBaby()) {
                if (!this.chocoboArmorHandler.getStackInSlot(0).isEmpty()) {
                    if (armorItems.getProtection() > ((ChocoboArmorItems)this.chocoboArmorHandler.getStackInSlot(0).getItem()).getProtection()) {
                        this.spawnAtLocation(this.chocoboWeaponHandler.getStackInSlot(0), 0.0F);
                        this.chocoboArmorHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                        this.usePlayerItem(player, hand, heldItemStack);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    this.chocoboArmorHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                    this.usePlayerItem(player, hand, heldItemStack);
                    return InteractionResult.SUCCESS;
                }
                this.chocoboArmorHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                this.usePlayerItem(player, hand, heldItemStack);
                return InteractionResult.SUCCESS;
            }

            if (heldItemStack.getItem() instanceof ChocoboWeaponItems weaponItems && !this.isBaby()) {
                if (this.chocoboWeaponHandler.getStackInSlot(0).isEmpty()) {
                    this.chocoboWeaponHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                    this.usePlayerItem(player, hand, heldItemStack);
                    return InteractionResult.SUCCESS;
                } else if (weaponItems.getDamage() > ((ChocoboWeaponItems)this.chocoboWeaponHandler.getStackInSlot(0).getItem()).getDamage()) {
                    this.spawnAtLocation(this.chocoboWeaponHandler.getStackInSlot(0), 0.0F);
                    this.chocoboWeaponHandler.setStackInSlot(0, heldItemStack.copy().split(1));
                    return InteractionResult.SUCCESS;
                }
            }

            if (heldItemStack.getItem() == Items.NAME_TAG) {
                if (isOwnedBy(player)) {
                    this.setCustomName(heldItemStack.getHoverName());
                    this.setCustomNameVisible(true);
                    this.usePlayerItem(player, hand, heldItemStack);
                } else {
                    player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.not_owner"), true);
                }
                return InteractionResult.SUCCESS;
            }

            if (heldItemStack.getItem() == CHOCOBO_FEATHER.get().asItem()) {
                if (isOwnedBy(player)) {
                    this.setCustomNameVisible(!this.isCustomNameVisible());
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            if (heldItemStack.getItem() == GYSAHL_GREEN_ITEM.get()) {
            this.usePlayerItem(player, hand, player.getInventory().getSelected());
            if ((float) Math.random() < COMMON.tameChance.get().floatValue()) {
                this.setOwnerUUID(player.getUUID());
                this.setTame(true);
                player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.tame_success"), true);
            } else {
                player.displayClientMessage(new TranslatableComponent(DelChoco.MOD_ID + ".entity_chocobo.tame_fail"), true);
            }
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

    @Override
    protected void dropFromLootTable(@NotNull DamageSource damageSourceIn, boolean attackedRecently) {
        super.dropFromLootTable(damageSourceIn, attackedRecently);
        if (this.chocoboEquipmentHandler != null) {
            for (int i = 0; i<this.chocoboEquipmentHandler.getSlots(); i++){
                if (!this.chocoboEquipmentHandler.getStackInSlot(i).isEmpty()) {
                    this.spawnAtLocation(this.chocoboEquipmentHandler.getStackInSlot(i), 0.0f);
                }
            }
        }
        if (this.chocoboInventory != null && this.isSaddled()) {
            for (int i = 0; i < this.chocoboInventory.getSlots(); i++) {
                if (!this.chocoboInventory.getStackInSlot(i).isEmpty()) {
                    this.spawnAtLocation(this.chocoboInventory.getStackInSlot(i), 0.0f);
                }
            }
            this.spawnAtLocation(this.saddleItemStackHandler.getStackInSlot(0));
        } else if (this.isSaddled()) { this.spawnAtLocation(this.saddleItemStackHandler.getStackInSlot(0)); }
    }
    protected SoundEvent getAmbientSound() { return AMBIENT_SOUND.get(); }
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) { return AMBIENT_SOUND.get(); }
    protected SoundEvent getDeathSound() { return AMBIENT_SOUND.get(); }
    @Override
    protected float getSoundVolume() { return .6f; }
    @Override
    public int getAmbientSoundInterval() { return (24 * (int) (Math.random() * 100)); }
    @Override
    public boolean checkSpawnRules(@NotNull LevelAccessor worldIn, @NotNull MobSpawnType spawnReasonIn) {
        final Holder<Biome> currentBiome = this.level.getBiome(blockPosition().below());
        @SuppressWarnings("OptionalGetWithoutIsPresent") final ResourceKey<Biome> key = currentBiome.unwrapKey().get();
        if (BiomeDictionary.hasType(key, Type.NETHER)) { return true; }
        return super.checkSpawnRules(worldIn, spawnReasonIn);
    }
    @Override
    protected void reassessTameGoals() {
        super.reassessTameGoals();
        if(chocoboAvoidPlayerGoal == null) {
            //noinspection unchecked
            chocoboAvoidPlayerGoal = new AvoidEntityGoal(this, Player.class, livingEntity -> {
                if(livingEntity instanceof Player player) {
                    int chance = 0;
                    for (ItemStack stack : player.getInventory().armor) {
                        if (stack != null) {
                            if (stack.getItem() instanceof ChocoDisguiseItem) { chance += 25; }
                        }
                    }

                    return !RandomHelper.getChanceResult(chance);
                }
                return false;
            }, 10.0F, 1.0D, 1.2D, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        }
        if (roamAround == null) {
            roamAround = new WaterAvoidingRandomStrollGoal(this, 0.8D);
        }
        if(isTame()) {
            goalSelector.removeGoal(chocoboAvoidPlayerGoal);
        } else {
            goalSelector.addGoal(5, chocoboAvoidPlayerGoal);
        }
        goalSelector.addGoal(9, roamAround);
        noroam = false;
    }

    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }
    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, pRemainingPersistentAngerTime);
    }
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }
    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }
}
