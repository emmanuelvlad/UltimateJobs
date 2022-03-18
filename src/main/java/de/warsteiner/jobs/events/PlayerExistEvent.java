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
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.player.PlayerDataManager;
import de.warsteiner.jobs.player.PlayerManager;

public class PlayerExistEvent implements Listener {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		plugin.getExecutor().execute(() -> { 
			FileConfiguration config = UltimateJobs.getPlugin().getFileManager().getConfig();
			PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
			Player player = event.getPlayer();
			PlayerManager m = plugin.getPlayerManager();
			UUID UUID = player.getUniqueId();
			String name = player.getName();

			if (pl.ExistPlayer("" + UUID) == false) {
				pl.createPlayer("" + UUID, name);
 
			}

			m.loadData(name, UUID);
			
			JobsPlayer jb =plugin.getPlayerManager().getRealJobPlayer(""+UUID);
			
			if(config.getBoolean("EnabledDefaultJobs")) {
				for(String job :  config.getStringList("DefaultJobs")) {
					if(!pl.getOfflinePlayerOwnedJobs(""+UUID).contains(job)) {
						plugin.getPlayerManager().updateJobs(job.toUpperCase(), jb, "" + player.getUniqueId());
					}
				}
			}
  
		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		plugin.getExecutor().execute(() -> {
			Player player = event.getPlayer();
			PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
			PlayerManager m = plugin.getPlayerManager();
			UUID UUID = player.getUniqueId();

			pl.savePlayer(plugin.getPlayerManager().getRealJobPlayer(""+UUID), "" + UUID);
			m.removePlayerFromCache("" + UUID);
 
		});
	}

}