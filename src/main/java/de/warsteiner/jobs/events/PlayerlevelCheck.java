package de.warsteiner.jobs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerFinishWorkEvent; 

public class PlayerlevelCheck implements Listener {
	
	@EventHandler
	public void onChange(PlayerFinishWorkEvent event) {
		Player player = event.getPlayer();
		UltimateJobs.getPlugin().getLevelAPI().check(player, event.getJob(), event.getJobsPlayer());
	}

}
