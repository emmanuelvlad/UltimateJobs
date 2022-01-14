package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class VersionSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "See the Plugin's Version";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			PluginDescriptionFile d = plugin.getDescription();
			sender.sendMessage(AdminCommand.prefix + "§7Version §8: §bv" + d.getVersion()
					+ " §7running with API Version §8: §a" + d.getAPIVersion() + "§7.");
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin version");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command version";
	}

}
