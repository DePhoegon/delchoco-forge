package com.dephoegon.delchoco.common.events;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.aid.composable;
import com.dephoegon.delchoco.common.commands.chocoboTeams;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ModDataSerializers;
import com.dephoegon.delchoco.common.network.PacketManager;
import com.dephoegon.delchoco.utils.Log4jFilter;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import static com.dephoegon.delchoco.common.entities.Chocobo.createAttributes;
import static com.dephoegon.delchoco.common.init.ModEntities.CHOCOBO;

@Mod.EventBusSubscriber(modid = DelChoco.DELCHOCO_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModDataSerializers.init();
        PacketManager.init();
        Log4jFilter.init();
        event.enqueueWork(() -> SpawnPlacements.register(CHOCOBO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Chocobo::canSpawn));
    }
    @SubscribeEvent
    public static void registerEntityAttributes(@NotNull EntityAttributeCreationEvent event) { event.put(CHOCOBO.get(), createAttributes().build()); }
    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load event) { composable.addToList(); }
    @SubscribeEvent
    public void onRegisterCommandsEvent(@NotNull RegisterCommandsEvent event) { chocoboTeams.commands(event.getDispatcher()); }
}
