package de.warsteiner.jobs.api.plugins;

import java.util.Collection;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolderManager extends PlaceholderExpansion {

	private UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = UltimateAPI.getPlugin().getAPI();

	@Override
	public @NotNull String getIdentifier() {
		return "jobs";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Warsteiner37";
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, String pr) {

		JobsPlayer jb = plugin.getPlayerManager().getOnlineJobPlayers().get("" + player.getPlayer().getUniqueId());

		YamlConfiguration cfg = plugin.getMessages().getConfig();

		if (jb != null) {
			Collection<String> jobs = jb.getCurrentJobs();
			if (pr.contains("job_current_name")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;

				if (jobs.size() != 0) {

					if (jobs.size() >= Integer.valueOf(split[3])) {

						Job job = plugin.getJobCache().get(jb.getCurrentJobs().get(which));
						return job.getDisplay();

					} else {
						return up.toHex(cfg.getString("PlaceHolders.No_Job")).replaceAll("&", "§");
					}
				} else {
					return up.toHex(cfg.getString("PlaceHolders.No_Job")).replaceAll("&", "§");
				}
			} else if (pr.contains("job_current_level")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;

				if (jobs.size() != 0) {

					if (jobs.size() >= Integer.valueOf(split[3])) {
						return "" + jb.getLevelOf(jb.getCurrentJobs().get(which));
					} else {
						return up.toHex(cfg.getString("PlaceHolders.No_Level")).replaceAll("&", "§");
					}
				} else {
					return up.toHex(cfg.getString("PlaceHolders.No_Level")).replaceAll("&", "§");
				}
			} else if (pr.contains("job_current_exp")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;
				if (jobs.size() != 0) {
					if (jobs.size() >= Integer.valueOf(split[3])) {
						return "" + jb.getExpOf(jb.getCurrentJobs().get(which));
					} else {
						return up.toHex(cfg.getString("PlaceHolders.No_Exp")).replaceAll("&", "§");
					}
				} else {
					return up.toHex(cfg.getString("PlaceHolders.No_Exp")).replaceAll("&", "§");
				}
			}

		}

		return null;
	}
}
