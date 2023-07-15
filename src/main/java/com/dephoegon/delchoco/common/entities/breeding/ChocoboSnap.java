package com.dephoegon.delchoco.common.entities.breeding;

import static com.dephoegon.delchoco.aid.util.fallbackValues.*;
import static com.dephoegon.delchoco.common.configs.ChocoConfig.COMMON;
import static com.dephoegon.delchoco.common.entities.properties.ChocoboColor.FLAME;
import static com.dephoegon.delchoco.common.entities.properties.ChocoboColor.getRandomColor;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnEggItem.*;
import static com.dephoegon.delchoco.utils.RandomHelper.random;

public class ChocoboSnap {
     public ChocoboStatSnapshot TWEAKED_DEFAULT = new ChocoboStatSnapshot();

    private void setTWEAKED() {
        this.TWEAKED_DEFAULT.generation = 1;
        this.TWEAKED_DEFAULT.health = boundedIntRange(5, 10, ChocoConfigGet(COMMON.defaultHealth.get(), dHealth));
        this.TWEAKED_DEFAULT.stamina = ChocoConfigGet(COMMON.defaultStamina.get(),dStamina);
        this.TWEAKED_DEFAULT.speed = ChocoConfigGet(COMMON.defaultSpeed.get(), dSpeed) / 100f;
        this.TWEAKED_DEFAULT.attack = boundedIntRange(1, 3, ChocoConfigGet(COMMON.defaultAttackStrength.get(),dAttack));
        this.TWEAKED_DEFAULT.defense = boundedIntRange(2, 4, ChocoConfigGet(COMMON.defaultArmor.get(),dArmor));
        this.TWEAKED_DEFAULT.toughness = boundedIntRange(1, 3, ChocoConfigGet(COMMON.defaultArmorToughness.get(), dArmorTough));
        this.TWEAKED_DEFAULT.color = getRandomColor();
        this.TWEAKED_DEFAULT.flameBlood = TWEAKED_DEFAULT.color == FLAME;
        this.TWEAKED_DEFAULT.waterBreath = wbChocobos().contains(this.TWEAKED_DEFAULT.color);
        this.TWEAKED_DEFAULT.witherImmune = wiChocobos().contains(this.TWEAKED_DEFAULT.color);
        this.TWEAKED_DEFAULT.poisonImmune = piChocobos().contains(this.TWEAKED_DEFAULT.color);
        this.TWEAKED_DEFAULT.scale = setChocoScale(.50f > (float) Math.random());
    }
    public ChocoboSnap() { setTWEAKED(); }
    public int boundedIntRange(int lower, int upper, int origin) {
        int lowEnd = Math.max(origin - lower, 0);
        int upEnd = origin+upper;
        int range = upEnd-lowEnd;
        return random.nextInt(range)+lowEnd;
    }
    public static int setChocoScale(boolean isMale) {
        int base = random.nextInt(50) + 1;
        return isMale ? base - 20 : base - 30;
    }
}