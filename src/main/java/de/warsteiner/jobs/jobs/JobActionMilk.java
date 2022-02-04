package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionMilk implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(PlayerInteractAtEntityEvent event) {
		
		Entity clicked = event.getRightClicked();
		
		if (clicked instanceof Cow) { 
		//	Cow c = (Cow) clicked;
			plugin.getAPI().executeMilkWork(event, plugin.getPlayerManager().getOnlineJobPlayers().get(""+event.getPlayer().getUniqueId()));
		}
		
	}

}
