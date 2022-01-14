package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.module.JobsModule;

public class ReloadSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getDescription() {
		return "See the Plugin's Addons";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			plugin.setupConfigs();
			plugin.getAPI().loadJobs(plugin.getLogger());
			sender.sendMessage(AdminCommand.prefix + "§aReloaded UltimateJobs : §bJobs and Config");
			return;
		}
		if (args.length == 2) {

			String addon = args[1].toUpperCase();

			if (findAddon(addon) == null) {
				sender.sendMessage(AdminCommand.prefix + "§7Error! Cannot find Addon §c" + addon.toLowerCase() + "§7.");
				return;
			}

			JobsModule ad = findAddon(addon);

			ad.reload(sender);

			sender.sendMessage(AdminCommand.prefix + "§aReloaded UltimateJobs Addon : §b" + ad.getPluginName());
			return;
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin reload <addon>");
		}
	}

	public JobsModule findAddon(String name) {
		for (JobsModule c : plugin.getModuleRegistry().getModuleList()) {
			if (c.getPluginName().toUpperCase().equalsIgnoreCase(name.toUpperCase())) {
				return c;
			}
		}
		return null;
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command reload addons";
	}

}
