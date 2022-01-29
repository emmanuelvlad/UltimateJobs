package de.warsteiner.jobs.utils;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.warsteiner.datax.module.UltimateModule;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;

public class PluginModule extends UltimateModule {
	
	private UltimateJobs plugin = UltimateJobs.getPlugin();
 
	@Override
	public String getDeveloper() { 
		return "Warsteiner37";
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
	public void reloadConfig(CommandSender sender) { 
		plugin.setupConfigs();
		plugin.getAPI().loadJobs(plugin.getLogger());
		sender.sendMessage(AdminCommand.prefix + "§aReloaded UltimateJobs : §bJobs and Config");
	}

	@Override
	public String getIcon() { 
		return "IRON_PICKAXE";
	}

	@Override
	public ArrayList<String> getDescription() {  
		ArrayList<String> l = new ArrayList<String>();
		
		l.add("§8-> §7UltimateJobs is a free to use Jobs Plugin");
		
		return l;
	}
 
}
