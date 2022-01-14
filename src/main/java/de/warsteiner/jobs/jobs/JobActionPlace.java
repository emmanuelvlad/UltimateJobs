package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.block.BlockPlaceEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionPlace implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent event) { 
		plugin.getAPI().executeBlockPlaceWork(event, plugin.getPlayerManager().getJonPlayers().get(""+event.getPlayer().getUniqueId()));
	}
	
}
