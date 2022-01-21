package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.utils.UpdateChecker;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class UpdateSub extends AdminSubCommand {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public String getDescription() {
		return "See if theres a new Version";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			
			new UpdateChecker(plugin, 99198).getVersion(version -> {
				if (!plugin.getDescription().getVersion().equals(version)) {
					sender.sendMessage(AdminCommand.prefix + "§7Theres a new Plugin Version §aavailable§7! You run on version : §c"+plugin.getDescription().getVersion()+" §8-> §7new version : §a"+version);
				} else {
					sender.sendMessage(AdminCommand.prefix+"§7You are running the latest Version!");
				}
			}); 
			
				 
			 
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin update");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command update";
	}

}
