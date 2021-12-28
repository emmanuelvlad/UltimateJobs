package de.warsteiner.jobs.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.CommandAPI;
import de.warsteiner.jobs.utils.JobAPI;

public class JobTabComplete  implements TabCompleter {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		ArrayList<String> l = new ArrayList<String>();
		if(s instanceof Player) {
			Player p = (Player) s;
			String UUID = ""+p.getUniqueId();
			CommandAPI cmd = plugin.getCommand();
			JobAPI api = plugin.getJobAPI();
	
		 
	
			int lg = args.length; 
			
			if(lg == 1) {
			
			 if(cmd.isEnabled("HELP")) {
				l.add(cmd.getUsage("HELP").toLowerCase());
			 }  if(cmd.isEnabled("LEAVEALL")) {
				l.add(cmd.getUsage("LEAVEALL").toLowerCase());
			 }  if(cmd.isEnabled("LEAVE")) {
				l.add(cmd.getUsage("LEAVE").toLowerCase());
			 }  if(cmd.isEnabled("STATS")) {
				l.add(cmd.getUsage("STATS").toLowerCase());
			 }  if(cmd.isEnabled("SALARY")) {
				l.add(cmd.getUsage("SALARY").toLowerCase());
			 }
			
			} else 	if(lg == 2) {
				String ar0 = args[0];
				if(cmd.isEnabled("LEAVE")) {
					if(ar0.equalsIgnoreCase(cmd.getUsage("LEAVE"))) {
						for(String b : api.getJobsInListAsIDIfIsIn(UUID)) {
							l.add(b);
						}
					}
				}
				if(cmd.isEnabled("SALARY")) {
					if(ar0.equalsIgnoreCase(cmd.getUsage("SALARY"))) {
						for(String b : api.getJobsInListAsID()) {
							l.add(b);
						}
					}
				}
				if(cmd.isEnabled("STATS")) {
					if(ar0.equalsIgnoreCase(cmd.getUsage("STATS"))) {
						for(String b : api.getPlayerNameList()) {
							l.add(b);
						}
					}
				}
			}
		}
		return l;
	 
	}
}
 
