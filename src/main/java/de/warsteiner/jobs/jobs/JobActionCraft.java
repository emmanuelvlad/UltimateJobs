package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionCraft  implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(CraftItemEvent event) {
		plugin.getAPI().executeCraftWork(event, plugin.getPlayerManager().getJonPlayers().get(""+event.getWhoClicked().getUniqueId()));
	}
}
