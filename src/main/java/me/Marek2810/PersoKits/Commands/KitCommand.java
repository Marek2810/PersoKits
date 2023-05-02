package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.PKitMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.InventoryUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class KitCommand implements TabExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("only-player-command")));
			return true;			
		}
		// /kit - show all available kits for player
		Player p = (Player) sender;		
		if (args.length == 0) {		
			List<String> playerKits = KitUtils.getAviableKitsForPlayer(p);
			if (playerKits.size() > 0) {
				ComponentBuilder builder = new ComponentBuilder();
				builder.append(new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("kits"))).create());					
				int i = 1;				
				for (String kitName : playerKits) {
					ComponentBuilder hoverBuilder = new ComponentBuilder();	
					String color = "&a";
					PersoKit kit = PersoKits.kits.get(kitName);
					if (!KitUtils.haveUses(p, kit)) {
						color = "&c";
						hoverBuilder.append(new ComponentBuilder(
								ChatUtils.format(ChatUtils.getMessage("no-uses")))
							.create());					
					}
					else if (!KitUtils.isAviable(p, kitName)) {
						color = "&e";
						String msg = ChatUtils.getMessage("on-cooldown");
//						msg = msg.replace("%time-left%", String.valueOf(KitUtils.aviableAt(p, name)));
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
				sender.spigot().sendMessage(builder.create());	
				return true;
			}	
			else {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-kits")));
				return true;
			}			
		}		

		// /kit <name> 
			//kot not exist
		String kitName = args[0];
		if (!PersoKits.kits.containsKey(kitName)){
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-exist")));
			return true;
		}
			// no permissions for kit
		if (!KitUtils.hasPermission(p, kitName)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-permission")));
			return true;
		}		
			// kit is on cooldown
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId().toString() + "." + kitName + ".availableAt") != null) {
			if (!KitUtils.isAviable(p, kitName)) {
				String msg = ChatUtils.getMessage("on-cooldown");
//				msg = msg.replace("%time-left%", String.valueOf(KitUtils.aviableAt(p, kitName)));
				msg = ChatUtils.formatWithPlaceholders(p, msg, kitName);
				p.sendMessage(ChatUtils.format(msg));
				return true;
			}			
		}
		
		PersoKit kit = PersoKits.kits.get(kitName);		
			//check for uses
		if (kit.getUses() >= 0) {
			if (!KitUtils.haveUses(p, kit)) {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-uses")));
				return true;
			}
		}
	
		List<ItemStack> items = new ArrayList<>();		
		if (kit.isPersokit()) {
			if (kit.getPersokits().get(p.getUniqueId()) != null) {
				items = kit.getPersokits().get(p.getUniqueId());
			}
			else {				
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-persokit-set")));
				PlayerMenuUtility util = PersoKits.getPlayerMenuUtility(p);
				double secs = 1.25;
				double ticks = secs*20;
				util.setKit(kitName);
				new BukkitRunnable() {			
					public void run() {
						new PKitMenu(util).open();
					}
				}.runTaskLater(PersoKits.getPlugin(), (int) ticks);				
				return true;
			}
		} else {
			items = kit.getItems();
		}
		
		//Is there items?
		if (items.isEmpty()) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-items")));
			return true;
		}
		
		// no space in inventory
		if (!InventoryUtils.canBeAdded(p.getInventory(), items)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-space")));
			return true;
		}	
		
			//getting kit
		for (ItemStack item : items) {
			p.getInventory().addItem(item);
		}
		
			//setting CD
		if (kit.getCooldwon() != 0) {			
			long time = System.currentTimeMillis();
			long at = (int)(kit.getCooldwon()*1000)+time;						
			PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + "." + kitName + ".availableAt", at);	
			PersoKits.dataFile.saveConfig();
		}
		if (kit.getUses() >= 0) {
			int playerUses = 0;
			if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + kitName + ".uses") != null) {
				playerUses = PersoKits.dataFile.getConfig().getInt("players." + p.getUniqueId() + "." + kitName + ".uses");
			}
			PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + "." + kitName + ".uses", playerUses+1);
			PersoKits.dataFile.saveConfig();
		}		
		String msg = ChatUtils.getMessage("on-kit-receive");
		msg = msg.replace("%name%", kitName);
		
		p.sendMessage(ChatUtils.format(msg));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			if (sender.hasPermission("persokits.kit.*") || sender.hasPermission("persokits.kit." + name)) 
				kits.add(name);			
		}
		for (String kitName : kits) {
			if (kitName.startsWith(args[0]))
				results.add(kitName);
		}
		
		return results;
	}

}
