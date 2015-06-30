package com.cognoscan.bravenewworld.Renderers;

import com.cognoscan.bravenewworld.TileEntities.TileEntityToolbox;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderToolbox extends TileEntitySpecialRenderer
{
    private static final ResourceLocation textureNormal = new ResourceLocation("bravenewworld:textures/entity/toolbox.png");
    private ModelChest simpleChest = new ModelChest();

    public void renderTileEntityAt(TileEntity tile, double posX, double posZ, double p_180538_6_, float p_180538_8_, int p_180538_9_)
    {
        int j;

        if (!tile.hasWorldObj())
        {
            j = 0;
        }
        else
        {
            Block block = tile.getBlockType();
            j = tile.getBlockMetadata();
        }

        ModelChest modelchest = this.simpleChest;


        if (p_180538_9_ >= 0)
        {
        	this.bindTexture(DESTROY_STAGES[p_180538_9_]);
        	GlStateManager.matrixMode(5890);
        	GlStateManager.pushMatrix();
        	GlStateManager.scale(4.0F, 4.0F, 1.0F);
        	GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
        	GlStateManager.matrixMode(5888);
        }
        else
        {
        	this.bindTexture(textureNormal);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (p_180538_9_ < 0)
        {
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.translate((float)posX, (float)posZ + 1.0F, (float)p_180538_6_ + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        short short1 = 0;

        if (j == 2)
        {
        	short1 = 180;
        }

        if (j == 3)
        {
        	short1 = 0;
        }

        if (j == 4)
        {
        	short1 = 90;
        }

        if (j == 5)
        {
        	short1 = -90;
        }

        GlStateManager.rotate((float)short1, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float prevLidAngle = ((TileEntityToolbox)tile).prevLidAngle;
        float lidAngle = ((TileEntityToolbox)tile).lidAngle;
        float f1 = prevLidAngle + (lidAngle - prevLidAngle) * p_180538_8_;
        float f2;


        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
        modelchest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (p_180538_9_ >= 0)
        {
        	GlStateManager.matrixMode(5890);
        	GlStateManager.popMatrix();
        	GlStateManager.matrixMode(5888);
        }
    }

}