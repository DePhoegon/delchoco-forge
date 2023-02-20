package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChocoboSpawnEggItem extends Item {
    private final ChocoboColor color;

    public ChocoboSpawnEggItem(Properties pProperties, ChocoboColor color) {
        super(pProperties);
        this.color = color;
    }

    private @NotNull Component name(@NotNull ItemStack egg) {
        return egg.getHoverName();
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        final Chocobo chocobo = ModEntities.CHOCOBO.get().create(worldIn);
        if (chocobo != null) {
            final BlockPos pos = context.getClickedPos();
            final Player player = context.getPlayer();
            if (player != null) {
                if (player.isCrouching()) {
                    chocobo.setAge(-7500);
                }
            }

            chocobo.moveTo(pos.getX() + .5, pos.getY() + 1.5F, pos.getZ() + .5, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
            chocobo.yHeadRot = chocobo.getYRot();
            chocobo.yBodyRot = chocobo.getYRot();
            chocobo.setChocoboColor(color);
            Component nameCheck = name(context.getItemInHand());
            if (context.getItemInHand().hasCustomHoverName()) { chocobo.setCustomName(nameCheck); }
            chocobo.setFlame(color == ChocoboColor.FLAME);
            chocobo.setWaterBreath(wbChocobos().contains(color));
            chocobo.setFromEgg(true);
            chocobo.finalizeSpawn((ServerLevel)worldIn, worldIn.getCurrentDifficultyAt(chocobo.blockPosition()), MobSpawnType.SPAWN_EGG, (SpawnGroupData)null, (CompoundTag)null);
            worldIn.addFreshEntity(chocobo);
            chocobo.playAmbientSound();
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }
    public static @NotNull ArrayList<ChocoboColor> wbChocobos() {
        ArrayList<ChocoboColor> out = new ArrayList<>();
        out.add(ChocoboColor.BLUE);
        out.add(ChocoboColor.GOLD);
        out.add(ChocoboColor.BLACK);
        out.add(ChocoboColor.PURPLE);
        return out;
    }
}
