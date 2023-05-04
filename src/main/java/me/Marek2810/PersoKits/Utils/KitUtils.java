package me.Marek2810.PersoKits.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;

public class KitUtils {

	public static boolean hasPermission(Player p, String name) {
		if (p.hasPermission("persokits.kit.*")) return true;
		if (p.hasPermission("persokits.kit." + name)) return true;
		return false;
	}

	public static boolean isAviable(Player p, String kitName) {
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + kitName + ".availableAt");
		if (availableAt < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public static int aviableAt(Player p, String kitName) {
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + kitName + ".availableAt");
		return (int) (availableAt-System.currentTimeMillis())/1000;
	}
	
	public static boolean haveUses(Player p, PersoKit kit) {
		int playerUses = 0;
		String kitName = kit.getName();
		if (kit.getUses() < 0 ) return true;
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + kitName + ".uses") != null) {
			playerUses = PersoKits.dataFile.getConfig().getInt("players." + p.getUniqueId() + "." + kitName + ".uses");
		}
		if (playerUses >= kit.getUses()) {			
			return false;
		}
		return true;
	}
	
	public static List<String> getAviableKitsForPlayer(Player p) {
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			if (hasPermission(p, name)) {
				kits.add(name);
			}			
		}
		
		return kits;
	}
	
	public static void loadKits() {		
		if (PersoKits.kitsFile.getConfig().getConfigurationSection("kits") != null) {
			String msg = ChatUtils.getConsoleMessage("kits-loading");
			PersoKits.console.sendMessage(ChatUtils.format(msg));
			Set<String> kits = PersoKits.kitsFile.getConfig().getConfigurationSection("kits").getKeys(false);
			PersoKit kit = null;
			for (String kitName : kits) {
				try {
					kit = loadKit(kitName);
				} catch (Exception e) {
//					PersoKits.console.sendMessage(ChatUtils.format(" &cError while loading kit &e%name%&c."));
					String errorMsg = ChatUtils.getConsoleMessage("kit-loading-error");
					errorMsg = errorMsg.replace("%name%", kitName);
					PersoKits.console.sendMessage(ChatUtils.format(errorMsg));

				}
				PersoKits.kits.put(kitName, kit);
//				PersoKits.console.sendMessage(ChatUtils.format(" &aKit &e%name% &aloaded successfuly."));
				String loadedMsg = ChatUtils.getConsoleMessage("kit-loaded");
				loadedMsg = loadedMsg.replace("%name%", kitName);
				PersoKits.console.sendMessage(ChatUtils.format(loadedMsg));
			}
		}
	}
	
	public static PersoKit loadKit(String name) {
//		PersoKits.console.sendMessage(ChatUtils.format("&aLoading kit &e" + name + "&a..."));
		String msg = ChatUtils.getConsoleMessage("kit-loading");
		msg = msg.replace("%name%", name);
		PersoKits.console.sendMessage(ChatUtils.format(msg));
		double cooldwon = PersoKits.kitsFile.getConfig().getDouble("kits." + name + ".cooldown");
		int uses = PersoKits.kitsFile.getConfig().getInt("kits." + name + ".uses");
		boolean isPersokit = PersoKits.kitsFile.getConfig().getBoolean("kits." + name + ".persokit");
		int slots = PersoKits.kitsFile.getConfig().getInt("kits." + name + ".slots");
		List<ItemStack> items = loadKitItems(name);
		List<ItemStack> options = loadOptionItems(name);
		HashMap<UUID, List<ItemStack>> persokits = new HashMap<>();
		if (isPersokit) {
			persokits = loadPersoKits(name);
		}		
		return new PersoKit(
				name,
				cooldwon,
				uses,
				isPersokit,
				slots,
				items,
				options,
				persokits
			);				
	}

	private static List<ItemStack> loadKitItems(String name) {
//		PersoKits.console.sendMessage(ChatUtils.format("&aLoading items for kit &e" + name + "&a..."));
		String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-items");
		loadingMsg = loadingMsg.replace("%name%", name);
		PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
		List<ItemStack> items = new ArrayList<>();
		if (PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".items") != null) {
			Set<String> itemKeys = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".items").getKeys(false);
			ConfigurationSection itemSection = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".items");	
			for (String itemKey : itemKeys) {
				if(itemSection.isItemStack(itemKey)) {
					items.add(itemSection.getItemStack(itemKey));
				}
				else {
					if (Material.matchMaterial(itemSection.getString(itemKey)) == null) {							
						String msg = ChatUtils.getConsoleMessage("item-error");
						msg = msg.replace("%name%", name);
						msg = msg.replace("%itemKey%", itemKey);
						PersoKits.console.sendMessage(ChatUtils.format(msg));				
						continue;
					}
					ItemStack item = new ItemStack(Material.valueOf(itemSection.getString(itemKey)));
					items.add(item);
				}
			}
		}
		return items;
	}
	
	private static List<ItemStack> loadOptionItems(String name) {
//		PersoKits.console.sendMessage(ChatUtils.format("&aLoading options items for kit &e" + name + "&a..."));
		String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-options");
		loadingMsg = loadingMsg.replace("%name%", name);
		PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
		List<ItemStack> items = new ArrayList<>();
		if (PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".options") != null) {
			Set<String> itemKeys = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".options").getKeys(false);
			ConfigurationSection itemSection = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".options");	
			for (String itemKey : itemKeys) {
				if(itemSection.isItemStack(itemKey)) {
					items.add(itemSection.getItemStack(itemKey));
				}
				else {
					if (Material.matchMaterial(itemSection.getString(itemKey)) == null) {							
						String msg = ChatUtils.getConsoleMessage("item-error");
						msg = msg.replace("%name%", name);
						msg = msg.replace("%itemKey%", itemKey);
						PersoKits.console.sendMessage(ChatUtils.format(msg));				
						continue;
					}
					ItemStack item = new ItemStack(Material.valueOf(itemSection.getString(itemKey)));
					items.add(item);
				}
			}
		}
		return items;
	}
	
	public static HashMap<UUID, List<ItemStack>> loadPersoKits(String name) {
		HashMap<UUID, List<ItemStack>> persokits = new HashMap<>();
		if (PersoKits.kitsFile.getConfig().getBoolean("kits." + name + ".persokit")) {
//			PersoKits.console.sendMessage(ChatUtils.format("&aLoading PersoKits for kit &e" + name + "&a..."));
			String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-persokits");
			loadingMsg = loadingMsg.replace("%name%", name);
			PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
			if (PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name) != null) {
				Set<String> pKits = PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name).getKeys(false);
				for (String pKit : pKits) {					
					UUID pUUID = UUID.fromString(pKit);
					List<ItemStack> items = new ArrayList<>();
					if ( PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name + "." + pKit) == null) continue;
					Set<String> itemsKeys = PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name + "." + pKit).getKeys(false);
					ConfigurationSection itemSection = PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name + "." + pKit);
					for (String itemKey : itemsKeys) {
						items.add(itemSection.getItemStack(itemKey));
					}
					persokits.put(pUUID, items);
				}
			}
		}
		return persokits;
	}
	
	public static void loadFirstJoinKit() {
		setFirstJoinKitStatus();
		if (!getFirstJoinKitStatus()) {
			PersoKits.firstJoinKit = null;
			return;			
		}
		PersoKits.firstJoinKit = PersoKits.kits.get(PersoKits.kitsFile.getConfig().getString("first-join-kit"));
		String msg = ChatUtils.getConsoleMessage("first-join-kit-set");
//		PersoKits.console.sendMessage("Kit " +PersoKits.firstJoinKit.getName() + " set as first join kit.");
		msg = msg.replace("%name%", PersoKits.firstJoinKit.getName());
		PersoKits.console.sendMessage(ChatUtils.format(msg));		
	}
	
	
	public static void setFirstJoinKitStatus() {
		if (PersoKits.kitsFile.getConfig().getString("first-join-kit") != null) {
			if (PersoKits.kits.containsKey(PersoKits.kitsFile.getConfig().getString("first-join-kit"))) {
				PersoKits.firstJoinKitStatus = true;
//				PersoKits.console.sendMessage("First join kit ennabled.");
				PersoKits.console.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("first-join-kit-enabled")));
				return;
			}
		}
		PersoKits.firstJoinKitStatus = false;
//		PersoKits.console.sendMessage("First join kit disabled.");
		PersoKits.console.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("first-join-kit-disabled")));
	}
	
	public static PersoKit getFirstJoinKit() {
		return PersoKits.firstJoinKit;
	}
	
	public static boolean getFirstJoinKitStatus() {
		return PersoKits.firstJoinKitStatus;
	}
	
	public static boolean getFirstKitClaimbed(Player p) {
		return PersoKits.dataFile.getConfig().getBoolean("players." + p.getUniqueId() + ".first-kit-claimbed");
	}
	
	public static void setFirstKitClaimbed(Player p) {
		PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + ".first-kit-claimbed", true);
		PersoKits.dataFile.saveConfig();
		if (PersoKits.fistKitTasks.get(p) != null) {
			PersoKits.fistKitTasks.get(p).cancel();
			PersoKits.fistKitTasks.remove(p);
		}		
	}
}