package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionBreak implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(BlockBreakEvent event) { 
		plugin.getJobWorkManager().executeBlockBreakWork(event, plugin.getPlayerManager().getRealJobPlayer(""+event.getPlayer().getUniqueId())); 
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		e.getBlock().setMetadata("placed-by-player",  new FixedMetadataValue(UltimateJobs.getPlugin(), "placed-by-player"));
	}
	
}
