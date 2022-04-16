package de.warsteiner.jobs.api.plugins;

import java.util.Collection; 
import org.bukkit.OfflinePlayer; 
import org.jetbrains.annotations.NotNull;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;
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
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(""+player.getUniqueId());
 
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
						return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_job");
					}
				} else {
					return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_job");
				}
			} else if (pr.contains("job_current_level")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;

				if (jobs.size() != 0) {

					if (jobs.size() >= Integer.valueOf(split[3])) {
						return "" + jb.getLevelOf(jb.getCurrentJobs().get(which));
					} else {
						return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_level");
					}
				} else {
					return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_level");
				}
			} else if (pr.contains("job_current_exp")) {
				String[] split = pr.split("_");
				Integer which = Integer.valueOf(split[3]) - 1;
				if (jobs.size() != 0) {
					if (jobs.size() >= Integer.valueOf(split[3])) {
						return "" + jb.getExpOf(jb.getCurrentJobs().get(which));
					} else {
						return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_exp");
					}
				} else {
					return plugin.getPluginManager().getAMessage(jb.getUUID(), "placeholder_no_exp");
				}
			}

		}

		return null;
	}
}
