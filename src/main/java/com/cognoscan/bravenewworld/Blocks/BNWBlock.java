package com.cognoscan.bravenewworld.Blocks;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BNWBlock extends Block
{
	public abstract String getName();
	
	public BNWBlock (Material material)
	{
		super(material);
		GameRegistry.registerBlock(this, getName());
		setUnlocalizedName(BraveNewWorld.MODID + "_" + getName());
	}
	
	public void RegisterModel(ItemModelMesher mesher)
	{
		mesher.register(
				Item.getItemFromBlock(this), 
				0,
				new ModelResourceLocation(BraveNewWorld.MODID + ":" + getName(), "inventory")
				);
	}
	

}