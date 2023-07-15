package com.dephoegon.delchoco.common.handler;

//@Mod.EventBusSubscriber(modid = delchoco.MOD_ID)
public class LootTableEventHandler {
    // Selective Removal of the ability fruits, & commenting out the skeleton code for future use.
    // Handled in the Configs. currently in ' com.dephoegon.delchoco.common.configs.ChocoConfig.java ', names must match.
    // Removal of Ability Fruits
    // Keeping the LootPool InjectPool setup for possible future use

    // entire class commented out to prevent ingame resource usage
    /*
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {

        if (!ChocoConfig.COMMON.add_blank_ToDungeonLoot.get()) return;

        ResourceLocation lootTable = event.getName();
        if (!lootTable.getPath().startsWith("chests/")) return;

        event.getTable().addPool(getInjectPool());
    }

    public static LootPool getInjectPool() {
        LootPool.Builder builder = LootPool.lootPool();
        // builder.add(injectFruit(item.get()));
        builder.setBonusRolls(UniformGenerator.between(0, 1))
                .name("_blank_");
        return builder.build();
    }

    private static LootPoolEntryContainer.Builder injectFruit(Item item) {
        return LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                .setQuality(1)
                .setWeight(ChocoConfig.COMMON._blank_DungeonLootWeight.get());
    }
    */
}
