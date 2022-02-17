package de.warsteiner.jobs.command.playercommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LeaveSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	
	@Override
	public String getName() {
		return plugin.getCommandConfig().getConfig().getString("Command.LEAVE.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getCommandConfig().getConfig().getString("Command.LEAVE.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		JobAPI api = plugin.getAPI();
		if (args.length == 2) {
			String job = args[1].toUpperCase();
			if (api.checkIfJobIsReal(job, player)) {
				Job file = api.isJobFromConfigID(job);
				if (jb.isInJob(job)) {
					jb.remoCurrentJob(job);
					player.sendMessage(api.getMessage("Left_Job").replaceAll("<job>", file.getDisplay()));
				} else {
					player.sendMessage(api.getMessage("Not_In_Job").replaceAll("<job>", file.getDisplay()));
				}
			}
		} else {
			player.sendMessage(
					up.toHex(plugin.getCommandConfig().getConfig().getString("Command.LEAVE.Syntax")
							.replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
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
		return plugin.getCommandConfig().getConfig().getBoolean("Command.LEAVE.Enabled");
	}

}
