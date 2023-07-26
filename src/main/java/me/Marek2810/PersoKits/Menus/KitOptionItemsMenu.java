package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;

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

public class KitOptionItemsMenu extends PersoKitsMenu {
	
	public KitOptionItemsMenu(PlayerMenuUtility util) {
		super(util);
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
		inv.setItem(0, backMenuItem);
		
		String name = pMenuUtil.getKit();
		PersoKit kit = PersoKits.kits.get(name);
		
		ItemStack item = MenuUtils.getMenuItem(name);
		inv.setItem(4, item);
		
		ItemStack kitSlots = new ItemBuilder(Material.BOOKSHELF)
//				.name("&eSet slots amount")
				.name((MenuUtils.getText("optionsmenu", "slots-item-name")))
				.function("setSlots")
				.make();
		inv.setItem(1, kitSlots);
		
		if (kit.getOptions() != null ) {
			if (!kit.getOptions().isEmpty()) {
				for (ItemStack optionItem : kit.getOptions()) {
					inv.addItem(optionItem);
				}
			}
		}	
		
		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
				.name("&aSave options")
				.function("saveOptions")
				.make();
		
		int slots = getRows()*9;
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
		if (e.getView().getTopInventory() != null && !e.getView().getTopInventory().equals(e.getClickedInventory())) {
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
				return;
			}
			else if (function.equals("saveOptions")) {
				List<ItemStack> items = new ArrayList<>();
				for (int i = 9; i < slots-9; i++) {
					if (e.getInventory().getItem(i) == null) continue;
					items.add(e.getInventory().getItem(i));
				}							
				PersoKits.kits.get(pMenuUtil.getKit()).setOptions(items);
				p.closeInventory();				
//				p.sendMessage(ChatUtils.format("&aYou saved option items of kit &e" + pMenuUtil.getKit() + "&a."));
				String msg = ChatUtils.getMessage("saved-options");
				msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
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
	
	@Override
	public void open() {	
		setDefaultItems();	
		setMenuItems();
		owner.openInventory(inv);
	}

}
