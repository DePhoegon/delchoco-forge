package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.blockentities.ChocoboEggBlockEntity;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ChocoboMateGoal extends Goal {
    private final static Vec3i[] LAY_EGG_CHECK_OFFSETS = {
            new Vec3i(0, 0, 0), new Vec3i(-1, 0, 0), new Vec3i(-1, 0, -1),
            new Vec3i(0, 0, -1), new Vec3i(+1, 0, -1), new Vec3i(+1, 0, 0),
            new Vec3i(+1, 0, +1), new Vec3i(0, 0, +1), new Vec3i(-1, 0, +1),
            new Vec3i(0, 1, 0), new Vec3i(-1, 1, 0), new Vec3i(-1, 1, -1),
            new Vec3i(0, 1, -1), new Vec3i(+1, 1, -1), new Vec3i(+1, 1, 0),
            new Vec3i(+1, 1, +1), new Vec3i(0, 1, +1), new Vec3i(-1, 1, +1),
    };

    private final Chocobo chocobo;
    private final Level world;
    private final double moveSpeed;
    private Chocobo targetMate;
    private int spawnBabyDelay;

    public ChocoboMateGoal(@NotNull Chocobo chocobo, double moveSpeed) {
        this.chocobo = chocobo;
        this.world = chocobo.level;
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
        for (Vec3i offset : LAY_EGG_CHECK_OFFSETS) {
            BlockPos offsetPos = pos.offset(offset);
            BlockState state = this.world.getBlockState(offsetPos);
            if (state.getMaterial().isReplaceable() && !state.getMaterial().isLiquid() && ModRegistry.CHOCOBO_EGG.get().canSurvive(state, this.world, offsetPos)) {
                if (!this.world.setBlockAndUpdate(offsetPos, ModRegistry.CHOCOBO_EGG.get().defaultBlockState())) { return; }
                BlockEntity tile = this.world.getBlockEntity(offsetPos);
                if(tile instanceof ChocoboEggBlockEntity eggTile) { eggTile.setBreedInfo(new ChocoboBreedInfo(new ChocoboStatSnapshot(this.chocobo), new ChocoboStatSnapshot(this.targetMate))); }
                return;
            }
        }

    }
}