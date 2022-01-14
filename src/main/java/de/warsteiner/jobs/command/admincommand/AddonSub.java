package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.module.JobsModule;

public class AddonSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "addons";
	}

	@Override
	public String getDescription() {
		return "See the Plugin's Addons";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			if (plugin.getModuleRegistry().getModuleList().size() != 0) {
				sender.sendMessage("§7");
				sender.sendMessage(" §8| §9UltimateJobs §8- §4Addons §8|");
				for (JobsModule c : plugin.getModuleRegistry().getModuleList()) {
					sender.sendMessage("§8-> §6" + c.getPluginName() + " §8- §7Version§8: §bv" + c.getVersion() + " §7by §c"
							+ c.getDeveloper());
				}
				sender.sendMessage("§7");
			} else {
				sender.sendMessage(AdminCommand.prefix + "§cCannot find any installed Addons!");
			}
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin addons");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command addons";
	}

}
