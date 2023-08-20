package me.Marek2810.PersoKits.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import me.Marek2810.PersoKits.Utils.PersoKitsMenu;

public class MenuListener implements Listener {

	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		
		if (holder instanceof PersoKitsMenu menu) {
			e.setCancelled(true);
//			if (e.getCurrentItem() == null) return;
			menu.handleMenu(e);
		}		
	}
}