package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.client.ClientHandler;
import com.dephoegon.delchoco.client.models.armor.ChocoDisguiseModel;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.dephoegon.delbase.item.shiftingDyes.CLEANSE_SHIFT_DYE;
import static com.dephoegon.delchoco.aid.dyeList.CHOCO_COLOR_ITEMS;
import static com.dephoegon.delchoco.common.init.ModArmorMaterial.*;

public class ChocoDisguiseItem extends ArmorItem {
	private final LazyLoadedValue<HumanoidModel<?>> model;
	public String NBTKEY_COLOR = "Color";

	public ChocoDisguiseItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
		this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(this.slot)), () -> () -> null);
	}
	public String setCustomModel(@NotNull String customModelData) {
		ArmorMaterial armor = getMaterial();
		String folder = "armor";
		if (armor == IRON_CHOCO_DISGUISE) { folder = "iron"; }
		if (armor == DIAMOND_CHOCO_DISGUISE) { folder = "diamond"; }
		if (armor == NETHERITE_CHOCO_DISGUISE) { folder = "netherite"; }
		folder = "armor"; // Override till Textures are filled out
		/*
			Insert Material Based picking, atm uses same as base.
			Create patches of iron, diamond, & netherite showing for those tiers in the template
		*/
		return switch (customModelData) {
			default -> DelChoco.MOD_ID + ":textures/models/"+folder+"/yellow.png";
			case "green" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/green.png";
			case "pink" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/pink.png";
			case "red" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/red.png";
			case "blue" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/blue.png";
			case "gold" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/gold.png";
			case "black" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/black.png";
			case "flame" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/flame.png";
			case "white" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/white.png";
			case "purple" -> DelChoco.MOD_ID + ":textures/models/"+folder+"/purple.png";
		};
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
	public String getArmorTexture(@NotNull ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		CompoundTag color = stack.getTag();
		if (color == null) { return setCustomModel("yellow"); }
		return setCustomModel(color.getString(NBTKEY_COLOR));
	}
	private @NotNull String getCustomModelData(@NotNull ItemStack itemStack) {
		CompoundTag out = itemStack.getTag();
		if (out == null) { return "yellow"; }
		return out.getString(NBTKEY_COLOR);
	}
	public CompoundTag serialize(String key, String string) {
		CompoundTag nbt = new CompoundTag();
		nbt.putString(key, string);
		return nbt;
	}
	private void setNBT(@NotNull ItemStack itemStack, @NotNull ItemStack shrinkMe, String colorValue) {
		itemStack.setTag(serialize(NBTKEY_COLOR, colorValue));
		shrinkMe.shrink(1);
	}
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
		ItemStack mainHand = pPlayer.getItemBySlot(EquipmentSlot.MAINHAND);
		ItemStack offHand = pPlayer.getItemBySlot(EquipmentSlot.OFFHAND);
		ItemStack outHand = mainHand;
		if (mainHand.getItem() instanceof ChocoDisguiseItem) {
			if (CHOCO_COLOR_ITEMS.containsKey(offHand.getItem())) {
				ChocoboColor chocoboColor = CHOCO_COLOR_ITEMS.get(offHand.getItem());
				if (mainHand.getTag() != null) {
					String itemColor = mainHand.getTag().getString(NBTKEY_COLOR);
					if (!itemColor.equals(itemColor(chocoboColor))) {
						if (itemColor.equals("yellow")) { setNBT(mainHand, offHand, itemColor(chocoboColor)); }
					} else if (offHand.getItem().getDefaultInstance() == CLEANSE_SHIFT_DYE.get().getDefaultInstance()) {
						if (!itemColor.equals("yellow")) { setNBT(mainHand, offHand, "yellow"); }
					}
				} else { setNBT(mainHand, offHand, itemColor(chocoboColor)); }
			}
		}
		if (offHand.getItem() instanceof ChocoDisguiseItem) {
			if (CHOCO_COLOR_ITEMS.containsKey(mainHand.getItem())) {
				ChocoboColor chocoboColor = CHOCO_COLOR_ITEMS.get(mainHand.getItem());
				if (offHand.getTag() != null) {
					String itemColor = offHand.getTag().getString(NBTKEY_COLOR);
					if (!itemColor.equals(itemColor(chocoboColor))) {
						if (itemColor.equals("yellow")) { setNBT(offHand, mainHand, itemColor(chocoboColor)); }
					} else if (mainHand.getItem().getDefaultInstance() == CLEANSE_SHIFT_DYE.get().getDefaultInstance()) {
						if (!itemColor.equals("yellow")) { setNBT(offHand, mainHand, "yellow"); }
					}
				} else { setNBT(offHand, mainHand, itemColor(chocoboColor)); }
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
		tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".choco_disguise_"+getCustomModelData(stack)));
	}
	public String getNBTKEY_COLOR() {
		ItemStack stack = new ItemStack(this);
		if (stack.getTag() != null) {
			return stack.getTag().getString(NBTKEY_COLOR);
		} else { return "yellow"; }
	}
	public boolean isFireResistant() {
		String color = getNBTKEY_COLOR();
		if (color.equals("gold") || color.equals("flame")) { return true; }
		return super.isFireResistant();
	}
}