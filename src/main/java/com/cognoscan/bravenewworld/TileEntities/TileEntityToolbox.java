package com.cognoscan.bravenewworld.TileEntities;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.cognoscan.bravenewworld.BraveNewWorld;
import com.cognoscan.bravenewworld.Blocks.Toolbox;

import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
    private int posSets;
    private BlockPos nextBlock;
    private int miningCount;
    
    public TileEntityToolbox()
    {
    	super();
        this.nextBlock = new BlockPos(0,0,0);
    }
    
    public void setDist(int dim, BlockPos markerPos) {
    	if (posSets < 7)
    	{
    		switch(dim) {
    		case 0: xDist = markerPos.getX(); posSets |= 0x1; break;
    		case 1: yDist = markerPos.getY(); posSets |= 0x2; break;
    		case 2: zDist = markerPos.getZ(); posSets |= 0x4; break;
    		}
    		
    		if (posSets == 7) {
    			// Setup starting block to mine
    			if (yDist > this.pos.getY()) {
    				nextBlock = new BlockPos(this.pos.getX(), yDist, this.pos.getZ());
    			} else {
    				nextBlock = this.pos;
    			}
    			if (xDist > nextBlock.getX()) nextBlock.add(1, 0, 0); else nextBlock.add(-1, 0, 0);
    			nextBlock.add(0,-1,0);
    			if (zDist > nextBlock.getZ()) nextBlock.add(0, 0, 1); else nextBlock.add( 0, 0,-1);
    		}
    	}
    }
    
    // Mines blocks within the designated area
    public void doMining() {
    	if (posSets != 7) return;
    	if (this.chestContents[30] == null) return;
    	if (!(this.chestContents[30].getItem() instanceof ItemPickaxe)) return;
    	
    	
    	miningCount++;
    	if (miningCount < 4) return;
    	
    	ItemPickaxe pick = (ItemPickaxe) this.chestContents[30].getItem();
    	Block toMine = this.worldObj.getBlockState(nextBlock).getBlock();
    	
    	if (!this.worldObj.isAirBlock(nextBlock)) { 
    		if (pick.canHarvestBlock(toMine)) {
    			List<ItemStack> items;
    			// Harvest direct if silk touch'd
    			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, this.chestContents[30]) > 0)
    	        {
    				int i = 0;
    		        Item item = Item.getItemFromBlock(toMine);

    		        if (item != null && item.getHasSubtypes())
    		        {
    		            i = toMine.getMetaFromState(this.worldObj.getBlockState(nextBlock));
    		        }

    		        items = new java.util.ArrayList<ItemStack>();
    		        ItemStack itemstack = new ItemStack(item, 1, i);
    		        items.add(itemstack);
    	        }
    	        else
    	        {
    	        	// Get itemstacks it would normally drop
    	            int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, this.chestContents[30]);
    	            items = toMine.getDrops(this.worldObj, nextBlock, this.worldObj.getBlockState(nextBlock), i);
    	        }
    			
    			boolean mined = false;
    			for (ItemStack stack : items)
    			{
    				if (this.mergeItemStack(stack)) {
    					mined = true;
    				}
    			}
    			if (mined) {
    				this.worldObj.destroyBlock(nextBlock, false);
    			
    				// Damage the pickaxe if the block is hard and the pickaxe can be damaged.
    				if (toMine.getBlockHardness(this.worldObj, nextBlock) != 0.0f) {
    					if (this.chestContents[30].isItemStackDamageable())
    					{
    						// Attempt to damage item. If it is destroyed, decrease the stack.
    						if (this.chestContents[30].attemptDamageItem(1, this.worldObj.rand))
    						{
    							--this.chestContents[30].stackSize;


    							if (this.chestContents[30].stackSize < 0)
    							{
    								this.chestContents[30].stackSize = 0;
    							}

    							this.chestContents[30].setItemDamage(0);
    						}
    					}
    				}
    			}
    			 miningCount = 0;
    		}
    	}
    	
    	int x = nextBlock.getX();
    	int y = nextBlock.getY();
    	int z = nextBlock.getZ();
    	
    	x += (xDist > this.pos.getX()) ? 1 : -1;
    	if (x == xDist) {
    		x = this.pos.getX();
    		x += (xDist > this.pos.getX()) ? 1 : -1;
    		z += (zDist > this.pos.getZ()) ? 1 : -1;
    		if (z == zDist) {
    			z = this.pos.getZ();
    			z += (zDist > this.pos.getZ()) ? 1 : -1;
    			y--;
    			if (yDist > this.pos.getY()) {
    				if (y == this.pos.getY()) posSets = 8; // Stop mining
    			} else {
    				if (y == yDist) posSets = 8; // Stop mining
    			}
    		}
    	}
		
    	nextBlock = new BlockPos(x,y,z);
    	
    	return;
    }
    
    public boolean mergeItemStack(ItemStack stack) {

        boolean flag1 = false;
        int k = 0;

        ItemStack itemstack1;

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && k < 27)
            {
                itemstack1 = this.chestContents[k];

                if (itemstack1 != null 
                		&& itemstack1.getItem() == stack.getItem() 
                		&& (!stack.getHasSubtypes() || stack.getMetadata() == itemstack1.getMetadata()) 
                		&& ItemStack.areItemStackTagsEqual(stack, itemstack1))
                {
                    int l = itemstack1.stackSize + stack.stackSize;

                    if (l <= stack.getMaxStackSize())
                    {
                        stack.stackSize = 0;
                        itemstack1.stackSize = l;
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < stack.getMaxStackSize())
                    {
                        stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = stack.getMaxStackSize();
                        flag1 = true;
                    }
                }
                
                ++k;
            }
        }

        if (stack.stackSize > 0)
        {
        	k = 0;
            
            while (k < 27)
            {
                itemstack1 = this.chestContents[k];

                if (itemstack1 == null) // Forge: Make sure to respect isItemValid in the slot.
                {
                	this.chestContents[k] = stack;
                    stack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                ++k;
            }
        }

        return flag1;
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
        this.posSets = compound.getInteger("posSets");
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
        compound.setInteger("posSets", posSets);
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

    	doMining();
        
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