package de.warsteiner.jobs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.GuiManager;

public class AdminCommand implements CommandExecutor {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	 
		if(sender instanceof Player) {
		 
			Player player = (Player) sender;
			 
			if(checkPermissions(player, "admin.command")) {
				plugin.getCommand().executeAdminCommandAsPlayer(player, args);
			} 
		} else {
			plugin.getLogger().info("test");
		}
		
		return false;
	}

	public static boolean checkPermissions(Player player, String text) {
		if(player.hasPermission("ultimatejobs."+text) || player.hasPermission("ultimatejobs.admin.all")) {
			return true;
		}
		player.sendMessage(plugin.getJobAPI().getMessage("No_Perm"));
		return false;
	}
	
}
