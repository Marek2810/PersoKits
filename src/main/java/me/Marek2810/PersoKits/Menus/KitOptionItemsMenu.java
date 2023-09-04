package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;

import me.Marek2810.PersoKits.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;

public class KitOptionItemsMenu extends PaginatedMenu {

	private List<ItemStack> optionItems;
	private PersoKit kit;

	public KitOptionItemsMenu(PlayerMenuUtility util) {
		super(util);
		kit = PersoKits.kits.get(pMenuUtil.getKit());
//		optionItems = new ArrayList<>(PersoKits.kits.get(pMenuUtil.getKit()).getOptions());
		optionItems = new ArrayList<>(kit.getOptions());
	}
	
	@Override
	public String getTitle() {
		return MenuUtils.getText("optionsmenu", "title");
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		int slots = getRows()*9;
		maxItemsPerPage = slots-18;

		inv.setItem(0, backMenuItem);
		
		String name = kit.getName();

		ItemStack item = MenuUtils.getMenuItem(name);
		inv.setItem(4, item);
		
		ItemStack kitSlots = new ItemBuilder(Material.BOOKSHELF)
				.name((MenuUtils.getText("optionsmenu", "slots-item-name")))
				.function("setSlots")
				.make();
		inv.setItem(1, kitSlots);

		for (int i = 0; i < getMaxItemsPerPage(); i++) {
			index = getMaxItemsPerPage() * page + i;
			if (index >= optionItems.size()) break;
			if (optionItems.get(index) != null) {
				inv.addItem(optionItems.get(index));
			}
		}
		
		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
				.name((MenuUtils.getText("optionsmenu", "save-kit-item-name")))
				.function("saveOptions")
				.make();

		inv.setItem(slots-5, saveKit);
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		ItemStack item = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		int slots = getRows()*9;
		int slot = e.getRawSlot();
		if (slot > 8 && slot < slots-9) {
			e.setCancelled(false);
		}
		if (!e.getView().getTopInventory().equals(e.getClickedInventory())) {
			e.setCancelled(false);
		}
		if (item == null) return;
		if (item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING) != null) {
			String function = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING);
			if (function.equals("setSlots")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("slots");
				p.closeInventory();
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("enter-slots")));
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("cancel-editing")));
				return;
			}
			else if (function.equals("saveOptions")) {
				saveOptionItems();
				kit.setOptions(optionItems);
				p.closeInventory();				
				String msg = ChatUtils.getMessage("saved-options");
				msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
				return;
			}
			else if (function.equals("nextPage")) {
				saveOptionItems();
				int maxItems = maxItemsPerPage*(page+1);
				if (maxItems >= optionItems.size() && inv.firstEmpty() >= 0) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-last-page")));
				}
				else {
					page += 1;
				}
				open();
				return;
			}
			else if (function.equals("previousPage")) {
				saveOptionItems();
				if (page == 0) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("already-first-page")));
				}
				else {
					page -= 1;
				}
				open();
				return;
			}
			else if (function.equals("close")) {
				p.closeInventory();
				return;
			}
			else if (function.equals("backMenu")) {
				new KitEditMenu(pMenuUtil).open();
				return;
			}			
		}
	}

	public void saveOptionItems() {
		int y = (page*35)+page;
		for (int i = 9; i < 45; i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				if (y < optionItems.size()) {
					optionItems.remove(y);
				}
				continue;
			}
			else if (y >= optionItems.size()) {
				optionItems.add(y, inv.getItem(i));
			}
			else {
				optionItems.set(y, inv.getItem(i));
			}
			y++;
		}
	}
	
	@Override
	public void open() {
		inv = Bukkit.createInventory(this, getRows()*9, ChatUtils.format(getTitle()));
		setDefaultItems();
		setMenuItems();
		owner.openInventory(inv);
	}

}
