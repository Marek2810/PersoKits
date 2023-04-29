package me.Marek2810.PersoKits.Utils;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {
	
	private Player owner;
	
	private String kit;
	private boolean editingKit;
	private String kitSetting;	
	
	private String pKit;

	public PlayerMenuUtility(Player p) {
		this.owner = p;
	}

	public Player getOwner(){
		return owner;
	}
	
	public String getKit(){
		return kit;
	}
	
	public void setKit(String kit) {
		this.kit = kit;
	}	

	public boolean isEditingKit() {
		return editingKit;
	}

	public void setEditingKit(boolean editingKit) {
		this.editingKit = editingKit;
	}
	
	public String getKitSetting() {
		return kitSetting;
	}

	public void setKitSetting(String setting) {
		this.kitSetting = setting;
	}

	public String getpKit() {
		return pKit;
	}

	public void setpKit(String pKit) {
		this.pKit = pKit;
	}

}