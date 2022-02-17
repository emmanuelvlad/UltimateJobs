package de.warsteiner.jobs.utils;
 
import org.bukkit.command.CommandSender;

import de.warsteiner.datax.utils.module.SimplePluginModule;
import de.warsteiner.jobs.UltimateJobs; 

public class PluginModule extends SimplePluginModule {
 
	@Override
	public String getDeveloper() { 
		return "Warsteiner37";
	}

	@Override
	public String getIcon() { 
		return "DIAMOND_PICKAXE";
	}

	@Override
	public String getName() {  
		return "§9UltimateJobs";
	}

	@Override
	public String getPluginName() { 
		return "UltimateJobs";
	}

	@Override
	public String getVersion() { 
		return UltimateJobs.getPlugin().getDescription().getVersion();
	}

	@Override
	public void reloadConfig(CommandSender arg0) {
		UltimateJobs.getPlugin().getAPI().loadJobs(UltimateJobs.getPlugin().getLogger());
		UltimateJobs.getPlugin().setupConfigs();
	}

}
