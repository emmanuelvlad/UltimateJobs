package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerShearEntityEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionShear implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	 
	@EventHandler(priority = EventPriority.HIGHEST) 
	public void onEvent(PlayerShearEntityEvent event) {
		plugin.getAPI().executeShearWork(event, plugin.getPlayerManager().getOnlineJobPlayers().get(""+event.getPlayer().getUniqueId()));
	}
}
