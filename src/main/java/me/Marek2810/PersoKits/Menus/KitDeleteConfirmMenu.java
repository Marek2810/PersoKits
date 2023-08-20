package me.Marek2810.PersoKits.Menus;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.ItemBuilder;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PersoKitsMenu;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class KitDeleteConfirmMenu extends PersoKitsMenu {

	public KitDeleteConfirmMenu(PlayerMenuUtility pMenuUtil) {
		super(pMenuUtil);
	}

	@Override
	public String getTitle() {
		return MenuUtils.getText("KitDeleteConfirmMenu", "title");
	}

	@Override
	public int getRows() {
		return 2;
	}

	@Override
	public void setMenuItems() {
		inv.setItem(0, backMenuItem);
		inv.setItem(4, MenuUtils.getMenuItem(pMenuUtil.getKit()));
		
		ItemStack yesItem = new ItemBuilder(Material.GREEN_CONCRETE)
				.name(MenuUtils.getText("KitDeleteConfirmMenu", "yes-item-name"))
				.function("yes")
				.make();
		inv.setItem(12, yesItem);
		
		ItemStack noItem = new ItemBuilder(Material.RED_CONCRETE)
				.name(MenuUtils.getText("KitDeleteConfirmMenu", "no-item-name"))
				.function("no")
				.make();
		inv.setItem(14, noItem);
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if (item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING) != null) {
			String function = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING);
			if (function.equalsIgnoreCase("yes")) {
				PersoKit kit = PersoKits.kits.get(pMenuUtil.getKit());
				kit.remove();
				p.closeInventory();
				String msg = ChatUtils.getMessage("kit-removed");
				p.sendMessage(ChatUtils.formatWithPlaceholders(p, msg, kit));
				return;
			}
			else if (function.equalsIgnoreCase("no")) {
				p.closeInventory();
				return;
			}
			else if (function.equals("editKit")) {					
				new KitEditMenu(pMenuUtil).open();
				return;								
			}
			else if (function.equals("backMenu")) {
				new KitsMenu(pMenuUtil).open();
				return;
			} 			
		}
	}

}
