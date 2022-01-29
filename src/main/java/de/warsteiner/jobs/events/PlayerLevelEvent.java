package de.warsteiner.jobs.events;

import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;

public class PlayerLevelEvent implements Listener {

	@EventHandler
	public void onLevelUp(PlayerLevelJobEvent event) {
		Player player = event.getPlayer();
		YamlConfiguration config = UltimateJobs.getPlugin().getMainConfig().getConfig();
		UltimateJobs.getPlugin().getAPI().playSound("LEVEL_UP", player);
		if (config.getBoolean("Levels.FireWork")) {
			UltimateJobs.getPlugin().getAPI().spawnFireworks(player.getLocation());
		} 
	}

}
