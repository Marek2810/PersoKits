package me.Marek2810.PersoKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PersoKits.Commands.KitCommand;
import me.Marek2810.PersoKits.Commands.KitEditorCommand;
import me.Marek2810.PersoKits.Files.CustomConfig;
import me.Marek2810.PersoKits.Listeners.KitChatListener;
import me.Marek2810.PersoKits.Listeners.MenuListener;
import me.Marek2810.PersoKits.Listeners.PlayerListener;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;
	public static ConsoleCommandSender console;
	
	public static CustomConfig kitsFile;
	public static CustomConfig dataFile;
	public static CustomConfig messagesFile;
	
	public static final HashMap<String, PersoKit> kits = new HashMap<>();	
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();


	@Override
	public void onEnable() {
		inst = this;		
		console = getServer().getConsoleSender();
		
		kitsFile = new CustomConfig(this, "kits.yml");
		dataFile = new CustomConfig(this, "data.yml");
		messagesFile = new CustomConfig(this, "messages.yml");
		loadKits();
	
		this.getCommand("kit").setExecutor(new KitCommand());
		this.getCommand("kiteditor").setExecutor(new KitEditorCommand());
		
		this.getServer().getPluginManager().registerEvents(new MenuListener(),this);
		this.getServer().getPluginManager().registerEvents(new KitChatListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(),this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}
	
	public static void loadKits() {
		Set<String> kitsList = kitsFile.getConfig().getKeys(false);
		console.sendMessage(ChatUtils.format("&aLoading kits..."));
		for (String name : kitsList) {
			console.sendMessage(ChatUtils.format("&aLoading kit &e" + name + "&a..."));
			console.sendMessage(ChatUtils.format("&aLoading items..."));
			List<ItemStack> items = new ArrayList<>();
			if(kitsFile.getConfig().get(name + ". items") != null
					&& !kitsFile.getConfig().getConfigurationSection(name + ". items").getKeys(false).isEmpty() ) {
				Set<String> itemList = kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
				ConfigurationSection itemSection = kitsFile.getConfig().getConfigurationSection(name + ".items");			
				for (String itemKey : itemList) {
					if (!itemSection.isItemStack(itemKey)) {
						if (Material.matchMaterial(itemSection.getString(itemKey)) == null) {							
							String msg = ChatUtils.getMessage("error");
							msg = msg.replace("%name%", name);
							msg = msg.replace("%itemKey%", itemKey);
							console.sendMessage(ChatUtils.format(msg));				
							continue;
						}
						ItemStack item = new ItemStack(Material.valueOf(itemSection.getString(itemKey)));
						items.add(item);
					}
					else {
						items.add(itemSection.getItemStack(itemKey));
					}
				}
			}	
			
			List<ItemStack>  options = new ArrayList<>();
			if (kitsFile.getConfig().getBoolean(name + ".persokit") 
					&& kitsFile.getConfig().get(name + ".options") != null
					&& !kitsFile.getConfig().getConfigurationSection(name + ".options").getKeys(false).isEmpty() ){
				console.sendMessage(ChatUtils.format("&aLoading option items..."));
				Set<String> optionsList = kitsFile.getConfig().getConfigurationSection(name + ".options").getKeys(false);
				ConfigurationSection  optionsSection = kitsFile.getConfig().getConfigurationSection(name + ".options");				
				for (String optionsKey : optionsList) {
					if (!optionsSection.isItemStack(optionsKey)) {
						if (Material.matchMaterial(optionsSection.getString(optionsKey)) == null) {							
							String msg = ChatUtils.getMessage("error");
							msg = msg.replace("%name%", name);
							msg = msg.replace("%itemKey%", optionsKey);
							console.sendMessage(ChatUtils.format(msg));				
							continue;
						}
						ItemStack item = new ItemStack(Material.valueOf(optionsSection.getString(optionsKey)));
						options.add(item);
					}
					else {
						options.add(optionsSection.getItemStack(optionsKey));
					}
				}
			}
			
			
			kits.put(name, new PersoKit(
					name,
					items, 
					kitsFile.getConfig().getDouble(name + ".cooldown"), 
					kitsFile.getConfig().getInt(name + ".uses"),
					kitsFile.getConfig().getBoolean(name + ".persokit"),
					kitsFile.getConfig().getInt(name + ".slots"),
					options
				));
		}
	}
	
	public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
    	if (!playerMenuUtilityMap.containsKey(p)) {
    		PlayerMenuUtility util = new PlayerMenuUtility(p);
    		playerMenuUtilityMap.put(p, util);
    		return util;
    	}    	
    	return playerMenuUtilityMap.get(p);
    }
	
}
