package me.Marek2810.PersoKits.Menus;

import java.util.ArrayList;
import java.util.List;

import me.Marek2810.PersoKits.Utils.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;

public class KitEditMenu extends PersoKitsMenu {

	private List<ItemStack> kitItems;
	private PersoKit kit;
	public KitEditMenu(PlayerMenuUtility util) {
		super(util);
		kit = PersoKits.kits.get(pMenuUtil.getKit());
		kitItems = new ArrayList<>(kit.getItems());

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
		inv.setItem(0, backMenuItem);
		
		String name = kit.getName();

		ItemStack item = MenuUtils.getMenuItem(name);
		inv.setItem(4, item);
		
		ItemStack cdItem = new ItemBuilder(Material.CLOCK)
				.name(MenuUtils.getText("kitmenu", "cooldown-item-name"))
				.function("setCooldown")
				.make();
		inv.setItem(1, cdItem);
		
		ItemStack usesItem = new ItemBuilder(Material.DISPENSER)
				.name(MenuUtils.getText("kitmenu", "uses-item-name"))
				.function("setUses")
				.make();
		inv.setItem(2, usesItem);
		
		ItemStack persoKit = new ItemBuilder(Material.PLAYER_HEAD)
				.name(MenuUtils.getText("kitmenu", "togglePersoKit-item-name"))
				.function("togglePersoKit")
				.make();
		inv.setItem(3, persoKit);


		List<String> fistJoinKitLore = new ArrayList<>();

		if (!PersoKits.firstJoinKitStatus) {
			fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-disabled"), pMenuUtil.getKit()));
			fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-click-to-set"), pMenuUtil.getKit()));
		}
		else {
			if (kit.equals(PersoKits.firstJoinKit)) {
				fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-this-kit"), pMenuUtil.getKit()));
				fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-click-to-disable"), pMenuUtil.getKit()));
			}
			else {
				fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-another-kit"), pMenuUtil.getKit()));
				fistJoinKitLore.add(ChatUtils.formatWithPlaceholders(pMenuUtil.getOwner(), MenuUtils.getText("kitmenu", "first-join-kit-item-lore-click-to-set"), pMenuUtil.getKit()));
			}
		}

		ItemStack fistJoinKit = new ItemBuilder(Material.SHULKER_BOX)
				.name(MenuUtils.getText("kitmenu", "first-join-kit-item-name"))
				.lore(fistJoinKitLore)
				.function("setFirstJoinKit")
				.make();
		inv.setItem(5, fistJoinKit);

		if (kit.isPersokit()) {
			ItemStack persoItems = new ItemBuilder(Material.ENDER_CHEST)
					.name(MenuUtils.getText("kitmenu", "options-item-name"))
					.function("setPersoKitItems")
					.make();
			inv.setItem(6, persoItems);
		}
		
		if (kitItems != null ) {
			if (!kitItems.isEmpty()) {
				for (ItemStack kitItem : kitItems) {
					inv.addItem(kitItem);
				}	
			}			
		}

		ItemStack saveKit = new ItemBuilder(Material.GREEN_CONCRETE)
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
		if (!e.getView().getTopInventory().equals(e.getClickedInventory())) {
			e.setCancelled(false);
		}
		if (item == null) return;
		if (item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING) != null) {
			String function = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PersoKits.getPlugin(), "function"), PersistentDataType.STRING);
			if (function.equals("setCooldown")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("cooldown");
				p.closeInventory();
				String msg = ChatUtils.getMessage("enter-cooldown");
				msg = ChatUtils.formatWithPlaceholders(p, msg, PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("cancel-editing")));
				return;
			}
			else if (function.equals("setUses")) {
				pMenuUtil.setEditingKit(true);
				pMenuUtil.setKitSetting("uses");
				p.closeInventory();
				String msg = ChatUtils.getMessage("enter-uses");
				msg = ChatUtils.formatWithPlaceholders(p, msg, PersoKits.getPlayerMenuUtility(p).getKit());
				p.sendMessage(ChatUtils.format(msg));
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("cancel-editing")));
				return;
			}
			else if (function.equals("togglePersoKit")) {
				if (kit.isPersokit()) {
					kit.setPersokit(false);						
					String msg = ChatUtils.getMessage("persokit-disabled");
					msg = ChatUtils.formatWithPlaceholders(p, msg, PersoKits.getPlayerMenuUtility(p).getKit());
					p.sendMessage(ChatUtils.format(msg));
				}
				else {
					kit.setPersokit(true);
					String msg = ChatUtils.getMessage("persokit-enabled");
					msg = ChatUtils.formatWithPlaceholders(p, msg, PersoKits.getPlayerMenuUtility(p).getKit());
					p.sendMessage(ChatUtils.format(msg));
				}
				saveKitItems();
				inv.clear();
				open();
				return;
			}
			else if (function.equals("setFirstJoinKit")) {
				if (!PersoKits.firstJoinKitStatus) {
					PersoKits.kitsFile.getConfig().set("first-join-kit", pMenuUtil.getKit());
					KitUtils.loadFirstJoinKit();
				}
				else {
					if (kit.equals(PersoKits.firstJoinKit)) {
						PersoKits.kitsFile.getConfig().set("first-join-kit", "disabled");
						KitUtils.loadFirstJoinKit();
					}
					else {
						PersoKits.kitsFile.getConfig().set("first-join-kit", pMenuUtil.getKit());
						KitUtils.loadFirstJoinKit();
					}
				}
				PersoKits.kitsFile.saveConfig();
				saveKitItems();
				inv.clear();
				open();
				return;
			}
			else if (function.equals("setPersoKitItems")) {
				new KitOptionItemsMenu(pMenuUtil).open();
				return;
			}
			else if (function.equals("saveKit")) {
				saveKitItems();
				kit.setItems(kitItems);
				p.closeInventory();
				String msg = ChatUtils.getMessage("saved-kit");
				msg = ChatUtils.formatWithPlaceholders(p, msg, PersoKits.getPlayerMenuUtility(p).getKit());
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

	public void saveKitItems() {
		int y = 0;
		for (int i = 9; i < 45; i++) {
			if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				if (y < kitItems.size()) {
					kitItems.remove(y);
				}
				continue;
			}
			else if (y >= kitItems.size()) {
				kitItems.add(y, inv.getItem(i));
			}
			else {
				kitItems.set(y, inv.getItem(i));
			}
			y++;
		}
	}

	@Override
	public void open() {
		setDefaultItems();
		setMenuItems();
		owner.openInventory(inv);
	}

}
