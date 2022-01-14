package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class DiscordSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "discord";
	}

	@Override
	public String getDescription() {
		return "Join the Developer's Discord";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(AdminCommand.prefix + "§7Join the Discord here -> §bhttps://discord.com/invite/sy9nDEpYVp");
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin discord");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command discord";
	}

}

