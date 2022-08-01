package com.dephoegon.chococraft.common.init;

import com.dephoegon.chococraft.Chococraft;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Chococraft.MOD_ID);

	public static final RegistryObject<Attribute> MAX_STAMINA = ATTRIBUTES.register("chocobo_max_stamina", () ->
			new RangedAttribute("chocobo.maxStamina", 10.0D, 10D, 1024.0D).setSyncable(true));
}
