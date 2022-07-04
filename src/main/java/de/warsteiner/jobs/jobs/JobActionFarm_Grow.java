package de.warsteiner.jobs.jobs;

import java.util.UUID;
 
import org.bukkit.block.Block; 
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.block.BlockGrowEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionFarm_Grow implements Listener  {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFarm(BlockGrowEvent event) { 
		
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;    
		}
	 
		Block block = event.getBlock();
		
		if(block == null) {
			return;
		}
		 
		if(block.hasMetadata("saplingby")) {
			
			String player = plugin.getAPI().compareData(block);
			 
			if(player != null) {
				plugin.getJobWorkManager().executeFarmGrowWork(block, UUID.fromString(player.toString())); 
			}
			
		}
		
		 
	}

}
