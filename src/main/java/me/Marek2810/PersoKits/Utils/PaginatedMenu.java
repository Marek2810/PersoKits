package me.Marek2810.PersoKits.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class PaginatedMenu extends PersoKitsMenu {

	protected int page = 0;
	protected int maxItemsPerPage;
	protected int index = 0;
	
	protected ItemStack nextPage = new ItemBuilder(Material.ARROW).name("&9Next page").function("nextPage").make();
	protected ItemStack previousPage = new ItemBuilder(Material.ARROW).name("&9Previous page").function("previousPage").make();
	
	public PaginatedMenu(PlayerMenuUtility pMenuUtil) {
		super(pMenuUtil);
	}
	
	public int getMaxItemsPerPage() { 
		return maxItemsPerPage;
    }
	
	public void setDefaultItems() {
		if (getRows() < 3) return;

		//first row
		for (int i = 0; i < 9; i++) {
			inv.setItem(i, blackGlass);
		}
		
		int slots = getRows()*9;
		for (int i = slots-9; i < slots; i++) {
			inv.setItem(i, blackGlass);
		}
		inv.setItem(slots-6, previousPage);
		inv.setItem(slots-5, closeItem);
		inv.setItem(slots-4, nextPage);		
	}
	
}
