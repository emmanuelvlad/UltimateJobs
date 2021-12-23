package de.warsteiner.jobs.command;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;

public class JobsCommand  implements CommandExecutor {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		ArrayList<File> jobs = plugin.getLoadedJobs();
		int length = args.length;
		
		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			 
			if (length == 0) {
				player.sendMessage("Jobs: "+jobs);
			
			} else if (length == 1 && args[0].equalsIgnoreCase("demo")) {
				player.sendMessage("You can find demo files here; ");
			}
		}
		
		return false;
	}
}