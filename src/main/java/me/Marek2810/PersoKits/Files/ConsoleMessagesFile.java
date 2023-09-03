package me.Marek2810.PersoKits.Files;

import me.Marek2810.PersoKits.PersoKits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleMessagesFile extends CustomFile {

	public ConsoleMessagesFile(PersoKits plugin, String name) {
		super(plugin, name);
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

	@Override
	public String currentVersion() {
		return "1.0";
	}

	@Override
	public void update() {
		baseUpdate();
	}

	@Override
	public void setup() {
		file = new File(plugin.getDataFolder(), name);
		InputStreamReader internalConfigFileStream = new InputStreamReader(PersoKits.getPlugin().getResource(name), StandardCharsets.UTF_8);
		configFile = YamlConfiguration.loadConfiguration(internalConfigFileStream);
	}
}
