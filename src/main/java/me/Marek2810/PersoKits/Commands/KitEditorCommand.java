package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.KitsMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;

public class KitEditorCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("only-player-command")));
			return true;			
		}
		Player p = (Player) sender;	
		new KitsMenu(PersoKits.getPlayerMenuUtility(p)).open();
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
//		List<String> kits = new ArrayList<>();
//		for (String name : PersoKits.kits.keySet()) {
//			if (sender.hasPermission("persokits.kit.*") || sender.hasPermission("persokits.kit." + name)) 
//				kits.add(name);			
//		}
//		for (String kitName : kits) {
//			if (kitName.startsWith(args[0]))
//				results.add(kitName);
//		}
		
		return results;
	}
}
