package me.Marek2810.PersoKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PersoKits.Commands.KitCommand;
import me.Marek2810.PersoKits.Files.CustomConfig;
import me.Marek2810.PersoKits.Utils.PersoKit;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;
	public static ConsoleCommandSender console;
	
	public static CustomConfig kitsFile;
	
	public static HashMap<String, PersoKit> kits = new HashMap<>();

	@Override
	public void onEnable() {
		inst = this;		
		console = getServer().getConsoleSender();
		
		kitsFile = new CustomConfig(this, "kits.yml");
		loadKits();
	
		this.getCommand("kit").setExecutor(new KitCommand());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}
	
	public static void loadKits() {
		Set<String> kitsList = kitsFile.getConfig().getKeys(false);
		for (String name : kitsList) {
			Set<String> itemList = kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
			ConfigurationSection itemSection = kitsFile.getConfig().getConfigurationSection(name + ".items");
			List<ItemStack> items = new ArrayList<>();
			for (String itemKey : itemList) {
				if (!itemSection.isItemStack(itemKey)) {
					if (Material.matchMaterial(itemSection.getString(itemKey)) == null) {
						console.sendMessage("[PersoKits] Error while loading kit " + name + " with item " + itemKey + ". Item is not loaded in kit.");
						continue;
					}
					ItemStack item = new ItemStack(Material.valueOf(itemSection.getString(itemKey)));
					items.add(item);
				}
				else {
					items.add(itemSection.getItemStack(itemKey));
				}
			}
			kits.put(name, new PersoKit(name, items, kitsFile.getConfig().getInt("cooldown")));
		}
	}
	
}
