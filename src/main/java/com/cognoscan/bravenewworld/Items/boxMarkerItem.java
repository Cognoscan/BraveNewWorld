package com.cognoscan.bravenewworld.Items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import com.cognoscan.bravenewworld.BraveNewWorld;
import com.cognoscan.bravenewworld.TileEntities.TileEntityToolbox;


public class boxMarkerItem extends BNWItem {

	@Override
	public String getName() {
		return "boxMarkerItem";
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	BlockPos placePos = pos.offset(side);
    	if (!worldIn.isBlockModifiable(playerIn, placePos) 
    			|| !worldIn.isAirBlock(placePos)
    			|| !BraveNewWorld.boxMarker.canPlaceBlockAt(worldIn, placePos)) {
    		return false;
    	}
    	
    	worldIn.setBlockState(placePos, BraveNewWorld.boxMarker.getDefaultState(), 3);
    	
    	NBTTagCompound nbtTag = stack.getTagCompound();
		if (nbtTag != null && nbtTag.hasKey("dim")) {
			int dim = nbtTag.getInteger("dim");
			int x = nbtTag.getInteger("boxX");
			int y = nbtTag.getInteger("boxY");
			int z = nbtTag.getInteger("boxZ");
			TileEntity te = worldIn.getTileEntity(new BlockPos(x,y,z));
			if (te instanceof TileEntityToolbox) {
				((TileEntityToolbox)te).setDist(dim, placePos);	
			}
		}
    	
    	--stack.stackSize;
        return true;
    }
	
	@Override
	public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		NBTTagCompound nbtTag = stack.getTagCompound();
		if (nbtTag != null && nbtTag.hasKey("dim")) {
			String str;
			switch(nbtTag.getInteger("dim")) {
			case 0 : str = "X @ "; break;
			case 1 : str = "Y @ "; break;
			case 2 : str = "Z @ "; break;
			default: str = "ERROR @ ";
			}
			tooltip.add(str + nbtTag.getInteger("boxX") + "," + nbtTag.getInteger("boxY") + "," + nbtTag.getInteger("boxZ"));
		}
	}

}
