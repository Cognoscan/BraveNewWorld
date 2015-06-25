package com.cognoscan.bravenewworld.Blocks;

import java.util.Random;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class TallowBlock extends BNWBlock 
{
	public TallowBlock ()
	{
		super(Material.clay);
	}
	
	@Override
	public String getName()
	{
		return "tallowBlock";
	}	
	
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return BraveNewWorld.tallowBrick;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 4;
    }
}
