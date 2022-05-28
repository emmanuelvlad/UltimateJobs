package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionEat implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			plugin.getJobWorkManager().executeEatAction(event);
		}
	}
}
