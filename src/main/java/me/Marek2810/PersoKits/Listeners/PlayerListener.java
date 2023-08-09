package me.Marek2810.PersoKits.Listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.Marek2810.PersoKits.PersoKits;
import me.Marek2810.PersoKits.Utils.ChatUtils;
import me.Marek2810.PersoKits.Utils.KitUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (PersoKits.getPlayerMenuUtility(p).isEditingKit()) {
			PersoKits.getPlayerMenuUtility(p).setEditingKit(false);
		}
		if (PersoKits.fistKitTasks.get(p) != null) {
			PersoKits.fistKitTasks.get(p).cancel();
			PersoKits.fistKitTasks.remove(p);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (!PersoKits.firstJoinKitStatus) return;
		Player p = e.getPlayer();
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + ".first-kit-claimbed") == null) {
			PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + ".first-kit-claimbed", false);
			PersoKits.dataFile.saveConfig();
		}		
		if (KitUtils.getFirstKitClaimbed(p)) return;
		if (PersoKits.firstJoinKit.isPersokit()) {
			String msg = ChatUtils.getMessage("first-join-kit-reminder");
			
			ComponentBuilder hoverBuilder = new ComponentBuilder(ChatUtils.format(ChatUtils.getMessage("first-join-kit-reminder-hover")));
			
			ComponentBuilder builder = new ComponentBuilder(ChatUtils.format(msg))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + PersoKits.firstJoinKit.getName()))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuilder.create()));
						
			boolean firstJoinMessage = PersoKits.inst.getConfig().getBoolean("first-join-kit.msg-enabled");
			boolean kitReminder = PersoKits.inst.getConfig().getBoolean("first-join-kit.reminder.enabled");
			p.sendMessage("message: " + firstJoinMessage);
			p.sendMessage("kitReminder: " + kitReminder);
			if (!p.hasPlayedBefore()) {
				if (firstJoinMessage && !kitReminder) {	
					new BukkitRunnable() {			
						public void run() {
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
							p.spigot().sendMessage(builder.create());
							cancel();
						}
					}.runTaskLater(PersoKits.getPlugin(), 10*20);
				}			
			}
			if (!kitReminder) return;							
			double delay = PersoKits.inst.getConfig().getDouble("first-join-kit.reminder.delay-for-msg-after-join")*20;
			double interval = PersoKits.inst.getConfig().getDouble("first-join-kit.reminder.repeat-after")*20;
			p.sendMessage("delay: " + delay);
			p.sendMessage("interval: "+  interval);
			BukkitTask firstKitRemider = new BukkitRunnable() {			
				public void run() {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
					p.spigot().sendMessage(builder.create());
				}
			}.runTaskTimer(PersoKits.getPlugin(), (int)delay, (int)interval);
			PersoKits.fistKitTasks.put(p, firstKitRemider);
			return;
		}
		else {
			p.performCommand("kit " + PersoKits.firstJoinKit.getName());
		}		
	}

}

