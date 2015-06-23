package com.cognoscan.bravenewworld;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BraveNewWorld.MODID, version = BraveNewWorld.VERSION, name = BraveNewWorld.NAME)
public class BraveNewWorld
{
    public static final String MODID = "bravenewworld";
    public static final String VERSION = "0.01";
    public static final String NAME = "bravenewworld";
    
    public static BNWItem arcaniteIngot;
    public static BNWItem arcaniteDust;
    public static BNWItem jobOrder;
    public static BNWItem steelIngot;
    public static BNWItem steelNugget;
    public static BNWItem tallowBrick;
    public static BNWItem wroughtIron;
    
    @Instance(value = BraveNewWorld.MODID) // Tell Forge what instance to use
	public static BraveNewWorld instance;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		arcaniteIngot = new ArcaniteIngot();
		arcaniteDust = new ArcaniteDust();
		jobOrder = new JobOrder();
		steelIngot = new SteelIngot();
		steelNugget = new SteelNugget();
		tallowBrick = new TallowBrick();
		wroughtIron = new WroughtIron();
		
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	GameRegistry.addShapelessRecipe(new ItemStack(steelNugget, 9), steelIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(steelIngot),"AAA","AAA","AAA",'A',steelNugget);
    	
    	// Generate Items
    	if (event.getSide() == Side.CLIENT)
		{
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			
			arcaniteIngot.RegisterModel(mesher);
			arcaniteDust.RegisterModel(mesher);
			jobOrder.RegisterModel(mesher);
			steelIngot.RegisterModel(mesher);
			steelNugget.RegisterModel(mesher);
			tallowBrick.RegisterModel(mesher);
			wroughtIron.RegisterModel(mesher);
		}
    }
}
