package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class UpdateSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "update";
	}

	@Override
	public String getDescription() {
		return "Update the Plugin";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) { 
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				
			  
					player3.playSound(player3.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 3);
					
					UltimateJobs.getPlugin().getGUIAddonManager().createUpdateMenu(player3);
				 
			} else {
				sender.sendMessage(AdminCommand.prefix + "§cThis Command is only for Players!");
			}
		 
			 
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
		return "command update";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin update";
	}
	
	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.update";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}