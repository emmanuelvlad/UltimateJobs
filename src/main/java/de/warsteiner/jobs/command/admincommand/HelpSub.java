package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class HelpSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "See the Plugin's Commands";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			AdminCommand.sendHelp(sender);
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command help";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin help";
	}

}
