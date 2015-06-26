package com.cognoscan.bravenewworld.armors;

import com.cognoscan.bravenewworld.BraveNewWorld;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ArcaniteArmor extends BNWArmor
{
	
	public ArcaniteArmor(ArmorMaterial material, int armorType, String subName) {
		super(material, armorType, subName);
	}

	@Override
	public String getName () {
		return "arcanite" + getSubName();
	}
	
	@Override
	public void onArmorTick (World world, EntityPlayer player, ItemStack stack)
	{
		/* For reference
		if (stack.getItem() == MeinMod.tealHelmet)
		{
			if (player.isInWater())
			{
				player.setAir(200);
			}
		}
		*/
		
	}
	
}