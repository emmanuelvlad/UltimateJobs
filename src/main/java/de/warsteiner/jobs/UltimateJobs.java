package de.warsteiner.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.jobs.command.JobsCommand;
import de.warsteiner.jobs.events.PlayerBlockBreak;
import de.warsteiner.jobs.events.PlayerExistEvent;
import de.warsteiner.jobs.events.PlayerlevelCheck;
import de.warsteiner.jobs.events.inventorys.MainMenuClickEvent;
import de.warsteiner.jobs.events.inventorys.SettingsMenuClickEvent;
import de.warsteiner.jobs.utils.CommandAPI;
import de.warsteiner.jobs.utils.DataFile;
import de.warsteiner.jobs.utils.GuiManager;
import de.warsteiner.jobs.utils.JobAPI;
import de.warsteiner.jobs.utils.LevelAPI;
import de.warsteiner.jobs.utils.PlayerAPI;
import de.warsteiner.jobs.utils.YamlConfigFile; 
import net.milkbowl.vault.economy.Economy;
 
public class UltimateJobs extends JavaPlugin {
	
	private static UltimateJobs plugin;
	private static Economy econ;
	
	private ArrayList<File> jobs;
	
	private YamlConfigFile config;
	private YamlConfigFile messages;
	
	private DataFile data;
	private JobAPI api;
	private PlayerAPI player;
	private GuiManager gui;
	private CommandAPI command;
	private LevelAPI levels;
	
	@Override
	public void onEnable() {

		plugin = this;
		
		api = new JobAPI();
		player = new PlayerAPI();
		jobs = new ArrayList<File>();
		gui = new GuiManager();
		command = new CommandAPI(); 
		levels = new LevelAPI();
		
		this.getLogger().info("§bLoading UltimateJobs...");
		
		setupEconomy();
		
		this.getLogger().info("§bLoaded Vault for UltimateJobs");
		 
		createFolders();
		
		loadFiles();
		
		setupConfigs(this.getLogger());
		
		loadJobs(this.getLogger());
		
		getCommandAPI().loadBasicJobCommands(getMainConfig());
		
		//basic events
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuClickEvent(), this);
		
		if(getMainConfig().getConfig().getBoolean("Enable_Levels")) {
			Bukkit.getPluginManager().registerEvents(new PlayerlevelCheck(), this);
		}
		//job-events
		Bukkit.getPluginManager().registerEvents(new PlayerBlockBreak(), this);
		 
		getCommand("jobs").setExecutor(new JobsCommand());
 
		if(getMessages().getConfig().getBoolean("Reward.Enable_BossBar")) {
			getJobAPI().startSystemCheck();
		}
		
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
	
	public void loadFiles() {
		data = new DataFile(getPlugin(), "data", getDataFolder().getPath() + File.separator + "data" + File.separator);
		data.reload();
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
	
	private void setupConfigs(Logger logger) {
		File file_config = new File(getDataFolder() + File.separator, "Config.yml");
		File file_messages = new File(getDataFolder() + File.separator, "Messages.yml");
		 
		config = new YamlConfigFile(getPlugin(), file_config); 
		messages = new YamlConfigFile(getPlugin(), file_messages); 
 
		try {
			config.load(); 
			messages.load();
		} catch (IOException e) { 
			logger.info("§4§lFailed to create Config Files");
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
	
	public YamlConfigFile getMainConfig() {
		return config;
	}
	
	public YamlConfigFile getMessages() {
		return messages;
	}
	
	public DataFile getPlayerData() {
		return data;
	}
	
	public LevelAPI getLevelAPI() {
		return levels;
	}
	
	public CommandAPI getCommandAPI() {
		return command;
	}
	
	public PlayerAPI getPlayerAPI() {
		return player;
	}
	
	public GuiManager getGUIManager() {
		return gui;
	}
	
	public ArrayList<File> getLoadedJobs()  {
		return jobs;
	}

	public JobAPI getJobAPI() {
		return api;
	}
}
