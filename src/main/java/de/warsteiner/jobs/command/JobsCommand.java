package de.warsteiner.jobs.command;
 

import java.util.UUID;

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
			String UUID = ""+player.getUniqueId();
			JobsPlayer jb =plugin.getPlayerManager().getRealJobPlayer(UUID);
			GuiManager gui = plugin.getGUI();

			if (length == 0) {
				gui.createMainGUIOfJobs(player);

			} else {
				String ar = args[0].toLowerCase();

				if (find(ar, jb.getUUID()) == null) {
					player.sendMessage(plugin.getPluginManager().getMessage(jb.getUUID(), "command_notfound").replaceAll("<cmd>", ar));
					return true;
				} else {

					SubCommand cmd = find(ar, jb.getUUID());

					if(cmd.isEnabled()) {
						cmd.perform(player, args, jb);
					} else {
						player.sendMessage(plugin.getPluginManager().getMessage(jb.getUUID(), "command_notfound").replaceAll("<cmd>", ar));
						return true;
					}

				}

			}
		} else {
			sender.sendMessage("§c§lThis Command is only for players!");
		}

		return false;
	}

	public SubCommand find(String given, UUID UUID) {
		for (SubCommand subCommand : plugin.getSubCommandManager().getSubCommandList()) {
			if (given.equalsIgnoreCase(subCommand.getName(UUID).toLowerCase())) {
				return subCommand;
			}
		}
		return null;
	}

}