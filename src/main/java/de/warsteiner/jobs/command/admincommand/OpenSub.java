package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.GUIType;

public class OpenSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "open";
	}

	@Override
	public String getDescription() {
		return "Open a Player a GUI per Command";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 3) {

			String name = args[1];
			String type = args[2];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			
			if (GUIType.valueOf(type.toUpperCase()) == null) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
			}

			GUIType f = GUIType.valueOf(type.toUpperCase());
			
			UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f, Bukkit.getPlayer(name), null, null, true);
  
		} else if (args.length == 4) {

			String name = args[1];
			String type = args[2];
			String job = args[3];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			
			if (GUIType.valueOf(type.toUpperCase()) == null) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
			}

			GUIType f2 = GUIType.valueOf(type.toUpperCase());

			Job f = null;

			for (String jobs : UltimateJobs.getPlugin().getLoaded()) {

				Job j = UltimateJobs.getPlugin().getJobCache().get(jobs);

				if (j.getConfigID().equalsIgnoreCase(job)) {
					f = j;
				}
			}

			if (f != null) {
				UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f2, Bukkit.getPlayer(name), f,
						null, true);

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			}

		} else if (args.length == 5) {

			String name = args[1];
			String type = args[2];
			String job = args[3];
			String about = args[4];

			if (!Bukkit.getPlayer(name).isOnline()) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cPlayer is not online!");
			}
			
			if (GUIType.valueOf(type.toUpperCase()) == null) {
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				sender.sendMessage(AdminCommand.prefix + "§cError: GUI Type does not exist");
			}

			GUIType f2 = GUIType.valueOf(type.toUpperCase());

			Job f = null;

			for (String jobs : UltimateJobs.getPlugin().getLoaded()) {

				Job j = UltimateJobs.getPlugin().getJobCache().get(jobs);

				if (j.getConfigID().equalsIgnoreCase(job)) {
					f = j;
				}
			}

			if (f != null) {
				UltimateJobs.getPlugin().getGUIOpenManager().openGuiByGuiID(sender, f2, Bukkit.getPlayer(name), f,
						about, true);

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}
			}

		} else {
			if (sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command open players_online gui_types jobs_listed";
	}

	@Override
	public String getUsage() {
		return "/JobsAdmin open <player> <gui> <job> <about_player>";
	}

	@Override
	public String getPermission() {
		return "ultimatejobs.admin.open";
	}

	@Override
	public boolean showOnHelp() {
		return true;
	}

}
