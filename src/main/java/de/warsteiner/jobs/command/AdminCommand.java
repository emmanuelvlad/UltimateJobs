package de.warsteiner.jobs.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;

public class AdminCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (plugin.getAPI().checkPermissions(player, "admin.command")) {
				plugin.getCommand().executeAdminCommandAsPlayer(player, args);
			}
		} else {
			plugin.getCommand().executeAdminCommandAsPlayer(sender, args);
		}

		return false;
	}

	 

}
