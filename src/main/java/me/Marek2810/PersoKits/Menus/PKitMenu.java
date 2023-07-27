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
import org.bukkit.scheduler.BukkitRunnable;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.ItemBuilder;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PaginatedMenu;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitMenu extends PaginatedMenu {
	
	private List<ItemStack> varaintItems = new ArrayList<>();
	private List<ItemStack> varOptionItems = new ArrayList<>();
	private ItemStack green = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
			.name(MenuUtils.getText("pkitmenu", "free-slot-item-name"))
			.make();
	
	public PKitMenu(PlayerMenuUtility util) {
		super(util);
		PersoKit pkit = PersoKits.kits.get(util.getKit());
		if (pkit.getPersokits().get(util.getOwner().getUniqueId()) != null) {
			varaintItems = pkit.getPersokits().get(util.getOwner().getUniqueId());
		}
		else {
			varaintItems = pkit.getItems();
		}
		if (pkit.getOptions() != null) {
			varOptionItems = new ArrayList<>(pkit.getOptions());
		}				
	}
	
	@Override
	public String getTitle() {
		return MenuUtils.getText("pkitmenu", "title");
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		inv.setItem(0, backMenuItem);
		
		String name = pMenuUtil.getKit();
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
			
		List<ItemStack> options = varOptionItems;
		for (int i = 0; i < getMaxItemsPerPage(); i++) {
			index = getMaxItemsPerPage() * page + i;
			if (index >= options.size()) break;
			ItemStack menuItem = new ItemStack(options.get(index));
			inv.addItem(new ItemBuilder(menuItem)
					.function("addItem")
					.make());
		}
		
		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
				.name(MenuUtils.getText("pkitmenu", "save-kit-item-name"))
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
				PersoKit kit = PersoKits.kits.get(pMenuUtil.getKit());
				kit.addPersoKitVariant(p.getUniqueId(), varaintItems);
				p.closeInventory();
				String msg = ChatUtils.getMessage("saved-pkit");
				msg = ChatUtils.formatWithPlaceholders(p, msg, kit.getName());
				if(!kit.equals(PersoKits.firstJoinKit)) {
					p.sendMessage(ChatUtils.format(msg));
				}
				else {
					if (KitUtils.getFirstKitClaimbed(p)) {
						p.sendMessage(ChatUtils.format(msg));
					}
				}
				new BukkitRunnable() {			
					public void run() {
						p.performCommand("kit " + kit.getName());
						cancel();
					}
				}.runTaskLater(PersoKits.getPlugin(), (int) 2);					
				return;
			}
			else if (function.equals("addItem")) {
				if (PersoKits.kits.get(pMenuUtil.getKit()).getSlots() <= varaintItems.size()) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-slot")));
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
				new PKitsMenu(pMenuUtil).open();
				return;
			}	
			else if (function.equals("nextPage")) {	
				if ((index+1) >= varOptionItems.size()) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-last-page")));
					return;
				}
				page += 1;
				inv.clear();
				super.open();
				return;
			}
			else if (function.equals("previousPage")) {
				if (page == 0) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-first-page")));
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
