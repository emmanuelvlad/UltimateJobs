package de.warsteiner.jobs.events;

import java.io.File;
 
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAPI;
import de.warsteiner.jobs.utils.PlayerAPI;
 
public class PlayerBlockBreak implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		if (block.hasMetadata("placed-by-player")) {
			return;
		} 
		 
		JobAPI api = plugin.getJobAPI(); 
		PlayerAPI p = plugin.getPlayerAPI();
 
		for (File job : plugin.getLoadedJobs()) {
			
			if(api.needCustomItemToWork(job)) {
				if(!api.hasItemInhandWhichIsNeed(job, player)) { 
					return;
				}
			}
			
			String id = api.getName(job.getName()); 
			if (api.canWorkThere(player, job)) {
				
				Material mat = block.getType();
				
				if(api.canReward(player, job, ""+mat)) { 
					
					String UUID = "" + player.getUniqueId();

					
					if (p.isInJob(UUID, id)) {
						player.sendMessage("give reward §b"+job);
						return;
					}
				}
			}
		}
	}
}