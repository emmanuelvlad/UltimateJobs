package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent; 
import de.warsteiner.jobs.UltimateJobs;

public class JobActionKillMob implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(EntityDeathEvent event) {
		
		if( event.getEntity().getKiller() == null) {
			return;
		}
		
		plugin.getAPI().executeKillWork(event, plugin.getPlayerManager().getOnlineJobPlayers().get(""+event.getEntity().getKiller().getUniqueId()));
	}

}
