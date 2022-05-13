package de.warsteiner.jobs.jobs;
 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener; 

import de.warsteiner.jobs.UltimateJobs;
import me.flyfunman.moreores.OreBreakEvent;

public class JobActionMoreOres_BreakEvent implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(OreBreakEvent event) {
		 
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		
		Player player = event.getBreakEvent().getPlayer();
		
		String fin = event.getOreName().replaceAll(" ", "_").toUpperCase();
		
		plugin.getMoreOresManager().executeBreakEvent(player, fin, plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId()));
		
	}
}