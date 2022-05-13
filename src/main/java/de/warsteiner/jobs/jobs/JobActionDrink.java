package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData; 

import de.warsteiner.jobs.UltimateJobs;

public class JobActionDrink implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(PlayerItemConsumeEvent event) {
		
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		  
		if (event.getItem() != null && event.getItem().hasItemMeta()) {
		    if (event.getItem().getItemMeta() instanceof PotionMeta) {
		        final PotionMeta meta = (PotionMeta) event.getItem().getItemMeta();
		        final PotionData data = meta.getBasePotionData();
		       
		        	if(data.getType() != null) {
		        		plugin.getJobWorkManager().executeDrinkEvent(event.getPlayer(), data.getType().toString(), plugin.getPlayerAPI().getRealJobPlayer(""+event.getPlayer().getUniqueId()));
		        	}
		         
		    }
		}
	}
}
