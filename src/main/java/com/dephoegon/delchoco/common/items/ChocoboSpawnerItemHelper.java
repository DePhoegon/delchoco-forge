package com.dephoegon.delchoco.common.items;

import com.dephoegon.delchoco.common.entities.Chocobo;
import com.dephoegon.delchoco.common.entities.properties.ChocoboColor;
import com.dephoegon.delchoco.common.init.ModEntities;
import com.dephoegon.delchoco.common.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.BiomeDictionary.Type;

import static net.minecraft.world.level.biome.Biome.getBiomeCategory;
import static net.minecraft.world.level.block.Blocks.AIR;
import static net.minecraftforge.common.BiomeDictionary.hasType;

public class ChocoboSpawnerItemHelper extends Item {
    public ChocoboSpawnerItemHelper(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockPos pos = context.getClickedPos();
        final Player player = context.getPlayer();
        BlockState block = worldIn.getBlockState(pos);
        if (block.getBlock().defaultBlockState() == ModRegistry.CHOCOBO_EGG.get().defaultBlockState()) {
            final Chocobo chocobo = ModEntities.CHOCOBO.get().create(worldIn);
            final Holder<Biome> currentBiome = worldIn.getBiome(pos);
            final ResourceKey<Biome> biomeKey = currentBiome.unwrapKey().get();
            Biome.BiomeCategory biomeCategory = getBiomeCategory(currentBiome);
            ChocoboColor color=null;
            if (hasType(biomeKey, Type.END)) { color = ChocoboColor.PURPLE; }
            if (biomeCategory == Biome.BiomeCategory.NETHER) { 
                color = ChocoboColor.FLAME;
                assert chocobo != null;
                chocobo.setFlame(1);
            }
            if (biomeCategory == Biome.BiomeCategory.MESA) { color = ChocoboColor.RED; }
            if (biomeCategory == Biome.BiomeCategory.MUSHROOM) { color = ChocoboColor.PINK; }
            if (hasType(biomeKey, Type.HOT) && hasType(biomeKey, Type.DRY) &&
                    !(hasType(biomeKey, Type.MESA)) && !(hasType(biomeKey, Type.NETHER))) { color = ChocoboColor.BLACK; }
            if (hasType(biomeKey, Type.SNOWY)) { color = ChocoboColor.WHITE; }
            if (hasType(biomeKey, Type.OCEAN)) { color = ChocoboColor.BLUE; }
            if (biomeCategory == Biome.BiomeCategory.SWAMP) { color = ChocoboColor.GREEN; }
            if (color != null) {
                chocobo.moveTo(pos.getX() + .5, pos.getY() + 0.5F, pos.getZ() + .5, Mth.wrapDegrees(worldIn.random.nextFloat() * 360.0F), 0.0F);
                chocobo.yHeadRot = chocobo.getYRot();
                chocobo.yBodyRot = chocobo.getYRot();
                chocobo.setChocoboColor(color);
                chocobo.finalizeSpawn((ServerLevel)worldIn, worldIn.getCurrentDifficultyAt(chocobo.blockPosition()), MobSpawnType.SPAWN_EGG, (SpawnGroupData)null, (CompoundTag)null);
                worldIn.addFreshEntity(chocobo);
                chocobo.playAmbientSound();
                context.getItemInHand().shrink(1);
                worldIn.setBlockAndUpdate(pos, AIR.defaultBlockState());
            }
        }
        return InteractionResult.SUCCESS;
    }
}
