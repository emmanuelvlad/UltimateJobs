package de.warsteiner.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.warsteiner.jobs.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.files.PlayerDataFile;
import de.warsteiner.datax.utils.files.YamlConfigFile;
import de.warsteiner.datax.utils.other.UpdateChecker;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.LevelAPI;
import de.warsteiner.jobs.api.PluginAPI;
import de.warsteiner.jobs.api.plugins.AlonsoLevelsManager;
import de.warsteiner.jobs.api.plugins.NotQuestManager;
import de.warsteiner.jobs.api.plugins.PlaceHolderManager;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.command.AdminTabComplete;
import de.warsteiner.jobs.command.JobTabComplete;
import de.warsteiner.jobs.command.JobsCommand; 
import de.warsteiner.jobs.command.admincommand.HelpSub; 
import de.warsteiner.jobs.command.admincommand.SetLevelSub;
import de.warsteiner.jobs.command.admincommand.SetMaxSub;
import de.warsteiner.jobs.command.admincommand.SetPointsSub; 
import de.warsteiner.jobs.command.admincommand.VersionSub;
import de.warsteiner.jobs.command.playercommand.JoinSub;
import de.warsteiner.jobs.command.playercommand.LeaveAllSub;
import de.warsteiner.jobs.command.playercommand.LeaveSub;
import de.warsteiner.jobs.command.playercommand.LimitSub;
import de.warsteiner.jobs.command.playercommand.PointsSub;
import de.warsteiner.jobs.command.playercommand.SubHelp; 
import de.warsteiner.jobs.inventorys.AreYouSureMenuClickEvent;
import de.warsteiner.jobs.inventorys.MainMenuClickEvent;
import de.warsteiner.jobs.inventorys.SettingsMenuClickEvent;
import de.warsteiner.jobs.jobs.JobActionAdvancement;
import de.warsteiner.jobs.jobs.JobActionBreak;
import de.warsteiner.jobs.jobs.JobActionCraft;
import de.warsteiner.jobs.jobs.JobActionEat;
import de.warsteiner.jobs.jobs.JobActionFarm;
import de.warsteiner.jobs.jobs.JobActionFish;
import de.warsteiner.jobs.jobs.JobActionHoney;
import de.warsteiner.jobs.jobs.JobActionKillMob;
import de.warsteiner.jobs.jobs.JobActionMilk;
import de.warsteiner.jobs.jobs.JobActionPlace;
import de.warsteiner.jobs.jobs.JobActionShear;
import de.warsteiner.jobs.manager.ClickManager;
import de.warsteiner.jobs.manager.GuiManager;
import de.warsteiner.jobs.manager.JobWorkManager; 
import de.warsteiner.jobs.manager.PlayerDataManager;
import de.warsteiner.jobs.manager.PlayerManager;
import de.warsteiner.jobs.manager.SQLPlayerManager;
import de.warsteiner.jobs.manager.YMLPlayerManager;
import de.warsteiner.jobs.utils.BossBarHandler; 
import de.warsteiner.jobs.utils.PluginModule;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommandRegistry; 
import de.warsteiner.jobs.utils.playercommand.SubCommandRegistry;
import net.milkbowl.vault.economy.Economy;

public class UltimateJobs extends JavaPlugin {

	private static UltimateJobs plugin;
	private Economy econ;
	private PlayerManager pm;
	private LevelAPI levels;
	private ArrayList<String> loaded;
	private HashMap<String, Job> ld;
	private de.warsteiner.jobs.manager.GuiManager gui;
	private YamlConfigFile config;
	private YamlConfigFile messages;
	private JobAPI api;
	private ClickManager click;
	private SQLPlayerManager sql;
	private ExecutorService executor;
	private SubCommandRegistry cmdmanager;
	private AdminSubCommandRegistry admincmdmanager; 
	private NotQuestManager notquest;
	private YMLPlayerManager yml;
	private PlayerDataManager pmanager;
	private PlayerDataFile datafile;
	private JobWorkManager work;
	private YamlConfigFile command; 
	private AlonsoLevelsManager alonso; 
	public PluginAPI plapi;

