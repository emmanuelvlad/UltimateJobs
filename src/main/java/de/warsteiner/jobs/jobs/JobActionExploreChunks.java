package de.warsteiner.jobs.jobs;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 
import org.bukkit.event.player.PlayerMoveEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionExploreChunks implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	  public void inventoryClick(PlayerMoveEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}
	 
		plugin.getJobWorkManager().executeMoveAction(e.getPlayer().getUniqueId(), e);
	}
}