package me.Marek2810.PersoKits.Menus;

import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.MenuUtils;
import me.Marek2810.PersoKits.Utils.PersoKitsMenu;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class KitsMenu extends PersoKitsMenu {
	
	public KitsMenu(PlayerMenuUtility util) {
		super(util);
	}
	
	@Override
	public String getTitle() {
		return "&0Kits editor";
	}

	@Override
	public int getRows() {
		return 6;
	}

	@Override
	public void setMenuItems() {
		Set<String> kits = PersoKits.kits.keySet();
		for (String name : kits) {
//			PersoKit kit = PersoKits.kits.get(name);
//			Material mat = Material.CHEST;
//			List<String> lore = new ArrayList<>();
//			lore.add("");
////			lore.add(ChatUtils.format("&ePersoKit: &f" + kit.getUses()));
//			lore.add(ChatUtils.format("&eCooldown: &f" + kit.getCooldwon() + " secs"));
//			String uses = "&cdisabled";
//			if (kit.getUses() > 0) {
//				uses = String.valueOf(kit.getUses());
//			}
//			lore.add(ChatUtils.format("&eUses: &f" + uses));
//			ItemStack item = new ItemBuilder(mat)
//					.name("&a" + name)
//					.lore(lore)
//					.addData("name", name)
//					.make();
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
				new KitMenu(pMenuUtil).open();
				return;
			}
		}
	}	

}
