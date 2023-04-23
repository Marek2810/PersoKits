package me.Marek2810.PersoKits;

import org.bukkit.plugin.java.JavaPlugin;

import me.Marek2810.PersoKits.Files.CustomConfig;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;
	
	public static CustomConfig kits;

	@Override
	public void onEnable() {
		inst = this;		
		kits = new CustomConfig(this, "kits.yml");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}
	
}
