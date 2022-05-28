package de.warsteiner.jobs.jobs;
 
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 

import de.warsteiner.jobs.UltimateJobs;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;

public class JobActionMMKill implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(MythicMobDeathEvent event) {
		
		if(event.getEntity() == null) {
			return;
		}
		
		if(event.getKiller() == null) {
			return;
		}
		
		plugin.getMythicMobsManager().executeWork(event); 
	}
}
