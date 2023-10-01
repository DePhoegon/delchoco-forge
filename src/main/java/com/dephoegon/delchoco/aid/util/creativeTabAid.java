package com.dephoegon.delchoco.aid.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.dephoegon.delchoco.DelChoco.DELCHOCO_ID;
import static com.dephoegon.delchoco.common.init.ModRegistry.CHOCOBO_SADDLE;
import static com.dephoegon.delchoco.common.init.ModRegistry.GYSAHL_GREEN_SEEDS;

@Mod.EventBusSubscriber(modid = DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class creativeTabAid {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS_CHOCO = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DELCHOCO_ID);
    public static final RegistryObject<CreativeModeTab> CHOCO_TAB = CREATIVE_MODE_TABS_CHOCO.register("dephoegon_chocobos", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(CHOCOBO_SADDLE.get()))
            .title(Component.translatable("itemGroup.dephoegon_chocobos"))
            .build());


}
