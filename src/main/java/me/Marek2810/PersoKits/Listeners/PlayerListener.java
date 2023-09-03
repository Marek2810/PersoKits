package me.Marek2810.PersoKits.Listeners;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.KitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PersoKits.getPlayerMenuUtility(p).isEditingKit()) {
			PersoKits.getPlayerMenuUtility(p).setEditingKit(false);
		}
		if (PersoKits.firstKitTasks.get(p) != null) {
			PersoKits.firstKitTasks.get(p).cancel();
			PersoKits.firstKitTasks.remove(p);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!PersoKits.firstJoinKitStatus) return;
		Player p = e.getPlayer();
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + ".first-kit-claimed") == null) {
			PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + ".first-kit-claimed", false);
			PersoKits.dataFile.saveConfig();
		}		
		if (KitUtils.getFirstKitClaimed(p)) return;
		if (PersoKits.firstJoinKit.isPersokit()) {
			boolean firstJoinMessage = PersoKits.fistJoinMsgStatus;
			boolean kitReminder = PersoKits.reminderStatus;
			if (firstJoinMessage && !kitReminder) {
				KitUtils.sendFistJoinMsg(p);
			}
			if (!kitReminder) return;
			KitUtils.setupReminder(p);
			return;
        }
		else {
			p.performCommand("firstjoinkit");
			return;
        }
    }

}

