package me.Marek2810.PersoKits.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;

public class PersoKit {

	protected String name;	
	protected double cooldwon;	
	protected int uses;
	protected boolean isPersokit;
	protected int slots;
	protected List<ItemStack> items;
	protected List<ItemStack> options;
	
	protected HashMap<UUID, List<ItemStack>> persokits;	

	public PersoKit(String name, double cooldwon, int uses, boolean isPersokit, int slots, 
			List<ItemStack> items, List<ItemStack> options, HashMap<UUID, List<ItemStack>> persokits) {
		this.name = name;
		this.cooldwon = cooldwon;
		this.uses = uses;
		this.isPersokit = isPersokit;
		this.slots = slots;
		this.items = items;
		this.options = options;
		this.persokits = persokits;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ItemStack> getItems() {
		return items;
	}
	
	public void setItems(List<ItemStack> items) {		
		this.items = items;
		Set<String> keys = Collections.emptySet();
		if (PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".items") != null) {
			keys = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".items").getKeys(false);
		}
		int i = 1;
		for (ItemStack item : items) {
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.kitsFile.getConfig().set("kits." + name + ".items." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.kitsFile.getConfig().set("kits." + name + ".items." + key, null);
		}
		PersoKits.kitsFile.saveConfig();
	}
	
	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		PersoKits.kitsFile.getConfig().set("kits." + name + ".uses", uses);
		PersoKits.kitsFile.saveConfig();
		this.uses = uses;
	}

	public double getCooldwon() {
		return cooldwon;
	}
	
	public void setCooldwon(double cooldwon) {
		PersoKits.kitsFile.getConfig().set("kits." + name + ".cooldown", cooldwon);
		PersoKits.kitsFile.saveConfig();
		this.cooldwon = cooldwon;
	}
	
	public boolean isPersokit() {
		return isPersokit;
	}

	public void setPersokit(boolean isPersokit) {
		PersoKits.kitsFile.getConfig().set("kits." + name + ".persokit", isPersokit);
		PersoKits.kitsFile.saveConfig();
		this.isPersokit = isPersokit;
	}
	
	public int getSlots() {
		return slots;
	}
	
	public void setSlots(int slots) {
		PersoKits.kitsFile.getConfig().set("kits." + name + ".slots", slots);
		PersoKits.kitsFile.saveConfig();
		this.slots = slots;
	}
	
	public List<ItemStack> getOptions() {
		return options;
	}

	public void setOptions(List<ItemStack> options) {
		this.options = options;
		Set<String> keys = Collections.emptySet();
		if (PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".options") != null) {
			keys = PersoKits.kitsFile.getConfig().getConfigurationSection("kits." + name + ".options").getKeys(false);
		}
		int i = 1;
		for (ItemStack item : options) {
			if (item == null) continue;
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.kitsFile.getConfig().set("kits." + name + ".options." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.kitsFile.getConfig().set("kits." + name + ".options." + key, null);
		}
		PersoKits.kitsFile.saveConfig();
	}
	
	public HashMap<UUID, List<ItemStack>> getPersokits() {
		return persokits;
	}

	public void setPersokits(HashMap<UUID, List<ItemStack>> persokits) {
		this.persokits = persokits;
	}
	
	public void addPersoKitVariant(UUID uuid, List<ItemStack> items) {
		if (this.persokits.get(uuid) != null) this.persokits.remove(uuid);
		this.persokits.put(uuid, items);
		List<String> keys = new ArrayList<>();
		if (PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name + "." + uuid) != null) {
			keys = new ArrayList<>(PersoKits.pKitsFile.getConfig().getConfigurationSection("pkits." + name + "." + uuid).getKeys(false));
		}
		int i = 1;
		for (ItemStack item : items) {
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.pKitsFile.getConfig().set("pkits." + name + "." + uuid + "." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.pKitsFile.getConfig().set("pkits." + name + "." + uuid + "." + key, null);
		}
		PersoKits.pKitsFile.saveConfig();
		
	}	
	
	public PersoKit create() {
		setPersokit(false);
		setSlots(0);
		setCooldwon(cooldwon);
		setUses(uses);
		setItems(items);
		return this;
	}
	
	public void remove() {
		PersoKits.kitsFile.getConfig().set("kits." + name, null);
		PersoKits.kitsFile.saveConfig();
		PersoKits.kits.remove(name);
	}

	public boolean permittedToUse(Player p) {
		if (p.hasPermission("persokits.kit.*")) return true;
		if (p.hasPermission("persokits.kit." + getName())) return true;
		return false;
	}

	public boolean hasBypassPermission(Player p, String permName) {
		if (p.hasPermission("persokits.bypass.*")) return true;
		if (p.hasPermission("persokits.bypass." + permName)) return true;
		return false;
	}

	public boolean isAvailable(Player p) {
		if (hasBypassPermission(p, "cooldown")) return true;
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + getName() + ".availableAt");
		if (availableAt < System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public int availableAt(Player p) {
		long availableAt = PersoKits.dataFile.getConfig().getLong("players." + p.getUniqueId() + "." + getName() + ".availableAt");
		return (int) (availableAt-System.currentTimeMillis())/1000;
	}

	public boolean haveUses(Player p) {
		int playerUses = 0;
		if (getUses() < 0 ) return true;
		if (hasBypassPermission(p, "uses")) return true;
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + getName() + ".uses") != null) {
			playerUses = PersoKits.dataFile.getConfig().getInt("players." + p.getUniqueId() + "." + getName() + ".uses");
		}
		if (playerUses >= getUses()) {
			return false;
		}
		return true;
	}

	public void setPlayerCooldown(Player p) {
		long time = System.currentTimeMillis();
		long at = (int)(getCooldwon()*1000)+time;
		PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + "." + getName() + ".availableAt", at);
		PersoKits.dataFile.saveConfig();
	}

	public void setPlayerUses(Player p) {
		int playerUses = 0;
		if (PersoKits.dataFile.getConfig().get("players." + p.getUniqueId() + "." + getName() + ".uses") != null) {
			playerUses = PersoKits.dataFile.getConfig().getInt("players." + p.getUniqueId() + "." + getName() + ".uses");
		}
		PersoKits.dataFile.getConfig().set("players." + p.getUniqueId() + "." + getName() + ".uses", playerUses+1);
		PersoKits.dataFile.saveConfig();
	}

	public List<ItemStack> getPlayerPersoKit(Player p) {
		List<ItemStack> returnKit = new ArrayList<>();
		if (getPersokits().get(p.getUniqueId()) == null) {
			return returnKit;
		}
		returnKit = getPersokits().get(p.getUniqueId());
		return returnKit;
	}

}