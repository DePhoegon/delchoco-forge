package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.utils.RandomHelper.random;
import static net.minecraft.world.item.Items.WITHER_ROSE;

public class ChocoboCombatEffects {
    @SubscribeEvent
    public void onChocoboAttack(@NotNull LivingAttackEvent event) {
        Chocobo chocoboAttacker = event.getSource().getEntity() instanceof Chocobo choco ? choco : null;
        Chocobo chocoboTarget = event.getEntityLiving() instanceof Chocobo choco ? choco : null;
        if (chocoboAttacker != null) {
            /* Chance For Thing when Chocobos Attack */
        }
        if (chocoboTarget != null) {
            if (random.nextInt(100)+1 > 35) {
                chocoboTarget.spawnAtLocation(CHOCOBO_FEATHER.get());
            }
        }
    }
    @SubscribeEvent
    public void onChocoboKillOrDie(@NotNull LivingDeathEvent event) {
        Chocobo chocoboKill = event.getSource().getEntity() instanceof  Chocobo choco ? choco : null;
        Chocobo chocoboDie = event.getEntityLiving() instanceof  Chocobo choco ? choco : null;
        if (chocoboKill != null) {
            if (chocoboKill.getChocoboColor() == ChocoboColor.BLACK) {
                if (random.nextInt(100)+1 < 45) {
                    chocoboKill.spawnAtLocation(WITHER_ROSE);
                }
            }
        }

        if (chocoboDie != null) {
            @NotNull ItemStack egg = switch (chocoboDie.getChocoboColor()) {
                case YELLOW -> new ItemStack(YELLOW_CHOCOBO_SPAWN_EGG.get());
                case WHITE -> new ItemStack(WHITE_CHOCOBO_SPAWN_EGG.get());
                case GREEN -> new ItemStack(GREEN_CHOCOBO_SPAWN_EGG.get());
                case FLAME -> new ItemStack(FLAME_CHOCOBO_SPAWN_EGG.get());
                case BLACK -> new ItemStack(BLACK_CHOCOBO_SPAWN_EGG.get());
                case GOLD -> new ItemStack(GOLD_CHOCOBO_SPAWN_EGG.get());
                case BLUE -> new ItemStack(BLUE_CHOCOBO_SPAWN_EGG.get());
                case RED -> new ItemStack(RED_CHOCOBO_SPAWN_EGG.get());
                case PINK -> new ItemStack(PINK_CHOCOBO_SPAWN_EGG.get());
                case PURPLE -> new ItemStack(PURPLE_CHOCOBO_SPAWN_EGG.get());
            };
            if (random.nextInt(1000)+1 < 85) {
                chocoboDie.spawnAtLocation(egg);
            }
        }
    }
}
