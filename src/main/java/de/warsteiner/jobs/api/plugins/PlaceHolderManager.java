package de.warsteiner.jobs.api.plugins;

import java.util.Collection; 
import org.bukkit.OfflinePlayer; 
import org.jetbrains.annotations.NotNull;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceHolderManager extends PlaceholderExpansion {

	private UltimateJobs plugin = UltimateJobs.getPlugin(); 

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
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+player.getUniqueId());
 
		if (jb != null) {
			String UUID = ""+player.getUniqueId();
			Collection<String> jobs = jb.getCurrentJobs();
			if (pr.contains("job_current_name")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						Job job = plugin.getJobCache().get(internal.toUpperCase());
						return job.getDisplay(UUID);

					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
				}
			} if (pr.contains("job_current_online")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {

						Job job = plugin.getJobCache().get(internal.toUpperCase());
						return ""+plugin.getPlayerAPI().getOnlinePlayersInJob(job).size();

					} else {
						return "0";
					}
				} else {
					return "0";
				}
			}  else if (pr.contains("job_current_level")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {
 
						return ""+jb.getStatsOf(internal.toUpperCase()).getLevel();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
				}
			} else if (pr.contains("job_current_exp")) {
				String[] split = pr.split("_");
				String internal = split[3];
				if (jobs.size() != 0) {
					if (jobs.contains(internal.toUpperCase())) {
 
						return ""+jb.getStatsOf(internal.toUpperCase()).getExp();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
				}
			}  else if (pr.contains("job_current_levelname")) {
				String[] split = pr.split("_");
				String internal = split[3];

				if (jobs.size() != 0) {

					if (jobs.contains(internal.toUpperCase())) {
						Job job = plugin.getJobCache().get(internal.toUpperCase());
						int lvl = jb.getStatsOf(internal.toUpperCase()).getLevel();
						return job.getLevelDisplay(lvl, UUID);
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_levelname");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_levelname");
				}
			}

		}

		return null;
	}
}