	public void onLoad() {

		plugin = this;
		plapi = new PluginAPI();
		 
		createFolders();

		setupConfigs();

		executor = Executors.newFixedThreadPool(this.config.getConfig().getInt("ExecutorServiceThreads"));
		
		if (getPluginAPI().isInstalled("WorldGuard")) {
			WorldGuardManager.setClass();
			WorldGuardManager.load(); 
			getLogger().info("§bLoaded WorldGuard-Support for UltimateJobs!");
		}
		
		if(getPluginAPI().isInstalled("SimpleAPI")) {
			getExecutor().execute(() -> {   
				if(config.getConfig().getBoolean("CheckForUpdates")) {
					SimpleAPI.getPlugin().getWebManager().loadVersionAndCheckUpdate("https://api.war-projects.com/v1/ultimatejobs/version.txt", "UltimateJobs",getDescription().getVersion());
				}
				SimpleAPI.getPlugin().getModuleRegistry().getModuleList().add(new PluginModule());
				this.getLogger().info("§6Hooked into SimpleAPI!");
			});
		}
		
	}

	@Override
	public void onEnable() {

		 
		setupEconomy();

		getLogger().info("§bLoaded Vault...");
  
		loadClasses(); 

		api.loadJobs(getLogger());
		
		getLogger().info("§bLoading UltimateJobs...");
		
		// basic events
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockFireWorkDamage(), this);
		Bukkit.getPluginManager().registerEvents(new AreYouSureMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLevelEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRewardCommandEvent(), this);

		Bukkit.getPluginManager().registerEvents(new IntegrationEvents(), this);
 
		// job events
		loadEvents();
		
		getLogger().info("§bLoaded Events...");
		
		getLogger().info("§bLoading Supported Plugins...");

		if (getPluginAPI().isInstalled("PlaceHolderAPI")) {
			new PlaceHolderManager().register();
			getLogger().info("§6Loaded PlaceHolderAPI Hook for UltimateJobs!");
		}

		if (getPluginAPI().isInstalled("NotQuests")) {
			getNotQuestManager().setClass();
			getLogger().info("§6Loaded NotQuests Hook for UltimateJobs!");
		}
		if (getPluginAPI().isInstalled("AlonsoLevels")) { 
			getLogger().info("§6Loaded AlonsoLevels Hook for UltimateJobs!");
		}
		
		getLogger().info("§bLoading Addons..."); 

		setupCommands();
		getCommand("jobs").setExecutor(new JobsCommand());
		getCommand("jobs").setTabCompleter(new JobTabComplete());

		getCommand("jobsadmin").setExecutor(new AdminCommand());
		getCommand("jobsadmin").setTabCompleter(new AdminTabComplete());
		
		getLogger().info("§bLoaded Commands...");

		if (getMessages().getConfig().getBoolean("Reward.Enable_BossBar")) {
			BossBarHandler.startSystemCheck();
			getLogger().info("§6Started System Check for BossBars...");
		}

		pm.startSave();
		getLogger().info("§6Started Check for Backup-System...");
    
