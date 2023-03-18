package com.dephoegon.delchoco.aid;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;
import static com.dephoegon.delchoco.common.init.ModRegistry.GYSAHL_GREEN_SEEDS;

@Mod.EventBusSubscriber(modid = DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class creativeTabAid {
    public static CreativeModeTab CHOCO_TAB;
    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event){
        CHOCO_TAB = event.registerCreativeModeTab(new ResourceLocation(DELCHOCO_ID, "dephoegon_chocobos"),
                builder -> builder.icon(() -> new ItemStack(GYSAHL_GREEN_SEEDS.get()))
                        .title(Component.translatable("itemGroup.dephoegon_chocobos")));
    }
}
