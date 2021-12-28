package de.warsteiner.jobs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;

public class CommandAPI {

 
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
 
	public void execute(Player player, String args[]) {
		 
		 int lg = args.length;
		 YamlConfiguration tr = plugin.getMessages().getConfig();
		 JobAPI api = plugin.getJobAPI();
		 String UUID = ""+player.getUniqueId(); 
		 PlayerAPI p = plugin.getPlayerAPI();
		 String ar1 = args[0].toUpperCase();
		 
		 if(lg == 1 && isEnabled("HELP") && ar1.equalsIgnoreCase(getUsage("HELP"))) {
			 for(String m : tr.getStringList("Help")) {
				 player.sendMessage(api.toHex(m).replaceAll("&", "§"));
			 }
		 } else  if(lg == 1 && isEnabled("LEAVEALL") && ar1.equalsIgnoreCase(getUsage("LEAVEALL"))) {
			 if(!p.hasAnyJob(UUID)) {
					p.setCurrentJobsToNull(UUID); 
					player.sendMessage(api.getMessage("Leave_All"));
				} else {
					player.sendMessage(api.getMessage("Already_Left_All"));
				} 
		 }  else  if(lg == 2 && isEnabled("LEAVE") && ar1.equalsIgnoreCase(getUsage("LEAVE"))) {
			 String job = args[1].toUpperCase(); 
			 if(checkIfJobIsReal(job, player)) {
				 File file = api.isJobFromConfigID(job);
				if(plugin.getPlayerAPI().isInJob(UUID, job.toUpperCase())) {
					plugin.getPlayerAPI().remCurrentJobs(UUID, api.getID(file));
					player.sendMessage(plugin.getJobAPI().getMessage("Left_Job").replaceAll("<job>", plugin.getJobAPI().getDisplay(file)));
				}   else {
					player.sendMessage(plugin.getJobAPI().getMessage("Not_In_Job").replaceAll("<job>", plugin.getJobAPI().getDisplay(file)));
				}
			 }
		 } else  if(lg == 2 && isEnabled("STATS") && ar1.equalsIgnoreCase(getUsage("STATS"))
				 && isThereAsPlugin("JobStats")) {
			String oth = args[1].toUpperCase(); 
			player.sendMessage("test with: "+oth);
		 } else {
			 player.sendMessage(api.getMessage("Not_Found_Command"));
		 }
	}
	
	public void executeAdminCommandAsPlayer(Player player, String args[]) {
		 
		 int lg = args.length;
		 YamlConfiguration tr = plugin.getMessages().getConfig();
		 JobAPI api = plugin.getJobAPI(); 
		 PlayerAPI p = plugin.getPlayerAPI();
	 
	 
		 if(lg == 0) {
			 if(AdminCommand.checkPermissions(player, "admin.help")) {
				 for(String m : tr.getStringList("Admin_Help")) {
					 player.sendMessage(api.toHex(m).replaceAll("&", "§"));
				 }  
			 }
		 } else  if(lg == 1 && args[0].equalsIgnoreCase("HELP")) {
			 if(AdminCommand.checkPermissions(player, "admin.help")) {
				 for(String m : tr.getStringList("Admin_Help")) {
					 player.sendMessage(api.toHex(m).replaceAll("&", "§"));
				 }  
			 }
		 }else  if(lg == 1 && args[0].equalsIgnoreCase("VERSION")) {
			 if(AdminCommand.checkPermissions(player, "admin.version")) {
				player.sendMessage("§9UltimateJobs §7is running on Version §6v"+plugin.getDescription().getVersion()+" §7with API-Version §a"+plugin.getDescription().getAPIVersion()
						+ " §7by §cWarsteiner37");
			 }
		 }  else if(lg == 2 && args[0].equalsIgnoreCase("RELOAD") && args[1] != null) {
			  
			 String type = args[1].toUpperCase();
			 
		 } else {
			 player.sendMessage(api.getMessage("Admin_Not_Found"));
		 }
	}
	
 
	public boolean isThereAsPlugin(String name) {
		return Bukkit.getPluginManager().isPluginEnabled(name);
	}
	
	public boolean checkifConfigTypeExist(String arg, Player player) {
		String id = arg.toUpperCase();
		if(plugin.getJobAPI().isJobFromConfigID(id) != null) {
			return true;
		}
		player.sendMessage(plugin.getJobAPI().getMessage("Not_Found_Job").replaceAll("<job>", arg));
		return false;
	}
 
	public boolean checkIfJobIsReal(String arg, Player player) {
		String id = arg.toUpperCase();
		if(plugin.getJobAPI().isJobFromConfigID(id) != null) {
			return true;
		}
		player.sendMessage(plugin.getJobAPI().getMessage("Not_Found_Job").replaceAll("<job>", arg));
		return false;
	}
	
	public boolean isEnabled(String t) {
		return plugin.getMainConfig().getConfig().getBoolean("Command."+t+".Enabled");
	}
	
	public String getUsage(String t) {
		return plugin.getMainConfig().getConfig().getString("Command."+t+".Usage").toUpperCase();
	}
	
	public boolean isAddon(String t) {
		return plugin.getMainConfig().getConfig().contains("Command."+t+".Addon");
	}
	
	public String getAddonName(String t) {
		return plugin.getMainConfig().getConfig().getString("Command."+t+".Usage").toUpperCase();
	}
 
}
