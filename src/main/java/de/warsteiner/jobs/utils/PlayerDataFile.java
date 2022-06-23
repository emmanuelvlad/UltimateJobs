package de.warsteiner.jobs.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
 
public class PlayerDataFile {

	public String name;
	
	public PlayerDataFile(String file) {
		this.name = file;
	}
	
	public File cfile;
	public FileConfiguration config;

	public String location = "plugins/UltimateJobs/data/";
	public UltimateJobs plugin = UltimateJobs.getPlugin();

	public String getName() {
		return name;
	}
	
	public void create() {
		if (getfile() != null && !this.plugin.getDataFolder().exists())
			this.plugin.getDataFolder().mkdir();
		if (!getfile().exists()) {
			try {
				getfile().createNewFile();
			} catch (Exception e) { 
			}
		}
		this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.cfile);
	}

	public File getfile() {
		this.cfile = new File(this.location, name+".json");
		if (this.cfile != null) {
			return this.cfile;
		}
		return null;
	}

	public void load() {
		this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.cfile);
	}

	public FileConfiguration get() {
		return this.config;
	}

	public void save() {
		try {
			this.config.save(getfile());
		} catch (Exception e) { 
		}
	}
	
}