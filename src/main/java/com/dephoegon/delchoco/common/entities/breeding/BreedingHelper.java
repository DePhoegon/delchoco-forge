package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModAttributes;
import com.dephoegon.delchoco.common.init.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.dephoegon.delchoco.aid.fallbackValues.*;
import static com.dephoegon.delchoco.aid.fallbackValues.dMaxArmorToughness;
import static com.dephoegon.delchoco.common.configs.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.breeding.ChocoboSnap.setChocoScale;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnEggItem.*;
import static java.lang.Math.*;

public class BreedingHelper {
    private static double minCheck(double one, double two) { return round((one + two) / 2) < 11 ? round((one+two)/2+1) : round((one + two) / 2); }

    public static @Nullable Chocobo createChild(ChocoboBreedInfo breedInfo, Level world, ItemStack egg) {
        final Chocobo baby = ModEntities.CHOCOBO.get().create(world);
        if (baby == null) { return null; }
        final ChocoboStatSnapshot mother = breedInfo.getMother();
        final ChocoboStatSnapshot father = breedInfo.getFather();

        baby.setGeneration(mother.generation > father.generation ? mother.generation+1 : father.generation+1);
        float health = round(((mother.health + father.health) / 2) * (ChocoConfigGet(COMMON.poslossHealth.get(), dPossLoss).floatValue() + ((float) random() * ChocoConfigGet(COMMON.posgainHealth.get(), dPossGain).floatValue())));
        Objects.requireNonNull(baby.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(min(health, ChocoConfigGet(COMMON.maxHealth.get(),dMaxHealth).floatValue()));
        float speed = ((mother.speed + father.speed) / 2f) * (ChocoConfigGet(COMMON.poslossSpeed.get(),dPossLoss).floatValue() + ((float) random() * ChocoConfigGet(COMMON.posgainSpeed.get(),dPossGain).floatValue()));
        Objects.requireNonNull(baby.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(min(speed, (ChocoConfigGet(COMMON.maxSpeed.get(),dMaxSpeed).floatValue() / 100f)));
        float stamina = round((mother.stamina + father.stamina) / 2) * (ChocoConfigGet(COMMON.poslossStamina.get(),dPossLoss).floatValue() + ((float) random() * ChocoConfigGet(COMMON.posgainStamina.get(),dPossGain).floatValue()));
        Objects.requireNonNull(baby.getAttribute(ModAttributes.MAX_STAMINA.get())).setBaseValue(min(stamina, ChocoConfigGet(COMMON.maxStamina.get(), dMaxStamina)));
        double attack = minCheck(mother.attack, father.attack) * (dPossLoss.floatValue() + ((float) random() * (dPossGain+25D)));
        attack = attack < dAttack ? dAttack : attack;
        Objects.requireNonNull(baby.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(min(attack, ChocoConfigGet(COMMON.maxStrength.get(), dMaxStrength)));
        double defence = minCheck(mother.defense, father.defense) * (dPossLoss.floatValue() + ((float) random() * (dPossGain+25D)));
        defence = defence < dArmor ? dArmor : defence;
        Objects.requireNonNull(baby.getAttribute(Attributes.ARMOR)).setBaseValue(min(defence, ChocoConfigGet(COMMON.maxArmor.get(), dMaxArmor)));
        double toughness = minCheck(mother.toughness, father.toughness) * (dPossLoss.floatValue() + ((float) random() * (dPossGain+25D)));
        toughness = toughness < dArmorTough ? dArmorTough : toughness;
        Objects.requireNonNull(baby.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(min(toughness, ChocoConfigGet(COMMON.maxToughness.get(),dMaxArmorToughness)));

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
        int scale = mother.scale+father.scale+(setChocoScale(baby.isMale()));
        baby.setChocoboScale(baby.isMale(), Math.floorDiv(scale, 3), true);
        baby.setWaterBreath(mother.waterBreath || father.waterBreath || wbChocobos().contains(bColor));
        baby.setWitherImmune(mother.witherImmune || father.witherImmune || wiChocobos().contains(bColor));
        baby.setPoisonImmune(mother.poisonImmune || father.poisonImmune || piChocobos().contains(bColor));
        baby.setFlame(mother.flameBlood || father.flameBlood || bColor == flame);
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
}
