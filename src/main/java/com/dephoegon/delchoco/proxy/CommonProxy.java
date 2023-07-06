package com.dephoegon.delchoco.proxy;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.network.OwnerSyncShowStatsPacket;
import com.dephoegon.delchoco.common.network.PlayWhistlePacket;
import com.dephoegon.delchoco.common.network.PressKeyPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;

import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy implements IProxy
{
    public static void setup(FMLCommonSetupEvent event)
    {
        String version = DelChoco.info.getVersion().toString();

        DelChoco.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(DelChoco.DELCHOCO_ID, "callablehorseschannel"), () -> version, version::equals, version::equals);

        DelChoco.network.registerMessage(1, OwnerSyncShowStatsPacket.class, OwnerSyncShowStatsPacket::toBytes, OwnerSyncShowStatsPacket::new, OwnerSyncShowStatsPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        DelChoco.network.registerMessage(2, PressKeyPacket.class, PressKeyPacket::toBytes, PressKeyPacket::new, PressKeyPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        DelChoco.network.registerMessage(3, PlayWhistlePacket.class, PlayWhistlePacket::toBytes, PlayWhistlePacket::new, PlayWhistlePacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    @Override
    public Level getWorld()
    {
        return null;
    }

    @Override
    public Player getPlayer()
    {
        return null;
    }

    @Override
    public void displayStatViewer()
    {
    }

}
