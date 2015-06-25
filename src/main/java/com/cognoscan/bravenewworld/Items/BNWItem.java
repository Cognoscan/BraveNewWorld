package com.cognoscan.bravenewworld.Items;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BNWItem extends Item
{
	
	public abstract String getName();
	
	public BNWItem()
	{
		GameRegistry.registerItem(this, getName());
		this.setUnlocalizedName(BraveNewWorld.MODID + "_" + getName());
	}
	
	public void RegisterModel(ItemModelMesher mesher)
	{
		mesher.register(
				this, 
				0,
				new ModelResourceLocation(BraveNewWorld.MODID + ":" + getName(), "inventory")
				);
	}
	
}