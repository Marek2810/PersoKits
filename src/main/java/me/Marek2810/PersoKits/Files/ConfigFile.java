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
		return "1.2";
	}

	@Override
	public void update() {
		String oldVersion = "1.0";
		if (oldConfigFile.get("version") != null ) {
			oldVersion = oldConfigFile.getString("version");
		}
		if (oldVersion.equalsIgnoreCase("1.0")) {
			if (oldConfigFile.get("first-join-kit-msg-enabled") != null) {
				getConfig().set("first-join-kit.msg-enabled", oldConfigFile.get("first-join-kit-msg-enabled"));
			}
			if (oldConfigFile.get("first-join-kit-reminder.enabled") != null) {
				getConfig().set("first-join-kit.reminder.enabled", oldConfigFile.get("first-join-kit-reminder.enabled"));
			}
			if (oldConfigFile.get("first-join-kit-reminder.delay-for-msg-after-join") != null) {
				getConfig().set("first-join-kit.reminder.delay-for-msg-after-join", oldConfigFile.get("first-join-kit-reminder.delay-for-msg-after-join"));
			}
			if (oldConfigFile.get("first-join-kit-reminder.repeat-after") != null) {
				getConfig().set("first-join-kit.reminder.repeat-after", oldConfigFile.get("first-join-kit-reminder.repeat-after"));
			}
		}
		else if (oldVersion.equalsIgnoreCase("1.1")) {
			getConfig().set("first-join-kit.delay-for-msg-after-join", oldConfigFile.get("first-join-kit.reminder.delay-for-msg-after-join"));
		}
		getConfig().set("version", currentVersion());
		saveConfig();
		baseUpdate();
	}
}
