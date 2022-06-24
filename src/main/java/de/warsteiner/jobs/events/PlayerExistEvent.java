package de.warsteiner.jobs.events;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.PlayerAPI;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.utils.JsonMessage;
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

				data.addPlayerToPlayersList("" + UUID, name);

				String lang = plugin.getFileManager().getLanguageConfig().getString("PlayerDefaultLanguage");

				if (data.getSettingData("" + UUID, "LANG") == null) {
					data.createSettingData("" + UUID, "LANG", lang);
				}

			}

			cache.loadData(name, UUID);

			JobsPlayer jb = cache.getRealJobPlayer("" + UUID);

			if (config.getBoolean("EnabledDefaultJobs")) {
				for (String job : config.getStringList("DefaultJobs")) {
					if (!jb.getOwnJobs().contains(job)) {
						jb.addOwnedJob(job);
					}
				}
			}

			plugin.getLocationAPI().setLocation(player.getLocation(), "LastLoc." + UUID);

			new BukkitRunnable() {

				@Override
				public void run() {
					if (!plugin.getPlayerDataAPI().isFirstPluginStart()) {
						player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2, 3);
						new JsonMessage()
								.append("§7\n§8[§9UltimateJobs§8] §7Welcome, §6" + name
										+ "§7.\n §7Please Click here, to view the §afirst §7Steps of the Plugin!\n§7")
								.setClickAsExecuteCmd("/jobsadmin first").save().send(player);
					}
					
					if(plugin.getWebManager().canUpdate) {
						if(player.hasPermission("ultimatejobs.admin.update")) { 
							new JsonMessage()
									.append("§7\n§8[§9UltimateJobs§8] §7Click to View a new §aUpdate§7! §7Current Version§8: §b"+plugin.getDescription().getVersion()
											+" §7new Version§8: §c"+plugin.getWebManager().newVersion+"\n§7")
									.setClickAsExecuteCmd("/jobsadmin update").save().send(player);
						}
					}
					
				}
			}.runTaskLater(plugin, 60);

		});

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(PlayerQuitEvent event) {
		plugin.getExecutor().execute(() -> {
			Player player = event.getPlayer();
			PlayerAPI cache = plugin.getPlayerAPI();
			PlayerDataAPI data = plugin.getPlayerDataAPI();
			UUID UUID = player.getUniqueId();

			plugin.getLocationAPI().setLocation(player.getLocation(), "LastLoc." + UUID);

			data.savePlayer(cache.getRealJobPlayer("" + UUID), "" + UUID);
			cache.removePlayerFromCache("" + UUID);

		});
	}

}