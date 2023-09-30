package com.dephoegon.delchoco.common.handler;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.handler.loot.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class lootTableEventBus {
    @SubscribeEvent
    public static void registerModifierSerializer(final RegistryEvent.@NotNull Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new desertChestAdditionModifier.Serializer().setRegistryName(new ResourceLocation(DelChoco.DELCHOCO_ID, "desert_chest_addition")),
                new endCityChestAdditionModifier.Serializer().setRegistryName(new ResourceLocation(DelChoco.DELCHOCO_ID, "end_city_chest_addition")),
                new grassBlockLootAdditionModifier.Serializer().setRegistryName(new ResourceLocation(DelChoco.DELCHOCO_ID, "grass_block_addition")),
                new jungleTempleChestAdditionModifier.Serializer().setRegistryName(new ResourceLocation(DelChoco.DELCHOCO_ID, "jungle_temple_chest_addition")),
                new woodlandMansionChestAdditionModifier.Serializer().setRegistryName(new ResourceLocation(DelChoco.DELCHOCO_ID, "woodland_mansion_chest_addition"))
        );
    }
}