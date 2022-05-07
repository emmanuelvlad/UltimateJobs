package de.warsteiner.jobs.jobs;
 
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action; 
import org.bukkit.event.player.PlayerInteractEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionHoney implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(PlayerInteractEvent  event) {
		
		Action action = event.getAction();
		Block clickedBlock = event.getClickedBlock();
	 
		if (action == Action.RIGHT_CLICK_BLOCK && clickedBlock != null
				&& (clickedBlock.getType().equals(Material.BEEHIVE)
						|| clickedBlock.getType().equals(Material.BEE_NEST))) {
		
			BlockData bdata = clickedBlock.getBlockData();
			Beehive beehive = (Beehive) bdata;

			if (beehive.getHoneyLevel() != beehive.getMaximumHoneyLevel()) {
				if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
					event.setCancelled(true);
				}
				return;
			}
			
			plugin.getJobWorkManager().executeHoneyAction(event,
					plugin.getPlayerAPI().getRealJobPlayer(""+event.getPlayer().getUniqueId()));
		 
		}
	}
}
