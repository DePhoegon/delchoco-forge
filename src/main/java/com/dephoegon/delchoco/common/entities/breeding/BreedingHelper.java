package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static java.lang.Math.*;
import static com.dephoegon.delchoco.common.ChocoConfig.COMMON;

public class BreedingHelper {
    private static double minCheck(double one, double two) {
        return round((one + two) / 2) < 11 ? round((one+two)/2+1) : round((one + two) / 2);
    }

    public static @Nullable Chocobo createChild(ChocoboBreedInfo breedInfo, Level world, ItemStack egg) {
        final Chocobo baby = ModEntities.CHOCOBO.get().create(world);
        if (baby == null) { return null; }

        final ChocoboStatSnapshot mother = breedInfo.getMother();
        final ChocoboStatSnapshot father = breedInfo.getFather();

        baby.setGeneration(mother.generation > father.generation ? mother.generation+1 : father.generation+1);
        float health = round(((mother.health + father.health) / 2) * (COMMON.poslossHealth.get().floatValue() + ((float) random() * COMMON.posgainHealth.get().floatValue())));
        Objects.requireNonNull(baby.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(min(health, COMMON.maxHealth.get().floatValue()));
        float speed = ((mother.speed + father.speed) / 2f) * (COMMON.poslossSpeed.get().floatValue() + ((float) random() * COMMON.posgainSpeed.get().floatValue()));
        Objects.requireNonNull(baby.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(min(speed, (COMMON.maxSpeed.get().floatValue() / 100f)));
        float stamina = round((mother.stamina + father.stamina) / 2) * (COMMON.poslossStamina.get().floatValue() + ((float) random() * COMMON.posgainStamina.get().floatValue()));
        Objects.requireNonNull(baby.getAttribute(ModAttributes.MAX_STAMINA.get())).setBaseValue(min(stamina, COMMON.maxStamina.get()));
        double attack = minCheck(mother.attack, father.attack) *(1D + ((float) random()* .25D));
        Objects.requireNonNull(baby.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(min(attack, COMMON.maxStrength.get()));
        double defence = minCheck(mother.defense, father.defense) *(1D + ((float) random()* .25D));
        Objects.requireNonNull(baby.getAttribute(Attributes.ARMOR)).setBaseValue(min(defence, COMMON.maxArmor.get()));
        double toughness = minCheck(mother.toughness, father.toughness) *(1D + ((float) random()* .25D));
        Objects.requireNonNull(baby.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(min(toughness, COMMON.maxToughness.get()));

        BlockPos centerBlock = null;
        if (baby.getNestPosition() != null) { centerBlock = baby.getNestPosition().below(); }

        ChocoboColor yellow = ChocoboColor.YELLOW;
        ChocoboColor green = ChocoboColor.GREEN;
        ChocoboColor blue = ChocoboColor.BLUE;
        ChocoboColor white = ChocoboColor.WHITE;
        ChocoboColor black = ChocoboColor.BLACK;
        ChocoboColor gold = ChocoboColor.GOLD;
        ChocoboColor pink = ChocoboColor.PINK;
        ChocoboColor red = ChocoboColor.RED;
        ChocoboColor purple = ChocoboColor.PURPLE;
        ChocoboColor flame = ChocoboColor.FLAME;
        ChocoboColor mColor = mother.color;
        ChocoboColor fColor = father.color;
        ChocoboColor bColor = eggColor(mColor, fColor, yellow, .03f);

        if (mColor == yellow) {
            if (fColor == yellow) {
                int rng = (int)(random()*(100-1+1)+1);
                if (rng < 25) { bColor = eggColor(mColor, fColor, yellow, .50f); }
                else if (rng < 50) { bColor = eggColor(mColor, fColor, green, .50f); }
                else if (rng < 75) { bColor = eggColor(mColor, fColor, blue, .50f); }
                else { bColor = eggColor(mColor, fColor, white, .50f); }
            }
            if (fColor == black) { bColor = eggColor(mColor, fColor, gold, .35f); }
        }
        if (mColor == green) { if (fColor == blue) { bColor = eggColor(mColor,fColor, black, .40f); } }
        if (mColor == blue) {
            if (fColor == red) { bColor = eggColor(mColor, fColor, purple, .40f); }
            if (fColor == green) { bColor = eggColor(mColor, fColor, black, .40f); }
        }
        if (mColor == white) {
            if (fColor == flame) { bColor = eggColor(mColor, fColor, red, .60f); }
            if (fColor == red) { bColor = eggColor(mColor, fColor, pink, .50f); }
        }
        if (mColor == black) { if (fColor == yellow) { bColor = eggColor(mColor, fColor, gold, .35f); } }
        if (mColor == red) {
            if (fColor == white) { bColor = eggColor(mColor, fColor, pink, .50f); }
            if (fColor == blue) { bColor = eggColor(mColor, fColor, purple, .40f); }
        }
        if (mColor == flame) { if (fColor == white) { bColor = eggColor(mColor, fColor, red, .60f); } }

        baby.setMale(.50f > (float) random());
        baby.setChocoboColor(bColor);
        baby.setWaterBreath(mother.waterBreath || father.waterBreath);
        baby.setFlame(mother.flameBlood || father.flameBlood);
        baby.setFromEgg(true);
        if (egg.hasCustomHoverName()) { baby.setCustomName(egg.getHoverName()); }

        baby.setAge(-7500);

        return baby;
    }

    private static ChocoboColor eggColor(ChocoboColor mother, ChocoboColor father, ChocoboColor baby, float chance) {
        boolean newColor = chance > Math.random();
        if (newColor) { return baby; }
        else return .50f > Math.random() ? mother : father;
    }
    private static boolean alter(BlockState centerDefaultBlockstate, BlockState NEWS_blockstate, BlockPos centerPos, @NotNull Level world) {
        if (world.getBlockState(centerPos).getBlock().defaultBlockState() == centerDefaultBlockstate && world.getBlockState(centerPos.north()).getBlock().defaultBlockState() == NEWS_blockstate && world.getBlockState(centerPos.south()).getBlock().defaultBlockState() == NEWS_blockstate && world.getBlockState(centerPos.east()).getBlock().defaultBlockState() == NEWS_blockstate && world.getBlockState(centerPos.west()).getBlock().defaultBlockState() == NEWS_blockstate) {
            return true;
        } else { return false; }
    }
}
