package me.Marek2810.PersoKits.Menus;

import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PersoKitsMenu;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitsMenu extends PersoKitsMenu {
	
	public PKitsMenu(PlayerMenuUtility util) {
		super(util);
	}
	
	@Override
	public String getTitle() {
		return MenuUtils.getText("pkitsmenu", "title");
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		Set<String> kits = PersoKits.kits.keySet();
		for (String name : kits) {
			if (!PersoKits.kits.get(name).isPersokit()) continue;
			if (!KitUtils.hasPermission(owner, name)) continue;
			ItemStack item = MenuUtils.getMenuItem(name);
			inv.addItem(item);
		}
		
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();	
		Player p = (Player) e.getWhoClicked();		
		if (item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING) != null) {
			String function = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING);
			if (function.equals("close")) {
				p.closeInventory();
				return;
			}
			else if (function.equals("editKit")) {
				String name = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "kitName"), PersistentDataType.STRING);
				pMenuUtil.setKit(name);
				new PKitMenu(pMenuUtil).open();
				return;
			}
		}
	}	

}
