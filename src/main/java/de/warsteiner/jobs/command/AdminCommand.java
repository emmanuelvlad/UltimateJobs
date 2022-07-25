package de.warsteiner.jobs.command;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand; 

public class AdminCommand implements CommandExecutor {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public static String prefix = "§9UltimateJobs §8-> §7";

	public static Map<String, String> raw_messages = Map.of(
		"noperm", "<prefix> §cYou dont have Permissions!",
		"command_usage", "<prefix> §7Error! §7Use §6/Jobsadmin help§7.",
		"admin_help_header", " §8| §9UltimateJobs §8- §4Admin Help #<page> §8|",
		"admin_help_page_not_found", "§cNo Help Page found.",
		"admin_help_previous_page", "§8-> §cPrevious Page §8|",
		"admin_help_next_page", "§8-> §aNext Page",
		"admin_help_command_usage", "§8-> §6<usage> §8| §7<description>",
		"admin_help_click_here", "Click here"
	);

	public static String getMessage(CommandSender sender, String key) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			var jb = plugin.getPlayerAPI().getRealJobPlayer(player.getUniqueId());

			String msg = jb.getLanguage().getStringFromLanguage(player.getUniqueId(), key);
			if (msg != null) {
				return msg;
			}
		}

		return raw_messages.get(key).replaceAll("<prefix>", prefix);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		int length = args.length;

		if (sender.hasPermission("ultimatejobs.admin.command")) {

			if (length == 0) {
				sendHelp(sender, 1);
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			} else {
				String ar = args[0].toLowerCase();

				if (find(ar) == null) {
					sender.sendMessage(getMessage(sender, "command_usage").replaceAll("<usage>", "/Jobsadmin"));
					return true;
				} else {

					AdminSubCommand cmd = find(ar);

					if (sender.hasPermission(cmd.getPermission())) {
					
						cmd.perform(sender, args);

					} else {
						sender.sendMessage(getMessage(sender, "noperm"));
						return true;
					}
				}

			}

		} else {
			sender.sendMessage(getMessage(sender, "noperm"));
			return true;
		}
		return false;
	}

	public AdminSubCommand find(String given) {
		for (AdminSubCommand subCommand : plugin.getAdminSubCommandManager().getSubCommandList()) {
			if (given.equalsIgnoreCase(subCommand.getName().toLowerCase())) {
				return subCommand;
			}
		}
		return null;
	}

	public static void sendHelp(CommandSender sender, int page) {
		
		if(page == 1 || page >= 1) {
 
		List<AdminSubCommand> commands = plugin.getAdminSubCommandManager().getSubCommandList();

		int pageLength = 6;

		int calc = pageLength * page + 1; 
		int min = calc - 5;
		if(commands.size() >= min) {
			
			sender.sendMessage("§7");
			sender.sendMessage(getMessage(sender, "admin_help_header").replaceAll("<page>", ""+page));
			
			for (int i = (page - 1) * pageLength; i < (page * pageLength) && i < commands.size(); i++) {
				AdminSubCommand which = commands.get(i);

				if(which.showOnHelp()) {
					String us = which.getUsage();
					
					if(sender instanceof Player) {
						Player player = (Player) sender;
						 new JsonMessage() 
						 .append(getMessage(sender, "admin_help_command_usage").replaceAll("<usage>", which.getUsage()).replaceAll("<description>", which.getDescription())).setHoverAsTooltip("§7"+which.getDescription())
						//  .append("§8-> §6" + which.getUsage() + " §8| §7" + which.getDescription()).setHoverAsTooltip("§7"+which.getDescription())
						 .setClickAsSuggestCmd(us).save().send(player);
					} else {
						sender.sendMessage(getMessage(sender, "admin_help_command_usage").replaceAll("<usage>", which.getUsage()).replaceAll("<description>", which.getDescription()));
					}
				}
				
			 

			}
	 
			sender.sendMessage("§7");
	  
			if(sender instanceof Player) {
				Player player = (Player) sender;
				 int c = page - 1;
				 int c2 = page + 1;
			 
				 
					
					if(page == 1) {
						 new JsonMessage() 
						 .append(ChatColor.GREEN + getMessage(sender, "admin_help_next_page")).setHoverAsTooltip(getMessage(sender, "admin_help_click_here"))
						 .setClickAsExecuteCmd("/jobsadmin help "+c2).save().send(player);
					} else {
						
						 
						
						if(commands.size() >= calc) {
							 new JsonMessage().append(ChatColor.RED + getMessage(sender, "admin_help_previous_page")).setHoverAsTooltip(getMessage(sender, "admin_help_click_here"))
							 .setClickAsExecuteCmd("/jobsadmin help "+c).save()
							 
							 .append(ChatColor.GREEN + " §aNext Page").setHoverAsTooltip(getMessage(sender, "admin_help_click_here"))
							 .setClickAsExecuteCmd("/jobsadmin help "+c2).save().send(player);
						} else {
							 new JsonMessage().append(ChatColor.RED + getMessage(sender, "admin_help_previous_page")).setHoverAsTooltip(getMessage(sender, "admin_help_click_here"))
							 .setClickAsExecuteCmd("/jobsadmin help "+c).save().send(player);
						}
						
						 
					}
 
				 

				sender.sendMessage("§7");
			}
		} else {
			sender.sendMessage(getMessage(sender, "admin_help_page_not_found"));
		}
		}  
	}

}
