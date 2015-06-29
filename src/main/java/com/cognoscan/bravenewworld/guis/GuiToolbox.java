package com.cognoscan.bravenewworld.guis;

import com.cognoscan.bravenewworld.TileEntities.TileEntityToolbox;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiToolbox extends GuiContainer {
	
	private static final ResourceLocation texture = new ResourceLocation("bravenewworld", "textures/gui/toolbox.png");
	private TileEntityToolbox toolbox;
	
	public GuiToolbox(InventoryPlayer invPlayer, TileEntityToolbox toolbox) {
		super(new ContainerToolbox(invPlayer, toolbox));
		
		xSize = 176;
		ySize = 222;
		
		this.toolbox = toolbox;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		// Bind the image texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		// Draw the image
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
