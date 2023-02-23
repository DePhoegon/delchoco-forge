package com.dephoegon.delchoco.common.items;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

import java.util.Map;

public class ChocoboWeaponItems extends SwordItem {
    private final float attackSpeed;
    public static final int CHOCOBO_DAMAGE_MODIFIER = 5;
    public static final Map<Integer, Tier> CHOCOBO_WEAPON_TIERS = Util.make(Maps.newHashMap(), (map) ->{
        map.put(1, Tiers.STONE);
        map.put(2, Tiers.IRON);
        map.put(3, Tiers.DIAMOND);
        map.put(4, Tiers.NETHERITE);
    });
    private static final Map<Tier, Integer> CHOCOBO_WEAPON_TIER = Util.make(Maps.newHashMap(), (map) ->{ for (int i = 1; i <= CHOCOBO_WEAPON_TIERS.size(); i++) { map.put(CHOCOBO_WEAPON_TIERS.get(i), i); } });
    private static int totalTierDamage(Tier tier, int additive, boolean initialTier) {
        int out = initialTier ? (int)tier.getAttackDamageBonus() + additive : (int)(tier.getAttackDamageBonus() / 2) + additive;
        int nextLowestWeaponTier = CHOCOBO_WEAPON_TIER.get(tier)-1;
        return nextLowestWeaponTier > 0 ? totalTierDamage(CHOCOBO_WEAPON_TIERS.get(nextLowestWeaponTier), out, false) : out;
    }
    public ChocoboWeaponItems(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, (totalTierDamage(pTier, CHOCOBO_DAMAGE_MODIFIER, true) - (int)pTier.getAttackDamageBonus()), pAttackSpeedModifier, pProperties);
        this.attackSpeed = pAttackSpeedModifier + pTier.getSpeed();
    }
    public float getAttackSpeed() { return this.attackSpeed; }
}
