package me.Marek2810.PersoKits.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.Marek2810.PersoKits.PersoKits;

public class KitUtils {

	public static boolean hasPermission(Player p, String name) {
		if (p.hasPermission("persokits.kit.*")) return true;
		if (p.hasPermission("persokits.kit." + name)) return true;
		return false;
	}
	
	public static boolean hasPersoPermission(Player p, String name) {
		if (p.hasPermission("persokits.pkit.*")) return true;
		if (p.hasPermission("persokits.pkit." + name)) return true;
		return false;
	}
	
	public static boolean isAviable(Player p, String kitName) {
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + kitName + ".availableAt");
		if (availableAt < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public static int aviableAt(Player p, String kitName) {
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + kitName + ".availableAt");
		return (int) (availableAt-System.currentTimeMillis())/1000;
	}
	
	public static boolean haveUsses(Player p, PersoKit kit) {
		int playerUses = 0;
		String kitName = kit.getName();
		if (kit.getUses() < 0 ) return true;
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + kitName + ".uses") != null) {
			playerUses = PersoKits.dataFile.getConfig().getInt("players." + p.getUniqueId() + "." + kitName + ".uses");
		}
		if (playerUses >= kit.getUses()) {			
			return false;
		}
		return true;
	}
	
	public static List<String> getAviableKitsForPlayer(Player p) {
		List<String> kits = new ArrayList<>();
		for (String name : PersoKits.kits.keySet()) {
			if (hasPermission(p, name)) {
				kits.add(name);
			}			
		}
		
		return kits;
	}
	
}