		getLogger().info("§7");
		getLogger().info("§7");
		getLogger().info("  _   _ _  _____ ___ __  __   _ _______ ____  ___ ____  __ __");
		getLogger().info(" | | | | ||_   _|_ _|  \\/  | /_\\_   _| __| | |/  _ \\| _ ) __|");
		getLogger().info(" | |_| | |__| |  | || |\\/| |/ _ \\| | | _|  | || (_)|| _ \\__\\");
		getLogger().info("  \\___/|____|_|  |_||_|  |_/_/ \\_|_| |___\\__/ \\___/|___/___/");
		getLogger().info("                                                            "); 
		getLogger().info("       §bRunning plugin UltimateJobs v"+getDescription().getVersion()+" ("+getDescription().getAPIVersion()+")"); 
		getLogger().info("       §bRunning UltimateJobs with "+getLoaded().size()+" Jobs");
		getLogger().info("§7");
		getLogger().info("§7");
	}

	public void onDisable() {

		if (config.getConfig().getBoolean("KickOnReload")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.kickPlayer(" ");
			}
		}

		if (getExecutor().isShutdown()) {
			return;
		}
		if (getExecutor() != null) {
			getExecutor().shutdown();
		}
		 
		getLogger().warning("§7");
		getLogger().warning("     §cPlugin has been disabled!");
		getLogger().warning("     §cThank you for using my plugin!");
		getLogger().warning("§7");
	}

	public void setupCommands() {
		if (getCommandConfig().getConfig().getBoolean("Command.HELP.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new SubHelp());
		}
		if (getCommandConfig().getConfig().getBoolean("Command.LEAVE.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LeaveSub());
		}
		if (getCommandConfig().getConfig().getBoolean("Command.LEAVEALL.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LeaveAllSub());
		}
		if (getCommandConfig().getConfig().getBoolean("Command.POINTS.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new PointsSub());
		}
		if (getCommandConfig().getConfig().getBoolean("Command.LIMIT.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LimitSub());
		}
		if (getCommandConfig().getConfig().getBoolean("Command.JOIN.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new JoinSub());
		}

		getAdminSubCommandManager().getSubCommandList().add(new HelpSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetMaxSub()); 
		getAdminSubCommandManager().getSubCommandList().add(new SetLevelSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetPointsSub());
		getAdminSubCommandManager().getSubCommandList().add(new VersionSub());
		 
		getLogger().info("§bLoaded Sub-Commands...");
	}

	public void loadClasses() {
		loaded = new ArrayList<>();
		pm = new PlayerManager(plugin);
		ld = new HashMap<>();
		levels = new LevelAPI(plugin, this.messages.getConfig());
		 
		cmdmanager = new SubCommandRegistry();
		api = new JobAPI(plugin, messages.getConfig());
		gui = new GuiManager(plugin);
		click = new ClickManager(plugin, this.config.getConfig(), this.gui);
		admincmdmanager = new AdminSubCommandRegistry(); 
		notquest = new NotQuestManager();
		work = new JobWorkManager(plugin, this.api);
		alonso = new AlonsoLevelsManager(); 
		datafile = new PlayerDataFile("jobs");
		sql = new SQLPlayerManager();
		yml = new YMLPlayerManager();
		if (SimpleAPI.getPlugin().getPluginMode().equalsIgnoreCase("SQL")) {
			getSQLPlayerManager().createtables();
			getLogger().info("§6SQL for UltimateJobs...");
		} else {
			datafile.create();
			getLogger().info("§6YML for UltimateJobs...");
		}
		pmanager = new PlayerDataManager(yml, sql);
		
		getLogger().info("§bLoaded Classes for UltimateJobs...");
	}
	
	public PluginAPI getPluginAPI() {
		return plapi;
	}
 
	public AlonsoLevelsManager getAlonsoManager() {
		return alonso;
	}
 
	public JobWorkManager getJobWorkManager() {
		return work;
	}

	public PlayerDataFile getPlayerDataFile() {
		return datafile;
	}

	public PlayerDataManager getPlayerDataModeManager() {
		return pmanager;
	}

	public YMLPlayerManager getYMLPlayerManager() {
		return yml;
	}

	public NotQuestManager getNotQuestManager() {
		return notquest;
	}
 
	public AdminSubCommandRegistry getAdminSubCommandManager() {
		return admincmdmanager;
	}

	public SubCommandRegistry getSubCommandManager() {
		return cmdmanager;
	}

	public SQLPlayerManager getSQLPlayerManager() {
		return sql;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public static UltimateJobs getPlugin() {
		return plugin;
	}

	public ClickManager getClickManager() {
		return click;
	}

	public HashMap<String, Job> getID() {
		return ld;
	}

	public de.warsteiner.jobs.manager.GuiManager getGUI() {
		return gui;
	}

	public PlayerManager getPlayerManager() {
		return pm;
	}

	public JobAPI getAPI() {
		return api;
	}

	public LevelAPI getLevelAPI() {
		return levels;
	}

	private void createFolders() {

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();

			File folder_1 = new File(getDataFolder(), "jobs");

			folder_1.mkdir();
  
			getLogger().info("§6Created Folders for UltimateJobs");
		}

	}

	public void loadEvents() {
		Bukkit.getPluginManager().registerEvents(new JobActionBreak(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionFarm(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionPlace(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionFish(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionMilk(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionKillMob(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionShear(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionCraft(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionAdvancement(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionEat(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionHoney(), this);
	}

	public void setupConfigs() {
		File file_config = new File(getDataFolder() + File.separator, "Config.yml");
		File file_messages = new File(getDataFolder() + File.separator, "Messages.yml");
		File file_cmd = new File(getDataFolder() + File.separator, "Command.yml");

		config = new YamlConfigFile(getPlugin(), file_config);
		messages = new YamlConfigFile(getPlugin(), file_messages);
		command = new YamlConfigFile(getPlugin(), file_cmd);

		try {
			config.load();
			messages.load();
			command.load();
			getLogger().info("§bLoaded Configs...");
		} catch (IOException e) {
			getLogger().warning("§cFailed to create Config Files");
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

	public Economy getEco() {
		return econ;
	}

	public HashMap<String, Job> getJobCache() {
		return ld;
	}

	public YamlConfigFile getMainConfig() {
		return config;
	}

	public YamlConfigFile getMessages() {
		return messages;
	}

	public YamlConfigFile getCommandConfig() {
		return command;
	}

	public ArrayList<String> getLoaded() {
		return loaded;
	}

}
