package de.warsteiner.jobs.events;

import java.util.UUID;
 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.PlayerManager;
import de.warsteiner.jobs.manager.SQLManager;

public class PlayerExistEvent implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		plugin.getExecutor().execute(() -> {

			Player player = event.getPlayer();
			SQLManager l = plugin.getSQLManager();
			PlayerManager m = plugin.getPlayerManager();
			UUID UUID = player.getUniqueId();
			String name = player.getName();

			if (l.ExistPlayer("" + UUID) == false) {
				l.createPlayer("" + UUID, name);
				m.loadData(name, UUID);
			} else {
				m.loadData(name, UUID);
			}

		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		plugin.getExecutor().execute(() -> {
			Player player = event.getPlayer();
			SQLManager l = plugin.getSQLManager();
			PlayerManager m = plugin.getPlayerManager();
			UUID UUID = player.getUniqueId();
			String name = player.getName();

			l.savePlayer(plugin.getPlayerManager().getJonPlayers().get("" + UUID), "" + UUID);
			m.removePlayerFromCache(UUID, name);

			if (plugin.getEventManager().getLevelQueue().containsKey(""+player.getUniqueId())) {
				plugin.getEventManager().getLevelQueue().remove(""+player.getUniqueId());
			}
		});
	}

}