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
	
	private List<String> defaultlanguages = Arrays.asList("en-US","de-DE","de-BAR","es-ES","fr-FR");
	
	public  boolean generateFiles() {
		createGUIFile();
		createDefaultLanguages();
		createCMDFile();
		createHelpGUIFile();
		createSettingsGUIFile();
		createConfirmGUIFIle();
		createCFGFile();
	 
		//addons
		createAddonStatsFiles();
		createAddonRewardsFiles(); 
		return true;
	}
	
	public  boolean reloadFiles() {
		try {
			gui = YamlConfiguration.loadConfiguration(gui_file);
			cfg = YamlConfiguration.loadConfiguration(cfg_file);
			cmd = YamlConfiguration.loadConfiguration(cmd_file);
			help = YamlConfiguration.loadConfiguration(help_file);
			settings = YamlConfiguration.loadConfiguration(settings_file);
			confirm = YamlConfiguration.loadConfiguration(confirm_file);
			stats = YamlConfiguration.loadConfiguration(stats_file);
			rewards = YamlConfiguration.loadConfiguration(rewards_file); 
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
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
 
	public  boolean reloadGUIFile() {
		try {
			gui = YamlConfiguration.loadConfiguration(gui_file);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
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
	
	public boolean createAddonRewardsFiles() {
		rewards_file = new File(UltimateJobs.getPlugin().getDataFolder(), "addons" + File.separatorChar + "rewards.yml");
        if (!rewards_file.exists()) {
        	rewards_file.getParentFile().mkdirs();
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "rewards.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("addons" + File.separatorChar + "stats.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "confirm_gui.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "settings_gui.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "help_gui.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "command.yml", false);
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
        	UltimateJobs.getPlugin().saveResource("settings" + File.separatorChar + "main_gui.yml", false);
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
