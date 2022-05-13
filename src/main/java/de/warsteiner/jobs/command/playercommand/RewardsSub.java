package de.warsteiner.jobs.command.playercommand;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class RewardsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Rewards.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Rewards.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		ArrayList<String> size = jb.getCurrentJobs();
		if (args.length == 2) {
			String job = args[1].toUpperCase();

			if (!plugin.getAPI().checkIfJobIsReal(job.toUpperCase(), player)) {
				plugin.getAPI().playSound("COMMAND_JOB_NOT_FOUND", player);
				return;
			}

			plugin.getGUIAddonManager().createRewardsGUI(player, UpdateTypes.OPEN,
					plugin.getAPI().checkIfJobIsRealAndGet(job.toUpperCase(), player));

			return;
		} else if (args.length == 1 && size.size() == 1) {

			plugin.getGUIAddonManager().createRewardsGUI(player, UpdateTypes.OPEN,
					plugin.getJobCache().get(size.get(0)));

			return;
		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(
					jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command rewards jobs_listed";
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Rewards.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Rewards.UsageMessage");
	}

}
