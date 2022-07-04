package de.warsteiner.jobs.manager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
 
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;

public class FileManager {
	
	private  FileConfiguration gui;
	private  File gui_file;
	
	private  FileConfiguration cfg;
	private  File cfg_file;
 
	private  FileConfiguration cmd;
	private  File cmd_file;
	
	private  FileConfiguration help;
	private  File help_file;
	
	private  FileConfiguration settings;
	private  File settings_file;
	
	private  FileConfiguration confirm;
	private  File confirm_file;
  
	private  FileConfiguration stats;
	private  File stats_file;
	
	private  FileConfiguration rewards;
	private  File rewards_file;
	
	private  FileConfiguration levels;
	private  File levels_file;
	
	private  FileConfiguration earnings_all;
	private  File earnings_all_file;
	
	private  FileConfiguration earnings_job;

	private  File earnings_job_file;
	
	private  FileConfiguration data;
	private  File data_file;
	
	private  FileConfiguration lang;
	private  File lang_file;
	
	private  FileConfiguration langg;
	private  File langg_file;
	
	private  FileConfiguration with;
	private  File with_file;
	
	private  FileConfiguration withc;
	private  File withc_file;
	
	private  FileConfiguration cleave;
	private  File cleave_file;
	
	//"es-ES","fr-FR","nl-NL","tr-TR", "de-DE","de-BAR"
	private List<String> defaultlanguages = Arrays.asList("en-US");
	private List<String> defaultjobs = Arrays.asList("Miner","Lumberjack","FarmGrow","Digger", "Killer","Fishman","Milkman"
			, "Crafter", "Shear","Advancements","Eat","Honey","Tame","MythicMobs","Breed"
			, "Berrys","Saplings","KillBow","TNT");
	
	public  boolean generateFiles(boolean d) {
		createGUIFile();
		createDefaultLanguages();
		createCMDFile();
		createHelpGUIFile();
		createSettingsGUIFile();
		createConfirmGUIFIle();
		createCFGFile();
		createLevelsFile();
		createEarningsJobFile();
		createDataFile();
		createLangFile();
		createLangGUIFile();
		createWithFile();
		createWithConfirmFile();
		createLeaveConfirmFile();
 
		//addons
		createAddonStatsFiles();
		createAddonRewardsFiles(); 
		createEarningsAllFile();
		
		if(d) {
			UltimateJobs.getPlugin().getLanguageAPI().loadLanguages();
		}
		
		return true;
	}
	
	public  void reloadAllFiles() {
		try {
			gui = YamlConfiguration.loadConfiguration(gui_file);
			cfg = YamlConfiguration.loadConfiguration(cfg_file);
			cmd = YamlConfiguration.loadConfiguration(cmd_file);
			help = YamlConfiguration.loadConfiguration(help_file);
			settings = YamlConfiguration.loadConfiguration(settings_file);
			confirm = YamlConfiguration.loadConfiguration(confirm_file);
			stats = YamlConfiguration.loadConfiguration(stats_file);
			rewards = YamlConfiguration.loadConfiguration(rewards_file); 
			levels = YamlConfiguration.loadConfiguration(levels_file); 
			earnings_all = YamlConfiguration.loadConfiguration(earnings_all_file); 
			earnings_job = YamlConfiguration.loadConfiguration(earnings_job_file); 
			lang = YamlConfiguration.loadConfiguration(lang_file); 
			data = YamlConfiguration.loadConfiguration(data_file); 
			langg = YamlConfiguration.loadConfiguration(langg_file); 
			with = YamlConfiguration.loadConfiguration(with_file); 
			withc = YamlConfiguration.loadConfiguration(withc_file); 
		} catch(Exception ex) {
			ex.printStackTrace(); 
		}
	}
	
