package me.Marek2810.PersoKits.Files;

import me.Marek2810.PersoKits.PersoKits;

public class ConfigFile extends CustomFile {

	public ConfigFile(PersoKits plugin) {
		super(plugin, "config.yml");
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}

	@Override
	public String currentVersion() {
		return null;
	}

	@Override
	public void update() {

	}
}
