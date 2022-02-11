package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.utils.Messages; 
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
			 
			if(plugin.isLatest != null && !plugin.isLatest.equalsIgnoreCase("LATEST")) {
				
				sender.sendMessage(Messages.prefix+"§7There is a new Version of §9UltimateJobs §7available! Download now: https://www.spigotmc.org/resources/ultimatejobs-reloaded.99198/");
				
			}
			
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
