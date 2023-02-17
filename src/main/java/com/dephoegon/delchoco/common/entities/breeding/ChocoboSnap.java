package com.dephoegon.delchoco.common.entities.breeding;

import com.dephoegon.delchoco.common.ChocoConfig;

import static com.dephoegon.delchoco.common.entities.properties.ChocoboColor.FLAME;
import static com.dephoegon.delchoco.common.entities.properties.ChocoboColor.getRandomColor;
import static com.dephoegon.delchoco.common.items.ChocoboSpawnEggItem.wbChocobos;
import static com.dephoegon.delchoco.utils.RandomHelper.random;

public class ChocoboSnap {
     public ChocoboStatSnapshot TWEAKED_DEFAULT = new ChocoboStatSnapshot();

    private void setTWEAKED() {
        this.TWEAKED_DEFAULT.generation = 1;
        this.TWEAKED_DEFAULT.health = boundedIntRange(5, 10, ChocoConfig.COMMON.defaultHealth.get());
        this.TWEAKED_DEFAULT.stamina = ChocoConfig.COMMON.defaultStamina.get();
        this.TWEAKED_DEFAULT.speed = ChocoConfig.COMMON.defaultSpeed.get() / 100f;
        this.TWEAKED_DEFAULT.attack = boundedIntRange(1, 3,ChocoConfig.COMMON.defaultAttackStrength.get());
        this.TWEAKED_DEFAULT.defense = boundedIntRange(2, 4, ChocoConfig.COMMON.defaultArmor.get());
        this.TWEAKED_DEFAULT.toughness = boundedIntRange(1, 3, ChocoConfig.COMMON.defaultArmorToughness.get());
        this.TWEAKED_DEFAULT.color = getRandomColor();
        this.TWEAKED_DEFAULT.flameBlood = TWEAKED_DEFAULT.color == FLAME;
        this.TWEAKED_DEFAULT.waterBreath = wbChocobos().contains(TWEAKED_DEFAULT.color);
    }
    public ChocoboSnap() {
        setTWEAKED();
    }
    public int boundedIntRange(int lower, int upper, int origin) {
        int lowEnd = Math.max(origin - lower, 0);
        int upEnd = origin+upper;
        int range = upEnd-lowEnd;
        return random.nextInt(range)+lowEnd;
    }
}
