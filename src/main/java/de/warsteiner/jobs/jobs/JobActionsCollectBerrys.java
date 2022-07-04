package de.warsteiner.jobs.jobs;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionsCollectBerrys implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTame(PlayerHarvestBlockEvent event) { 
		
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		
		if(event.getHarvestedBlock() == null) {
			return;
		}
		 
		String id = event.getHarvestedBlock().getType().toString();
	 
		plugin.getJobWorkManager().executeBerrysEvent(event.getPlayer(), id, event.getHarvestedBlock());
		
	}
}