	public void createDefaultJobs() { 
		for(String b : defaultjobs) {
			File f = new File(UltimateJobs.getPlugin().getDataFolder(), "jobs" + File.separatorChar + b+".yml");
	        if (!f.exists()) {
	        	f.getParentFile().mkdirs();
	        	UltimateJobs.getPlugin().saveResource("jobs" + File.separatorChar + b+".yml", false);
	         }
		}
	}
	
	public void createDefaultLanguages() { 
		for(String b : defaultlanguages) {
			File f = new File(UltimateJobs.getPlugin().getDataFolder(), "lang" + File.separatorChar + b+".yml");
	        if (!f.exists()) {
	        	f.getParentFile().mkdirs();
	        	UltimateJobs.getPlugin().saveResource("lang" + File.separatorChar + b+".yml", false);
	         }
		}
	}
 
	/*
	 * file
	 */
	public  FileConfiguration getGUI() {
		return gui;
	}
	
	public  File getGUIFile() {
		return gui_file;
	}
	
 
	public  FileConfiguration getCMDSettings() {
		return cmd;
	}
	
	public  File getCMDFile() {
		return cmd_file;
	}
	
	public  FileConfiguration getHelpSettings() {
		return help;
	}
	
	public  File getHelpFile() {
		return help_file;
	}
	
	public  FileConfiguration getSettings() {
		return settings;
	}
	
	public  File getSettingsFile() {
		return settings_file;
	}
	
	public  FileConfiguration getConfirm() {
		return confirm;
	}
	
	public  File getConfirmFile() {
		return confirm_file;
	}
	
	public  FileConfiguration getConfig() {
		return cfg;
	}
	
	public  File getConfigFile() {
		return cfg_file;
	}
	
	public  FileConfiguration getStatsConfig() {
		return stats;
	}
	
	public  File getStatsFile() {
		return stats_file;
	}
	
	public  FileConfiguration getRewardsConfig() {
		return rewards;
	}
	
	public  File getRewardsFile() {
		return rewards_file;
	}
	
	public  FileConfiguration getLevelGUIConfig() {
		return levels;
	}
	
	public  File getLevelGUIFile() {
		return levels_file;
	}
	
	public  FileConfiguration getEarningsAllConfig() {
		return earnings_all;
	}
	
	public  File getEarningAllFIle() {
		return earnings_all_file;
	}
	
	public  FileConfiguration getEarningsJobConfig() {
		return earnings_job;
	}
	
	public  File getEarningJobFIle() {
		return earnings_job_file;
	}
	

	public  FileConfiguration getDataConfig() {
		return data;
	}
	
	public  File getDataFile() {
		return data_file;
	}
	
	public  FileConfiguration getLanguageConfig() {
		return lang;
	}
	
	public  File getLanguageFile() {
		return lang_file;
	}
	
	public  FileConfiguration getLanguageGUIConfig() {
		return langg;
	}
	
	public  File getLanguageGUIFile() {
		return langg_file;
	}
	
	
	public  FileConfiguration getWithdrawConfig() {
		return with;
	}
	
	public  File getWithdrawFile() {
		return with_file;
	}
	
	
	public  FileConfiguration getWithdrawConfirmConfig() {
		return withc;
	}
	
	public  File getWithdrawConfirmFile() {
		return withc_file;
	}
	
	public  FileConfiguration getLeaveConfirmConfig() {
		return cleave;
	}
	
	public  File getLeaveConfirmFile() {
		return cleave_file;
	}
	
