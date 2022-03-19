package de.warsteiner.jobs.utils;
 
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.warsteiner.datax.utils.module.SimplePluginModule;
import de.warsteiner.jobs.UltimateJobs; 

public class PluginModule extends SimplePluginModule {

	@Override
	public String getPluginName() { 
		return "UltimateJobs";
	}

	@Override
	public void reloadConfig(CommandSender arg0) {
		UltimateJobs.getPlugin().getFileManager().reloadFiles();
		UltimateJobs.getPlugin().getAPI().loadJobs(UltimateJobs.getPlugin().getLogger()); 
	}

	@Override
	public File getFile() {  
		return Bukkit.getPluginManager().getPlugin("UltimateJobs").getDataFolder().getAbsoluteFile();
	}
 
 
}
