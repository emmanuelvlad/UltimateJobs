package de.warsteiner.jobs.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.api.PlayerManager;
import de.warsteiner.jobs.utils.GuiManager;
import de.warsteiner.jobs.utils.command.SubCommand;

public class JobsCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		int length = args.length;

		if (sender instanceof Player) {
			Player player = (Player) sender; 
			JobsPlayer j = plugin.getPlayerManager().getJonPlayers().get(""+player.getUniqueId());
			GuiManager gui = plugin.getGUI();
			 
			if (length == 0) {
			  gui.createMainGUIOfJobs(player);  
			   
			} else {
				// plugin.getCommand().execute(player, args); 
			 
				 for (SubCommand subCommand : plugin.getSubCommandManager().getSubCommandList()) {
		                if (args[0].equalsIgnoreCase(subCommand.getName())) {
		                    subCommand.perform(player, args);
		                }
		            }
				
			}
		} else {
			plugin.getLogger().warning("§cThis Command is only for Players. Please use /Jobsadmin.");
		}

		return false;
	}
}