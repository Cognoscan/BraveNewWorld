package com.cognoscan.bravenewworld.guis;

import com.cognoscan.bravenewworld.TileEntities.TileEntityToolbox;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerToolbox implements IGuiHandler {

	private static final int GUI_ID_TOOLBOX = 1;
	public static int getGuiId() { return GUI_ID_TOOLBOX; }
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID != getGuiId()) {
			System.err.println("Invalid ID: expected" + getGuiId() + ", received " + ID);
		}
		
		BlockPos xyz = new BlockPos(x,y,z);
		TileEntity tileEntity = world.getTileEntity(xyz);
		if (tileEntity instanceof TileEntityToolbox) {
			return new ContainerToolbox(player.inventory, (TileEntityToolbox)tileEntity);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (ID != getGuiId()) {
			System.err.println("Invalid ID: expected" + getGuiId() + ", received " + ID);
		}
		
		BlockPos xyz = new BlockPos(x,y,z);
		TileEntity tileEntity = world.getTileEntity(xyz);
		if (tileEntity instanceof TileEntityToolbox) {
			return new GuiToolbox(player.inventory, (TileEntityToolbox)tileEntity);
		}
		return null;
	}

}
