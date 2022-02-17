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

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.files.PlayerDataFile;
import de.warsteiner.datax.utils.files.YamlConfigFile;
import de.warsteiner.datax.utils.other.UpdateChecker;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.LevelAPI;
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
import de.warsteiner.jobs.command.admincommand.UpdateSub;
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
import de.warsteiner.jobs.manager.PlayerDataManager;
import de.warsteiner.jobs.manager.PlayerManager;
import de.warsteiner.jobs.manager.SQLPlayerManager;
import de.warsteiner.jobs.manager.YMLPlayerManager;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.LogType;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommandRegistry;
import de.warsteiner.jobs.utils.cevents.CustomEventManager;
import de.warsteiner.jobs.utils.playercommand.SubCommandRegistry;
import net.milkbowl.vault.economy.Economy;

public class UltimateJobs extends JavaPlugin {

	private static UltimateJobs plugin;
	private Economy econ;
	private PlayerManager pm;
	private LevelAPI levels;
	private ArrayList<Job> loaded;
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
	private CustomEventManager customevent;
	private NotQuestManager notquest;
	private YMLPlayerManager yml;
	private PlayerDataManager pmanager;
	private PlayerDataFile datafile;

	public String isLatest = null;

	public void onLoad() {

		plugin = this;

		if (isInstalledWorldGuard()) {
			WorldGuardManager.setClass();
			WorldGuardManager.load();
			getLogger().info("§bLoaded WorldGuard-Support for UltimateJobs!");
		}
	}

	@Override
	public void onEnable() {

		createFolders();

		setupConfigs();

		getLogger().info("§bLoading UltimateJobs...");

		loadClasses();

		setupEconomy();

		getLogger().info("§bLoaded Vault for UltimateJobs");

		api.loadJobs(getLogger());

		// basic events
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockFireWorkDamage(), this);
		Bukkit.getPluginManager().registerEvents(new AreYouSureMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLevelEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRewardCommandEvent(), this);
		Bukkit.getPluginManager().registerEvents(new IntegrationEvents(), this);

		// events
		loadEvents();

		if (isInstalledPlaceHolder()) {
			new PlaceHolderManager().register();
			getLogger().info("§6Loaded PlaceHolderAPI for UltimateJobs");
		}

		if (isInstalledNotQuests()) {
			getNotQuestManager().setClass();
		}

		setupCommands();
		getCommand("jobs").setExecutor(new JobsCommand());
		getCommand("jobs").setTabCompleter(new JobTabComplete());

		getCommand("jobsadmin").setExecutor(new AdminCommand());
		getCommand("jobsadmin").setTabCompleter(new AdminTabComplete());

		if (getMessages().getConfig().getBoolean("Reward.Enable_BossBar")) {
			BossBarHandler.startSystemCheck();
		}

		datafile = new PlayerDataFile("jobs");

		datafile.create();

		pm.startSave();

		customevent.startSystemCheck();

		getLogger().info("§bLoaded UltimateJobs! Jobs: §a" + loaded.size());

		if (config.getConfig().getBoolean("CheckForUpdates")) {
			new UpdateChecker(plugin, 99198).getVersion(version -> {
				if (!plugin.getDescription().getVersion().equals(version)) {
					this.getLogger().warning("§7Theres a new Plugin Version §aavailable§7! You run on version : §c"
							+ plugin.getDescription().getVersion() + " §8-> §7new version : §a" + version);
					isLatest = version;
				} else {
					isLatest = "LATEST";
				}
			});
		}

		sql = new SQLPlayerManager();
		yml = new YMLPlayerManager();

		pmanager = new PlayerDataManager();

		getSQLPlayerManager().createtables();
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
	}

	public void setupCommands() {
		if (getMainConfig().getConfig().getBoolean("Command.HELP.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new SubHelp());
		}
		if (getMainConfig().getConfig().getBoolean("Command.LEAVE.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LeaveSub());
		}
		if (getMainConfig().getConfig().getBoolean("Command.LEAVEALL.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LeaveAllSub());
		}
		if (getMainConfig().getConfig().getBoolean("Command.POINTS.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new PointsSub());
		}
		if (getMainConfig().getConfig().getBoolean("Command.LIMIT.Enabled")) {
			getSubCommandManager().getSubCommandList().add(new LimitSub());
		}

		getAdminSubCommandManager().getSubCommandList().add(new HelpSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetMaxSub());
		getAdminSubCommandManager().getSubCommandList().add(new UpdateSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetLevelSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetPointsSub());
	}

	public void loadClasses() {
		loaded = new ArrayList<Job>();
		pm = new PlayerManager(plugin);
		ld = new HashMap<>();
		levels = new LevelAPI(plugin, messages.getConfig());

		executor = Executors.newFixedThreadPool(config.getConfig().getInt("ExecutorServiceThreads"));
		cmdmanager = new SubCommandRegistry();
		api = new JobAPI(plugin, messages.getConfig());
		gui = new de.warsteiner.jobs.manager.GuiManager(plugin);
		click = new ClickManager(plugin, config.getConfig(), gui);
		admincmdmanager = new AdminSubCommandRegistry();
		customevent = new CustomEventManager();
		notquest = new NotQuestManager();

	}

	public void doLog(LogType t, String m) {
		if (config.getConfig().getBoolean("Debug")) {
			if (t == LogType.FAILED) {
				getLogger().warning(t.getColor() + m);
			} else {
				getLogger().info(t.getColor() + m);
			}
		}

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

	public boolean isInstalledNotQuests() {
		Plugin nqPlugin = Bukkit.getServer().getPluginManager().getPlugin("NotQuests");
		if (nqPlugin != null) {
			return true;
		}
		return false;
	}

	public boolean isInstalledPlaceHolder() {
		Plugin papiPlugin = Bukkit.getServer().getPluginManager().getPlugin("PlaceHolderAPI");
		if (papiPlugin != null) {
			return true;
		}
		return false;
	}

	public boolean isInstalledWorldGuard() {
		Plugin wgPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (wgPlugin != null) {
			return true;
		}
		return false;
	}

	public CustomEventManager getEventManager() {
		return customevent;
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
