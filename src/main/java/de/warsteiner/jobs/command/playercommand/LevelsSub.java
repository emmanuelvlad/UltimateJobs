package de.warsteiner.jobs.command.playercommand;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LevelsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Levels.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Levels.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();

		ArrayList<String> size = jb.getCurrentJobs();

		if (args.length == 2) {
			String job = args[1].toUpperCase();

			if (!plugin.getAPI().checkIfJobIsReal(job.toUpperCase(), player)) {
				return;
			}

			plugin.getGUIAddonManager().createLevelsGUI(player, UpdateTypes.OPEN,
					plugin.getAPI().checkIfJobIsRealAndGet(job.toUpperCase(), player));

			return;
		} else if (args.length == 1 && size.size() == 1) {

			plugin.getGUIAddonManager().createLevelsGUI(player, UpdateTypes.OPEN,
					plugin.getJobCache().get(size.get(0)));

			return;

		}

		else {
			player.sendMessage(
					plugin.getPluginManager().getAMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}

	}

	@Override
	public String FormatTab() {
		return "command levels jobs_listed";
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Levels.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Levels.UsageMessage");
	}

}