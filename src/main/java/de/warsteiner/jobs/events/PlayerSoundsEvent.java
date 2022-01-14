package de.warsteiner.jobs.events;

import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.JobsPluginSoundEvent;

public class PlayerSoundsEvent implements Listener {

	private YamlConfiguration cfg;

	public PlayerSoundsEvent(YamlConfiguration cfg) {
		this.cfg = cfg;
	}

	@EventHandler
	public void onAction(JobsPluginSoundEvent event) {

		UltimateJobs.getPlugin().getExecutor().execute(() -> {
			String type = event.getType();
			Player player = event.getPlayer();

			if (cfg.getStringList("Sounds") != null) {

				for (String a : cfg.getStringList("Sounds")) {
					String[] split = a.split(":");

					String ty = split[0];

					if (ty.equalsIgnoreCase(type)) {

						String sound = split[1];
						String volume = split[2];
						String pitch = split[3];

						player.playSound(player.getLocation(), Sound.valueOf(sound), Integer.valueOf(volume),
								Integer.valueOf(pitch));
					}

				}
			}
		});

	}

}
