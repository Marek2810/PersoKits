package me.Marek2810.PersoKits.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class PersoKitsMenu implements InventoryHolder {

	protected Inventory inv;
    protected PlayerMenuUtility pMenuUtil;
    protected Player owner;
	
	protected ItemStack blackGlass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name(" ").make();
	protected ItemStack grayGlass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").make();
	protected ItemStack closeItem = new ItemBuilder(Material.BARRIER).name("&cClose menu").function("close").make();
	protected ItemStack backMenu = new ItemBuilder(Material.ARROW).name("&cPrevious menu").function("backMenu").make();
	
	public PersoKitsMenu(PlayerMenuUtility pMenuUtil) {
		this.pMenuUtil = pMenuUtil;
		owner = pMenuUtil.getOwner();
		inv = Bukkit.createInventory(this, getRows()*9, ChatUtils.format(getTitle()));				
	}
	
	public abstract String getTitle();
	public abstract int getRows();
	public abstract void setMenuItems();

	public abstract void handleMenu(InventoryClickEvent e);
	
	public void open() {	
		setDefaultItems();	
		setMenuItems();
		fillInventory();
		owner.openInventory(inv);
	}	
	
	public void setDefaultItems() {
		if (getRows() < 3) return;

		//first row
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, blackGlass);
		}
		
//		//border
//		for (int i = 1; i < getRows(); i++) {
//			inv.setItem(i*9, blackGlass);
//			inv.setItem(9*i+8, blackGlass);
//		}		
		
		//last row
		int slots = getRows()*9;
		for (int i = slots-9; i < slots; i++) {
			inv.setItem(i, blackGlass);
		}
		inv.setItem(slots-5, closeItem);
	}
	
	public void fillInventory() {
		for (int i = 0; i < getRows()*9; i++) {
			if (inv.getItem(i) != null) continue;
			inv.setItem(i, grayGlass);
		}
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
}
