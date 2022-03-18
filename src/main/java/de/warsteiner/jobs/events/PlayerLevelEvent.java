package de.warsteiner.jobs.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent;

public class PlayerLevelEvent implements Listener {

	private SimpleAPI plugin = SimpleAPI.getPlugin();
	
	@EventHandler
	public void onLevelUp(PlayerLevelJobEvent event) {
		Player player = event.getPlayer();
		FileConfiguration config = UltimateJobs.getPlugin().getFileManager().getConfig();
		String me = UltimateJobs.getPlugin().getPluginManager().getMessage(player.getUniqueId(), "Levels.BoardCastMessage");
		UltimateJobs.getPlugin().getAPI().playSound("LEVEL_UP", player);
		
		Job job = event.getJob();
		String name = player.getName(); 
		
		if (config.getBoolean("Levels.FireWork")) {
			UltimateJobs.getPlugin().getAPI().spawnFireworks(player.getLocation());
		} 
		if(config.getBoolean("Levels.BroadCastLevelUps")) { 
			Bukkit.broadcastMessage(plugin.getAPI().toHex(me).replaceAll("<level>", ""+event.getNewLevel()).replaceAll("<job>", job.getDisplay(""+player.getUniqueId())).replaceAll("<name>", name).replaceAll("&", "§"));
		}
	}

}
