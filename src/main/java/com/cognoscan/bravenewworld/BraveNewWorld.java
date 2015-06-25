package com.cognoscan.bravenewworld;

import com.cognoscan.bravenewworld.Blocks.*;
import com.cognoscan.bravenewworld.Items.*;
import com.cognoscan.bravenewworld.Renderers.*;
import com.cognoscan.bravenewworld.TileEntities.*;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
    public static BNWItem arcaniumDust;
    public static BNWItem jobOrder;
    public static BNWItem steelIngot;
    public static BNWItem steelNugget;
    public static BNWItem tallowBrick;
    public static BNWItem wroughtIron;
    
    public static BNWBlock tallowBlock;
	public static Block toolbox;
    
    @Instance(value = BraveNewWorld.MODID) // Tell Forge what instance to use
	public static BraveNewWorld instance;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		arcaniteIngot = new ArcaniteIngot();
		arcaniumDust = new ArcaniumDust();
		jobOrder = new JobOrder();
		steelIngot = new SteelIngot();
		steelNugget = new SteelNugget();
		tallowBrick = new TallowBrick();
		wroughtIron = new WroughtIron();
		
		tallowBlock = new TallowBlock();
		toolbox = new Toolbox();
		
		GameRegistry.registerTileEntity(TileEntityToolbox.class, "TE_toolbox");
		
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	GameRegistry.addShapelessRecipe(new ItemStack(steelNugget, 9), steelIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(steelIngot),"AAA","AAA","AAA",'A',steelNugget);
    	GameRegistry.addShapedRecipe(new ItemStack(tallowBlock), "AA", "AA", 'A', tallowBrick);
    	GameRegistry.addShapelessRecipe(new ItemStack(tallowBrick, 4), tallowBlock);
    	
    	// Generate Items
    	if (event.getSide() == Side.CLIENT)
		{
			ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
			
			arcaniteIngot.RegisterModel(mesher);
			arcaniumDust.RegisterModel(mesher);
			jobOrder.RegisterModel(mesher);
			steelIngot.RegisterModel(mesher);
			steelNugget.RegisterModel(mesher);
			tallowBrick.RegisterModel(mesher);
			wroughtIron.RegisterModel(mesher);
			
			tallowBlock.RegisterModel(mesher);
			
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToolbox.class, new RenderToolbox());
		}
    }
}