	public boolean createLeaveConfirmFile() {
		cleave_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "leave_job_confirm_gui.yml");
        if (!cleave_file.exists()) {
        	cleave_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "leave_job_confirm_gui.yml", true);
        }
        
        cleave = new YamlConfiguration();
        try {
        	cleave.load(cleave_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createWithConfirmFile() {
		withc_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "withdraw_confirm_gui.yml");
        if (!withc_file.exists()) {
        	withc_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "withdraw_confirm_gui.yml", true);
        }
        
        withc = new YamlConfiguration();
        try {
        	withc.load(withc_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createWithFile() {
		with_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "withdraw_gui.yml");
        if (!with_file.exists()) {
        	with_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "withdraw_gui.yml", true);
        }
        
        with = new YamlConfiguration();
        try {
        	with.load(with_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLangGUIFile() {
		langg_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "lang_gui.yml");
        if (!langg_file.exists()) {
        	langg_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "lang_gui.yml", true);
        }
        
        langg = new YamlConfiguration();
        try {
        	langg.load(langg_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLangFile() {
		lang_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "languages.yml");
        if (!lang_file.exists()) {
        	lang_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "languages.yml", true);
        }
        
        lang = new YamlConfiguration();
        try {
        	lang.load(lang_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	
	public boolean createDataFile() {
		data_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "data.yml");
        if (!data_file.exists()) {
        	data_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "data.yml", true);
        }
        
        data = new YamlConfiguration();
        try {
        	data.load(data_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createEarningsJobFile() {
		earnings_job_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "earnings_job.yml");
        if (!earnings_job_file.exists()) {
        	earnings_job_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "earnings_job.yml", true);
        }
        
        earnings_job = new YamlConfiguration();
        try {
        	earnings_job.load(earnings_job_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createEarningsAllFile() {
		earnings_all_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "earnings_all.yml");
        if (!earnings_all_file.exists()) {
        	earnings_all_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "earnings_all.yml", true);
        }
        
        earnings_all = new YamlConfiguration();
        try {
        	earnings_all.load(earnings_all_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createLevelsFile() {
		levels_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "levelsgui.yml");
        if (!levels_file.exists()) {
        	levels_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "levelsgui.yml", true);
        }
        
        levels = new YamlConfiguration();
        try {
        	levels.load(levels_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createAddonRewardsFiles() {
		rewards_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "rewards.yml");
        if (!rewards_file.exists()) {
        	rewards_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "rewards.yml", true);
        }
        
        rewards = new YamlConfiguration();
        try {
        	rewards.load(rewards_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createAddonStatsFiles() {
		stats_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "stats.yml");
        if (!stats_file.exists()) {
        	stats_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "stats.yml", true);
        }
        
        stats = new YamlConfiguration();
        try {
        	stats.load(stats_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createCFGFile() {
		cfg_file = new File(UltimateJobs.getPlugin().getDataFolder(), "Config.yml");
        if (!cfg_file.exists()) {
        	cfg_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("Config.yml", false);
         }

        cfg = new YamlConfiguration();
        try {
        	cfg.load(cfg_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createConfirmGUIFIle() {
		confirm_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "confirm_gui.yml");
        if (!confirm_file.exists()) {
        	confirm_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "confirm_gui.yml", true);
         }

        confirm = new YamlConfiguration();
        try {
        	confirm.load(confirm_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public boolean createSettingsGUIFile() {
		settings_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "settings_gui.yml");
        if (!settings_file.exists()) {
        	settings_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "settings_gui.yml", true);
         }

        settings = new YamlConfiguration();
        try {
        	settings.load(settings_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
 
	public boolean createHelpGUIFile() {
		help_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "help_gui.yml");
        if (!help_file.exists()) {
        	help_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "help_gui.yml", true);
         }

        help = new YamlConfiguration();
        try {
        	help.load(help_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public  boolean createCMDFile() {
		cmd_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "command.yml");
        if (!cmd_file.exists()) {
        	cmd_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "command.yml", true);
         }

        cmd = new YamlConfiguration();
        try {
        	cmd.load(cmd_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
	public  boolean createGUIFile() {
		gui_file = new File(UltimateJobs.getPlugin().getDataFolder(), "settings" + File.separatorChar + "main_gui.yml");
        if (!gui_file.exists()) {
        	gui_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "main_gui.yml", true);
         }

        gui = new YamlConfiguration();
        try {
        	gui.load(gui_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
  
}
