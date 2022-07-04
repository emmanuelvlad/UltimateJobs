package de.warsteiner.jobs.jobs;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionTNT implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(EntityExplodeEvent event) {
		
		if(event.getEntity() == null) {
			return;
		}
		
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		
		if(event.getEntity().getType() != EntityType.PRIMED_TNT) {
			return;
		}
		Bukkit.broadcastMessage("1");
		if(event.getEntity().hasMetadata("saplingby")) {
			Bukkit.broadcastMessage("2");
			String player = plugin.getAPI().compareData(event.getEntity());
			 
			if(player != null) {
				plugin.getJobWorkManager().executeTNTEvent("TNT", event.getEntity(), UUID.fromString(player.toString())); 
			}
			
		}
	 
		 
		
	}
	
}
