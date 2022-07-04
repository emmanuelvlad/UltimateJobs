package de.warsteiner.jobs.api;
   
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs; 
 
public class ItemAPI {
	
	private UltimateJobs plugin = UltimateJobs.getPlugin();
 
	@SuppressWarnings("deprecation")
	public ItemStack createItem(Player p, String item) {
		ItemStack i = null;
		if (Material.getMaterial(item.toUpperCase()) == null) {
			if(item.contains(";")) { 
				String[] split = item.split(";"); 
				if(split[0].toLowerCase().equalsIgnoreCase("url")) { 
					i = plugin.getSkullCreatorAPI().itemFromUrl(split[1]);
				} else if(split[0].toLowerCase().equalsIgnoreCase("uuid")) {
					i = plugin.getSkullCreatorAPI().itemFromUuid(split[1]);
				} else if(split[0].toLowerCase().equalsIgnoreCase("name")) {  
					i = plugin.getSkullCreatorAPI().itemFromName(split[1].replaceAll("<name>", p.getName()));
				} else if(split[0].toLowerCase().equalsIgnoreCase("base64")) { 
					i = plugin.getSkullCreatorAPI().itemFromBase64(split[1]);
				}
				
			} 
		} else {
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack createItem(String p, String item) {
		ItemStack i = null;
		if (Material.getMaterial(item.toUpperCase()) == null) {
			if(item.contains(";")) { 
				String[] split = item.split(";"); 
				if(split[0].toLowerCase().equalsIgnoreCase("url")) { 
					i = plugin.getSkullCreatorAPI().itemFromUrl(split[1]);
				} else if(split[0].toLowerCase().equalsIgnoreCase("uuid")) {
					i = plugin.getSkullCreatorAPI().itemFromUuid(split[1]);
				} else if(split[0].toLowerCase().equalsIgnoreCase("name")) {  
					i = plugin.getSkullCreatorAPI().itemFromName(split[1].replaceAll("<name>", p));
				} else if(split[0].toLowerCase().equalsIgnoreCase("base64")) { 
					i = plugin.getSkullCreatorAPI().itemFromBase64(split[1]);
				}
				
			} 
		} else {
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}
		return i;
	}
 
}
