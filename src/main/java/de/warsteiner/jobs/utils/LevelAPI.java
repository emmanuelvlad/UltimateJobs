package de.warsteiner.jobs.utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;

public class LevelAPI {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public double getJobNeedExp(String uuid, File job, String id) {
	 
		YamlConfiguration cfg = plugin.getJobAPI().getJobConfig(job);
  
		int next = plugin.getPlayerAPI().getJobLevel(uuid, id)+1;
 
		return cfg.getDouble("LEVELS."+next+".Need"); 
	}
	
	public boolean isMaxLevel(String uuid, File job, int level) {
		if(getLevelName(job, level+1) != null) {
			return false;
		}
		return true;
	}
	
	public String getLevelName(File job, int level) {
		 
		YamlConfiguration cfg = plugin.getJobAPI().getJobConfig(job);
    
		String need = cfg.getString("LEVELS."+level+".Display");
		 
		return need; 
	}
	
	 @SuppressWarnings("deprecation")
	public  void check(Player player, File j) {
		 String UUID = ""+player.getUniqueId();
		 String id = plugin.getJobAPI().getID(j); 
		 PlayerAPI p = plugin.getPlayerAPI();
		 YamlConfiguration tr = plugin.getMessages().getConfig();
		 int old_level = p.getJobLevel(UUID, id);
		 if(!isMaxLevel(UUID, j, old_level)) {
	 
			double need = getJobNeedExp(UUID, j, id);
 
			double exp = plugin.getPlayerAPI().getJobExp(UUID, id); 
			if(exp == need || exp >= need) { 
				p.setJobExp(UUID, id, 0);
				p.addLevelOfJob(UUID, id, 1);
				
				int level = p.getJobLevel(UUID, id);
				
				for(String cmd : plugin.getJobAPI().getLevelRewards(j, level)) {
					
					 ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
			    
					 Bukkit.dispatchCommand((CommandSender)console, cmd.replaceAll("<name>", player.getName()));
					
				}
				
				if(tr.getBoolean("Levels.Enable_Title")) {
					String title_1 = plugin.getJobAPI().toHex(tr.getString("Levels.Ttitle_1")
							.replaceAll("<level_name>",getLevelName(j, level)).replaceAll("<level_int>", ""+level).replaceAll("&", "§"));
					String title_2 = plugin.getJobAPI().toHex(tr.getString("Levels.Ttitle_2")
							.replaceAll("<level_name>",getLevelName(j, level)).replaceAll("<level_int>", ""+level).replaceAll("&", "§"));
					player.sendTitle(title_1, title_2);
				}
				
				if(tr.getBoolean("Levels.Enable_Message")) {
					String message = plugin.getJobAPI().toHex(tr.getString("Levels.Message")
							.replaceAll("<level_name>",getLevelName(j, level)).replaceAll("<level_int>", ""+level).replaceAll("&", "§"));
					player.sendMessage(message);
				}
				return;
			}
		 }
	 }


}
