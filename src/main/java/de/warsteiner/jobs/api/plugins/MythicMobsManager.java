package de.warsteiner.jobs.api.plugins;
 
import org.bukkit.entity.Player; 

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
 
public class MythicMobsManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public void executeWork(MythicMobDeathEvent event) {
		
		  
		plugin.getJobWorkManager().finalWork("" + event.getMobType().getInternalName(), ((Player) event.getKiller()).getUniqueId(), JobAction.MMKill, "mmkill-action", 1, null, event.getEntity(),true,false,true);
		return;

	}

}
