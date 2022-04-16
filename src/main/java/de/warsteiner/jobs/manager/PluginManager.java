package de.warsteiner.jobs.manager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.Language;
import de.warsteiner.jobs.UltimateJobs;

public class PluginManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin();

	public boolean isInstalled(String plugin) {
		Plugin Plugin = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (Plugin != null) {
			return true;
		}
		return false;
	}

	public String getDate() {
		DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
		Date data = new Date();
		return format.format(data);
	}

	private HashMap<String, Language> langs = new HashMap<String, Language>();
	private ArrayList<Language> arraylangs = new ArrayList<Language>();

	public ArrayList<Language> getLanguagesAsArray() {
		return arraylangs;
	}

	public HashMap<String, Language> getLanguages() {
		return langs;
	}

	public Language getLanguageFromID(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);

			if (l.getID().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}

	public Language getLanguage(String id) {
		for (int i = 0; i < langs.size(); i++) {
			Language l = arraylangs.get(i);

			if (l.getName().toLowerCase().equalsIgnoreCase(id.toLowerCase())) {
				return l;
			}
		}
		return null;
	}

	public String get(UUID UUID, String path) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		String found = null;
		if (langs.get(lang).getConfig().getString(path) == null) {
			found = path;
		} else {
			found = langs.get(lang).getConfig().getString(path);
		}
		return SimpleAPI.getPlugin().getAPI().toHex(found).replaceAll("<prefix>", getPrefix(UUID)).replaceAll("&", "§");

	}

	public String getAMessage(UUID UUID, String path) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		String found = null;
		if (langs.get(lang).getConfig().getString(path) == null) {
			found = path;
		} else {
			found = langs.get(lang).getConfig().getString(path);
		}
		return SimpleAPI.getPlugin().getAPI().toHex(found).replaceAll("<prefix>", getPrefix(UUID)).replaceAll("&", "§");

	}

	public String getFromPath(UUID UUID, String what) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		String found = null;

		if (what.contains("}")) {
			String[] got = what.split(":");

			String path = got[1].replaceAll("}", " ").replaceAll(" ", "");

			if (langs.get(lang).getConfig().getString(path) == null) {
				found = what;
			} else {
				found = langs.get(lang).getConfig().getString(path);
			}
		} else {
			found = what;
		}
		return SimpleAPI.getPlugin().getAPI().toHex(found).replaceAll("<prefix>", getPrefix(UUID)).replaceAll("&", "§");

	}

	public List<String> getListFromPath(UUID UUID, String what) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		List<String> found = null;

		if (what.contains("}")) {
			String[] got = what.split(":");

			String path = got[1].replaceAll("}", " ").replaceAll(" ", "");

			if (langs.get(lang).getConfig().getStringList(path) == null) {
				found = null;
			} else {
				found = langs.get(lang).getConfig().getStringList(path);
			}
		} else {
			found = null;
		}
		return found;

	}

	public List<String> getListFromLang(UUID UUID, String path) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		return langs.get(lang).getConfig().getStringList(path);

	}

	public String getPrefix(UUID UUID) {

		String lang = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).getLanguage().getName();
		String prefix = langs.get(lang).getConfig().getString("prefix");
		return SimpleAPI.getPlugin().getAPI().toHex(prefix).replaceAll("&", "§");

	}

	public void loadLanguages() {
		File dataFolder = new File("plugins/UltimateJobs/lang/");
		File[] files = dataFolder.listFiles();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				File file = files[i];
				UltimateJobs.getPlugin().getLogger().info("§cChecking Language : " + name + "...");

				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				if (cfg.contains("prefix")) {
					String newname = name.replaceAll(".yml", " ").replaceAll(" ", "");
					Language lg = new Language(newname, cfg, file, cfg.getString("LanguageName"), null, null);
					arraylangs.add(lg);
					langs.put(newname, lg);
					UltimateJobs.getPlugin().getLogger().info("§aLoaded Language : " + newname + "...");
				}
			}
		}
	}

}
