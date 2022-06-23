package de.warsteiner.jobs.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.utils.objects.Language;
 
public class LanguageAPI {


	private HashMap<String, Language> langs = new HashMap<String, Language>();
	private ArrayList<Language> arraylangs = new ArrayList<Language>();
	
	public ArrayList<Language> getLoadedLanguagesAsArray() {
		return arraylangs;
	}
	
	public int getLanguagesAmount() {
		return langs.size();
	}

	public HashMap<String, Language> getLanguages() {
		return langs;
	}
	
	public Language getLanguageFromID(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);
			 
			if(l.getID().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}
	
	
	public Language getLanguage(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);
			 
			if(l.getName().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}
	 

	public void loadLanguages() {
		File dataFolder = new File("plugins/UltimateJobs/lang/");
		File[] files = dataFolder.listFiles();
 
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				File file = files[i]; 
				
				if(name.contains(".yml")) {
					Bukkit.getConsoleSender().sendMessage("§cChecking Language : "+name+"...");
					 
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					if(cfg.contains("prefix")) {
						String newname = name.replaceAll(".yml", " ").replaceAll(" ", "");
						Language lg = new Language(newname, cfg, file, cfg.getString("LanguageName"), cfg.getString("LanguageIcon"), cfg.getString("LanguageDisplay"));
						
						if(cfg.getBoolean("LanguageEnabled")) {
							arraylangs.add(lg);
						}
						
						langs.put(newname, lg);
						Bukkit.getConsoleSender().sendMessage("§aLoaded Language : "+newname+"...");
					}
				}
				
			}
		}
	}
	
}
