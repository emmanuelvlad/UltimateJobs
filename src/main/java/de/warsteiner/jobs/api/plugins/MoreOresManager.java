package de.warsteiner.jobs.api.plugins;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class MoreOresManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public void executeBreakEvent(Player player, String id, Block block) {
		plugin.getJobWorkManager().finalWork(
				id.toUpperCase(),
				player.getUniqueId(), JobAction.MMORES_BREAK, "moreores-break-action", 1, block, null, true,true,false);
		return;

	}
	
}
