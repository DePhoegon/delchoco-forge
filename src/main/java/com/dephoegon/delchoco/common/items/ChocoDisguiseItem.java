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

import static com.dephoegon.delbase.item.shiftingDyes.CLEANSE_SHIFT_DYE;
import static com.dephoegon.delchoco.aid.dyeList.CHOCO_COLOR_ITEMS;
import static com.dephoegon.delchoco.common.init.ModArmorMaterial.*;

public class ChocoDisguiseItem extends ArmorItem {
	private final LazyLoadedValue<HumanoidModel<?>> model;
	private final static String NBTKEY_COLOR = "Color";
	public final static String yellow = "yellow"; // default
	public final static String green = "green";
	public final static String pink = "pink";
	public final static String red = "red";
	public final static String blue = "blue";
	public final static String gold = "gold";
	public final static String black = "black";
	public final static String flame = "flame";
	public final static String white = "white";
	public final static String purple = "purple";

	public ChocoDisguiseItem(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
		ItemStack stack = new ItemStack(this);
		stack.setTag(serialize(NBTKEY_COLOR, yellow));
		this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(this.slot)), () -> () -> null);
	}
	public String setCustomModel(String customModelData) {
		ArmorMaterial armor = getMaterial();
		String folder = DelChoco.MOD_ID + ":textures/models/armor/";
		if (armor == IRON_CHOCO_DISGUISE) { folder = DelChoco.MOD_ID + ":textures/models/iron/"; }
		if (armor == DIAMOND_CHOCO_DISGUISE) { folder = DelChoco.MOD_ID + ":textures/models/diamond/"; }
		if (armor == NETHERITE_CHOCO_DISGUISE) { folder = DelChoco.MOD_ID + ":textures/models/netherite/"; }
		folder = DelChoco.MOD_ID + ":textures/models/armor/"; // Override till Textures are filled out
		/*
			Insert Material Based picking, atm uses same as base.
			Create patches of iron, diamond, & netherite showing for those tiers in the template
		*/
		return switch (customModelData) {
			default -> folder + "yellow.png";
			case green -> folder + "green.png";
			case pink -> folder + "pink.png";
			case red -> folder + "red.png";
			case blue -> folder + "blue.png";
			case gold -> folder + "gold.png";
			case black -> folder + "black.png";
			case flame -> folder + "flame.png";
			case white -> folder + "white.png";
			case purple -> folder + "purple.png";
		};
	}
	private String itemColor(@NotNull ChocoboColor chocoboColor) {
		return switch (chocoboColor) {
			default -> yellow;
			case GREEN -> green;
			case PINK -> pink;
			case RED -> red;
			case BLUE -> blue;
			case GOLD -> gold;
			case BLACK -> black;
			case FLAME -> flame;
			case WHITE -> white;
			case PURPLE -> purple;
		};
	}
	public String getArmorTexture(@NotNull ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		CompoundTag color = stack.getTag();
		if (color != null && color.contains(NBTKEY_COLOR)) { return setCustomModel(color.getString(NBTKEY_COLOR)); }
		return setCustomModel(yellow);
	}
	private String getCustomModelColor(@NotNull ItemStack stack) {
		CompoundTag color = stack.getTag();
		if (color != null && color.contains(NBTKEY_COLOR)) { return color.getString(NBTKEY_COLOR); }
		return yellow;
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
				CompoundTag coloring = mainHand.getTag();
				if (coloring != null && coloring.contains(NBTKEY_COLOR)) {
					String armorColor = coloring.getString(NBTKEY_COLOR);
					if ((armorColor.equals(yellow) || itemColor(chocoboColor).equals(yellow)) && !itemColor(chocoboColor).equals(armorColor)) { setNBT(mainHand, offHand, itemColor(chocoboColor)); }
				} else if (!itemColor(chocoboColor).equals(yellow)) { setNBT(mainHand, offHand, itemColor(chocoboColor)); }
			}
			if (offHand.getItem().getDefaultInstance() == CLEANSE_SHIFT_DYE.get().getDefaultInstance()) {
				CompoundTag coloring = mainHand.getTag();
				if (coloring != null && coloring.contains(NBTKEY_COLOR) && !coloring.getString(NBTKEY_COLOR).equals(yellow)) { setNBT(mainHand, offHand, yellow); }
			}
		}
		if (offHand.getItem() instanceof ChocoDisguiseItem) {
			if (CHOCO_COLOR_ITEMS.containsKey(mainHand.getItem())) {
				ChocoboColor chocoboColor = CHOCO_COLOR_ITEMS.get(mainHand.getItem());
				CompoundTag coloring = offHand.getTag();
				if (coloring != null && coloring.contains(NBTKEY_COLOR)) {
					String armorColor = coloring.getString(NBTKEY_COLOR);
					if ((armorColor.equals(yellow) || itemColor(chocoboColor).equals(yellow)) && !itemColor(chocoboColor).equals(armorColor)) { setNBT(offHand, mainHand, itemColor(chocoboColor)); }
				} else if (!itemColor(chocoboColor).equals(yellow)) { setNBT(offHand, mainHand, itemColor(chocoboColor)); }
			}
			if (mainHand.getItem().getDefaultInstance() == CLEANSE_SHIFT_DYE.get().getDefaultInstance()) {
				CompoundTag coloring = mainHand.getTag();
				if (coloring != null && coloring.contains(NBTKEY_COLOR) && !coloring.getString(NBTKEY_COLOR).equals(yellow)) { setNBT(offHand, mainHand, yellow); }
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
		tooltip.add(new TranslatableComponent("item." + DelChoco.MOD_ID + ".choco_disguise_"+ getCustomModelColor(stack)));
	}
	public String getNBTKEY_COLOR() {
		ItemStack stack = new ItemStack(this);
		if (stack.getTag() != null) {
			return stack.getTag().getString(NBTKEY_COLOR);
		} else { return yellow; }
	}
	public boolean isFireResistant() {
		String color = getNBTKEY_COLOR();
		if (color.equals(gold) || color.equals(flame)) { return true; }
		return super.isFireResistant();
	}
}