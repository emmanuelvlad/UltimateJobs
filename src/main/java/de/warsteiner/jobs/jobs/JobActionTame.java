package de.warsteiner.jobs.jobs;
 
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.entity.EntityTameEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionTame implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTame(EntityTameEvent event) { 
		AnimalTamer player = event.getOwner();
		
		if(player instanceof Player) {
		 
			plugin.getJobWorkManager().executeTameWork(event); 
		}
		
	}
}