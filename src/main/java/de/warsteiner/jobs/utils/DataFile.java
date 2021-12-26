package de.warsteiner.jobs.utils;

import java.io.File;
import java.io.IOException; 
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
 
public class DataFile {
	private File file;

	private FileConfiguration customFile;

	private final Plugin plugin;

	private String fileName;

	public DataFile(Plugin msg, String fileName, String path) {
		this.plugin = msg;
		this.fileName = fileName;
		setup(path);
	}

	private void setup(String path) {
		if (!this.plugin.getDataFolder().exists())
			this.plugin.getDataFolder().mkdir();
		this.file = new File(path, this.fileName + ".yml");
		if (!this.file.exists())
			try {
				this.file.createNewFile();
			 
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.customFile = (FileConfiguration) YamlConfiguration.loadConfiguration(this.file);

	}
	
	public boolean isEmpty() {
		return get().getKeys(true).size() == 0;
	}
	
	public boolean exist() { 
		return this.file.exists(); 
	}
 
	public FileConfiguration get() {
		return this.customFile;
	}

	public void save() {
		try {
			this.customFile.save(this.file);
		} catch (IOException iOException) {
		}
	}

	public void reload() {
		this.customFile = (FileConfiguration) YamlConfiguration.loadConfiguration(this.file);
	}
}
