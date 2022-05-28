package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class ReloadSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getDescription() {
		return "Reload the Plugin";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			UltimateJobs.getPlugin().getFileManager().reloadFiles();
			UltimateJobs.getPlugin().getAPI().loadJobs(UltimateJobs.getPlugin().getLogger());
			sender.sendMessage(AdminCommand.prefix + "§aReloaded Plugin Config's and Job's.");
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command reload";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin reload";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.reload";
	}
	
}
