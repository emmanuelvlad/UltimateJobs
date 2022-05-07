package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent; 

import de.warsteiner.jobs.UltimateJobs;

public class JobActionBreed implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTame(EntityBreedEvent event) { 
		if(event.getBreeder() instanceof Player) { 
			plugin.getJobWorkManager().executeBreedWork(event, plugin.getPlayerAPI().getRealJobPlayer(""+event.getBreeder().getUniqueId())); 
		}
	}
}
