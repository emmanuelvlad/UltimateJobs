package de.warsteiner.jobs.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;

public class LevelAPI {

	private UltimateJobs plugin;
	private YamlConfiguration tr;

	public LevelAPI(UltimateJobs plugin, YamlConfiguration tr) {
		this.tr = tr;
		this.plugin = plugin;
	}

	public double getJobNeedExp(Job job, JobsPlayer pl) {

		int next = pl.getLevelOf(job.getID()) + 1;

		return job.getExpOfLevel(next);
	}
	
	public double getJobNeedExpWithOutPlayer(Job job, int level) {
 
		int next = level + 1;

		return job.getExpOfLevel(next);
	}

	public boolean canLevelMore(String uuid, Job job, int level) {
		if (job.getLevelDisplay(level) != null) {
			return false;
		}
		return true;
	}

	public boolean canlevelUp(Job Job, JobsPlayer pl) {
		double need = getJobNeedExp(Job, pl);

		double exp = pl.getExpOf(Job.getID());

		if (exp == need || exp >= need) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void check(Player player, Job job, JobsPlayer pl) {

		String UUID = "" + player.getUniqueId();

		JobAPI api = plugin.getAPI();

		int old_level = pl.getLevelOf(job.getID());
		int new_level = old_level + 1;

		if (!canLevelMore(UUID, job, new_level)) {

			if (canlevelUp(job, pl)) {

				for (String cmd : job.getLevelCommands(new_level)) {

					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

					Bukkit.dispatchCommand((CommandSender) console, cmd.replaceAll("<name>", player.getName()));

				}

				plugin.getExecutor().execute(() -> {

					pl.updateLevel(job.getID(), new_level);
					pl.updateExp(job.getID(), 0);
					
					if(plugin.isInstalledAlonso()) {
						if(job.isAlonsoLevelsOnLevel(new_level)) {
							String r = job.getAlonsoLevelsOnLevel(new_level);
							plugin.getAlonsoLevelsPlugin().addExp(player.getUniqueId(), Integer.valueOf(r));
						}
					}

					String level_name = job.getNameOfLevel(new_level);

					if (tr.getBoolean("Levels.Enable_Title")) {
						String title_1 = api.toHex(tr.getString("Levels.Ttitle_1")
								.replaceAll("<prefix>", api.getPrefix()).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));

						String title_2 = api.toHex(tr.getString("Levels.Ttitle_2")
								.replaceAll("<prefix>", api.getPrefix()).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendTitle(title_1, title_2);
					}

					if (tr.getBoolean("Levels.Enable_Message")) {
						String message = api.toHex(tr.getString("Levels.Message")
								.replaceAll("<prefix>", api.getPrefix()).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendMessage(message);
					}

					if (tr.getBoolean("Levels.Enabled_Actionbar")) {
						String message = api.toHex(tr.getString("Levels.Actionbar")
								.replaceAll("<prefix>", api.getPrefix()).replaceAll("<level_name>", level_name)
								.replaceAll("<level_int>", "" + new_level).replaceAll("&", "§"));
						player.sendMessage(message);
					}
					return;

				});
			}

		}
	}

}
