package de.warsteiner.jobs.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand; 

public class AdminTabComplete implements TabCompleter {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {

		ArrayList<String> l = new ArrayList<String>();

		JobAPI api = plugin.getAPI();

		if (s.hasPermission("ultimatejobs.complete")) {

			if (args.length == 1) {

				for (String found : find(1)) {
					l.add(found);
				}

			} else if (args.length != 1) {

				for (AdminSubCommand c : plugin.getAdminSubCommandManager().getSubCommandList()) {

					if (c.getTabLength() <= args.length) {

						if (args[0].toLowerCase().equalsIgnoreCase(c.getName().toLowerCase())) {
							if (!getFromFormat(args.length, c).equalsIgnoreCase("NOT_FOUND")) {

								String type = getFromFormat(args.length, c).toUpperCase();

								if (type.equalsIgnoreCase("JOBS_LISTED")) {
									for (String b : api.getJobsInListAsID()) {
										l.add(b);
									}
								} else if (type.equalsIgnoreCase("PLAYERS_ONLINE")) {
									for (Player b : Bukkit.getOnlinePlayers()) {
										l.add(b.getName());
									}
								}  

							}
						}

					}

				}

			}
		} else {
			l.add("none");
		}
		return l;

	}

	public ArrayList<String> find(int length) {
		ArrayList<String> list = new ArrayList<String>();
		for (AdminSubCommand c : plugin.getAdminSubCommandManager().getSubCommandList()) {
			if (getFromFormat(1, c) != null) {
				list.add(c.getName().toLowerCase());
			}
		}
		return list;
	}

	public String getFromFormat(int length, AdminSubCommand c) {
		String[] format = c.FormatTab().split(" ");
		if (format.length <= length) {
			return "NOT_FOUND";
		}
		if (format[length] == null) {
			return "NOT_FOUND";
		}
		return format[length];

	}

}