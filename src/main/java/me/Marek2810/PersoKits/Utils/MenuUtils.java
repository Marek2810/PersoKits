package me.Marek2810.PersoKits.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;

public class MenuUtils {

	public static ItemStack getMenuItem(String name) {
		PersoKit kit = PersoKits.kits.get(name);
		Material mat = Material.CHEST;
		List<String> lore = new ArrayList<>();
		lore.add("");		
//		lore.add(ChatUtils.format("&eCooldown: &f" + kit.getCooldwon() + " secs"));
		lore.add(ChatUtils.format(MenuUtils.getText("kititem", "cooldown") + kit.getCooldwon() + " " + MenuUtils.getText("kititem", "seconds")));
//		String uses = "&cdisabled";
		String uses = MenuUtils.getText("kititem", "disabled");
		if (kit.getUses() > 0) {
			uses = String.valueOf(kit.getUses());
		}
		lore.add(ChatUtils.format(MenuUtils.getText("kititem", "uses") + uses));
		if (kit.isPersokit) {
//			lore.add(ChatUtils.format("&ePersoKit: &a" + kit.isPersokit()));
//			lore.add(ChatUtils.format("&eSlots: &f" + kit.getSlots()));
			lore.add(ChatUtils.format(MenuUtils.getText("kititem", "persokit") + "&a" + kit.isPersokit()));
			lore.add(ChatUtils.format(MenuUtils.getText("kititem", "slots") + kit.getSlots()));
		}
		else {
			lore.add(ChatUtils.format(MenuUtils.getText("kititem", "persokit") + "&c" + kit.isPersokit()));
		}
		ItemStack item = new ItemBuilder(mat)
				.name("&a" + name)
				.lore(lore)
				.addData("kitName", name)
				.function("editKit")
				.make();
		return item;
	}
	
	public static String getText(String gui, String name) {
		return PersoKits.messagesFile.getConfig().getString("guis." + gui + "." + name);
	}
	
}
