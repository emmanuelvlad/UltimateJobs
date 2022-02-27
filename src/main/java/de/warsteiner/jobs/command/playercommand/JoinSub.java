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

public class JoinSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	
	@Override
	public String getName() {
		return plugin.getCommandConfig().getConfig().getString("Command.JOIN.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getCommandConfig().getConfig().getString("Command.JOIN.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		JobAPI api = plugin.getAPI();
		if (args.length == 2) {
			String job = args[1].toUpperCase();
			if (api.checkIfJobIsReal(job, player)) {
				Job file = api.isJobFromConfigID(job);
				if(jb.getOwnJobs().contains(file.getID())) {
					int max = jb.getMaxJobs();

					if (jb.getCurrentJobs().size() <= max) {
						if (!jb.isInJob(file.getID())) {
							jb.addCurrentJob(file.getID());
							player.sendMessage(api.getMessage("Command_Joined_Job").replaceAll("<job>", file.getDisplay()));
						} else {
							player.sendMessage(api.getMessage("Command_Already_In_Job").replaceAll("<job>", file.getDisplay()));
						}
					} else {
						player.sendMessage(api.getMessage("Command_Reached_Max").replaceAll("<job>", file.getDisplay()));
					}
				} else {
					player.sendMessage(api.getMessage("Command_Not_own_Job").replaceAll("<job>", file.getDisplay()));
				}
			}
		} else {
			player.sendMessage(
					up.toHex(plugin.getCommandConfig().getConfig().getString("Command.JOIN.Syntax")
							.replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
		}
	}

	@Override
	public String FormatTab() {
		return "command join jobs_owned";
	}

	@Override
	public int getTabLength() {
		return 2;
	}
	
	@Override
	public boolean isEnabled() { 
		return plugin.getCommandConfig().getConfig().getBoolean("Command.JOIN.Enabled");
	}

}

