package de.warsteiner.jobs.api.plugins;
 
import org.bukkit.entity.Player; 

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.JobAction;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
 
public class MythicMobsManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public void executeWork(MythicMobDeathEvent event, JobsPlayer pl) {
		
		  
		plugin.getJobWorkManager().finalWork("" + event.getMobType().getInternalName(), (Player) event.getKiller(), pl, JobAction.MMKill, "mmkill-action", 1);
		return;

	}

}
