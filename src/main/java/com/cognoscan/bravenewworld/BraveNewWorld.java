package com.cognoscan.bravenewworld;

import com.cognoscan.bravenewworld.Blocks.*;
import com.cognoscan.bravenewworld.Items.*;
import com.cognoscan.bravenewworld.Renderers.*;
import com.cognoscan.bravenewworld.TileEntities.*;
import com.cognoscan.bravenewworld.armors.*;
import com.cognoscan.bravenewworld.guis.GuiHandlerToolbox;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
    public static BNWBlock arcaniumOre;
    public static BNWBlock boxMarker;
	public static Block toolbox;
	
	public static BNWArmor arcaniteHelmet;
	public static BNWArmor arcaniteChestplate;
	public static BNWArmor arcaniteLeggings;
	public static BNWArmor arcaniteBoots;
	
	public static Item arcanitePickaxe;
	public static Item arcaniteAxe;
	public static Item arcaniteShovel;
	public static Item arcaniteHoe;
	public static Item arcaniteSword;
	
	BNWEventHandler eventHandler;

	ArmorMaterial arcaniteArmor = EnumHelper.addArmorMaterial(
			"arcaniteArmor",
			BraveNewWorld.MODID+":"+"arcaniteArmor",
			15,
			new int[] {2, 6, 5, 2},
			25);
	
	ToolMaterial arcaniteTool = EnumHelper.addToolMaterial(
			"arcaniteTool",
			2, // Harvest Level
			250, // Max uses
			6.0f, // Efficiency
			2.0f, // Damage
			22); // Enchantability
    
    @Instance(value = BraveNewWorld.MODID) // Tell Forge what instance to use
	public static BraveNewWorld instance;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		eventHandler = new BNWEventHandler();
		
		arcaniteIngot = new ArcaniteIngot();
		arcaniumDust = new ArcaniumDust();
		jobOrder = new JobOrder();
		steelIngot = new SteelIngot();
		steelNugget = new SteelNugget();
		tallowBrick = new TallowBrick();
		wroughtIron = new WroughtIron();
		boxMarker = new BoxMarker();
		
		tallowBlock = new TallowBlock();
		arcaniumOre = new ArcaniumOre();
		
		toolbox = new Toolbox();
		
		arcaniteHelmet     = new ArcaniteArmor(arcaniteArmor, 0, "Helmet");
		arcaniteChestplate = new ArcaniteArmor(arcaniteArmor, 1, "Chestplate");
		arcaniteLeggings   = new ArcaniteArmor(arcaniteArmor, 2, "Leggings");
		arcaniteBoots      = new ArcaniteArmor(arcaniteArmor, 3, "Boots");
		
		arcanitePickaxe = new ArcanitePickaxe(arcaniteTool);
		arcaniteAxe     = new ArcaniteAxe(    arcaniteTool);
		arcaniteShovel  = new ArcaniteShovel( arcaniteTool);
		arcaniteHoe     = new ArcaniteHoe(    arcaniteTool);
		arcaniteSword   = new ArcaniteSword(  arcaniteTool);
		
		GameRegistry.registerTileEntity(TileEntityToolbox.class, "TE_toolbox");
		
		GameRegistry.registerWorldGenerator(eventHandler, 0);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(BraveNewWorld.instance, GuiHandlerRegistry.getInstance());
		GuiHandlerRegistry.getInstance().registerGuiHandler(new GuiHandlerToolbox(), GuiHandlerToolbox.getGuiId());
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	///////////////////////////////////////////////////////////////////
    	// Horstpower Recipes
    	///////////////////////////////////////////////////////////////////
    	GameRegistry.addShapelessRecipe(new ItemStack(steelNugget, 9), steelIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(steelIngot),"AAA","AAA","AAA",'A',steelNugget);
    	GameRegistry.addShapedRecipe(new ItemStack(tallowBlock), "AA", "AA", 'A', tallowBrick);
    	GameRegistry.addShapelessRecipe(new ItemStack(tallowBrick, 4), tallowBlock);
    	
    	///////////////////////////////////////////////////////////////////
    	// Lichcraft Recipes
    	///////////////////////////////////////////////////////////////////
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteIngot),
    			" A ",
    			"ABA",
    			" A ",
    			'A', arcaniumDust,
    			'B', Items.iron_ingot);
    	
    	// Armor Recipes
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteHelmet), 
    			"AAA",
    			"A A",
    			'A', arcaniteIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteChestplate), 
    			"A A",
    			"AAA",
    			"AAA",
    			'A', arcaniteIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteLeggings), 
    			"AAA",
    			"A A",
    			"A A",
    			'A', arcaniteIngot);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteBoots), 
    			"A A",
    			"A A",
    			'A', arcaniteIngot);
    	// Tool recipes
    	GameRegistry.addShapedRecipe(new ItemStack(arcanitePickaxe), 
    			"AAA",
    			" B ",
    			" B ",
    			'A', arcaniteIngot,
    			'B', Items.stick);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteAxe), 
    			"AA",
    			"AB",
    			" B",
    			'A', arcaniteIngot,
    			'B', Items.stick);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteHoe), 
    			"AA",
    			"B ",
    			"B ",
    			'A', arcaniteIngot,
    			'B', Items.stick);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteShovel), 
    			"A",
    			"B",
    			"B",
    			'A', arcaniteIngot,
    			'B', Items.stick);
    	GameRegistry.addShapedRecipe(new ItemStack(arcaniteSword), 
    			"A",
    			"A",
    			"B",
    			'A', arcaniteIngot,
    			'B', Items.stick);
    	
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
			boxMarker.RegisterModel(mesher);
			
			tallowBlock.RegisterModel(mesher);
			arcaniumOre.RegisterModel(mesher);
			
			arcaniteHelmet.RegisterModel(mesher);
			arcaniteChestplate.RegisterModel(mesher);
			arcaniteLeggings.RegisterModel(mesher);
			arcaniteBoots.RegisterModel(mesher);
			
			((ArcanitePickaxe)arcanitePickaxe).RegisterModel(mesher);
			((ArcaniteAxe)arcaniteAxe).RegisterModel(mesher);
			((ArcaniteShovel)arcaniteShovel).RegisterModel(mesher);
			((ArcaniteHoe)arcaniteHoe).RegisterModel(mesher);
			((ArcaniteSword)arcaniteSword).RegisterModel(mesher);
			
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToolbox.class, new RenderToolbox());
		}
    }
}
