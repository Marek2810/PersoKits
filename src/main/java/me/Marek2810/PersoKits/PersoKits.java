package me.Marek2810.PersoKits;

import java.util.*;

import me.Marek2810.PersoKits.Files.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.Marek2810.PersoKits.Commands.KitCommand;
import me.Marek2810.PersoKits.Commands.KitEditorCommand;
import me.Marek2810.PersoKits.Commands.PKitCommand;
import me.Marek2810.PersoKits.Commands.PersoKitsCommand;
import me.Marek2810.PersoKits.Listeners.KitChatListener;
import me.Marek2810.PersoKits.Listeners.MenuListener;
import me.Marek2810.PersoKits.Listeners.PlayerListener;
import me.Marek2810.PersoKits.Utils.KitUtils;
import me.Marek2810.PersoKits.Utils.PersoKit;
import me.Marek2810.PersoKits.Utils.PlayerMenuUtility;

public class PersoKits extends JavaPlugin {
	
	public static PersoKits inst;
	public static ConsoleCommandSender console;
	
	public static CustomFile kitsFile;
	public static CustomFile pKitsFile;
	public static CustomFile dataFile;
	public static CustomFile messagesFile;
	
	public static boolean firstJoinKitStatus;
	public static PersoKit firstJoinKit;
	
	public static final LinkedHashMap<String, PersoKit> kits = new LinkedHashMap<>();
	public static final HashMap<Player, BukkitTask> fistKitTasks = new HashMap<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
   
    public static final List<CustomFile> customConfigs = new ArrayList<>();


	@Override
	public void onEnable() {
		inst = this;		
		console = getServer().getConsoleSender();
		
		new ConfigFile(this);
		saveDefaultConfig();

		kitsFile = new KitsFile(this, "kits.yml");
		customConfigs.add(kitsFile);
		dataFile = new DataFile(this, "data.yml");
		customConfigs.add(dataFile);
		messagesFile = new MessagesFile(this, "messages.yml");
		customConfigs.add(messagesFile);
		pKitsFile = new PersoKitsFile(this, "persokits.yml");
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
		fistKitTasks.clear();
		playerMenuUtilityMap.clear();
		customConfigs.clear();
		firstJoinKitStatus = false;
		firstJoinKit = null;
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
