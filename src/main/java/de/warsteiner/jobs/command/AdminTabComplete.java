package de.warsteiner.jobs.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.warsteiner.jobs.UltimateJobs;

public class AdminTabComplete implements TabCompleter {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {

		ArrayList<String> l = new ArrayList<String>();

		int lg = args.length;

		de.warsteiner.jobs.api.JobAPI api = plugin.getAPI();

		if (lg == 1) {
			l.add("help");
			l.add("version");
			l.add("reload");
			l.add("removejob");
			l.add("addjob");
			l.add("addons");
			l.add("setlevel");
		} else if (lg == 2) {
			String ar = args[0].toLowerCase();
			if (ar.equalsIgnoreCase("reload")) {
				for (String b : plugin.getTypes()) {
					l.add(b.toLowerCase());
				}
			} else if (ar.equalsIgnoreCase("removejob") || ar.equalsIgnoreCase("addjob")
					|| ar.equalsIgnoreCase("setlevel")) {
				for (String b : api.getPlayerNameList()) {
					l.add(b);
				}
			}
		} else if (lg == 3) {
			String ar = args[0].toLowerCase();
			String ar2 = args[1].toLowerCase();
			if (ar.equalsIgnoreCase("setlevel") && ar2 != null || ar.equalsIgnoreCase("removejob") && ar2 != null
					|| ar.equalsIgnoreCase("addjob") && ar2 != null) {
				for (String b : api.getJobsInListAsID()) {
					l.add(b);
				}
			}

		}

		return l;

	}
}