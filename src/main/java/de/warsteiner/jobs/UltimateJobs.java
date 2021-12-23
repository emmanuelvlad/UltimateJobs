package de.warsteiner.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.jobs.command.JobsCommand;
import de.warsteiner.jobs.utils.JobAPI;
import de.warsteiner.jobs.utils.YamlConfigFile;
import net.milkbowl.vault.economy.Economy;
 
public class UltimateJobs extends JavaPlugin {
	
	private static UltimateJobs plugin;
	private static Economy econ;
	
	private ArrayList<File> jobs;
	
	private YamlConfigFile config;
	private JobAPI api;
	
	@Override
	public void onEnable() {

		plugin = this;
		
		api = new JobAPI();
		jobs = new ArrayList<File>();
		 
		this.getLogger().info("§bLoading UltimateJobs...");
		
		setupEconomy();
		
		this.getLogger().info("§bLoaded Vault for UltimateJobs");
		
		//Bukkit.getPluginManager().registerEvents(new PlayerJobBreakEvent(), this);
		 
		getCommand("jobs").setExecutor(new JobsCommand());
		
		createFolders();
		
		setupConfigs();
		
		loadJobs(this.getLogger());
		
		
		this.getLogger().info("§a§lLoaded UltimateJobs! Jobs: §4§lx"+jobs.size());
	}
	
	public static UltimateJobs getPlugin() {
		return plugin;
	}
	
	private void createFolders() {
		
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();

			File folder_1 = new File(getDataFolder(), "jobs");
			File folder_2 = new File(getDataFolder(), "data"); 

			folder_1.mkdir();
			folder_2.mkdir(); 
			
			this.getLogger().info("§bCreated Folders for UltimateJobs");
		}
		
	}
	
	public void loadJobs(Logger logger) {
		File dataFolder = new File(getDataFolder() + File.separator +"jobs");
		File[] files = dataFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			File file = files[i];
			if (file.isFile()) {
				logger.info("§aChecking Jobs File §e§l" + name);
				if(getJobAPI().isJobFile(file)) {
					jobs.add(file);
					logger.info("§aLoaded Job with File §e§l" + name);
				} else {
					logger.info("§aCannot load Job File §e§l" + name+"§a! Creating new...");
					getJobAPI().createDemoFile(file);
					if(getJobAPI().isJobFile(file)) {
						jobs.add(file);
						logger.info("§aLoaded Job with File §e§l" + name);
					}
				}
			} else if (file.isDirectory()) {
				logger.info("§4§lCannot load Directory §e§l" + name);
			}
		}
	}
	
	private void setupConfigs() {
		File file_config = new File(getDataFolder() + File.separator, "Config.yml");
		 
		config = new YamlConfigFile(getPlugin(), file_config); 

		try {
			config.load(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
		}

		return (econ != null);
	}
	
	public static Economy getEco() {
		return econ;
	}
	
	public ArrayList<File> getLoadedJobs()  {
		return jobs;
	}

	public JobAPI getJobAPI() {
		return api;
	}
}
