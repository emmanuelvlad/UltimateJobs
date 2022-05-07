package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionFarm implements Listener  {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onFarm(BlockBreakEvent event) { 
		plugin.getJobWorkManager().executeFarmWork(event, plugin.getPlayerAPI().getRealJobPlayer(""+event.getPlayer().getUniqueId()));
	}

}
