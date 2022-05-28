
package de.warsteiner.jobs;

import java.io.File; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.warsteiner.jobs.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.files.PlayerDataFile;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.LanguageAPI;
import de.warsteiner.jobs.api.LevelAPI;
import de.warsteiner.jobs.api.PlayerAPI;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.api.plugins.AlonsoLevelsManager;
import de.warsteiner.jobs.api.plugins.MoreOresManager;
import de.warsteiner.jobs.api.plugins.MythicMobsManager;
import de.warsteiner.jobs.api.plugins.NotQuestManager;
import de.warsteiner.jobs.api.plugins.PlaceHolderManager;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.command.AdminTabComplete;
import de.warsteiner.jobs.command.JobTabComplete;
import de.warsteiner.jobs.command.JobsCommand;
import de.warsteiner.jobs.command.admincommand.AddPointsSub;
import de.warsteiner.jobs.command.admincommand.HelpSub;
import de.warsteiner.jobs.command.admincommand.ReloadSub;
import de.warsteiner.jobs.command.admincommand.RemovePointsSub;
import de.warsteiner.jobs.command.admincommand.SetLevelSub;
import de.warsteiner.jobs.command.admincommand.SetMaxSub;
import de.warsteiner.jobs.command.admincommand.SetPointsSub;
import de.warsteiner.jobs.command.admincommand.VersionSub;
import de.warsteiner.jobs.command.playercommand.JoinSub;
import de.warsteiner.jobs.command.playercommand.LangSub;
import de.warsteiner.jobs.command.playercommand.LeaveAllSub;
import de.warsteiner.jobs.command.playercommand.LeaveSub;
import de.warsteiner.jobs.command.playercommand.LevelsSub;
import de.warsteiner.jobs.command.playercommand.LimitSub;
import de.warsteiner.jobs.command.playercommand.PointsSub;
import de.warsteiner.jobs.command.playercommand.RewardsSub;
import de.warsteiner.jobs.command.playercommand.StatsSub;
import de.warsteiner.jobs.inventorys.AreYouSureMenuClickEvent;
import de.warsteiner.jobs.inventorys.HelpMenuClickEvent;
import de.warsteiner.jobs.inventorys.LevelsMenuClickEvent;
import de.warsteiner.jobs.inventorys.MainMenuClickEvent;
import de.warsteiner.jobs.inventorys.RewardsMenuClickEvent;
import de.warsteiner.jobs.inventorys.SettingsMenuClickEvent;
import de.warsteiner.jobs.inventorys.StatsMenuClickEvent;
import de.warsteiner.jobs.jobs.JobActionAdvancement;
import de.warsteiner.jobs.jobs.JobActionBreak;
import de.warsteiner.jobs.jobs.JobActionBreed;
import de.warsteiner.jobs.jobs.JobActionCraft;
import de.warsteiner.jobs.jobs.JobActionDrink;
import de.warsteiner.jobs.jobs.JobActionEat;
import de.warsteiner.jobs.jobs.JobActionFarm;
import de.warsteiner.jobs.jobs.JobActionFish;
import de.warsteiner.jobs.jobs.JobActionGrowSapling;
import de.warsteiner.jobs.jobs.JobActionHoney;
import de.warsteiner.jobs.jobs.JobActionKillByBow;
import de.warsteiner.jobs.jobs.JobActionKillMob;
import de.warsteiner.jobs.jobs.JobActionMMKill;
import de.warsteiner.jobs.jobs.JobActionMilk;
import de.warsteiner.jobs.jobs.JobActionMoreOres_BreakEvent;
import de.warsteiner.jobs.jobs.JobActionPlace;
import de.warsteiner.jobs.jobs.JobActionShear;
import de.warsteiner.jobs.jobs.JobActionStripLog;
import de.warsteiner.jobs.jobs.JobActionTame;
import de.warsteiner.jobs.jobs.JobActionsCollectBerrys;
import de.warsteiner.jobs.manager.ClickManager;
import de.warsteiner.jobs.manager.FileManager;
import de.warsteiner.jobs.manager.GuiAddonManager;
import de.warsteiner.jobs.manager.GuiManager;
import de.warsteiner.jobs.manager.JobWorkManager;
import de.warsteiner.jobs.manager.PluginManager;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.JsonMessage;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommandRegistry;
import de.warsteiner.jobs.utils.playercommand.SubCommandRegistry;
import net.milkbowl.vault.economy.Economy;

public class UltimateJobs extends JavaPlugin {

	private static UltimateJobs plugin;
	private Economy econ;
	private LevelAPI levels;
	private ArrayList<String> loaded;
	private HashMap<String, Job> ld;
	private de.warsteiner.jobs.manager.GuiManager gui;
	private JobAPI api;
	private ClickManager click;
	private ExecutorService executor;
	private SubCommandRegistry cmdmanager;
	private AdminSubCommandRegistry admincmdmanager;
	private NotQuestManager notquest;
	private PlayerDataFile datafile;
	private JobWorkManager work;
	private AlonsoLevelsManager alonso;
	private PluginManager plapi;
	private FileManager filemanager;
	private GuiAddonManager adddongui;
	private MythicMobsManager mm;

