package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs; 
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Leave.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Leave.Description");
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
				plugin.getAPI().playSound("COMMAND_LEAVE_SUCCESS", player);
				jb.remCurrentJob(job);
				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_leave_message").replaceAll("<job>",  plugin.getAPI().checkIfJobIsRealAndGet(d.toUpperCase(), player).getDisplay(""+UUID)));
			} else {
				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_leave_already"));
				plugin.getAPI().playSound("COMMAND_LEAVE_ALREADY", player);
			}

		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(
					jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
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
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Leave.UsageMessage");
	}

}
