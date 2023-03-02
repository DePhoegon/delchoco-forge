package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.client.models.armor.ChocoDisguiseModel;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.dephoegon.delchoco.aid.dyeList.CHOCO_COLOR_ITEMS;

public class ChocoDisguiseItem extends ArmorItem {
	private final LazyLoadedValue<HumanoidModel<?>> model;
	private String resource;
	public String CustomModelData;

	public ChocoDisguiseItem(ArmorMaterial material, EquipmentSlot slot, Properties properties, ChocoboColor color) {
		super(material, slot, properties);
		setCustomModel(color);
		this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(this.slot)), () -> () -> null);
	}
	public void setCustomModel(@NotNull ChocoboColor customModelData) {
		this.resource = switch (customModelData) {
			default -> DelChoco.MOD_ID + ":textures/models/armor/yellow.png";
			case GREEN -> DelChoco.MOD_ID + ":textures/models/armor/green.png";
			case PINK -> DelChoco.MOD_ID + ":textures/models/armor/pink.png";
			case RED -> DelChoco.MOD_ID + ":textures/models/armor/red.png";
			case BLUE -> DelChoco.MOD_ID + ":textures/models/armor/blue.png";
			case GOLD -> DelChoco.MOD_ID + ":textures/models/armor/gold.png";
			case BLACK -> DelChoco.MOD_ID + ":textures/models/armor/black.png";
			case FLAME -> DelChoco.MOD_ID + ":textures/models/armor/flame.png";
			case WHITE -> DelChoco.MOD_ID + ":textures/models/armor/white.png";
			case PURPLE -> DelChoco.MOD_ID + ":textures/models/armor/purple.png";
		};
		this.CustomModelData = itemColor(customModelData);
	}
	private String itemColor(@NotNull ChocoboColor chocoboColor) {
		return switch (chocoboColor) {
			default -> "yellow";
			case GREEN -> "green";
			case PINK -> "pink";
			case RED -> "red";
			case BLUE -> "blue";
			case GOLD -> "gold";
			case BLACK -> "black";
			case FLAME -> "flame";
			case WHITE -> "white";
			case PURPLE -> "purple";
		};
	}
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) { return this.resource; }
	private String getCustomModelData() { return this.CustomModelData; }
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
		ItemStack mainHand = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack offHand = pPlayer.getItemBySlot(EquipmentSlot.OFFHAND);
		ItemStack outHand = mainHand;
		if (mainHand.getItem() instanceof ChocoDisguiseItem armor) {
			if (CHOCO_COLOR_ITEMS.containsKey(offHand.getItem())) {
				if (!armor.getCustomModelData().equals(itemColor(CHOCO_COLOR_ITEMS.get(offHand.getItem())))) {
					armor.setCustomModel(CHOCO_COLOR_ITEMS.get(offHand.getItem()));
					offHand.shrink(1);
				}
			}
		}
		if (offHand.getItem() instanceof ChocoDisguiseItem armor) {
			if (CHOCO_COLOR_ITEMS.containsKey(mainHand.getItem())) {
				if (!armor.getCustomModelData().equals(itemColor(CHOCO_COLOR_ITEMS.get(mainHand.getItem())))) {
					armor.setCustomModel(CHOCO_COLOR_ITEMS.get(mainHand.getItem()));
					mainHand.shrink(1);
				}
			}
			outHand = offHand;
		}
		return InteractionResultHolder.success(outHand);
	}
	@OnlyIn(Dist.CLIENT)
	public HumanoidModel<?> provideArmorModelForSlot(EquipmentSlot slot) { return new ChocoDisguiseModel(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.CHOCO_DISGUISE), slot); }

	public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) { consumer.accept(new IItemRenderProperties() {
		public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) { return model.get(); }
	}); }
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
		super.appendHoverText(stack, level, tooltip, flagIn);
		tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".choco_disguise_"+this.CustomModelData));
	}
}
