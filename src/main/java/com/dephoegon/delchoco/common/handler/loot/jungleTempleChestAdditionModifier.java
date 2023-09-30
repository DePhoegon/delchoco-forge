package com.dephoegon.delchoco.common.handler.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class jungleTempleChestAdditionModifier extends LootModifier {
    private final Item additional;
    private final float chance;
    private final int num;
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected jungleTempleChestAdditionModifier(LootItemCondition[] conditionsIn, Item additional, float chance, int num) {
        super(conditionsIn);
        this.additional = additional;
        this.chance = chance;
        this.num = num;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, @NotNull LootContext context) {
        if (context.getRandom().nextFloat() <= chance) { generatedLoot.add(new ItemStack(additional, num)); }
        return generatedLoot;
    }
    public static class Serializer extends GlobalLootModifierSerializer<jungleTempleChestAdditionModifier> {
        public jungleTempleChestAdditionModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            Item addition = ForgeRegistries.ITEMS.getValue( new ResourceLocation(GsonHelper.getAsString(object, "addition")));
            float chance = GsonHelper.getAsFloat(object, "chance");
            int number = GsonHelper.getAsInt(object, "number");
            return new jungleTempleChestAdditionModifier(ailootcondition, addition, chance, number);
        }
        public JsonObject write(@NotNull jungleTempleChestAdditionModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(instance.additional)).toString());
            json.addProperty("chance", instance.chance);
            json.addProperty("number", instance.num);
            return json;
        }
    }
}