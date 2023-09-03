package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PaginatedMenu;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitsMenu extends PaginatedMenu {
	
	private Set<String> kitsSet = PersoKits.kits.keySet();
	private List<String> kits = new ArrayList<>();
	
	public PKitsMenu(PlayerMenuUtility util) {
		super(util);
		for (String name : kitsSet) {
			if (!PersoKits.kits.get(name).isPersokit()) continue;
			if (!PersoKits.kits.get(name).permittedToUse(util.getOwner())) continue;
			kits.add(name);
		}
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
		int slots = getRows()*9;
		maxItemsPerPage = slots-18;
		
		for (int i = 0; i < getMaxItemsPerPage(); i++) {
			index = getMaxItemsPerPage() * page + i;
			if (index >= kits.size()) break;
			ItemStack item = MenuUtils.getMenuItem(kits.get(index));
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
				new PKitEditMenu(pMenuUtil).open();
				return;
			}
			else if (function.equals("nextPage")) {	
				if ((index+1) >= kits.size()) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-last-page")));
					return;
				}
				page += 1;
				super.open();
				return;
			}
			else if (function.equals("previousPage")) {
				if (page == 0) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-first-page")));
					return;
				}
				page -= 1;
				super.open();
				return;
			}
		}
	}	

}
