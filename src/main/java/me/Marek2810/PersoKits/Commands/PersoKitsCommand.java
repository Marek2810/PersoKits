package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import me.Marek2810.PersoKits.Files.CustomFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;

public class PersoKitsCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatUtils.format("&a" + PersoKits.getPlugin().getName() + " &eby &aMarek2810"));
			return true;
		}
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("rl") || args[0].equalsIgnoreCase("reload")) {
				for (CustomFile file : PersoKits.customConfigs) {
					file.reloadConfig();
				}
				boolean changed = false;
				PersoKits.kits.clear();
				PersoKits.inst.reloadConfig();
				KitUtils.loadKits();
				KitUtils.loadFirstJoinKit();
				changed = (PersoKits.oldFirstJoinKitStatus == PersoKits.firstJoinKitStatus);
				sender.sendMessage("changed: " + changed);
				if (changed) {
					if (!PersoKits.firstJoinKitStatus) {
						List<Player> playerList = new ArrayList<>(PersoKits.firstKitTasks.keySet());
						for (Player player : playerList) {
							PersoKits.firstKitTasks.get(player).cancel();
						}
						PersoKits.firstKitTasks.clear();
					}
					else {
						if (PersoKits.reminderStatus) {
							for (Player p : Bukkit.getServer().getOnlinePlayers()) {
								if (KitUtils.getFirstKitClaimed(p)) continue;
								KitUtils.setupReminder(p);
							}
						}
					}
				}
				PersoKits.oldFirstJoinKitStatus = PersoKits.firstJoinKitStatus;
				sender.sendMessage(ChatUtils.format("&aPersoKits reloaded!"));
				return true;
			}
			else if (args[0].equalsIgnoreCase("h") || args[0].equalsIgnoreCase("help")) {
				String commandColor = ChatUtils.getHelpMessage("command-color");
				sender.sendMessage(ChatUtils.format("&ePersoKits help \n"));
				sender.sendMessage(ChatUtils.format("&8--------------- \n"));
				sender.sendMessage(ChatUtils.format(commandColor + "/kit &7- " + ChatUtils.getHelpMessage("kit") + "\n"));
				sender.sendMessage(ChatUtils.format(commandColor + "/kit <name> &7- " + ChatUtils.getHelpMessage("kit-name") + "\n"));
				sender.sendMessage(ChatUtils.format(commandColor + "/pkit &7- " + ChatUtils.getHelpMessage("pkit") + "\n"));
				sender.sendMessage(ChatUtils.format(commandColor + "/pkit <name> &7- " + ChatUtils.getHelpMessage("pkit-name") + "\n"));
				if (sender.hasPermission("persokits.kiteditor") || !(sender instanceof Player)) {
					sender.sendMessage(ChatUtils.format(commandColor + "/kiteditor &7- " + ChatUtils.getHelpMessage("kiteditor") + " \n"));
					sender.sendMessage(ChatUtils.format(commandColor + "/kiteditor <name> &7- " + ChatUtils.getHelpMessage("kiteditor-name") + "\n"));
				}
				sender.sendMessage(ChatUtils.format("&8---------------"));
				return true;
			}
			else {
				sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("unknown-command")));
				sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("unknown-command1")));
				return true;
			}
		}
		else {
			sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("unknown-command")));
			sender.sendMessage(ChatUtils.format(ChatUtils.getMessage("unknown-command1")));
			return true;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		List<String> subCommands = new ArrayList<>();
		subCommands.add("help");
		subCommands.add("rl");
		subCommands.add("reload");
		for (String subCommand : subCommands) {
			if (subCommand.startsWith(args[0]))
				results.add(subCommand);
		}
		
		return results;
	}

}
