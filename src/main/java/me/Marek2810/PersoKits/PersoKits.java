package me.Marek2810.PersoKits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.Marek2810.PersoKits.Commands.KitCommand;
import me.Marek2810.PersoKits.Commands.KitEditorCommand;
import me.Marek2810.PersoKits.Commands.PKitCommand;
import me.Marek2810.PersoKits.Commands.PersoKitsCommand;
import me.Marek2810.PersoKits.Files.CustomConfig;
import me.Marek2810.PersoKits.Listeners.KitChatListener;
import me.Marek2810.PersoKits.Listeners.MenuListener;
import me.Marek2810.PersoKits.Listeners.PlayerListener;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;
	public static ConsoleCommandSender console;
	
	public static CustomConfig kitsFile;
	public static CustomConfig pKitsFile;
	public static CustomConfig dataFile;
	public static CustomConfig messagesFile;
	
	public static boolean firstJoinKitStatus;
	public static PersoKit firstJoinKit;
	
	public static final HashMap<String, PersoKit> kits = new HashMap<>();	
	public static final HashMap<Player, BukkitTask> fistKitTasks = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
   
    public static final List<CustomConfig> customConfigs = new ArrayList<>();


	@Override
	public void onEnable() {
		inst = this;		
		console = getServer().getConsoleSender();
		
		saveDefaultConfig();
		kitsFile = new CustomConfig(this, "kits.yml");
		customConfigs.add(kitsFile);
		dataFile = new CustomConfig(this, "data.yml");
		customConfigs.add(dataFile);
		messagesFile = new CustomConfig(this, "messages.yml");
		customConfigs.add(messagesFile);
		pKitsFile = new CustomConfig(this, "persokits.yml");
		customConfigs.add(pKitsFile);
		KitUtils.loadKits();
		KitUtils.loadFirstJoinKit();	
	
		this.getCommand("kit").setExecutor(new KitCommand());
		this.getCommand("kiteditor").setExecutor(new KitEditorCommand());
		this.getCommand("pkit").setExecutor(new PKitCommand());
		this.getCommand("persokits").setExecutor(new PersoKitsCommand());
		
		this.getServer().getPluginManager().registerEvents(new MenuListener(),this);
		this.getServer().getPluginManager().registerEvents(new KitChatListener(),this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(),this);
	}
	
	@Override
	public void onDisable() {
		kits.clear();
	}
	
	public static PersoKits getPlugin() {
		return inst;
	}	
	
	public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
    	if (!playerMenuUtilityMap.containsKey(p)) {
    		PlayerMenuUtility util = new PlayerMenuUtility(p);
    		playerMenuUtilityMap.put(p, util);
    		return util;
    	}    	
    	return playerMenuUtilityMap.get(p);
    }	
	
}
