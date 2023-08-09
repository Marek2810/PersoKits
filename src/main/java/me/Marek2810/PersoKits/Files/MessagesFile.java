package me.Marek2810.PersoKits.Files;

import me.Marek2810.PersoKits.PersoKits;

public class MessagesFile extends CustomFile {

	public MessagesFile(PersoKits plugin, String name) {
		super(plugin, name);
	}

	@Override
	public boolean isUpdatable() {
		return true;
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
