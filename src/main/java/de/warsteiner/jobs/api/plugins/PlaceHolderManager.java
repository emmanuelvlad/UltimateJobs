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
				Integer which = Integer.valueOf(split[3]) - 1;

				if (jobs.size() != 0) {

					if (jobs.size() >= Integer.valueOf(split[3])) {

						Job job = plugin.getJobCache().get(jb.getCurrentJobs().get(which));
						return job.getDisplay(UUID);

					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_job");
				}
			} else if (pr.contains("job_current_level")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;

				if (jobs.size() != 0) {

					if (jobs.size() >= Integer.valueOf(split[3])) {
						return ""+jb.getStatsOf(jb.getCurrentJobs().get(which)).getLevel();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_level");
				}
			} else if (pr.contains("job_current_exp")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;
				if (jobs.size() != 0) {
					if (jobs.size() >= Integer.valueOf(split[3])) {
						return ""+jb.getStatsOf(jb.getCurrentJobs().get(which)).getExp();
					} else {
						return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
					}
				} else {
					return jb.getLanguage().getStringFromLanguage(jb.getUUID(), "placeholder_no_exp");
				}
			}

		}

		return null;
	}
}
