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

public class KitMenu extends PersoKitsMenu {
	
	public KitMenu(PlayerMenuUtility util) {
		super(util);
	}
	
	@Override
	public String getTitle() {
		return MenuUtils.getText("kitmenu", "title");
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		inv.setItem(0, backMenu);
		
		String name = pMenuUtil.getKit();
		PersoKit kit = PersoKits.kits.get(name);
		
		ItemStack item = MenuUtils.getMenuItem(name);
		inv.setItem(4, item);
		
		ItemStack cdItem = new ItemBuilder(Material.CLOCK)
				.name(MenuUtils.getText("kitmenu", "cooldown-item-name"))
				.function("setCooldown")
				.make();
		inv.setItem(1, cdItem);
		
		ItemStack usesItem = new ItemBuilder(Material.DISPENSER)
//				.name("&eSet uses of kit")
				.name(MenuUtils.getText("kitmenu", "uses-item-name"))
				.function("setUses")
				.make();
		inv.setItem(2, usesItem);
		
		ItemStack persoKit = new ItemBuilder(Material.PLAYER_HEAD)
//				.name("&eToggle if kit is PersoKit")
				.name(MenuUtils.getText("kitmenu", "togglePersoKit-item-name"))
				.function("togglePersoKit")
				.make();
		inv.setItem(3, persoKit);
		
		if (kit.isPersokit()) {
			ItemStack persoItems = new ItemBuilder(Material.ENDER_CHEST)
//					.name("&eSet option items")
					.name(MenuUtils.getText("kitmenu", "options-item-name"))
					.function("setPersoKitItems")
					.make();
			inv.setItem(5, persoItems);
		}
		
		if (kit.getItems() != null ) {
			if (!kit.getItems().isEmpty()) {
				for (ItemStack kitItem : kit.getItems()) {
					inv.addItem(kitItem);
				}	
			}			
		}	
		
		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
//				.name("&aSave kit")
				.name(MenuUtils.getText("kitmenu", "save-kit-item-name"))
				.function("saveKit")
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
			if (function.equals("setCooldown")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("cooldown");
				p.closeInventory();
//				p.sendMessage(ChatUtils.format("&eEnter cooldown in seconds:"));
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("enter-cooldown")));
				return;
			}
			else if (function.equals("setUses")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("uses");
				p.closeInventory();
//				p.sendMessage(ChatUtils.format("&eEnter kit uses amount for each player:"));
				String msg = ChatUtils.getMessage("persokit-enabled");
				msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
				return;
			}
			else if (function.equals("togglePersoKit")) {
				PersoKit kit = PersoKits.kits.get(pMenuUtil.getKit());
				if (kit.isPersokit()) {
					kit.setPersokit(false);						
//					p.sendMessage(ChatUtils.format("&cKit &e" + kit.getName() + " &cis no longer PersoKit!"));
					String msg = ChatUtils.getMessage("persokit-disabled");
					msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
					p.sendMessage(ChatUtils.format(msg));
				}
				else {
					kit.setPersokit(true);
//					p.sendMessage(ChatUtils.format("&aKit &e" + kit.getName() + " &ais now PersoKit!"));
					String msg = ChatUtils.getMessage("persokit-enabled");
					msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
					p.sendMessage(ChatUtils.format(msg));
				}
				return;
			}	
			else if (function.equals("setPersoKitItems")) {
				new OptionsMenu(pMenuUtil).open();
				return;
			}
			else if (function.equals("saveKit")) {
				List<ItemStack> items = new ArrayList<>();
				for (int i = 9; i < slots-9; i++) {
					if (e.getInventory().getItem(i) == null) continue;
					items.add(e.getInventory().getItem(i));
				}							
				PersoKits.kits.get(pMenuUtil.getKit()).setItems(items);
				p.closeInventory();
//				p.sendMessage(ChatUtils.format("&aYou saved kit &e" + pMenuUtil.getKit() + "&a."));
				String msg = ChatUtils.getMessage("persokit-enabled");
				msg = msg.replace("%name%", PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
				return;
			}						
			else if (function.equals("close")) {
				p.closeInventory();
				return;
			}
			else if (function.equals("backMenu")) {
				new KitsMenu(pMenuUtil).open();
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
