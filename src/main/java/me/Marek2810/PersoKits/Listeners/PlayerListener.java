package me.Marek2810.PersoKits.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Marek2810.PersoKits.PersoKits;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onAsyncPlayerChat(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PersoKits.getPlayerMenuUtility(p).isEditingKit()) {
			PersoKits.getPlayerMenuUtility(p).setEditingKit(false);
		}
	}

}

