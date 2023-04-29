package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.PKitMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("only-player-command")));
			return true;			
		}
		// /kit - show all aviable kits for player
		Player p = (Player) sender;		
		if (args.length == 0) {		
			List<String> playerKits = KitUtils.getAviableKitsForPlayer(p);
			if (playerKits.size() > 0) {
				//TODO
				
//				ComponentBuilder builder = new ComponentBuilder();
//				builder.append(new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("kits"))).create());					
//				int i = 1;
//				
//				for (String name : playerKits) {
//					ComponentBuilder hoverBuilder = new ComponentBuilder();	
//					String color = "&a";				
//					if (!KitUtils.haveUsses(p, PersoKits.kits.get(name))) {
//						color = "&c";
//						hoverBuilder.append(new ComponentBuilder(
////								ChatUtils.format("&cYou have no usses left for this kit."))
//								ChatUtils.format(ChatUtils.getMessage("no-usses")))
//							.create());					
//					}
//					else if (!KitUtils.isAviable(p, name)) {
//						color = "&e";
//						String msg = ChatUtils.getMessage("on-cooldown");
//						msg = msg.replace("%time-left%", String.valueOf(KitUtils.aviableAt(p, name)));
//						hoverBuilder.append(new ComponentBuilder(
////								ChatUtils.format("&cKit is on cooldonw for &e" + KitUtils.aviableAt(p, name) + " &cseconds." ))
//								ChatUtils.format(msg))
//							.create());
//					}
//					else {
//						hoverBuilder.append(new ComponentBuilder(
//								ChatUtils.format(ChatUtils.getMessage("available")))
//							.create());
//					}
//					
//					BaseComponent[] line = new ComponentBuilder(ChatUtils.format(color + name))
//							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuilder.create()))
//							.create();
//					builder.append(line);
//					if (i != playerKits.size()) builder.append(", ");					
//					i++;
//				}
//				sender.spigot().sendMessage(builder.create());	
				return true;
			}	
			else {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-kits")));
				return true;
			}			
		}

		// /pkit <name> 
			//pkit not exist
		String kitName = args[0];
			
		if (!PersoKits.kits.containsKey(kitName)){
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-exist")));
			return true;
		}
		PersoKit kit = PersoKits.kits.get(kitName);
		
			//is persokit?
		if (!kit.isPersokit()) {
			p.sendMessage(ChatUtils.format("&cKit is not PersoKit."));
			return true;
		}
		
			// no permissions for kit
		if (!KitUtils.hasPersoPermission(p, kitName)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-permission")));
			return true;
		}
		
		PlayerMenuUtility util = PersoKits.getPlayerMenuUtility(p);
		util.setpKit(kitName);
		new PKitMenu(util).open();		
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
