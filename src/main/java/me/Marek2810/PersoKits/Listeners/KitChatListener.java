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
				p.sendMessage(ChatUtils.format("&cYou must enter number."));
				return;
			}
			kit.setCooldwon(Double.valueOf(e.getMessage()));
			p.sendMessage(ChatUtils.format("&aCooldown for kit set for: " + e.getMessage() + " seconds."));
			menuUtil.setEditingKit(false);
		}
		else if (setting.equalsIgnoreCase("uses")) {
			if (!ChatUtils.isInt(e.getMessage())) {
				p.sendMessage(ChatUtils.format("&cYou must enter number."));
				return;
			}
			kit.setUses(Integer.valueOf(e.getMessage()));
			p.sendMessage(ChatUtils.format("&aUses of kit set to: &e" + e.getMessage() + "&a."));
			menuUtil.setEditingKit(false);
		}
		else if (setting.equalsIgnoreCase("slots")) {
			if (!ChatUtils.isInt(e.getMessage())) {
				p.sendMessage(ChatUtils.format("&cYou must enter number."));
				return;
			}
			kit.setSlots(Integer.valueOf(e.getMessage()));
			p.sendMessage(ChatUtils.format("&aSlots for persokit of kit set to: &e" + e.getMessage() + "&a."));
			menuUtil.setEditingKit(false);
		}
		
	}

}