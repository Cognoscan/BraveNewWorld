package com.cognoscan.bravenewworld;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class BNWEventHandler implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimensionId())
		{
		case -1:  generateNether(world, random, chunkX*16, chunkZ*16);
		case  0: generateSurface(world, random, chunkX*16, chunkZ*16);
		case  1:     generateEnd(world, random, chunkX*16, chunkZ*16);
		}
		
	}
	
	private void generateNether(World world, Random random, int x, int z)
	{
		
	}

	private void generateSurface(World world, Random random, int x, int z)
	{
		addOreSpawn(BraveNewWorld.arcaniumOre, Blocks.stone, world, random, x, z, 4+random.nextInt(4), 4, 1, 16);
	}
	
	private void generateEnd(World world, Random random, int x, int z)
	{
		
	}
	
	public void addOreSpawn(Block block, Block target, World world, Random random,
			int blockXPos, int blockZPos, int maxVeinSize, int chancesToSpawn, int minY, int maxY)
	{
		assert maxY > minY : "The maximum Y must be greater than the Minimum Y";
		assert minY > 0 : "addOreSpawn: The Minimum Y must be greater than 0";
		assert maxY < 256 && maxY > 0 : "addOreSpawn: The Maximum Y must be less than 256 but greater than 0";

		int diffBtwnMinMaxY = maxY - minY;
		for (int x = 0; x < chancesToSpawn; x++)
		{
			int posX = blockXPos + random.nextInt(16);
			int posY = minY + random.nextInt(diffBtwnMinMaxY);
			int posZ = blockZPos + random.nextInt(16);
			(new WorldGenMinable(block.getDefaultState(), maxVeinSize, BlockHelper.forBlock(target))).generate(world, random, new BlockPos(posX,posY,posZ));
		}
	}

}
