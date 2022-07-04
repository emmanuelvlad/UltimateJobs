package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class DefaultJobActions implements Listener  {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		e.getBlock().setMetadata("saplingby",  new FixedMetadataValue(UltimateJobs.getPlugin(), "uuid;"+e.getPlayer().getUniqueId())); 
	}

}
