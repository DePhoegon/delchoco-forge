package com.dephoegon.delchoco.common.items;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CustomBlockNamedItem extends ItemNameBlockItem {
	private final Supplier<Block> blockSupplier;
	@SuppressWarnings("DataFlowIssue")
	public CustomBlockNamedItem(Supplier<Block> blockSupplier, Properties properties) {
		super(null, properties);
		this.blockSupplier = blockSupplier;
	}
	@Override
	public @NotNull Block getBlock() { return this.blockSupplier.get() == null ? null : this.blockSupplier.get(); }
}