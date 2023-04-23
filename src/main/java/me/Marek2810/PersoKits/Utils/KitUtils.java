package me.Marek2810.PersoKits.Utils;

import org.bukkit.entity.Player;

public class KitUtils {

	public static boolean hasPermission(Player p, String name) {
		if (p.hasPermission("persokits.kit.*")) return true;
		if (p.hasPermission("persokits.kit." + name)) return true;
		return false;
	}
	
}