package com.dephoegon.delchoco.common.blockentities;

import com.dephoegon.delchoco.common.entities.breeding.ChocoboBreedInfo;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ChocoboEggBlockEntity extends BlockEntity {
    public final static String NBTKEY_BREEDINFO = "BreedInfo";
    private ChocoboBreedInfo breedInfo;
    public ChocoboEggBlockEntity(BlockPos pos, BlockState state) { super(ModRegistry.CHOCOBO_EGG_TILE.get(), pos, state); }
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.breedInfo = new ChocoboBreedInfo(compound.getCompound(NBTKEY_BREEDINFO));
    }
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.breedInfo != null) { compound.put(NBTKEY_BREEDINFO, this.breedInfo.serialize()); }
    }
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.saveAdditional(nbt);
        return nbt;
    }
    public void handleUpdateTag(CompoundTag tag) { super.handleUpdateTag(tag); }
    public ChocoboBreedInfo getBreedInfo() { return this.breedInfo; }
    public void setBreedInfo(ChocoboBreedInfo breedInfo) { this.breedInfo = breedInfo; }
}
