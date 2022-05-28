package de.warsteiner.jobs.jobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionKillMob implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(EntityDeathEvent event) {

		if (event.getEntity().getKiller() == null) {
			return;
		}

		if (plugin.getFileManager().getConfig().getBoolean("CanEarnMoneyFromSpawnerMobs")) {
			if (event.getEntity().hasMetadata("spawned-by-spawner")) {
				return;
			}
		}

		plugin.getJobWorkManager().executeKillWork(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(EntitySpawnEvent event) {
		event.getEntity().setMetadata("spawned-by-spawner",
				new FixedMetadataValue(UltimateJobs.getPlugin(), "spawned-by-spawner"));
	}

}
