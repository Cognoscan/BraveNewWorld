package com.cognoscan.bravenewworld.Items;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArcaniteAxe extends ItemAxe {

	public ArcaniteAxe(Item.ToolMaterial mat)
	{
		super(mat);
		GameRegistry.registerItem(this, getName());
		this.setUnlocalizedName(BraveNewWorld.MODID + "_" + getName());
	}
	
	public String getName() {
		return "arcaniteAxe";
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
