package com.dephoegon.delchoco.aid;

import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.DelChoco.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class creativeTabAid {
    public static CreativeModeTab CHOCO_TAB;
    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.@NotNull Register event){
        CHOCO_TAB = event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "dephoegon_blocks"),
                builder -> builder.icon(() -> new ItemStack(ModRegistry.GYSAHL_GREEN.get()))
                        .title(Component.translatable("itemGroup.delchoco")));
    }
}
