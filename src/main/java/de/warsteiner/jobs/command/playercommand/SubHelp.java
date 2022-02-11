package de.warsteiner.jobs.command.playercommand;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class SubHelp extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = UltimateAPI.getInstance().getAPI();

	@Override
	public String getName() {
		return plugin.getMainConfig().getConfig().getString("Command.HELP.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getMainConfig().getConfig().getString("Command.HELP.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		if (args.length == 1) {
			YamlConfiguration mg = plugin.getMessages().getConfig();
			for (String m : mg.getStringList("Help")) {
				player.sendMessage(up.toHex(m).replaceAll("&", "§"));
			}
		} else {
			player.sendMessage(up.toHex(plugin.getMainConfig().getConfig().getString("Command.HELP.Syntax")
					.replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
		}
	}

	@Override
	public String FormatTab() {
		return "command help";
	}

	@Override
	public int getTabLength() {
		return 1;
	}
	
	@Override
	public boolean isEnabled() { 
		return plugin.getMainConfig().getConfig().getBoolean("Command.HELP.Enabled");
	}

}
