package de.warsteiner.jobs.events;

import org.bukkit.Bukkit; 
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;

public class PlayerLevelEvent implements Listener {

	private UltimateAPI plugin = UltimateAPI.getPlugin();
	
	@EventHandler
	public void onLevelUp(PlayerLevelJobEvent event) {
		Player player = event.getPlayer();
		YamlConfiguration config = UltimateJobs.getPlugin().getMainConfig().getConfig();
		YamlConfiguration me = UltimateJobs.getPlugin().getMessages().getConfig();
		UltimateJobs.getPlugin().getAPI().playSound("LEVEL_UP", player);
		
		Job job = event.getJob();
		String name = player.getName(); 
		
		if (config.getBoolean("Levels.FireWork")) {
			UltimateJobs.getPlugin().getAPI().spawnFireworks(player.getLocation());
		} 
		if(me.getBoolean("Levels.BroadCastLevelUps")) {
			String message = me.getString("Levels.BoardCastMessage");
			Bukkit.broadcastMessage(plugin.getAPI().toHex(message).replaceAll("<level>", ""+event.getNewLevel()).replaceAll("<job>", job.getDisplay()).replaceAll("<name>", name).replaceAll("&", "§"));
		}
	}

}
