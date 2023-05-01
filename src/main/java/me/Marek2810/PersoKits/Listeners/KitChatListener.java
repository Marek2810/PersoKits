package me.Marek2810.PersoKits.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Marek2810.PersoKits.PersoKits;
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
		
	}

}
