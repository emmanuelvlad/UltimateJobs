package de.warsteiner.jobs.command;
 

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.manager.GuiManager;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class JobsCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		int length = args.length;
		 
		if (sender instanceof Player) {
			Player player = (Player) sender;
			JobsPlayer j = plugin.getPlayerManager().getOnlineJobPlayers().get("" + player.getUniqueId());
			GuiManager gui = plugin.getGUI();

			if (length == 0) {
				gui.createMainGUIOfJobs(player);

			} else {
				String ar = args[0].toLowerCase();

				if (find(ar) == null) {
					player.sendMessage(plugin.getAPI().getMessage("Not_Found_Command"));
					return true;
				} else {

					SubCommand cmd = find(ar);

					if(cmd.isEnabled()) {
						cmd.perform(player, args, j);
					} else {
						player.sendMessage(plugin.getAPI().getMessage("Not_Found_Command"));
						return true;
					}

				}

			}
		} else {
			plugin.getLogger().warning("§cThis Command is only for Players. Please use /Jobsadmin.");
		}

		return false;
	}

	public SubCommand find(String given) {
		for (SubCommand subCommand : plugin.getSubCommandManager().getSubCommandList()) {
			if (given.equalsIgnoreCase(subCommand.getName().toLowerCase())) {
				return subCommand;
			}
		}
		return null;
	}

}