package de.warsteiner.jobs.api;

import java.io.File; 
import java.util.logging.Logger; 
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration; 
import org.bukkit.plugin.Plugin; 

import de.warsteiner.jobs.UltimateJobs;  

public class PluginAPI {

	private UltimateJobs plugin = UltimateJobs.getPlugin();
  
	public boolean isInstalled(String plugin) {
		Plugin Plugin = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (Plugin != null) {
			return true;
		}
		return false;
	}
	
}
