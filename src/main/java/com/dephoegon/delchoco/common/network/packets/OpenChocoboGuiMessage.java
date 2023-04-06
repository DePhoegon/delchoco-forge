package com.dephoegon.delchoco.common.network.packets;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.items.ChocoboSaddleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkEvent.Context;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OpenChocoboGuiMessage {
	public int entityId;
	public int windowId;
	public CompoundTag saddle;
	public CompoundTag inventory;

	public OpenChocoboGuiMessage(@NotNull Chocobo chocobo, int windowId) {
		this.entityId = chocobo.getId();
		this.windowId = windowId;

		this.saddle = chocobo.saddleItemStackHandler.serializeNBT();
		ItemStack saddleStack = chocobo.getSaddle();
		if(!saddleStack.isEmpty() && saddleStack.getItem() instanceof ChocoboSaddleItem saddleItem) { if(saddleItem.getInventorySize() > 0) { this.inventory = chocobo.chocoboInventory.serializeNBT(); } }
	}
	public OpenChocoboGuiMessage(int entityID, int windowId, CompoundTag saddle, CompoundTag inventory) {
		this.entityId = entityID;
		this.windowId = windowId;

		this.saddle = saddle;
		this.inventory = inventory;
	}
	public void encode(@NotNull FriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.windowId);
		buf.writeNbt(saddle);
		buf.writeBoolean(this.inventory != null);
		if (this.inventory != null) { buf.writeNbt(inventory); }
	}
	@Contract("_ -> new")
	public static @NotNull OpenChocoboGuiMessage decode(final @NotNull FriendlyByteBuf buffer) { return new OpenChocoboGuiMessage(buffer.readInt(), buffer.readInt(), buffer.readNbt(), buffer.readBoolean() ? buffer.readNbt() : null); }
	public void handle(@NotNull Supplier<Context> context) {
		NetworkEvent.Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if(ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
				Minecraft mc = Minecraft.getInstance();
				assert mc.level != null;
				Entity entity = mc.level.getEntity(entityId);
				if (!(entity instanceof Chocobo chocobo)) {
					DelChoco.log.warn("Server send OpenGUI for chocobo with id {}, but this entity does not exist on my side", entityId);
					return;
				}

				com.dephoegon.delchoco.client.gui.ChocoboInventoryScreen.openInventory(windowId, chocobo);
				chocobo.saddleItemStackHandler.deserializeNBT(saddle);
				if (inventory != null) { chocobo.chocoboInventory.deserializeNBT(inventory); }
			}
		});
		ctx.setPacketHandled(true);
	}
}