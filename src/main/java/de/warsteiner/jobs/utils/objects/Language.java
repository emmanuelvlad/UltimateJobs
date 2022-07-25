package de.warsteiner.jobs.utils.objects;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
 
public class Language {
	
	private String name;
	private YamlConfiguration cfg;
	private File file;
	private String id;
	private String icon;
	private String display;
	private int data;
	
	public Language(String name, YamlConfiguration cfg, File file, String id, String icon, String display, int data)  {
		this.name = name;
		this.cfg = cfg;
		this.id = id;
		this.file = file;
		this.icon = icon;
		this.display = display;
		this.data = data;
	}
	
	public int getModelData() {
		return data;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public String getIcon() {
		return icon;
	}
 	
	public String getID() {
		return id;
	}
	
	public File getFile() {
		return file;
	}

	public YamlConfiguration getConfig() {
		return cfg;
	}
	

	public String getStringFromLanguage(UUID UUID, String path) {  
		if(cfg.getString(path) == null) {
			Bukkit.getConsoleSender().sendMessage("§4Missing Element from Path: "+path+" in Config "+name+" located in "+getFile().getPath());
			return null;
		}
		return UltimateJobs.getPlugin().getPluginManager().toHex(cfg.getString(path)).replaceAll("<prefix>", getPrefix(UUID)).replaceAll("&", "§");

	}

	public String getStringFromPath(UUID UUID, String what) {
 
		String found = null;

		if (what.contains("}")) {
			String[] got = what.split(":");

			String path = got[1].replaceAll("}", " ").replaceAll(" ", "");

			if (cfg.getString(path) == null) {
				found = what;
			} else {
				found = cfg.getString(path);
			}
		} else {
			found = what;
		}
		return UltimateJobs.getPlugin().getPluginManager().toHex(found).replaceAll("<prefix>", getPrefix(UUID)).replaceAll("&", "§");

	}

	public List<String> getListFromPath(UUID UUID, String what) {
 
		List<String> found = null;

		if (what.contains("}")) {
			String[] got = what.split(":");

			String path = got[1].replaceAll("}", " ").replaceAll(" ", "");

			if (cfg.getStringList(path) == null) {
				found = null;
			} else {
				found =cfg.getStringList(path);
			}
		} else {
			found = null;
		}
		return found;

	}

	public List<String> getListFromLanguage(UUID UUID, String path) {
 
		return cfg.getStringList(path);

	}

	public String getPrefix(UUID UUID) { 
		String prefix = cfg.getString("prefix");
		return UltimateJobs.getPlugin().getPluginManager().toHex(prefix).replaceAll("&", "§");

	}
	
	public String getName() {
		return name;
	}
	
}
