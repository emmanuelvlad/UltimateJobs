package de.warsteiner.jobs.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlConfigFile {
	private final Plugin plugin;
	private final File configFile;
	private YamlConfiguration config;
	private boolean copyDefaults = false;

	public YamlConfigFile(Plugin plugin, File configFile) {
		this.plugin = plugin;
		this.configFile = configFile;
	}

	private void createIfNotExists() throws IOException {
		if (!this.configFile.exists() || this.configFile.isDirectory()) {
			try {
				this.plugin.saveResource(getName(), false);
			} catch (IllegalArgumentException exception) {
				Validate.isTrue(this.configFile.createNewFile(), "Unable to create new file");
			}
		}
	}

	public void load() throws IOException {
		createIfNotExists();
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
		this.config.options().copyDefaults(this.copyDefaults);
	}

	public void save() throws IOException {
		createIfNotExists();
		this.config.save(this.configFile);
	}

	public void setCopyDefaults(boolean copyDefaults) {
		this.copyDefaults = copyDefaults;
	}

	public File getConfigFile() {
		return this.configFile;
	}

	public YamlConfiguration getConfig() {
		return this.config;
	}

	public String getName() {
		return this.configFile.getName();
	}
}
