package me.Marek2810.PersoKits.Utils;

import me.Marek2810.PersoKits.PersoKits;
import net.md_5.bungee.api.ChatColor;

public class ChatUtils {

	public static String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String getMessage(String name) {
		return PersoKits.messagesFile.getConfig().getString("messages." + name);
	}
	
}
