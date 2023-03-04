package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.items.ChocoDisguiseItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.init.ModRegistry.*;
import static com.dephoegon.delchoco.common.items.ChocoDisguiseItem.*;
import static com.dephoegon.delchoco.utils.RandomHelper.random;
import static net.minecraft.world.item.Items.*;

public class ChocoboCombatEffects {
    @SubscribeEvent
    public void onChocoboAttack(@NotNull LivingAttackEvent event) {
        Chocobo chocoboAttacker = event.getSource().getEntity() instanceof Chocobo choco ? choco : null;
        Chocobo chocoboTarget = event.getEntityLiving() instanceof Chocobo choco ? choco : null;
        if (chocoboAttacker != null) {
             LivingEntity target = event.getEntityLiving();
            if (target instanceof Spider e) { if (onHitMobChance(10)) { e.spawnAtLocation(STRING); } }
            if (target instanceof CaveSpider e) { if (onHitMobChance(5)) { e.spawnAtLocation(FERMENTED_SPIDER_EYE); } }
            if (target instanceof Skeleton e) { if (onHitMobChance(10)) { e.spawnAtLocation(BONE); } }
            if (target instanceof WitherSkeleton e) { if (onHitMobChance(10)) { e.spawnAtLocation(CHARCOAL); } }
            if (target instanceof IronGolem e) { if (onHitMobChance(5)) { e.spawnAtLocation(POPPY); } }
            if (target.getItemBySlot(EquipmentSlot.MAINHAND) != ItemStack.EMPTY) {
                if (onHitMobChance(30)) {
                    target.spawnAtLocation(target.getItemBySlot(EquipmentSlot.MAINHAND));
                    target.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                }
            }
            if (target.getItemBySlot(EquipmentSlot.OFFHAND) != ItemStack.EMPTY) {
                if (onHitMobChance(10)) {
                    target.spawnAtLocation(target.getItemBySlot(EquipmentSlot.OFFHAND));
                    target.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        }
        if (chocoboTarget != null) {
            if (random.nextInt(100)+1 > 35) { chocoboTarget.spawnAtLocation(CHOCOBO_FEATHER.get()); }
        }
    }
    @SubscribeEvent
    public void onChocoboKillOrDie(@NotNull LivingDeathEvent event) {
        Chocobo chocoboKill = event.getSource().getEntity() instanceof  Chocobo choco ? choco : null;
        Chocobo chocoboDie = event.getEntityLiving() instanceof  Chocobo choco ? choco : null;
        if (chocoboKill != null) {
            ChocoboColor color = chocoboKill.getChocoboColor();
            LivingEntity target = event.getEntityLiving();
            if (target instanceof Spider) { if (.20f > (float) Math.random()) { chocoboKill.spawnAtLocation(COBWEB); } }
            if (color == ChocoboColor.BLACK) { if (flowerChance()) {
               if (.50f > (float) Math.random()) { chocoboKill.spawnAtLocation(WITHER_ROSE); }
               else { chocoboKill.spawnAtLocation(DEAD_BUSH); }
            }}
            if (color == ChocoboColor.FLAME) {
                if (flowerChance()) {
                    if (.50f > (float) Math.random()) { chocoboKill.spawnAtLocation(CRIMSON_FUNGUS); }  else { chocoboKill.spawnAtLocation(WARPED_FUNGUS); }
                } else { if (.10f > (float) Math.random()) { chocoboKill.spawnAtLocation(MAGMA_CREAM); } }
            }
            if (color == ChocoboColor.GREEN) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { chocoboKill.spawnAtLocation(SPORE_BLOSSOM); } else {
                    if (.51f > (float) Math.random()) { chocoboKill.spawnAtLocation(SMALL_DRIPLEAF); }
                    else { chocoboKill.spawnAtLocation(MOSS_BLOCK); }
                }
            }}
            if (color == ChocoboColor.WHITE) {
                if (flowerChance()) {
                    if (.34f > (float) Math.random()) { chocoboKill.spawnAtLocation(SNOWBALL); } else {
                        if (.51f > (float) Math.random()) { chocoboKill.spawnAtLocation(LILY_OF_THE_VALLEY); }
                        else { chocoboKill.spawnAtLocation(OXEYE_DAISY); }
                    }
                } else if (.41f > (float) Math.random()) { chocoboKill.spawnAtLocation(BONE_MEAL); }
            }
            if (color == ChocoboColor.GOLD) {
                if (flowerChance()) { chocoboKill.spawnAtLocation(SUNFLOWER);}
                else { if (.03f > (float) Math.random()) { chocoboKill.spawnAtLocation(GOLD_NUGGET); } }
            }
            if (color == ChocoboColor.BLUE) { if (flowerChance()) {
                if (.50f > (float) Math.random()) { chocoboKill.spawnAtLocation(KELP); } else { chocoboKill.spawnAtLocation(SEA_PICKLE); }
                if (.10f > (float) Math.random()) { chocoboKill.spawnAtLocation(NAUTILUS_SHELL); }
            }}
            if (color == ChocoboColor.PINK) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { chocoboKill.spawnAtLocation(BROWN_MUSHROOM); } else {
                    if (.51f > (float) Math.random()) { chocoboKill.spawnAtLocation(RED_MUSHROOM); }
                    else { chocoboKill.spawnAtLocation(ALLIUM); }
                }
            }}
            if (color == ChocoboColor.RED) { if (flowerChance()) {
                if (.34f > (float) Math.random()) { chocoboKill.spawnAtLocation(STICK); } else {
                    if (.51f > (float) Math.random()) { chocoboKill.spawnAtLocation(BAMBOO); }
                    else { chocoboKill.spawnAtLocation(VINE); }
                }
            }}
            if (color == ChocoboColor.PURPLE) {
                if (flowerChance()) { chocoboKill.spawnAtLocation(CHORUS_FLOWER); }
                else if (.09f > (float) Math.random()) { chocoboKill.spawnAtLocation(ENDER_PEARL); }
            }
            if (color == ChocoboColor.YELLOW) { if (flowerChance()) {
                Item flower = switch (random.nextInt(12)+1) {
                    default -> DANDELION;
                    case 2 -> POPPY;
                    case 3 -> BLUE_ORCHID;
                    case 4 -> ALLIUM;
                    case 5 -> AZURE_BLUET;
                    case 6 -> RED_TULIP;
                    case 7 -> ORANGE_TULIP;
                    case 8 -> WHITE_TULIP;
                    case 9 -> PINK_TULIP;
                    case 10 -> OXEYE_DAISY;
                    case 11 -> CORNFLOWER;
                    case 12 -> LILY_OF_THE_VALLEY;
                };
                chocoboKill.spawnAtLocation(flower);
            }}
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
            if (random.nextInt(1000)+1 < 85) { chocoboDie.spawnAtLocation(egg); }
        }
    }
    private static boolean flowerChance() { return random.nextInt(100)+1 < 45; }
    private static boolean onHitMobChance(int percentChance) { return random.nextInt(100)+1 < percentChance; }
    @SubscribeEvent
    public void onPlayerTick(@NotNull TickEvent.PlayerTickEvent e) {
        Player player = e.player;
        if (player.tickCount % 60 == 0) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ChocoDisguiseItem disguiseHead) {
                String headColor = disguiseHead.getNBTKEY_COLOR();
                if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ChocoDisguiseItem disguiseChest) {
                    String chestColor = disguiseChest.getNBTKEY_COLOR();
                    if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ChocoDisguiseItem disguiseLeg) {
                        String legColor = disguiseLeg.getNBTKEY_COLOR();
                        if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ChocoDisguiseItem disguiseFeet) {
                            String feetColor = disguiseFeet.getNBTKEY_COLOR();
                            if (feetColor.equals(headColor) && chestColor.equals(legColor) && legColor.equals(feetColor)) {
                                if (headColor.equals(green) || headColor.equals(black) || headColor.equals(gold)) { if (player.hasEffect(MobEffects.POISON)) { player.removeEffect(MobEffects.POISON); } }
                                if (headColor.equals(black) || headColor.equals(red) || headColor.equals(purple) || headColor.equals(gold) || headColor.equals(pink)) { if (player.hasEffect(MobEffects.WITHER)) { player.removeEffect(MobEffects.WITHER); } }
                                if (headColor.equals(flame) || headColor.equals(gold)) { player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, false, false)); }
                                if (headColor.equals(gold)) { player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 0, true, false, false)); }
                            }
                        }
                    }
                }
            }
        }
    }
}