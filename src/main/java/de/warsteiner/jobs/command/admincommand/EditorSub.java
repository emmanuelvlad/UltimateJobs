package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class EditorSub  extends AdminSubCommand {

	@Override
	public String getName() {
		return "editor";
	}

	@Override
	public String getDescription() {
		return "Edit Jobs";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (args.length == 1) {
			UltimateJobs.getPlugin().getGUI().EditorChooseJob(player, true);
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin editor");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command editor";
	}

}
