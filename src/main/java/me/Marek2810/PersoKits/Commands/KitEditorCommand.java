package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.KitEditMenu;
import me.Marek2810.PersoKits.Menus.KitsEditorMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class KitEditorCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("only-player-command")));
			return true;			
		}
		Player p = (Player) sender;
		if (!p.hasPermission("persokits.kiteditor")) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-permission")));
			return true;
		}
		PlayerMenuUtility util = PersoKits.getPlayerMenuUtility(p);
		if (args.length == 0) {
			new KitsEditorMenu(util).open();
			return true;
		}
		String kitName = args[0];
		if (!PersoKits.kits.containsKey(kitName)){
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-exist")));
			return true;
		}
		util.setKit(kitName);
		new KitEditMenu(util).open();
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			kits.add(name);			
		}
		for (String kitName : kits) {
			if (kitName.startsWith(args[0]))
				results.add(kitName);
		}		
		return results;
	}
}
