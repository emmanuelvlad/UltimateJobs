package de.warsteiner.jobs.jobs;
    
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
 
public class JobActionAdvancement implements Listener  {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(PlayerAdvancementDoneEvent   event) { 
		plugin.getJobWorkManager().executeAchWork(event);
	}
}
