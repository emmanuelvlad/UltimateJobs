package de.warsteiner.jobs.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.warsteiner.datax.utils.Messages;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.PlayerDataManager;
import de.warsteiner.jobs.manager.PlayerManager;

public class PlayerExistEvent implements Listener {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		plugin.getExecutor().execute(() -> {
			PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
			Player player = event.getPlayer();
			PlayerManager m = plugin.getPlayerManager();
			UUID UUID = player.getUniqueId();
			String name = player.getName();

			if (pl.ExistPlayer("" + UUID) == false) {
				pl.createPlayer("" + UUID, name);

			}

			m.loadData(name, UUID);

			if (player.hasPermission("ultimatejobs.check.updates")) {

				if (plugin.isLatest != null && !plugin.isLatest.equalsIgnoreCase("LATEST")) {

					player.sendMessage(Messages.prefix
							+ "§7There is a new Version of §9UltimateJobs §7available! Download now: https://www.spigotmc.org/resources/ultimatejobs-reloaded.99198/");

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

			pl.savePlayer(plugin.getPlayerManager().getOnlineJobPlayers().get("" + UUID), "" + UUID);
			m.removePlayerFromCache("" + UUID);

			if (plugin.getEventManager().getLevelQueue().containsKey("" + player.getUniqueId())) {
				plugin.getEventManager().getLevelQueue().remove("" + player.getUniqueId());
			}
		});
	}

}