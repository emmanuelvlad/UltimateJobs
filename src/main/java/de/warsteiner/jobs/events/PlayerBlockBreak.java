package de.warsteiner.jobs.events;
 
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;
 
public class PlayerBlockBreak implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) { 
		plugin.getJobAPI().executeBlockBreakWork(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		e.getBlock().setMetadata("placed-by-player",  new FixedMetadataValue(UltimateJobs.getPlugin(), "placed-by-player"));
	}
	
}