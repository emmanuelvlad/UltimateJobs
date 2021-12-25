package de.warsteiner.jobs.utils;
 
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;

public class CommandAPI {
	
	private HashMap<String, YamlConfigFile> list = new HashMap<String, YamlConfigFile>(); 
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public void loadBasicJobCommands(YamlConfigFile yamlConfigFile) { 
		
		list.put("HELP", yamlConfigFile); 
		list.put("DEMO",  yamlConfigFile);
		list.put("STATS",  yamlConfigFile);
	 
		plugin.getLogger().info("§9Loaded Command for UltimateJobs");
	}
	
	public boolean isCommandCheckMain(String text) {
		return list.containsKey(text.toUpperCase());
	}
	
	public String getAction(String text) { 
	 
		if(list.get(text) != null) {
			YamlConfiguration config = list.get(text.toUpperCase()).getConfig(); 
			if(config.getString("Command."+text.toUpperCase()+".Action") != null) {
				return config.getString("Command."+text.toUpperCase()+".Action").toUpperCase();
			} else
			
			return "NOT_FOUND"; 
		} else
			
			return "NOT_FOUND"; 
	}
	
	public void execute(Player player, String args[]) {
		
		String text = args[0].toUpperCase(); 
		Bukkit.broadcastMessage("text: "+text);
		String action = getAction(text);
		Bukkit.broadcastMessage("Action: "+getAction(text));
		if(action.equalsIgnoreCase("NOT_FOUND")) {
			player.sendMessage("I DIDNT FOUND SOMETHING TO THIS, SORRY!");
		} else {
			int length = args.length; 
			if(length == getLenghtOfAction(action.toUpperCase())) {
				if(action.equalsIgnoreCase("MESSAGE")) {
					player.sendMessage("execute a single message");
					return;
				} else if(action.equalsIgnoreCase("STATS_OTHER")) {
					player.sendMessage("execute a single message of other player's §b"+args[1]);
					return;
				}  else if(action.equalsIgnoreCase("STATS_SELF")) {
					player.sendMessage("execute a single message of other player's §b"+player.getName());
					return;
				}
			}
		}
	}
	
	public int getLenghtOfAction(String text) {
		String tx = text.toUpperCase(); 
		if(tx.equalsIgnoreCase("MESSAGE")) {
			return 1;
		} else if(tx.equalsIgnoreCase("STATS_OTHER")) {
			return 2;
		} else if(tx.equalsIgnoreCase("STATS_SELF")) {
			return 1;
		}
		return 99;
	}
	
	public HashMap<String, YamlConfigFile> getListOfCommands() {
		return list;
		
	}
	
}
