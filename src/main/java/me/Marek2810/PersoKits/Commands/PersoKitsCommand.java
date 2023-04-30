package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Files.CustomConfig;
import me.Marek2810.PersoKits.Utils.ChatUtils;

public class PersoKitsCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatUtils.format("&a" + PersoKits.getPlugin().getName() + " &eby &aMarek2810"));
			return true;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			for (CustomConfig file : PersoKits.customConfigs) {
				file.reloadConfig();
			}
			PersoKits.kits.clear();
			PersoKits.loadKits();
			sender.sendMessage(ChatUtils.format("&aPersoKits reloaded!"));
		}
			
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		List<String> subCommands = new ArrayList<>();
		subCommands.add("reload");
		for (String kitName : subCommands) {
			if (kitName.startsWith(args[0]))
				results.add(kitName);
		}
		
		return results;
	}

}
