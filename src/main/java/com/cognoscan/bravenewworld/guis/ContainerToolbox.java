package com.cognoscan.bravenewworld.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import com.cognoscan.bravenewworld.TileEntities.TileEntityToolbox;

public class ContainerToolbox extends Container {
	private TileEntityToolbox toolbox;
	
	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
	
	private final int TOOLBOX_ROW_COUNT = 3;
	private final int TOOLBOX_COLUMN_COUNT = 9;
	private final int TOOLBOX_SLOT_COUNT = TOOLBOX_ROW_COUNT * TOOLBOX_COLUMN_COUNT;
	private final int TOOLBOX_X_SLOT = TOOLBOX_SLOT_COUNT;
	private final int TOOLBOX_Y_SLOT = TOOLBOX_X_SLOT + 1;
	private final int TOOLBOX_Z_SLOT = TOOLBOX_Y_SLOT + 1;
	private final int TOOLBOX_PICK_SLOT = TOOLBOX_Z_SLOT + 1;
	
	private final int TOOLBOX_PICK_SLOT_INDEX = VANILLA_SLOT_COUNT + TOOLBOX_SLOT_COUNT + 3;
	private final int TOOLBOX_X_SLOT_INDEX = VANILLA_SLOT_COUNT + TOOLBOX_SLOT_COUNT;
	
	public ContainerToolbox(InventoryPlayer invPlayer, TileEntityToolbox toolbox) {
		this.toolbox = toolbox;
		final int SLOT_X_SPACING = 18;
		final int SLOT_Y_SPACING = 18;
		
		int slotNumber;
		int xpos;
		int ypos;
		
		final int HOTBAR_XPOS = 8;
		final int HOTBAR_YPOS = 198;
		// Add the player's hotbar to the gui - the [xpos, ypos] location of each item
		for (int x=0; x < HOTBAR_SLOT_COUNT; x++) {
			slotNumber = x;
			addSlotToContainer(new Slot(invPlayer, slotNumber, HOTBAR_XPOS+SLOT_X_SPACING * x, HOTBAR_YPOS));
		}
		
		final int PLAYER_INVENTORY_XPOS = 8;
		final int PLAYER_INVENTORY_YPOS = 140;
		// Add the rest of the players inventory to the gui
		for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
			for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
				slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
				xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
				ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(invPlayer, slotNumber,  xpos, ypos));
			}
		}
		
		final int TOOLBOX_INVENTORY_XPOS = 8;
		final int TOOLBOX_INVENTORY_YPOS = 72;
		// Add the rest of the Toolbox inventory to the gui
		for (int y = 0; y < TOOLBOX_ROW_COUNT; y++) {
			for (int x = 0; x < TOOLBOX_COLUMN_COUNT; x++) {
				slotNumber = y * TOOLBOX_COLUMN_COUNT + x;
				xpos = TOOLBOX_INVENTORY_XPOS + x * SLOT_X_SPACING;
				ypos = TOOLBOX_INVENTORY_YPOS + y * SLOT_Y_SPACING;
				addSlotToContainer(new Slot(toolbox, slotNumber,  xpos, ypos));
			}
		}
		
		addSlotToContainer(new SlotMarker(toolbox, TOOLBOX_X_SLOT, 80, 45));
		addSlotToContainer(new SlotMarker(toolbox, TOOLBOX_Y_SLOT, 116, 45));
		addSlotToContainer(new SlotMarker(toolbox, TOOLBOX_Z_SLOT, 152, 45));
		addSlotToContainer(new SlotPickaxe(toolbox, TOOLBOX_PICK_SLOT, 116, 9));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return toolbox.isUseableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
	{
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();
		
		// Attempt to place pickaxe appropriately, and avoid plaing into X/Y/Z slots
		if (sourceSlotIndex >= 0 && sourceSlotIndex < VANILLA_SLOT_COUNT) {
			if (sourceStack.getItem() instanceof ItemPickaxe) {
				// Pickaxes don't go into the inventory as a result of this
				if (!mergeItemStack(sourceStack, TOOLBOX_PICK_SLOT_INDEX, TOOLBOX_PICK_SLOT_INDEX+1, false)) {
					return null;
				}
			} else if (!mergeItemStack(sourceStack, HOTBAR_SLOT_COUNT+PLAYER_INVENTORY_SLOT_COUNT, TOOLBOX_X_SLOT_INDEX, false)) {
				return null;
			} else {
				return null; // Don't let it go into X/Y/Z slots
			}
		}
		
		if (sourceStack.stackSize == 0) {
			sourceSlot.putStack(null);
		} else {
			sourceSlot.onSlotChanged();
		}
		
		sourceSlot.onPickupFromSlot(player, sourceStack);
		return copyOfSourceStack;
	}
	
	public class SlotPickaxe extends Slot {
		public SlotPickaxe(IInventory inventoryIn, int index, int xPos, int yPos) {
			super(inventoryIn, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return (stack.getItem() instanceof ItemPickaxe);
		}
	}
	
	public class SlotMarker extends Slot {
		public SlotMarker(IInventory inventoryIn, int index, int xPos, int yPos) {
			super(inventoryIn, index, xPos, yPos);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
}
