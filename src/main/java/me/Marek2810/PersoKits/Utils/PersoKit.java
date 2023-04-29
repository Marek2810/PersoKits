package me.Marek2810.PersoKits.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.Marek2810.PersoKits.PersoKits;

public class PersoKit {

	protected String name;
	protected List<ItemStack> items;
	protected double cooldwon;	
	protected int uses;
	protected boolean isPersokit;
	protected int slots;
	protected List<ItemStack> options;
	
	protected HashMap<UUID, List<ItemStack>> persokits;	

	public PersoKit(String name, List<ItemStack> items, double cd, int uses, boolean persokit, int slots,
			List<ItemStack> options, HashMap<UUID, List<ItemStack>> persokits) {
		this.name = name;
		this.items = items;
		this.cooldwon = cd;
		this.uses = uses;
		this.isPersokit = persokit;
		this.slots = slots;
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
		Set<String> keys = PersoKits.kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
		int i = 1;
		for (ItemStack item : items) {
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.kitsFile.getConfig().set(name + ".items." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.kitsFile.getConfig().set(name + ".items." + key, null);
		}
		PersoKits.kitsFile.saveConfig();
	}
	
	public int getUses() {
		return uses;
	}

	public void setUses(int uses) {
		PersoKits.kitsFile.getConfig().set(name + ".uses", uses);
		PersoKits.kitsFile.saveConfig();
		this.uses = uses;
	}

	public double getCooldwon() {
		return cooldwon;
	}
	
	public void setCooldwon(double cooldwon) {
		PersoKits.kitsFile.getConfig().set(name + ".cooldown", cooldwon);
		PersoKits.kitsFile.saveConfig();
		this.cooldwon = cooldwon;
	}
	
	public boolean isPersokit() {
		return isPersokit;
	}

	public void setPersokit(boolean isPersokit) {
		PersoKits.kitsFile.getConfig().set(name + ".persokit", isPersokit);
		PersoKits.kitsFile.saveConfig();
		this.isPersokit = isPersokit;
	}
	
	public int getSlots() {
		return slots;
	}
	
	public void setSlots(int slots) {
		PersoKits.kitsFile.getConfig().set(name + ".slots", slots);
		PersoKits.kitsFile.saveConfig();
		this.slots = slots;
	}
	
	public List<ItemStack> getOptions() {
		return options;
	}

	public void setOptions(List<ItemStack> options) {
		this.options = options;
		Set<String> keys = PersoKits.kitsFile.getConfig().getConfigurationSection(name + ".items").getKeys(false);
		int i = 1;
		for (ItemStack item : options) {
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.kitsFile.getConfig().set(name + ".options." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.kitsFile.getConfig().set(name + ".options." + key, null);
		}
		PersoKits.kitsFile.saveConfig();
	}
	
	public HashMap<UUID, List<ItemStack>> getPersokits() {
		return persokits;
	}

	public void setPersokits(HashMap<UUID, List<ItemStack>> persokits) {
		this.persokits = persokits;
	}
	
	public void setPersoKitVariant(UUID uuid, List<ItemStack> items) {		
//		int i = 1;
//		for (ItemStack item : items) {
//			PersoKits.pKitsFile.getConfig().set(name + "." + uuid + "." +  i, item);
//			i++;
//		}		
//		PersoKits.pKitsFile.saveConfig();		
		this.persokits.put(uuid, items);
		Set<String> keys = PersoKits.pKitsFile.getConfig().getConfigurationSection(name + "." + uuid).getKeys(false);
		int i = 1;
		for (ItemStack item : items) {
			if (keys.contains(String.valueOf(i))) keys.remove(String.valueOf(i));
			PersoKits.pKitsFile.getConfig().set(name + "." + uuid + "." + i, item);
			i++;
		}		
		for (String key : keys) {
			PersoKits.pKitsFile.getConfig().set(name + "." + uuid + "." + key, null);
		}
		PersoKits.pKitsFile.saveConfig();
		
	}
		
}
