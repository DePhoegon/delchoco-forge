package com.dephoegon.delchoco.common.entities.properties;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.items.ChocoDisguiseItem;
import com.dephoegon.delchoco.utils.RandomHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;

import static com.dephoegon.delchoco.utils.RandomHelper.random;
import static net.minecraft.world.level.pathfinder.PathComputationType.LAND;

public class ChocoboGoals {
    @SuppressWarnings("rawtypes")
    private static boolean doNotAttackClassCheck(Class target) {
        ArrayList<Class> out = new ArrayList<>();
        out.add(Chocobo.class);
        out.add(EnderDragon.class);
        out.add(Phantom.class);
        out.add(Pufferfish.class);
        out.add(TropicalFish.class);
        out.add(Cod.class);
        out.add(GlowSquid.class);
        out.add(Squid.class);
        out.add(Salmon.class);
        out.add(Bat.class);
        out.add(Dolphin.class);
        out.add(Guardian.class);
        out.add(ElderGuardian.class);
        out.add(Strider.class);
        out.add(Ghast.class);
        out.add(SkeletonHorse.class);
        return !out.contains(target);
    }
    public static class ChocoboLavaEscape extends Goal {
        private final PathfinderMob mob;
        private final Chocobo chocobo;
        public ChocoboLavaEscape(Chocobo pathfinderChocobo) {
            this.chocobo = pathfinderChocobo;
            this.mob = pathfinderChocobo;
        }
        private void TeleportTo(@NotNull BlockPos pPos) {
            BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(mob.level, pPos.mutable());
            if (blockpathtypes == BlockPathTypes.WALKABLE) {
                BlockPos blockpos = pPos.subtract(this.mob.blockPosition());
                this.mob.level.noCollision(this.mob, this.mob.getBoundingBox().move(blockpos));
            } else { mob.getMoveControl().setWantedPosition(pPos.getX(), pPos.getY(), pPos.getZ(), this.chocobo.getFollowSpeedModifier()); }
        }
        private void canTeleport() {
            BlockPos block = null;
            BlockState newBlock = null;
            double mobX = this.mob.getX();
            double mobY = this.mob.getY();
            double mobZ = this.mob.getZ();

            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(mobX - 10.0D), Mth.floor(mobY - 10.0D), Mth.floor(mobZ - 10.0D), Mth.floor(mobX + 10.0D), Mth.floor(mobY + 10D), Mth.floor(mobZ + 10.0D))) {
                if (!this.mob.level.getFluidState(blockpos1).is(FluidTags.LAVA)) {
                    block = blockpos1;
                    newBlock = this.mob.level.getBlockState(blockpos1);
                    break;
                }
            }
            if (newBlock != null) { this.TeleportTo(block); }
        }
        public void start() { canTeleport(); }
        @Override
        public boolean canUse() { return this.mob.isInLava() && this.chocobo.getRideTickDelay() >= 15; }
    }
    public static class ChocoboAvoidBlockGoal extends Goal {
        private final PathfinderMob mob;
        private final ArrayList<Class<? extends Block>> classes;

        public ChocoboAvoidBlockGoal(PathfinderMob pMob, ArrayList<Class<? extends Block>> classLists) {
            this.mob = pMob;
            this.classes = classLists;
        }
        public boolean canUse() { return mob.isOnGround(); }
        public void start() {
            BlockPos block = null;
            BlockPos newBlockPos = null;
            double mobX = mob.getX();
            double mobY = mob.getY();
            double mobZ = mob.getZ();

            // Checks ArrayList for Classes of blocks for match, & moves on.
            for(BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(mobX - 2.0D), Mth.floor(mobY - 2.0D), Mth.floor(mobZ - 2.0D), Mth.floor(mobX + 2.0D), this.mob.getBlockY(), Mth.floor(mobZ + 2.0D))) {
                Class<? extends Block> block1 = mob.level.getBlockState(blockpos1).getBlock().getClass();
                if (classes.contains(block1)) {
                    block = blockpos1;
                    break;
                }
            }

            // Gets Distance between Block in X & Z, uses it to check distance in favor of away from the block
            if (block != null) {
                double posX = ((mobX - block.getX())*-1)+mobX;
                double blockY = block.getY();
                double posZ = ((mobZ - block.getZ())*-1)+mobZ;
                // Increases the distance by a block to avoid accidental resource hogging/looping
                posZ = posZ > 0 ? posZ+1 : posZ-1;
                posX = posX > 0 ? posX+1 : posX-1;

                // Looks for Pathfindable ground within range of Chocobo to fence & Y+4 from chocobo, & blockposY-4,
                for(BlockPos blockPos2 : BlockPos.betweenClosed(Mth.floor(posX), Mth.floor(mobY+4), Mth.floor(posZ), Mth.floor(mobX), Mth.floor(blockY-4), Mth.floor(mobZ))) {
                    BlockState blockState = mob.level.getBlockState(blockPos2);
                    if (blockState.isPathfindable(mob.level, blockPos2, LAND)){
                        newBlockPos = blockPos2;
                        break;
                    }
                }
                // Sets position to wonder towards.
                if (newBlockPos != null) { mob.getMoveControl().setWantedPosition(newBlockPos.getX(), newBlockPos.getY(), newBlockPos.getZ(), 1.0D); }
            }
        }
    }
    protected static double boundsFlip(Double vec3, int block, double limit) {
        double positiveDif = positiveDifference(vec3, block);
        if (limit > positiveDif) { return vec3; }
        return vec3 >= block ? block + limit : block - limit;
    }
    protected static double positiveDifference(Double vec3, int block) {
        double out = vec3 < block ? block - vec3 : vec3 - block;
        return out < 0 ? out * -1 : out;
    }
    private static int boundedModifier(double lower, double upper) {
        return (int) (random.nextDouble(upper)-lower);
    }
    public static class ChocoboRandomStrollGoal extends RandomStrollGoal {
        BlockPos blockPos;
        double limit;
        double xSpot;
        double zSpot;
        public ChocoboRandomStrollGoal(Chocobo pMob, double pSpeedModifier, BlockPos position, Double RangeLimit) {
            super(pMob, pSpeedModifier);
            this.blockPos = position;
            this.limit = RangeLimit;
        }
        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3 = getRandomPosition();
            int x = boundedModifier(limit/2, limit);
            int z = boundedModifier(limit/2, limit);
            if (vec3 == null) { vec3 = new Vec3(this.blockPos.getX()+x, this.blockPos.getY(), this.blockPos.getZ()+z); }
            this.xSpot = boundsFlip(vec3.x(), this.blockPos.getX(), this.limit);
            this.zSpot = boundsFlip(vec3.z(), this.blockPos.getZ(), this.limit);
            return new Vec3(this.xSpot, vec3.y(), this.zSpot);
        }
        protected Vec3 getRandomPosition() { return DefaultRandomPos.getPos(this.mob, 10, 7); }
    }
    public static class ChocoboLocalizedWonder extends WaterAvoidingRandomStrollGoal {
        BlockPos blockPos;
        double limit;
        double xSpot;
        double zSpot;
        public ChocoboLocalizedWonder(Chocobo pMob, double pSpeedModifier, BlockPos position, Double RangeLimit) {
            super(pMob, pSpeedModifier);
            this.blockPos = position;
            this.limit = RangeLimit;
        }
        @Nullable
        protected Vec3 getPosition() {
            Vec3 vec3;
            if (this.mob.isInWaterOrBubble()) {
                vec3 = LandRandomPos.getPos(this.mob, 10, 7);
            } else {
                vec3 = this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 10, 7) : getRandomPosition();
            }
            int x = boundedModifier(limit/2, limit);
            int z = boundedModifier(limit/2, limit);
            if (vec3 == null) { vec3 = new Vec3(this.blockPos.getX()+x, this.blockPos.getY(), this.blockPos.getZ()+z); }
            this.xSpot = boundsFlip(vec3.x(), this.blockPos.getX(), this.limit);
            this.zSpot = boundsFlip(vec3.z(), this.blockPos.getZ(), this.limit);
            return new Vec3(this.xSpot, vec3.y(), this.zSpot);
        }
        protected Vec3 getRandomPosition() { return DefaultRandomPos.getPos(this.mob, 10, 7); }
    }
    public static class ChocoboOwnerHurtGoal extends OwnerHurtTargetGoal {
        private final TamableAnimal tameAnimal;

        public ChocoboOwnerHurtGoal(TamableAnimal pTameAnimal) {
            super(pTameAnimal);
            this.tameAnimal = pTameAnimal;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }
        public boolean canUse() {
            if (tameAnimal.isTame()) {
                LivingEntity livingentity = tameAnimal.getOwner();
                if (livingentity == null) { return false; }
                else {
                    Class<? extends LivingEntity> ownerLastHurt;
                    if (livingentity.getLastHurtMob() != null) { ownerLastHurt = livingentity.getLastHurtMob().getClass();}
                    else { ownerLastHurt = null; }
                    return (doNotAttackClassCheck(ownerLastHurt)) && super.canUse();
                }
            } else { return super.canUse(); }
        }
    }
    public static class ChocoboOwnerHurtByGoal extends OwnerHurtByTargetGoal {
        private final TamableAnimal tameAnimal;

        public ChocoboOwnerHurtByGoal(TamableAnimal pTameAnimal) {
            super(pTameAnimal);
            this.tameAnimal = pTameAnimal;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }
        public boolean canUse() {
            if (tameAnimal.isTame()) {
                LivingEntity livingentity = tameAnimal.getOwner();
                if (livingentity == null) { return false; }
                else {
                    Class<? extends LivingEntity> ownerLastHurtBy;
                    if (livingentity.getLastHurtMob() != null) { ownerLastHurtBy = livingentity.getLastHurtMob().getClass(); }
                    else { ownerLastHurtBy = null; }
                    return doNotAttackClassCheck(ownerLastHurtBy) && super.canUse();
                }
            } else { return super.canUse(); }
        }
    }
    public static class ChocoPanicGoal extends PanicGoal {
        PathfinderMob entity;

        public ChocoPanicGoal(PathfinderMob mob, double pSpeed) {
            super(mob, pSpeed);
            this.entity = mob;
        }
        private boolean dragonDamageCheck() {
            LivingEntity livingentity = this.entity.getLastHurtByMob();
            boolean tame;
            if (this.entity instanceof Chocobo chocobo) { tame = chocobo.isTame(); } else { tame = false; }
            if (livingentity != null && !tame) { return livingentity.getClass() == EnderDragon.class; }
            return false;
        }
        protected boolean shouldPanic() { return entity.isFreezing() || entity.isOnFire() || dragonDamageCheck(); }
    }
    @SuppressWarnings("rawtypes")
    public static class ChocoboAvoidPlayer extends AvoidEntityGoal {
        public ChocoboAvoidPlayer(PathfinderMob pMob) {
            super(pMob, Player.class, livingEntity -> {
                if(livingEntity instanceof Player player) {
                    int chance = 0;
                    for (ItemStack stack : player.getInventory().armor) { if (stack != null) {
                        if (stack.getItem() instanceof ChocoDisguiseItem) { chance += 25; }
                    } }
                    return !RandomHelper.getChanceResult(chance);
                }
                return false;
            }, 10.0F, 1.0D, 1.2D, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
        }
    }
    public static class ChocoboHurtByTargetGoal extends HurtByTargetGoal {
        public ChocoboHurtByTargetGoal(PathfinderMob pMob, Class<?>... pToIgnoreDamage) { super(pMob, pToIgnoreDamage); }
        public boolean canUse() {
            LivingEntity livingentity = this.mob.getLastHurtByMob();
            boolean canAttack = true;
            if (livingentity != null) { canAttack = doNotAttackClassCheck(livingentity.getClass()); }
            return canAttack && super.canUse();
        }
    }
}