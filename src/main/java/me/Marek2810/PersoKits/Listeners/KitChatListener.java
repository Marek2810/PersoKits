package me.Marek2810.PersoKits.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Menus.KitEditMenu;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class KitChatListener implements Listener {
	
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PlayerMenuUtility menuUtil = PersoKits.getPlayerMenuUtility(p);
		if (!menuUtil.isEditingKit()) return;
		String setting = menuUtil.getKitSetting();
		e.setCancelled(true);
		PersoKit kit = PersoKits.kits.get(menuUtil.getKit());		
		if (setting.equalsIgnoreCase("cooldown")) {			
			if (!ChatUtils.isDouble(e.getMessage())) {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("requires-number")));
				return;
			}
			kit.setCooldwon(Double.valueOf(e.getMessage()));
			String msg = ChatUtils.getMessage("set-cooldown");
			msg = msg.replace("%cooldown%", e.getMessage());
			msg = msg.replace("%name%", kit.getName());
			p.sendMessage(ChatUtils.format(msg));
			menuUtil.setEditingKit(false);
		}
		else if (setting.equalsIgnoreCase("uses")) {
			if (!ChatUtils.isInt(e.getMessage())) {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("requires-number")));
				return;
			}
			kit.setUses(Integer.valueOf(e.getMessage()));
			String msg = ChatUtils.getMessage("set-uses");
			msg = msg.replace("%uses%", e.getMessage());
			msg = msg.replace("%name%", kit.getName());
			p.sendMessage(ChatUtils.format(msg));
			menuUtil.setEditingKit(false);
		}
		else if (setting.equalsIgnoreCase("slots")) {
			if (!ChatUtils.isInt(e.getMessage())) {
				p.sendMessage(ChatUtils.format(ChatUtils.getMessage("requires-number")));
				return;
			}
			kit.setSlots(Integer.valueOf(e.getMessage()));
			String msg = ChatUtils.getMessage("set-slots");
			msg = msg.replace("%slots%", e.getMessage());
			msg = msg.replace("%name%", kit.getName());
			p.sendMessage(ChatUtils.format(msg));
			menuUtil.setEditingKit(false);
		}
		else if (setting.equalsIgnoreCase("addKit")) {
			String kitName = e.getMessage();
			if (PersoKits.kits.keySet().contains(kitName)) {
				p.sendMessage("Kit exist");
				return;
			}
			PersoKit newKit = new PersoKit(kitName, 0, -1, false, 0, new ArrayList<>(), new ArrayList<>(), new HashMap<>()).create();
			PersoKits.kits.put(kitName, newKit);
			String msg = ChatUtils.getMessage("kit-added");
			p.sendMessage(ChatUtils.formatWithPlaceholders(p, msg, newKit));
			p.sendMessage(ChatUtils.format(ChatUtils.getMessage("opening-settings")));
			menuUtil.setEditingKit(false);
			menuUtil.setKit(newKit.getName());
						
			new BukkitRunnable() {
				public void run() {					
					new KitEditMenu(menuUtil).open();
					cancel();
				} 
			}.runTaskLater(PersoKits.getPlugin(), 1);
					
			
		}
		
	}

}
