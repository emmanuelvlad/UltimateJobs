package de.warsteiner.jobs.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.warsteiner.datax.utils.objects.Language;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class JobTabComplete implements TabCompleter {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		ArrayList<String> l = new ArrayList<String>();
	 
			Player p = (Player) s;
			String UUID = "" + p.getUniqueId();
			JobAPI api = plugin.getAPI();
			JobsPlayer jb =plugin.getPlayerAPI().getRealJobPlayer(UUID);

			if (args.length == 1) {

				for (SubCommand found : plugin.getSubCommandManager().getSubCommandList()) {
					l.add(found.getName(jb.getUUID()));
				}

			} else if (args.length != 1) {

				for (SubCommand c : plugin.getSubCommandManager().getSubCommandList()) {

					if (c.getTabLength() <= args.length) {

						if (args[0].toLowerCase().equalsIgnoreCase(c.getName(jb.getUUID()).toLowerCase())) {
							if (!getFromFormat(args.length, c).equalsIgnoreCase("NOT_FOUND")) {

								if(c.isEnabled()) {
									String type = getFromFormat(args.length, c).toUpperCase();

									if (type.equalsIgnoreCase("JOBS_IN")) {
										if (jb.getCurrentJobs() != null) {
											for (String b : jb.getCurrentJobs()) {
												if(plugin.getJobCache().get(b) != null) {
													l.add(plugin.getJobCache().get(b).getDisplayID(UUID));
												}
											}
										}
									} else if (type.equalsIgnoreCase("JOBS_LISTED")) {
										for (String b : api.getJobsInListAsID(UUID)) {
											l.add(b);
										}
									} else if (type.equalsIgnoreCase("PLAYERS_ONLINE")) {
										for (Player b : Bukkit.getOnlinePlayers()) {
											l.add(b.getName());
										}
									} else if (type.equalsIgnoreCase("JOBS_OWNED")) {
										if (jb.getOwnJobs() != null) {
											for (String b : jb.getOwnJobs()) {
												if(plugin.getJobCache().get(b) != null) {
													l.add(plugin.getJobCache().get(b).getDisplayID(UUID));
												}
											}
										}
									}   else if (type.equalsIgnoreCase("LANGUAGES")) {
										if (jb.getOwnJobs() != null) {
											for (Language b : plugin.getLanguageAPI().getLoadedLanguagesAsArray()) {
												l.add(b.getID().toLowerCase());
											}
										}
									} 
								}
							}
						}

					}

				}
 
		}
		return l;

	}
 
	public String getFromFormat(int length, SubCommand c) {
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
