package me.Marek2810.PersoKits.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.InventoryUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;

public class KitCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatUtils.format("&cThis command can be run only by player."));
			return true;			
		}
		Player p = (Player) sender;		
		if (args.length == 0) {
			p.sendMessage("todo :( list of kits");
			return true;
		}
		String kitName = args[0];
		if (!PersoKits.kits.containsKey(kitName)){
			p.sendMessage(ChatUtils.format("&cKit not exist."));
			return true;
		}
		if (!KitUtils.hasPermission(p, kitName)) {
			p.sendMessage(ChatUtils.format("&cYou have no permissions to get this kit."));
			return true;
		}
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId().toString() + "." + kitName + ".availableAt") != null) {
			long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + kitName + ".availableAt");
			if (availableAt > System.currentTimeMillis()) {
				long secs = (availableAt-System.currentTimeMillis())/1000;
				p.sendMessage(ChatUtils.format("&cKit is on cooldonw for &e" + secs + " &cseconds." ));
				return true;
			}
		}		
		PersoKit kit = PersoKits.kits.get(kitName);
		if (!InventoryUtils.canBeAdded(p.getInventory(), kit.getItems())) {
			p.sendMessage(ChatUtils.format("&cYou dont have enought space in inventory."));
			return true;
		}
				
		for (ItemStack item : kit.getItems()) {
			p.getInventory().addItem(item);
		}
		long at = (kit.getCooldwon()*1000)+System.currentTimeMillis();		
		PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + "." + kitName + ".availableAt", at);
		PersoKits.dataFile.saveConfig();
		p.sendMessage(ChatUtils.format("&aYou recived kit &e" + kitName + "&a."));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> results = new ArrayList<>();
		return results;
	}

}
