package de.warsteiner.jobs.jobs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionDrinkPotion implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDrink(PlayerItemConsumeEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}

		Player p = e.getPlayer();
		if (!e.getItem().getType().name().contains("POTION"))
			return;
		PotionMeta pm = (PotionMeta) e.getItem().getItemMeta();
		Potion pot = Potion.fromItemStack(e.getItem());
		int level = 1;
		if (pm.getBasePotionData().isUpgraded()) {
			level = 2;
		}
	 
		String named = pm.getBasePotionData().getType().name().toUpperCase();

		String id = named + "_" + level;

		plugin.getJobWorkManager().executePotionDrink(id, p.getUniqueId());

	}
}