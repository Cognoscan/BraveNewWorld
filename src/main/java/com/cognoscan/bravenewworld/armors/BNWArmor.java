package com.cognoscan.bravenewworld.armors;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BNWArmor extends ItemArmor
{
	public abstract String getName();
	
	private String subName;
	
	public BNWArmor(ItemArmor.ArmorMaterial material, int armorType, String subName)
	{
		super(material, 0, armorType);
		this.subName = subName;
		GameRegistry.registerItem(this, getName());
		this.setUnlocalizedName(BraveNewWorld.MODID + "_" + getName());
	}
	
	public String getSubName() {
		return subName;
	}
	
	public void RegisterModel(ItemModelMesher mesher)
	{
		mesher.register(this, 0, new ModelResourceLocation(BraveNewWorld.MODID + ":" + getName(), "inventory"));
	}
	
}