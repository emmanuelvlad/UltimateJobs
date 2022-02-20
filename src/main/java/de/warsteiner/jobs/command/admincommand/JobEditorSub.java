package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class JobEditorSub  extends AdminSubCommand {

	@Override
	public String getName() {
		return "job-editor";
	}

	@Override
	public String getDescription() {
		return "Edit Jobs";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if (args.length == 1) {
			UltimateJobs.getPlugin().getEditorMenuManager().EditorChooseJob(player, true, "§6Jobs Editor");
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin job-editor");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command job-editor";
	}

}
