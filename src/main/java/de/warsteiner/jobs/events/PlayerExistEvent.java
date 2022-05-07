package de.warsteiner.jobs.events;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.PlayerAPI;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerExistEvent implements Listener {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		plugin.getExecutor().execute(() -> { 
			FileConfiguration config = UltimateJobs.getPlugin().getFileManager().getConfig();
			
			PlayerAPI cache = plugin.getPlayerAPI();
			PlayerDataAPI data = plugin.getPlayerDataAPI();
			
			Player player = event.getPlayer();
		 
			UUID UUID = player.getUniqueId();
		 
			String name = player.getName();

			if (data.ExistPlayer("" + UUID) == false) {
				data.createPlayer("" + UUID, name);
 
			}

			cache.loadData(name, UUID);
			
			JobsPlayer jb = cache.getRealJobPlayer(""+UUID);
			
			if(config.getBoolean("EnabledDefaultJobs")) {
				for(String job :  config.getStringList("DefaultJobs")) {
					if(!jb.getOwnJobs().contains(job)) {
						jb.addOwnedJob(job);
					}
				}
			}
  
		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		plugin.getExecutor().execute(() -> {
			Player player = event.getPlayer();
			PlayerAPI cache = plugin.getPlayerAPI();
			PlayerDataAPI data = plugin.getPlayerDataAPI();
			UUID UUID = player.getUniqueId();

			data.savePlayer(cache.getRealJobPlayer(""+UUID), "" + UUID);
			cache.removePlayerFromCache("" + UUID);
 
		});
	}

}