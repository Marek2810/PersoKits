package me.Marek2810.PersoKits.Files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Marek2810.PersoKits.PersoKits;

public abstract class CustomFile {
	
	protected File file;
	protected YamlConfiguration configFile;

	protected File oldFile;
	protected FileConfiguration oldConfigFile;
	
	protected PersoKits plugin;
	protected String name;
	
	public CustomFile(PersoKits plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		setup();
		if (isUpdatable()) {
			if (needUpdate()) {
				setOldFile();
				update();
			}
		}

		InputStream defaultStream = this.plugin.getResource(name);
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.configFile.setDefaults(defaultConfig);
		}
	}
	
	public abstract boolean isUpdatable();
	public abstract String currentVersion();
	public abstract void update();

	public String getFileVersion() {		
		return getConfig().getString("version");
	}
	
	public boolean needUpdate() {
		String version = getFileVersion();
		String currentVersion = currentVersion();
		if (version == null) {
			return true;
		}
		else if (currentVersion.equals(version)) {
			return false;
		}
		else if (newerVersionAvailable()) {
			return true;
		}
		return true;
	}
	
	public boolean newerVersionAvailable() {
		String version = getFileVersion();
		String currentVersion = currentVersion();
		String[] fileVersionSplit = version.split("\\.");
		String[] currentVersionSplit = currentVersion.split("\\.");
		if (Integer.valueOf(fileVersionSplit[0]) < Integer.valueOf(currentVersionSplit[0])) return true;
		if (Integer.valueOf(fileVersionSplit[1]) < Integer.valueOf(currentVersionSplit[1])) return true;		
		return false;
	};

	public void setOldFile() {
		String[] nameSplit = name.split("\\.");
		oldFile = new File(plugin.getDataFolder(), nameSplit[0] + ".old." + nameSplit[1]);
		file.renameTo(oldFile);
		oldConfigFile = YamlConfiguration.loadConfiguration(oldFile);
		file.delete();
		setup();
	}

	public void baseUpdate() {
		if (configFile == null) return;
		for (String path : configFile.getKeys(true)) {
			if (path.equals("version")) continue;
			if (configFile.isConfigurationSection(path)) continue;

			if (oldConfigFile.get(path) != null) {
				if (configFile.get(path).equals(oldConfigFile.get(path))) continue;
				configFile.set(path, oldConfigFile.get(path));
			}
		}
		saveConfig();
	}
	
	public void setup() {
		file = new File(plugin.getDataFolder(), name);
		
		if (!file.exists()) {
			plugin.saveResource(name, false);
		}		
		
		configFile = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig() {
		if (file == null || configFile == null) {
			setup();
		}		
		return configFile;
	}
	
	public void saveConfig() {
		if (file == null || configFile == null) {
			setup();
		}
				
		try {			
			configFile.options().width(50000);
			configFile.save(file);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
		}
	}
	
	public void reloadConfig() {
		if (file == null || configFile == null) {
			setup();
		}
		configFile = YamlConfiguration.loadConfiguration(file);
	}
	
}