package me.Marek2810.PersoKits.Files;

import me.Marek2810.PersoKits.PersoKits;

public class DataFile extends CustomFile {

	public DataFile(PersoKits plugin, String name) {
		super(plugin, name);
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

	@Override
	public String currentVersion() {
		return "1.1";
	}

	@Override
	public void update() {
		baseUpdate();
	}
}
