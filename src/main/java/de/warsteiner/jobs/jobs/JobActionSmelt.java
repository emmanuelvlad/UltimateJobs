package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionSmelt implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	  public void inventoryClick(InventoryClickEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}
		
	    Player p = (Player)e.getWhoClicked();
	    if (e.getInventory().getType() != InventoryType.FURNACE) {
	      return; 
	    }
	    if (e.getSlot() != 2 || e.getInventory().getItem(2) == null) {
	      return;
	    }
	    
	    ItemStack item = e.getInventory().getItem(2);
	    
	    int amount = item.getAmount();
	     
	    plugin.getJobWorkManager().executeSmelt(""+item.getType(), p.getUniqueId(), amount);
	    
	}
}