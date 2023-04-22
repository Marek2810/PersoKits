package me.Marek2810.PersoKits;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;

	@Override
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6PIČOVINA FUNGUJE"));
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}
	
}
