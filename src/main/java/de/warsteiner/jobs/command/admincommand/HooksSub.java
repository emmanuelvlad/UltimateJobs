package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand; 

public class HooksSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "hooks";
	}

	@Override
	public String getDescription() {
		return "See the supported Plugins";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			if (plugin.getSupportedPlugins().size() != 0) {
				sender.sendMessage("§7");
				sender.sendMessage(" §8| §9UltimateJobs §8- §4Supported & Loaded Plugins §8|");
				for (Plugin c : plugin.getSupportedPlugins()) {
					  
					sender.sendMessage("§8-> §6" + c.getDescription().getName() + " §8| §7Version§8: §bv" + c.getDescription().getVersion());
				}
				sender.sendMessage("§7");
			} else {
				sender.sendMessage(AdminCommand.prefix + "§cCannot find any supported Plugins!");
			}
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin hooks");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command hooks";
	}

}
