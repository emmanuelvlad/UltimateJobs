package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import de.warsteiner.jobs.UltimateJobs;
import scala.annotation.;

public class JobActionVillagerTrade_Buy implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	  public void inventoryClick(InventoryClickEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}
		
	    if (e.getInventory().getType() != InventoryType.MERCHANT) {
	        return; 
	    }
	
	}
}
