package me.Marek2810.PersoKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PersoKits.Commands.KitCommand;
import me.Marek2810.PersoKits.Commands.KitEditorCommand;
import me.Marek2810.PersoKits.Commands.PKitCommand;
import me.Marek2810.PersoKits.Commands.PersoKitsCommand;
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
	public static CustomConfig pKitsFile;
	public static CustomConfig dataFile;
	public static CustomConfig messagesFile;
	
	public static final HashMap<String, PersoKit> kits = new HashMap<>();	
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
   
    public static final List<CustomConfig> customConfigs = new ArrayList<>();


	@Override
	public void onEnable() {
		inst = this;		
		console = getServer().getConsoleSender();
		
		kitsFile = new CustomConfig(this, "kits.yml");
		customConfigs.add(kitsFile);
		dataFile = new CustomConfig(this, "data.yml");
		customConfigs.add(dataFile);
		messagesFile = new CustomConfig(this, "messages.yml");
		customConfigs.add(messagesFile);
		pKitsFile = new CustomConfig(this, "persokits.yml");
		customConfigs.add(pKitsFile);
		loadKits();
	
		this.getCommand("kit").setExecutor(new KitCommand());
		this.getCommand("kiteditor").setExecutor(new KitEditorCommand());
		this.getCommand("pkit").setExecutor(new PKitCommand());
		this.getCommand("persokits").setExecutor(new PersoKitsCommand());
		
		this.getServer().getPluginManager().registerEvents(new MenuListener(),this);
		this.getServer().getPluginManager().registerEvents(new KitChatListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(),this);
	}
	
	@Override
	public void onDisable() {
		kits.clear();
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}
	
	public static void loadKits() {
		Set<String> kitsList = kitsFile.getConfig().getKeys(false);
		console.sendMessage(ChatUtils.format("&aLoading kits..."));
		for (String name : kitsList) {
			console.sendMessage(ChatUtils.format("&aLoading kit &e" + name + "&a..."));
			List<ItemStack> items = new ArrayList<>();
			if(kitsFile.getConfig().get(name + ".items") != null
					&& !kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false).isEmpty() ) {
				Set<String> itemList = kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
				ConfigurationSection itemSection = kitsFile.getConfig().getConfigurationSection(name + ".items");	
				console.sendMessage(ChatUtils.format("&aLoading items..."));
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
			HashMap<UUID, List<ItemStack>> persokits = new HashMap<>();
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
				
				if (pKitsFile.getConfig().get(name) != null 
						&& !pKitsFile.getConfig().getConfigurationSection(name).getKeys(false).isEmpty() ) {
					Set<String> uuids = pKitsFile.getConfig().getConfigurationSection(name).getKeys(false);
					for (String uuid : uuids) {
						UUID uid = UUID.fromString(uuid);
						List<ItemStack> variantItems = new ArrayList<>();
						if (pKitsFile.getConfig().getString(name + "." + uuid).equalsIgnoreCase("default")) {
							Set<String> uItems = kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
							ConfigurationSection uItemsSection = kitsFile.getConfig().getConfigurationSection(name + ".items");							
							for (String uItemKey : uItems ) {
								if (!uItemsSection.isItemStack(uItemKey)) {
									if (Material.matchMaterial(uItemsSection.getString(uItemKey)) == null) {							
										String msg = ChatUtils.getMessage("error");
										msg = msg.replace("%name%", name);
										msg = msg.replace("%itemKey%", uItemKey);
										console.sendMessage(ChatUtils.format(msg));				
										continue;
									}
									ItemStack item = new ItemStack(Material.valueOf(uItemsSection.getString(uItemKey)));
									variantItems.add(item);
								}
								else {
									variantItems.add(uItemsSection.getItemStack(uItemKey));
								}
							}
							persokits.put(uid, variantItems);
						}
						else if (pKitsFile.getConfig().getConfigurationSection(name + "." + uid) != null 
								&& !pKitsFile.getConfig().getConfigurationSection(name + "." + uid).getKeys(false).isEmpty()) {
							Set<String> uItems = pKitsFile.getConfig().getConfigurationSection(name + "." + uid).getKeys(false);
							ConfigurationSection uItemsSection = pKitsFile.getConfig().getConfigurationSection(name + "." + uid);							
							for (String uItemKey : uItems ) {
								if (!uItemsSection.isItemStack(uItemKey)) {
									if (Material.matchMaterial(uItemsSection.getString(uItemKey)) == null) {							
										String msg = ChatUtils.getMessage("error");
										msg = msg.replace("%name%", name);
										msg = msg.replace("%itemKey%", uItemKey);
										console.sendMessage(ChatUtils.format(msg));				
										continue;
									}
									ItemStack item = new ItemStack(Material.valueOf(uItemsSection.getString(uItemKey)));
									variantItems.add(item);
								}
								else {
									variantItems.add(uItemsSection.getItemStack(uItemKey));
								}
							}
							persokits.put(uid, variantItems);
						}
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
					options,
					persokits
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
