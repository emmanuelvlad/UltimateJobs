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
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Leave.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Leave.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		JobAPI api = plugin.getAPI();
		if (args.length == 2) {
			String job = args[1].toUpperCase();
			if (api.checkIfJobIsReal(job, player)) { 
				if (jb.isInJob(job)) {
					jb.remoCurrentJob(job);
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_leave_message"));
				} else {
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_leave_already"));
				}
			}
		} else {
			player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
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
		return  plugin.getFileManager().getCMDSettings().getBoolean("Commands.Leave.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) { 
		return plugin.getPluginManager().getMessage(UUID, "Commands.Leave.UsageMessage");
	}

}
