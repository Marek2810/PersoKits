package me.Marek2810.PersoKits.Utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.Marek2810.PersoKits.PersoKits;

public class ItemBuilder {

public final ItemStack item;
	
	public ItemBuilder(final Material itemType) {
        item = new ItemStack(itemType);
    }
    public ItemBuilder(final ItemStack itemStack) {
        item = itemStack;
    }
    public ItemBuilder() {
        item = new ItemStack(Material.PLAYER_HEAD);
    }
	
    public ItemBuilder amount(final int number) {
    	item.setAmount(number);
    	return this;
    }
    
    public ItemBuilder name(final String name) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(ChatUtils.format(name));
    	item.setItemMeta(meta);
    	return this;
    }
    
    public ItemBuilder lore(final List<String> lore) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setLore(lore);
    	item.setItemMeta(meta);
    	return this;
    }
    
    public ItemBuilder customModelData(final int cusModDat) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setCustomModelData(cusModDat);
    	item.setItemMeta(meta);
    	return this;
    }
    
    public ItemBuilder addData(final String key, final String data) {
    	ItemMeta meta = item.getItemMeta();
    	PersistentDataContainer perData = meta.getPersistentDataContainer();
    	perData.set(new NamespacedKey(PersoKits.getPlugin(), key), PersistentDataType.STRING, data);
    	item.setItemMeta(meta);
    	return this;
    }
    
    public ItemBuilder addData(final String key, final Integer data) {
    	ItemMeta meta = item.getItemMeta();
    	PersistentDataContainer perData = meta.getPersistentDataContainer();
    	perData.set(new NamespacedKey(PersoKits.getPlugin(), key), PersistentDataType.INTEGER, data);
    	item.setItemMeta(meta);
    	return this;
    }    
    
    public ItemBuilder function(final String f) {
    	addData("function", f);
    	return this;
    }
       
    public ItemStack make() {
    	return item;
    }
	
}
