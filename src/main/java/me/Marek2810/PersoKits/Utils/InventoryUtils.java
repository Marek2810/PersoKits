package me.Marek2810.PersoKits.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtils {
	
	public static boolean canBeAdded(Inventory inv, ItemStack item) {
		boolean output = false;
		if (inv.firstEmpty() > -1) return true;
		for (int i = 0; i < inv.getSize(); i ++) {
			ItemStack invItem = inv.getItem(i);
			ItemMeta itemMeta = item.getItemMeta();
			ItemMeta invItemMeta = invItem.getItemMeta();			
			if (!item.getType().equals(invItem.getType())) continue;
			if (itemMeta.hasCustomModelData() != invItemMeta.hasCustomModelData()) continue;
			if (itemMeta.hasCustomModelData()) {
				if (itemMeta.getCustomModelData() != invItemMeta.getCustomModelData())continue;
			}
			if (invItem.getAmount() == invItem.getMaxStackSize()) continue;
			output = true;
			break;
		}		
		return output;
	}
	
	public static boolean canBeAdded(Inventory pinv, List<ItemStack> items) {
		boolean output = false;
		Inventory inv = Bukkit.createInventory(null, 36);
		inv.setStorageContents(pinv.getStorageContents());
		for (ItemStack item : items) {
			output = canBeAdded(inv, item);
			if (!output) break;
			inv.addItem(item);
		}
		return output;
	}

}
