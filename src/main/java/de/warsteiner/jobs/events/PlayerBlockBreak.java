package de.warsteiner.jobs.events;
 
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.warsteiner.jobs.UltimateJobs;
 
public class PlayerBlockBreak implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material mat = block.getType();
	 
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
 
		if (block.hasMetadata("placed-by-player")) {
			return;
		} 
	 
		plugin.getJobAPI().executeWork(player, ""+mat);
		 
	}
}