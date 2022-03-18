package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
import de.warsteiner.jobs.UltimateJobs; 
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveAllSub extends SubCommand {
 
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.LeaveALL.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.LeaveALL.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId(); 
		if (args.length == 1) {
			if (jb.getCurrentJobs() != null) {
				jb.updateCurrentJobs(null); 
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_leaveall_message"));
			} else {
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_leaveall_already"));
			}
		} else {
			player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command leaveall";
	}

	@Override
	public boolean isEnabled() { 
		return  plugin.getFileManager().getCMDSettings().getBoolean("Commands.LeaveALL.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) { 
		return plugin.getPluginManager().getMessage(UUID, "Commands.LeaveALL.UsageMessage");
	}

}
