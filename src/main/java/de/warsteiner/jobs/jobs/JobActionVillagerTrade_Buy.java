package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import de.warsteiner.jobs.UltimateJobs;
 

public class JobActionVillagerTrade_Buy implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	  public void Click(InventoryClickEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}
		
	    if (e.getInventory().getType() != InventoryType.MERCHANT) {
	        return; 
	    }
	    
	    Inventory villagerTrade = e.getInventory();
        MerchantInventory villagerTradeMeta = (MerchantInventory)villagerTrade;
	     
        
        if(e.getSlot() != 2) {
        	return;
        }
	   
        if(villagerTradeMeta.getItem(2) == null) {
        	return;
        }
        
	    if(villagerTradeMeta.getSelectedRecipe().getResult() != null) {
	       
	    	if(villagerTradeMeta.getItem(2).getType().equals(villagerTradeMeta.getSelectedRecipe().getResult().getType())) {
	    		  Player p = (Player) e.getWhoClicked();
			      
	  	        ItemStack result = villagerTradeMeta.getSelectedRecipe().getResult();
	  	   
	  	        int amount = result.getAmount();
	  	    	
	  	        plugin.getJobWorkManager().executeVilBuyTrade(""+result.getType(), p.getUniqueId(), amount);
	    	}
	    	
	    }
	    
	}
}
