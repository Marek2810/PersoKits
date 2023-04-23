package me.Marek2810.PersoKits.Utils;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PersoKit {

	protected String name;
	protected List<ItemStack> items;
	protected int cooldwon;	

	public PersoKit(String name, List<ItemStack> items, int cd) {
		this.name = name;
		this.items = items;
		this.cooldwon = cd;
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
	
	public void setItems(List<ItemStack> item) {
		this.items = item;
	}
	
	public int getCooldwon() {
		return cooldwon;
	}
	
	public void setCooldwon(int cooldwon) {
		this.cooldwon = cooldwon;
	}
		
}
