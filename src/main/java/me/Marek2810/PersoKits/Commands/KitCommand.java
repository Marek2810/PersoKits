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
import me.Marek2810.PersoKits.Menus.PKitEditMenu;
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
			sender.sendMessage(ChatUtils.format(ChatUtils.getConsoleMessage("only-player-command")));
			return true;
		}
		// /kit - show all available kits for player
		Player p = (Player) sender;
		if (args.length == 0) {
			List<String> playerKits = KitUtils.getAvailableKitsForPlayer(p);
			if (!playerKits.isEmpty()) {
//				sender.spigot().sendMessage(builder.create());
				sender.spigot().sendMessage(KitUtils.getKitsMessage(p).create());
            }
			else {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-kits")));
            }
            return true;
        }

		// /kit <name> 
			//kot not exist
		String kitName = args[0];
		if (!PersoKits.kits.containsKey(kitName)){
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-exist")));
			return true;
		}
		
		PersoKit kit = PersoKits.kits.get(kitName);

			// no permissions for kit
		if (!kit.permittedToUse(p)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-permission")));
			return true;
		}

		//check for uses
		if (!kit.haveUses(p)) {
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-uses")));
			return true;
		}

		// kit is on cooldown
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + kitName + ".availableAt") != null) {
			if (!kit.isAvailable(p)) {
				String msg = ChatUtils.getMessage("on-cooldown");
				msg = ChatUtils.formatWithPlaceholders(p, msg, kitName);
				p.sendMessage(ChatUtils.format(msg));
				return true;
			}	
		}
	
		List<ItemStack> items = new ArrayList<>();
		if (kit.isPersokit()) {
			if (kit.getPlayerPersoKit(p).isEmpty()) {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("no-persokit-set")));
				PlayerMenuUtility util = PersoKits.getPlayerMenuUtility(p);
				util.setKit(kitName);
				double secs = 1.25;
				double ticks = secs*20;
				new BukkitRunnable() {
				public void run() {
						new PKitEditMenu(util).open();
						cancel();
					}
				}.runTaskLater(PersoKits.getPlugin(), (int) ticks);
				return true;
			}
			else {
				items = kit.getPlayerPersoKit(p);
			}
		}
		else {
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
			kit.setPlayerCooldown(p);
		}
		if (kit.getUses() >= 0) {
			kit.setPlayerUses(p);
		}
		if (PersoKits.firstJoinKitStatus && kit.equals(PersoKits.firstJoinKit)) {
			if (!KitUtils.getFirstKitClaimed(p)) {
				KitUtils.setFirstKitClaimed(p);
				if (PersoKits.firstJoinKit.isPersokit()) {
					p.sendMessage(ChatUtils.format(ChatUtils.getMessage("on-first-kit-receive")));
				}
				return true;
			}
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