	private PlayerAPI papi;
	private PlayerDataAPI dataapi;
	private JsonMessage js;
	private LanguageAPI langapi; 
	private MoreOresManager moreores;

	public void onLoad() {

		plugin = this;
		plapi = new PluginManager();
		langapi = new LanguageAPI();
		filemanager = new FileManager();

		createFolders();

		filemanager.generateFiles();

		if (this.filemanager.getConfig().getInt("ExecutorServiceThreads") == 0) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of ExecutorServices in Config.yml");
		}

		executor = Executors.newFixedThreadPool(this.filemanager.getConfig().getInt("ExecutorServiceThreads"));

		loadClasses();
		
		if (getPluginManager().isInstalled("WorldGuard")) {
			WorldGuardManager.setClass();
			WorldGuardManager.load();
		}

		if (getPluginManager().isInstalled("SimpleAPI")) {
			getExecutor().execute(() -> {
				if (filemanager.getConfig().getBoolean("CheckForUpdates")) {
					SimpleAPI.getPlugin().getWebManager().loadVersionAndCheckUpdate(
							"https://api.war-projects.com/v1/ultimatejobs/version.txt", "UltimateJobs",
							getDescription().getVersion());
				}
			});
		}

	}

	@Override
	public void onEnable() {
		
		if (SimpleAPI.getPlugin().getPluginMode().equalsIgnoreCase("SQL")) {
			getPlayerDataAPI().createtables();
		} else {
			datafile.create();
		}

		getLanguageAPI().loadLanguages();

		// basic events
		loadBasicEvents();

		// job events
		loadEvents();

		setupEconomy();
 
		api.loadJobs(getLogger());

		if (getPluginManager().isInstalled("PlaceHolderAPI")) {
			new PlaceHolderManager().register();
			Bukkit.getConsoleSender().sendMessage("§bLoaded PlaceHolderAPI Support...");
		}

		if (getPluginManager().isInstalled("NotQuests")) {
			getNotQuestManager().setClass();
			Bukkit.getConsoleSender().sendMessage("§bLoaded NotQuests Support...");
		}
		if (getPluginManager().isInstalled("AlonsoLevels")) {
			Bukkit.getConsoleSender().sendMessage("§bLoaded AlonsoLevels Support...");
		}

		getCommand("jobs").setExecutor(new JobsCommand());
		getCommand("jobs").setTabCompleter(new JobTabComplete());

		getCommand("jobsadmin").setExecutor(new AdminCommand());
		getCommand("jobsadmin").setTabCompleter(new AdminTabComplete());

		registerSubCommands();

		BossBarHandler.startSystemCheck();

		getPlayerAPI().startSave();

		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage(
				",--. ,--.,--.,--------.,--.,--.   ,--.  ,---. ,--------.,------.     ,--. ,-----. ,-----.   ,---.   ");
		Bukkit.getConsoleSender().sendMessage(
				"|  | |  ||  |'--.  .--'|  ||   `.'   | /  O  \\'--.  .--'|  .---'     |  |'  .-.  '|  |) /_ '   .-'  ");
		Bukkit.getConsoleSender().sendMessage(
				"|  | |  ||  |   |  |   |  ||  |'.'|  ||  .-.  |  |  |   |  `--, ,--. |  ||  | |  ||  .-.  \\`.  `-.  ");
		Bukkit.getConsoleSender().sendMessage(
				"'  '-'  '|  '--.|  |   |  ||  |   |  ||  | |  |  |  |   |  `---.|  '-'  /'  '-'  '|  '--' /.-'    | ");
		Bukkit.getConsoleSender().sendMessage(
				" `-----' `-----'`--'   `--'`--'   `--'`--' `--'  `--'   `------' `-----'  `-----' `------' `-----'  ");
		Bukkit.getConsoleSender().sendMessage("       §bRunning plugin UltimateJobs v" + getDescription().getVersion() + " ("
				+ getDescription().getAPIVersion() + ")");
		Bukkit.getConsoleSender().sendMessage("       §bRunning UltimateJobs with " + getLoaded().size() + " Jobs");
		Bukkit.getConsoleSender().sendMessage("       §bLoaded " + getLanguageAPI().getLanguages().size() + " Languages");
		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("§7");
 
		 
	}

	public void onDisable() {

		if (filemanager.getConfig().getBoolean("KickOnReload")) {
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

		Bukkit.getConsoleSender().sendMessage("§7");
		Bukkit.getConsoleSender().sendMessage("     §cPlugin has been disabled!");
		Bukkit.getConsoleSender().sendMessage("     §cThank you for using my plugin!");
		Bukkit.getConsoleSender().sendMessage("§7");
	}

	public void registerSubCommands() {

		getSubCommandManager().getSubCommandList().add(new de.warsteiner.jobs.command.playercommand.HelpSub());
		getSubCommandManager().getSubCommandList().add(new LeaveSub());
		getSubCommandManager().getSubCommandList().add(new LeaveAllSub());
		getSubCommandManager().getSubCommandList().add(new PointsSub());
		getSubCommandManager().getSubCommandList().add(new LimitSub());
		getSubCommandManager().getSubCommandList().add(new JoinSub());
		if (SimpleAPI.getPlugin().getFileManager().getSystemConfig().getBoolean("EnabledLanguages")) {
			getSubCommandManager().getSubCommandList().add(new LangSub());
		}
		getSubCommandManager().getSubCommandList().add(new StatsSub());
		getSubCommandManager().getSubCommandList().add(new LevelsSub());
		getSubCommandManager().getSubCommandList().add(new RewardsSub());

		getAdminSubCommandManager().getSubCommandList().add(new HelpSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetMaxSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetLevelSub());
		getAdminSubCommandManager().getSubCommandList().add(new SetPointsSub());
		getAdminSubCommandManager().getSubCommandList().add(new AddPointsSub());
		getAdminSubCommandManager().getSubCommandList().add(new RemovePointsSub());
		getAdminSubCommandManager().getSubCommandList().add(new ReloadSub());
		getAdminSubCommandManager().getSubCommandList().add(new VersionSub());
	}

	public void loadClasses() {
		loaded = new ArrayList<>();
		ld = new HashMap<>();
		levels = new LevelAPI(plugin);

		cmdmanager = new SubCommandRegistry();
		api = new JobAPI(plugin);
		gui = new GuiManager(plugin);
		adddongui = new GuiAddonManager(plugin);
		click = new ClickManager(plugin, this.filemanager.getConfig(), this.gui);
		admincmdmanager = new AdminSubCommandRegistry();
		notquest = new NotQuestManager();
		work = new JobWorkManager(plugin, this.api);
		alonso = new AlonsoLevelsManager();
		datafile = new PlayerDataFile("jobs");
		js = new JsonMessage();
		papi = new PlayerAPI(plugin);
  
		dataapi = new PlayerDataAPI();

		
	}
	
	public JsonMessage getJsonMessage() {
		return js;
	}

	public GuiAddonManager getGUIAddonManager() {
		return adddongui;
	}

	public PluginManager getPluginManager() {
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

	public LanguageAPI getLanguageAPI() {
		return langapi;
	}

	public PlayerAPI getPlayerAPI() {
		return papi;
	}

	public PlayerDataAPI getPlayerDataAPI() {
		return dataapi;
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

	public JobAPI getAPI() {
		return api;
	}

	public LevelAPI getLevelAPI() {
		return levels;
	}

	private void createFolders() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();

		}

		File folder_1 = new File(getDataFolder(), "jobs");

		if (!folder_1.exists()) {
			folder_1.mkdir();

			// create default jobs
			getFileManager().createDefaultJobs();
		}

		File folder_2 = new File(getDataFolder(), "lang");

		if (!folder_2.exists()) {
			folder_2.mkdir();
		}

		File folder_4 = new File(getDataFolder(), "settings");

		if (!folder_4.exists()) {
			folder_4.mkdir();
		}

		File folder_5 = new File(getDataFolder(), "addons");
		if (!folder_5.exists()) {
			folder_5.mkdir();
		}
	}
	
	public void loadBasicEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerExistEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockFireWorkDamage(), this);
		Bukkit.getPluginManager().registerEvents(new AreYouSureMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLevelEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerRewardCommandEvent(), this);
		Bukkit.getPluginManager().registerEvents(new HelpMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new IntegrationEvents(), this);
		Bukkit.getPluginManager().registerEvents(new StatsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new RewardsMenuClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new LevelsMenuClickEvent(), this);
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
		Bukkit.getPluginManager().registerEvents(new JobActionTame(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionStripLog(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionBreed(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionDrink(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionsCollectBerrys(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionKillByBow(), this);
		Bukkit.getPluginManager().registerEvents(new JobActionGrowSapling(), this);
		
		if (getPluginManager().isInstalled("MythicMobs")) {
			mm = new MythicMobsManager();
			Bukkit.getPluginManager().registerEvents(new JobActionMMKill(), this);
			Bukkit.getConsoleSender().sendMessage("§bLoaded Support for MythicMobs");
		}
	 
		if (getPluginManager().isInstalled("MoreOres")) {
			moreores = new MoreOresManager();
			Bukkit.getPluginManager().registerEvents(new JobActionMoreOres_BreakEvent(), this);
			Bukkit.getConsoleSender().sendMessage("§bLoaded Support for MoreOres by Hello1231");
		}  
		
	}
	
	public MoreOresManager getMoreOresManager() {
		return moreores;
	}
 
	public MythicMobsManager getMythicMobsManager() {
		return mm;
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (economyProvider != null) {
			econ = (Economy) economyProvider.getProvider();
			Bukkit.getConsoleSender().sendMessage("§aVault was loaded for UltimateJobs!");
		} else {
			Bukkit.getConsoleSender().sendMessage("§cFailed to load Vault for UltimateJobs!");
		}
		return (econ != null);
	}

	public Economy getEco() {
		return econ;
	}

	public HashMap<String, Job> getJobCache() {
		return ld;
	}

	public FileManager getFileManager() {
		return filemanager;
	}

	public ArrayList<String> getLoaded() {
		return loaded;
	}

}
