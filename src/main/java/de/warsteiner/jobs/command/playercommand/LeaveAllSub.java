package de.warsteiner.jobs.command.playercommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveAllSub extends SubCommand {

	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return plugin.getCommandConfig().getConfig().getString("Command.LEAVEALL.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getCommandConfig().getConfig().getString("Command.LEAVEALL.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		JobAPI api = plugin.getAPI();
		if (args.length == 1) {
			if (jb.getCurrentJobs() != null) {
				jb.updateCurrentJobs(null);
				player.sendMessage(api.getMessage("Leave_All"));
			} else {
				player.sendMessage(api.getMessage("Already_Left_All"));
			}
		} else {
			player.sendMessage(
					up.toHex(plugin.getCommandConfig().getConfig().getString("Command.LEAVEALL.Syntax")
							.replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
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
		return plugin.getCommandConfig().getConfig().getBoolean("Command.LEAVEALL.Enabled");
	}

}
