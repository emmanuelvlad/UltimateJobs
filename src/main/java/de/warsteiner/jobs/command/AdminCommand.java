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
			plugin.getCommand().executeAdminCommandAsPlayer(sender, args);
		}
		
		return false;
	}

	public static boolean checkPermissions(CommandSender s, String text) {
		if(s.hasPermission("ultimatejobs."+text) || s.hasPermission("ultimatejobs.admin.all")) {
			return true;  
		}   
		s.sendMessage(plugin.getJobAPI().getMessage("No_Perm"));
		return false;
	}
	
}
