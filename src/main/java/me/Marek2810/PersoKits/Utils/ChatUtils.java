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
	
	@SuppressWarnings("unused")
	public static boolean isInt(String msg) {
		if (msg == null) {
	        return false;
	    }
	    try {
	        Integer num = Integer.parseInt(msg);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	@SuppressWarnings("unused")
	public static boolean isDouble(String msg) {
		if (msg == null) {
	        return false;
	    }
	    try {
	    	Double num = Double.parseDouble(msg);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
}
