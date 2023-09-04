package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.ItemBuilder;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PaginatedMenu;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class KitsMenu extends PaginatedMenu {
	
	private Set<String> kitsSet = PersoKits.kits.keySet();

	public KitsMenu(PlayerMenuUtility util) {
		super(util);
	}
	
	@Override
	public String getTitle() {
		return MenuUtils.getText("kitsmenu", "title");
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		int slots = getRows()*9;
		maxItemsPerPage = slots-19;
		
		List<String> kits = new ArrayList<>(kitsSet);
		for (int i = 0; i < getMaxItemsPerPage(); i++) {
			index = getMaxItemsPerPage() * page + i;
			if (index >= kits.size()) break;
			ItemStack item = MenuUtils.getMenuItem(kits.get(index));
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add("");
			lore.add(ChatUtils.format(MenuUtils.getText("kitsmenu", "remove-lore-text")));
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.addItem(item);
		}
		
		if (inv.firstEmpty() > -1) {
			ItemStack kitAddItem = new ItemBuilder(Material.GREEN_CONCRETE)
					.name(MenuUtils.getText("kitsmenu", "add-kit-item-name"))
					.function("addKit")
					.make();
			inv.addItem(kitAddItem);
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
				ClickType click = e.getClick();
				if (click.equals(ClickType.SHIFT_RIGHT)) {
					new KitDeleteConfirmMenu(pMenuUtil).open();
					return;
				}
				else {					
					new KitEditMenu(pMenuUtil).open();
					return;
				}				
			}
			else if (function.equals("addKit")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("addKit");
				p.closeInventory();
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("adding-kit")));
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("cancel-editing")));
				return;
			}
			else if (function.equals("nextPage")) {	
				if ((index+1) >= kitsSet.size()) {
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
