package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.PKitEditMenu;
import me.Marek2810.PersoKits.Menus.PKitsMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PKitCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("only-player-command")));
			return true;			
		}
		// /kit - show all aviable kits for player
		Player p = (Player) sender;
		if (args.length == 0) {
			List<String> playerKits = KitUtils.getAvailableKitsForPlayer(p);
			if (playerKits.size() > 0) {
				new PKitsMenu(PersoKits.getPlayerMenuUtility(p)).open();
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
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("not-persokit")));
			return true;
		}
		
			// no permissions for kit
		if (!kit.permittedToUse(p)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-permission")));
			return true;
		}
		
		PlayerMenuUtility util = PersoKits.getPlayerMenuUtility(p);
		util.setKit(kitName);
		new PKitEditMenu(util).open();
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			if (!PersoKits.kits.get(name).isPersokit()) continue;
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
