package de.warsteiner.jobs.manager;
 
import org.bukkit.Bukkit; 
import org.bukkit.plugin.Plugin; 

import de.warsteiner.jobs.UltimateJobs;  

public class PluginManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin();
  
	public boolean isInstalled(String plugin) {
		Plugin Plugin = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (Plugin != null) {
			return true;
		}
		return false;
	}
	 
}
