package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
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
			AdminCommand.sendHelp(sender, 1);
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
		} else if(args.length == 2) {
			
			if (!UltimateJobs.getPlugin().getAPI().isInt(args[1])) {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				return;
			}
			
			if(Integer.valueOf(args[1]) == 0) {
				sender.sendMessage(AdminCommand.prefix + "Error! Page cannot be 0.");
				return;
			}
			
			AdminCommand.sendHelp(sender, Integer.valueOf(args[1]));
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
			}
			
		} else {
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
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

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.help";
	}
	
}
