package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class FirstSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "first";
	}

	@Override
	public String getDescription() {
		return "-/-";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) { 
			if(sender instanceof Player) {
				UltimateJobs.getPlugin().getGUIAddonManager().createFirstStartMenu(((Player) sender));
			}
		  
		} else 	if (args.length == 2 && args[1].toLowerCase().equalsIgnoreCase("finish")) { 
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
			}
			
			UltimateJobs.getPlugin().getPlayerDataAPI().createFirstPluginStart(UltimateJobs.getPlugin().getPluginManager().getDateTodayFromCal());
			 
		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command first";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin first finish";
	}
	
	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.first";
	}

	@Override
	public boolean showOnHelp() { 
		return false;
	}
	
}