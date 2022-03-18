package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender; 
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class SubHelp extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();

	@Override
	public String getName(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Help.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Help.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {
			String mode = plugin.getFileManager().getHelpSettings().getString("Help_Mode").toUpperCase();
			if(mode.equalsIgnoreCase("GUI")) {
				plugin.getGUI().createHelpGUI(player);
			} else {
		 
				for (String m :plugin.getPluginManager().getMessageList(UUID, "Commands.Help.List")) {
					player.sendMessage(up.toHex(m).replaceAll("&", "§"));
				}
			}
		} else {
			player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
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
		return  plugin.getFileManager().getCMDSettings().getBoolean("Commands.Help.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) { 
		return plugin.getPluginManager().getMessage(UUID, "Commands.Help.UsageMessage");
	}
  
}
