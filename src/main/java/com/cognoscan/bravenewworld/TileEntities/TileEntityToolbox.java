package com.cognoscan.bravenewworld.TileEntities;

import java.util.Iterator;
import java.util.List;

import com.cognoscan.bravenewworld.BraveNewWorld;
import com.cognoscan.bravenewworld.Blocks.Toolbox;

import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityToolbox extends TileEntity implements IUpdatePlayerListBox, IInventory
{
	// 0-26 are inventory
	// 27 is X Coord slot
	// 28 is Y Coord slot
	// 29 is Z Coord slot
    private ItemStack[] chestContents = new ItemStack[31];
    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;
    /** The angle of the lid last tick */
    public float prevLidAngle;
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    
    private int xDist;
    private int yDist;
    private int zDist;
    private boolean isOn;
    private BlockPos nextBlock;
    
    public void setDist(int dim, BlockPos markerPos) {
    	if (!isOn)
    	{
    		switch(dim) {
    		case 0: xDist = markerPos.getX(); break;
    		case 1: yDist = markerPos.getY(); break;
    		case 2: zDist = markerPos.getZ(); break;
    		}

    		this.worldObj.destroyBlock(markerPos.east().down(), true);
    	}
    }

    public int getSizeInventory() { return 31; }

    public ItemStack getStackInSlot(int index)
    {
    	if (index >= 27 && index <= 29) {
    		ItemStack marker = new ItemStack(BraveNewWorld.boxMarkerItem);
    		NBTTagCompound nbt = new NBTTagCompound();
    		nbt.setInteger("dim", index - 27);
    		nbt.setInteger("boxX", pos.getX());
    		nbt.setInteger("boxY", pos.getY());
    		nbt.setInteger("boxZ", pos.getZ());
    		marker.setTagCompound(nbt);
    		return marker;
    	} else {
    		return this.chestContents[index];
    	}
    }
    
    public int getInventoryStackLimit() { return 64; }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
    	if (index < 27 || index == 30)
    	{
    		if (this.chestContents[index] != null)
    		{
    			ItemStack itemstack;

    			if (this.chestContents[index].stackSize <= count)
    			{
    				itemstack = this.chestContents[index];
    				this.chestContents[index] = null;
    				this.markDirty();
    				return itemstack;
    			}
    			else
    			{
    				itemstack = this.chestContents[index].splitStack(count);

    				if (this.chestContents[index].stackSize == 0)
    				{
    					this.chestContents[index] = null;
    				}

    				this.markDirty();
    				return itemstack;
    			}
    		}
    		else
    		{
    			return null;
    		}
    	} else if (index >= 27 && index < 30) {
    		return getStackInSlot(index);
    	} else {
    		return null;
    	}
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.chestContents[index] != null)
        {
            ItemStack itemstack = this.chestContents[index];
            this.chestContents[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.chestContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public String getName()
    {
        return "container.toolbox.name";
    }
    
    public boolean hasCustomName() { return false; }
    
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
        this.xDist = compound.getInteger("xDist");
        this.yDist = compound.getInteger("yDist");
        this.zDist = compound.getInteger("zDist");
        this.isOn = compound.getBoolean("isOn");
        this.nextBlock = new BlockPos(
        		compound.getInteger("nextX"),
        		compound.getInteger("nextY"),
        		compound.getInteger("nextZ"));
        
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        
        compound.setInteger("xDist", xDist);
        compound.setInteger("yDist", yDist);
        compound.setInteger("zDist", zDist);
        compound.setBoolean("isOn", isOn);
        compound.setInteger("nextX", nextBlock.getX());
        compound.setInteger("nextY", nextBlock.getY());
        compound.setInteger("nextZ", nextBlock.getZ());
        
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : 
        	player.getDistanceSq(
        			(double)this.pos.getX() + 0.5D,
        			(double)this.pos.getY() + 0.5D,
        			(double)this.pos.getZ() + 0.5D) 
        			<= 64.0D;
    }


    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;
        float f;

        // Refresh number of connected players on this inventory
        if (!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;
            f = 5.0F;
            List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, 
            		new AxisAlignedBB(
            				(double)((float)i - f), 
            				(double)((float)j - f), 
            				(double)((float)k - f), 
            				(double)((float)(i + 1) + f), 
            				(double)((float)(j + 1) + f), 
            				(double)((float)(k + 1) + f)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();

                if (entityplayer.openContainer instanceof ContainerChest)
                {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory();

                    if (iinventory == this)
                    {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d2;

        // Open chest
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double d1 = (double)i + 0.5D;
            d2 = (double)k + 0.5D;

            this.worldObj.playSoundEffect(d1, (double)j + 0.5D, d2,
            		"random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        // Handle the opening and closing of the chest
        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            // Open or close as required
            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            // Play closing sound effect when past the halfway point.
            if (this.lidAngle < 0.5f && f1 >= 0.5f)
            {
                d2 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;

                this.worldObj.playSoundEffect(d2, (double)j + 0.5D, d0, 
                		"random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
        }
        
        // Angle max is 1.0f
        if (this.lidAngle > 1.0F)
        {
            this.lidAngle = 1.0F;
        }
        // Angle min is 0.0f
        if (this.lidAngle < 0.0F)
        {
            this.lidAngle = 0.0F;
        }
    }

    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
        }
    }

    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof Toolbox)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
    	if (index >= 27) return false;
        return true;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    public String getGuiID()
    {
        return "bravenewworld:toolbox";
    }


    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        for (int i = 0; i < this.chestContents.length; ++i)
        {
            this.chestContents[i] = null;
        }
    }

}