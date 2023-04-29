package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.ItemBuilder;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PaginatedMenu;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitMenu extends PaginatedMenu {
	
	private List<ItemStack> varaintItems = new ArrayList<>();
	private List<ItemStack> varOptionItems = new ArrayList<>();
	private ItemStack green = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
			.name("&aFree slot for item")
			.make();
	
	public PKitMenu(PlayerMenuUtility util) {
		super(util);
//		varaintItems.clear();
//		varOptionItems.clear();
		PersoKit pkit =PersoKits.kits.get(util.getpKit());
		if (pkit.getPersokits().get(util.getOwner().getUniqueId()) != null) {
			varaintItems = PersoKits.kits.get(util.getpKit()).getPersokits().get(util.getOwner().getUniqueId());
		}
		if (pkit.getOptions() != null) {
			varOptionItems = new ArrayList<>(pkit.getOptions());
		}				
	}
	
	@Override
	public String getTitle() {
		return "&0Kit options menu";
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		inv.setItem(0, backMenu);
		
		String name = pMenuUtil.getpKit();
		PersoKit kit = PersoKits.kits.get(name);		
		inv.setItem(4, MenuUtils.getMenuItem(name));
	
		int slots = getRows()*9;
		maxItemsPerPage = slots-19-kit.getSlots();
		
		if (page == 0) {
			for (ItemStack persoItem : varaintItems) {
				ItemStack menuItem = new ItemStack(persoItem);
				if (varOptionItems.contains(persoItem)) varOptionItems.remove(persoItem);
				inv.addItem(new ItemBuilder(menuItem)
						.function("removeItem")
						.make());
			}
			
			int lastSlot = inv.firstEmpty();
			if (kit.getSlots() > varaintItems.size()) {			
				for (int i = 0; i < kit.getSlots()-varaintItems.size(); i++) {
					lastSlot = inv.firstEmpty();
					if (lastSlot >= slots-9 || inv.firstEmpty() < 0) break;
					inv.setItem(lastSlot, green);
				}
			}
			
			ItemStack barrier = new ItemBuilder(Material.BARRIER)
					.name(" ")
					.make();
			inv.addItem(barrier);
		}		
			
		if (!varOptionItems.isEmpty()) {
			List<ItemStack> options = varOptionItems;
			for (int i = 0; i < getMaxItemsPerPage(); i++) {
				index = getMaxItemsPerPage() * page + i;
				if (index >= options.size()) break;
				ItemStack menuItem = new ItemStack(options.get(index));
				inv.addItem(new ItemBuilder(menuItem)
						.function("addItem")
						.make());					
			}
		}
		
		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
				.name("&aSave PersoKit")
				.function("savePersoKit")
				.make();		
		inv.setItem(slots-5, saveKit);
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if (item == null) return;
		if (item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING) != null) {
			String function = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING);
			if (function.equals("savePersoKit")) {
				PersoKit kit = PersoKits.kits.get(pMenuUtil.getpKit());
				kit.setPersoKitVariant(p.getUniqueId(), varaintItems);
				p.closeInventory();
				p.sendMessage("saved");
				return;
			}
			else if (function.equals("addItem")) {
				if (PersoKits.kits.get(pMenuUtil.getpKit()).getSlots() <= varaintItems.size()) {
					p.sendMessage(ChatUtils.format("&cNo slot."));
					return;
				}	
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().remove(new NamespacedKey(PersoKits.getPlugin(), "function"));
				item.setItemMeta(meta);
				varaintItems.add(item);
				varOptionItems.remove(item);
				inv.clear();
				super.open();
				return;
			}
			else if (function.equals("removeItem")) {
				ItemMeta meta = item.getItemMeta();
				meta.getPersistentDataContainer().remove(new NamespacedKey(PersoKits.getPlugin(), "function"));
				item.setItemMeta(meta);
				varaintItems.remove(item);
				varOptionItems.add(item);
				inv.clear();
				super.open();
				return;
			}
			else if (function.equals("close")) {
				p.closeInventory();
				return;
			}
			else if (function.equals("backMenu")) {
				//TODO
				return;
			}	
			else if (function.equals("nextPage")) {
				List<ItemStack> options = PersoKits.kits.get(pMenuUtil.getpKit()).getOptions();				
				if ((index +1) >= options.size()) {
					p.sendMessage(ChatUtils.format("&cYou are on last page."));
					return;
				}
				page += 1;
				inv.clear();
				super.open();
				return;
			}
			else if (function.equals("previousPage")) {
				if (page == 0) {
					p.sendMessage(ChatUtils.format("&cYou are allready on first page."));
					return;
				}
				page -= 1;
				inv.clear();
				super.open();
				return;
			}
		}
	}
	
	
}