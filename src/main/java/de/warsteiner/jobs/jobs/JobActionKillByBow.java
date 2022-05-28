package de.warsteiner.jobs.jobs;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionKillByBow implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDeathEvent event) {

		EntityDamageEvent dc = event.getEntity().getLastDamageCause();
		if (dc instanceof EntityDamageByEntityEvent) {
			Entity e = ((EntityDamageByEntityEvent) dc).getDamager();
			if (e instanceof Arrow) {

				if (dc.isCancelled()) {
					if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
						dc.setCancelled(true);
					}
					return;
				}

				Arrow ar = (Arrow) e;

				if (ar.getShooter() instanceof Player) {

					Player player = (Player) ar.getShooter();

					EntityType type = event.getEntity().getType();

					plugin.getJobWorkManager().executeKillByBowEvent(player, type.toString(), event.getEntity());

				}

			}
		}
	}

}