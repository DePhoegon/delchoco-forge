package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.entities.Chocobo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ChocoboMateGoal extends Goal {
    private final Chocobo chocobo;
    private final Level world;
    private final double moveSpeed;
    private Chocobo targetMate;
    private int spawnBabyDelay;

    public ChocoboMateGoal(@NotNull Chocobo chocobo, double moveSpeed) {
        this.chocobo = chocobo;
        this.world = chocobo.level();
        this.moveSpeed = moveSpeed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    public boolean canUse() { return this.chocobo.isInLove() && (this.targetMate = this.getNearbyMate()) != null; }
    public boolean canContinueToUse() { return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60; }
    public void stop() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }
    public void tick() {
        this.chocobo.getLookControl().setLookAt(this.targetMate, 10.0F, (float) this.chocobo.getMaxHeadXRot());
        this.chocobo.getNavigation().moveTo(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.chocobo.distanceToSqr(this.targetMate) < 9.0D) { this.spawnEgg(); }
    }
    private Chocobo getNearbyMate() {
        List<Chocobo> list = this.world.getEntitiesOfClass(Chocobo.class, this.chocobo.getBoundingBox().inflate(8.0D));
        double dist = Double.MAX_VALUE;
        Chocobo closestMate = null;
        for (Chocobo entry : list) {
            if (this.chocobo.canMate(entry) && this.chocobo.distanceToSqr(entry) < dist) {
                closestMate = entry;
                dist = this.chocobo.distanceToSqr(entry);
            }
        }
        return closestMate;
    }
    private void spawnEgg() {
        if (this.chocobo.isMale()) return;
        this.chocobo.setAge(6000);
        this.targetMate.setAge(6000);
        this.chocobo.resetLove();
        this.targetMate.resetLove();

        BlockPos pos = this.chocobo.blockPosition();
        ChocoboBreedInfo breedInfo = new ChocoboBreedInfo(new ChocoboStatSnapshot(this.chocobo), new ChocoboStatSnapshot(this.targetMate));
        Chocobo baby = BreedingHelper.createChild(breedInfo, this.world);
        assert baby != null;
        baby.moveTo(pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 0.0F, 0.0F);
        assert this.world != null;
        this.world.addFreshEntity(baby);

        RandomSource random = baby.getRandom();
        for (int i =0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            double d3 = random.nextDouble() * baby.getBbWidth() * 2.0D - baby.getBbWidth();
            double d4 = 0.5D + random.nextDouble() * baby.getBbHeight();
            double d5 = random.nextDouble() * baby.getBbWidth() * 2.0D - baby.getBbWidth();
            this.world.addParticle(ParticleTypes.HEART, baby.getX() + d3, baby.getY() + d4, baby.getZ() + d5, d0, d1, d2);
        }
    }
}