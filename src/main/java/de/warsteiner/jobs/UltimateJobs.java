package de.warsteiner.jobs;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.YamlConfigFile;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.LevelAPI;
import de.warsteiner.jobs.api.PlayerManager;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.command.AdminTabComplete;
import de.warsteiner.jobs.command.JobTabComplete;
import de.warsteiner.jobs.command.JobsCommand;
import de.warsteiner.jobs.events.PlayerExistEvent;
import de.warsteiner.jobs.events.PlayerFinishedJob;
import de.warsteiner.jobs.events.PlayerSoundsEvent;
import de.warsteiner.jobs.events.PlayerlevelCheck;
import de.warsteiner.jobs.inventorys.MainMenuClickEvent;
import de.warsteiner.jobs.inventorys.SettingsMenuClickEvent;
import de.warsteiner.jobs.jobs.JobActionFarm;
import de.warsteiner.jobs.jobs.JobActionFish;
import de.warsteiner.jobs.jobs.JobActionKillMob;
import de.warsteiner.jobs.jobs.JobActionMilk;
import de.warsteiner.jobs.jobs.JobActionPlace;
import de.warsteiner.jobs.jobs.JobBlockBreak;
import de.warsteiner.jobs.utils.Action;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.ClickManager;
import de.warsteiner.jobs.utils.CommandAPI;
import de.warsteiner.jobs.utils.SQLManager;
import de.warsteiner.jobs.utils.command.SubCommandRegistry;
import net.milkbowl.vault.economy.Economy;

public class UltimateJobs extends JavaPlugin {

	private static UltimateJobs plugin;
	private Economy econ;
	private PlayerManager pm;
	private LevelAPI levels;
	private ArrayList<Job> loaded;
	private HashMap<String, Job> ld;
	private ArrayList<String> type;
	private CommandAPI cmd;
	private de.warsteiner.jobs.utils.GuiManager gui;
	private YamlConfigFile config;
	private YamlConfigFile messages;
	private JobAPI api;
	private ClickManager click;
	private SQLManager sql;
	private ArrayList<Plugin> addons;
	private Statement connection;
	private ExecutorService executor;
	private SubCommandRegistry cmdmanager;

	@Override
	public void onEnable() {

		plugin = this;

		createFolders();

		setupConfigs();

		loadClasses();

		YamlConfiguration cfg = config.getConfig();

		getTypes().add("CONFIGS");
		getTypes().add("ADDONS");
		getTypes().add("JOBS");

		getLogger().info("§bLoading UltimateJobs...");

		setupEconomy();

		getLogger().info("§bLoaded Vault for UltimateJobs");

		api.loadJobs(getLogger());
 
		// basic events
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerSoundsEvent(cfg), this);
		Bukkit.getPluginManager().registerEvents(new PlayerFinishedJob(), this);

		if (getMainConfig().getConfig().getBoolean("Enable_Levels")) {
			Bukkit.getPluginManager().registerEvents(new PlayerlevelCheck(), this);
		}

		getLogger().info("§fSetup SQL for UltimateJobs... ");

		try {
			connection = UltimateAPI.getInstance().getInit().getDataSource().getConnection().createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//events
		loadEvents();

		if (!UltimateAPI.getInstance().getInit().isClosed()) {
			getSQLManager().createtables();
			getLogger().info("§6Created job Tables... ");
		}

		getCommand("jobs").setExecutor(new JobsCommand());
		getCommand("jobs").setTabCompleter(new JobTabComplete());

		getCommand("jobsadmin").setExecutor(new AdminCommand());
		getCommand("jobsadmin").setTabCompleter(new AdminTabComplete());

		if (getMessages().getConfig().getBoolean("Reward.Enable_BossBar")) {
			BossBarHandler.startSystemCheck();
		}
		
		pm.startSave();

		getLogger().info("§bLoaded UltimateJobs! Jobs: §a" + loaded.size());
	}

	@Override
	public void onDisable() {

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer(" ");
		}

		if (getExecutor().isShutdown()) {
			return;
		}
		if (getExecutor() != null) {
			getExecutor().shutdown();
		}
	}
	
	public SubCommandRegistry getSubCommandManager() {
		return cmdmanager;
	}
	
	public void loadClasses() {
		loaded = new ArrayList<Job>();
		pm = new PlayerManager(plugin);
		type = new ArrayList<String>();
		addons = new ArrayList<Plugin>();
		ld = new HashMap<>();
		levels = new LevelAPI(plugin, messages.getConfig());
		sql = new SQLManager();
		executor = Executors.newFixedThreadPool(config.getConfig().getInt("ExecutorServiceThreads"));
		cmdmanager = new SubCommandRegistry();
		api = new JobAPI(plugin, messages.getConfig());
		gui = new de.warsteiner.jobs.utils.GuiManager(plugin, config.getConfig(), pm);
		click = new ClickManager(plugin, config.getConfig(), gui);
		cmd = new CommandAPI(plugin, pm, messages.getConfig());
	}

	public void doLog(String type, String m) {
		if (config.getConfig().getBoolean("Debug")) {
			if (type.equalsIgnoreCase("INFO")) {
				getLogger().info(m);
			} else {
				getLogger().warning(m);
			}
		}

	}

	public SQLManager getSQLManager() {
		return sql;
	}

	public Statement getConnection() {
		return connection;
	}

	public ArrayList<Plugin> getAddons() {
		return addons;
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

	public CommandAPI getCommand() {
		return cmd;
	}

	public HashMap<String, Job> getID() {
		return ld;
	}

	public de.warsteiner.jobs.utils.GuiManager getGUI() {
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
		Bukkit.getPluginManager().registerEvents(new JobBlockBreak(), plugin);
		Bukkit.getPluginManager().registerEvents(new JobActionFarm(), plugin);
		Bukkit.getPluginManager().registerEvents(new JobActionPlace(), plugin);
		Bukkit.getPluginManager().registerEvents(new JobActionFish(), plugin);
		Bukkit.getPluginManager().registerEvents(new JobActionMilk(), plugin);
		Bukkit.getPluginManager().registerEvents(new JobActionKillMob(), plugin);
	}
	 
	public void setupConfigs() {
		File file_config = new File(getDataFolder() + File.separator, "Config.yml");
		File file_messages = new File(getDataFolder() + File.separator, "Messages.yml");

		config = new YamlConfigFile(getPlugin(), file_config);
		messages = new YamlConfigFile(getPlugin(), file_messages);

		try {
			config.load();
			messages.load();
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

	public ArrayList<String> getTypes() {
		return type;
	}

	public YamlConfigFile getMainConfig() {
		return config;
	}

	public YamlConfigFile getMessages() {
		return messages;
	}

	public ArrayList<Job> getLoaded() {
		return loaded;
	}

}
