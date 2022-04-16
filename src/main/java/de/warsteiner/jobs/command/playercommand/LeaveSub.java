package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Leave.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Leave.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 2) {
			String d = args[1].toUpperCase();

			if (!plugin.getAPI().checkIfJobIsReal(d.toUpperCase(), player)) {
				return;
			}
			
			String job = plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getConfigID();

			if (jb.isInJob(job)) {
				jb.remoCurrentJob(job);
				player.sendMessage(plugin.getPluginManager().getAMessage(UUID, "command_leave_message").replaceAll("<job>",  plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getDisplay(""+UUID)));
			} else {
				player.sendMessage(plugin.getPluginManager().getAMessage(UUID, "command_leave_already"));
			}

		} else {
			player.sendMessage(
					plugin.getPluginManager().getAMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command leave jobs_in";
	}

	@Override
	public int getTabLength() {
		return 2;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Leave.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		return plugin.getPluginManager().getAMessage(UUID, "Commands.Leave.UsageMessage");
	}

}
