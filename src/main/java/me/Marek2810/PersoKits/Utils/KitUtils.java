package me.Marek2810.PersoKits.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KitUtils {

	public static List<String> getAvailableKitsForPlayer(Player p) {
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			if (PersoKits.kits.get(name).permittedToUse(p)) {
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
			PersoKit kit;
			for (String kitName : kits) {
				try {
					kit = loadKit(kitName);
					PersoKits.kits.put(kitName, kit);
				} catch (Exception e) {
					String errorMsg = ChatUtils.getConsoleMessage("kit-loading-error");
					errorMsg = errorMsg.replace("%name%", kitName);
					PersoKits.console.sendMessage(ChatUtils.format(errorMsg));
				}
//				String loadedMsg = ChatUtils.getConsoleMessage("kit-loaded");
//				loadedMsg = loadedMsg.replace("%name%", kitName);
//				PersoKits.console.sendMessage(ChatUtils.format(loadedMsg));
			}
		}
	}
	
	public static PersoKit loadKit(String name) {
//		String msg = ChatUtils.getConsoleMessage("kit-loading");
//		msg = msg.replace("%name%", name);
//		PersoKits.console.sendMessage(ChatUtils.format(msg));
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
//		String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-items");
//		loadingMsg = loadingMsg.replace("%name%", name);
//		PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
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
//		String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-options");
//		loadingMsg = loadingMsg.replace("%name%", name);
//		PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
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
//			String loadingMsg = ChatUtils.getConsoleMessage("kit-loading-persokits");
//			loadingMsg = loadingMsg.replace("%name%", name);
//			PersoKits.console.sendMessage(ChatUtils.format(loadingMsg));
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
		msg = msg.replace("%name%", PersoKits.firstJoinKit.getName());
		PersoKits.console.sendMessage(ChatUtils.format(msg));		
	}
	
	
	public static void setFirstJoinKitStatus() {
		if (PersoKits.kitsFile.getConfig().getString("first-join-kit") != null) {
			if (PersoKits.kits.containsKey(PersoKits.kitsFile.getConfig().getString("first-join-kit"))) {
				PersoKits.firstJoinKitStatus = true;
				PersoKits.console.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("first-join-kit-enabled")));
				return;
			}
		}
		PersoKits.firstJoinKitStatus = false;
		PersoKits.console.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("first-join-kit-disabled")));
	}
	public static boolean getFirstJoinKitStatus() {
		return PersoKits.firstJoinKitStatus;
	}
	
	public static boolean getFirstKitClaimed(Player p) {
		return PersoKits.dataFile.getConfig().getBoolean("players." + p.getUniqueId() + ".first-kit-claimed");
	}
	
	public static void setFirstKitClaimed(Player p) {
		PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + ".first-kit-claimed", true);
		PersoKits.dataFile.saveConfig();
		if (PersoKits.firstKitTasks.get(p) != null) {
			PersoKits.firstKitTasks.get(p).cancel();
			PersoKits.firstKitTasks.remove(p);
		}
	}

	public static void setReminderStatus() {
		PersoKits.reminderStatus = PersoKits.inst.getConfig().getBoolean("first-join-kit.reminder.enabled");
	}

	public static void setFistJoinMsgStatus() {
		PersoKits.reminderStatus = PersoKits.inst.getConfig().getBoolean("first-join-kit.msg-enabled");
	}

	public static void sendFistJoinMsg(Player p) {
		if (KitUtils.getFirstKitClaimed(p)) return;
		String msg = ChatUtils.getMessage("first-join-kit-reminder");

		ComponentBuilder hoverBuilder = new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("first-join-kit-reminder-hover")));

		double delay = PersoKits.inst.getConfig().getDouble("first-join-kit.delay-for-msg-after-join")*20;

		ComponentBuilder builder = new ComponentBuilder(ChatUtils.format(msg))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + PersoKits.firstJoinKit.getName()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuilder.create()));

		new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
				p.spigot().sendMessage(builder.create());
				cancel();
			}
		}.runTaskLater(PersoKits.getPlugin(), (long)delay*20);

	}

	public static void setupReminder(Player p) {
		if (KitUtils.getFirstKitClaimed(p)) return;
		String msg = ChatUtils.getMessage("first-join-kit-reminder");

		ComponentBuilder hoverBuilder = new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("first-join-kit-reminder-hover")));

		ComponentBuilder builder = new ComponentBuilder(ChatUtils.format(msg))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + PersoKits.firstJoinKit.getName()))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuilder.create()));

		double delay = PersoKits.inst.getConfig().getDouble("first-join-kit.delay-for-msg-after-join")*20;
		double interval = PersoKits.inst.getConfig().getDouble("first-join-kit.reminder.repeat-after")*20;
		BukkitTask firstKitReminder = new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
				p.spigot().sendMessage(builder.create());
			}
		}.runTaskTimer(PersoKits.getPlugin(), (int)delay, (int)interval);
		PersoKits.firstKitTasks.put(p, firstKitReminder);
	}

	public static ComponentBuilder getKitsMessage(Player p) {
		List<String> playerKits = getAvailableKitsForPlayer(p);
		ComponentBuilder builder = new ComponentBuilder();
		builder.append(new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("kits"))).create());
		int i = 1;
		for (String kitName : playerKits) {
			ComponentBuilder hoverBuilder = new ComponentBuilder();
			String color = "&a";
			PersoKit kit = PersoKits.kits.get(kitName);
			if (!kit.haveUses(p)) {
				color = "&c";
				hoverBuilder.append(new ComponentBuilder(
						ChatUtils.format(ChatUtils.getMessage("no-uses")))
						.create());
			}
			else if (!kit.isAvailable(p)) {
				color = "&e";
				String msg = ChatUtils.getMessage("on-cooldown");
				msg = ChatUtils.formatWithPlaceholders(p, msg, kitName);
				hoverBuilder.append(new ComponentBuilder(
						ChatUtils.format(msg))
						.create());
			}
			else if (kit.getItems().isEmpty()) {
				color = "&c&m";
				String msg = ChatUtils.getMessage("no-items");
				hoverBuilder.append(new ComponentBuilder(
						ChatUtils.format(msg))
						.create());
			}
			else {
				hoverBuilder.append(new ComponentBuilder(
						ChatUtils.format(ChatUtils.getMessage("available")))
						.create());
			}

			if (kit.isPersokit()) {
				color += "&l";
				if (!kit.getPersokits().containsKey(p.getUniqueId()) ) {
					color += "&n";
					hoverBuilder.append(new ComponentBuilder(
							ChatUtils.format("\n" + ChatUtils.getMessage("no-persokit-set")))
							.create());
				}
			}
			BaseComponent[] line = new ComponentBuilder(ChatUtils.format(color + kitName))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuilder.create()))
					.create();
			builder.append(line);
			if (i != playerKits.size()) builder.append(", ");
			i++;
		}
		return builder;
	}

}