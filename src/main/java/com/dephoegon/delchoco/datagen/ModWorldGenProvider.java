package com.dephoegon.delchoco.datagen;

import com.dephoegon.delchoco.DelChoco;
import com.dephoegon.delchoco.common.world.worldgen.ModFeatureConfig;
import com.dephoegon.delchoco.common.world.worldgen.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModFeatureConfig::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    public ModWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(DelChoco.DELCHOCO_ID));
    }
}
