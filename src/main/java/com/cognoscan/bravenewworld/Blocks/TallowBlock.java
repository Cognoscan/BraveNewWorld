package com.cognoscan.bravenewworld.Blocks;

import java.util.Random;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class TallowBlock extends BNWBlock 
{
    public static final Material tallow = new Material(MapColor.sandColor);
	public TallowBlock ()
	{
		super(tallow);
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
    
    // Flammability and Fire Spread Speed are the same as a dead bush.
    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 100;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return true;
    }
    
    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 60;
    }
}
