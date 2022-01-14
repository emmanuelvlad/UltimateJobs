package de.warsteiner.jobs.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.SQLManager;

public class PlayerManager {

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private HashMap<String, String> players = new HashMap<String, String>();

	public PlayerManager(UltimateJobs main) {
		plugin = main;
	}

	public HashMap<String, JobsPlayer> getJonPlayers() {
		return this.pllist;
	}

	public void startSave() {

		new BukkitRunnable() {

			public void run() {

				plugin.getExecutor().execute(() -> {
					
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (players.get(p.getName()) != null) {
							JobsPlayer jb = pllist.get(""+p.getUniqueId());
							plugin.getSQLManager().savePlayer(jb, ""+p.getUniqueId());
						}
					} 
				});

			}
		}.runTaskTimer(plugin, 0, 20 * plugin.getMainConfig().getConfig().getInt("Cache_Saved_Every"));
	}
	
	public void removePlayerFromCache(UUID uuid, String name) {
		plugin.getExecutor().execute(() -> {

			players.remove(name);
			pllist.remove("" + uuid);

			plugin.doLog("INFO", "§eRemoved Data from player " + name + " with UUID: " + uuid);
		});
	}

	public boolean isInAnyExist(String name) {
		SQLManager dt = UltimateJobs.getPlugin().getSQLManager();
		if (players.get(name) != null) {
			return true;
		}
		if (dt.getUUIDFromName(name) != null) {
			if (players.get(name) == null) {
				loadData(name, UUID.fromString(dt.getUUIDFromName(name).toString()));
				plugin.doLog("INFO", "§bLoaded Data from Offlineplayer " + name);
			}
			return true;
		}
		return false;
	}

	public boolean existInCache(String name) {
		return this.players.containsKey(name);
	}

	public void loadData(String name, UUID UUID) { 
		SQLManager s = UltimateJobs.getPlugin().getSQLManager();

		plugin.getExecutor().execute(() -> {

			if (existInCache(name)) {
				removePlayerFromCache(UUID, name);
			}

			HashMap<String, Integer> levels = new HashMap<String, Integer>();
			HashMap<String, Double> exp = new HashMap<String, Double>();
			HashMap<String, Integer> broken = new HashMap<String, Integer>();
			HashMap<String, String> date = new HashMap<String, String>();

			List<String> owned = s.getOwnedJobs("" + UUID);
			List<String> current = s.getCurrentJobs("" + UUID);
	 
			for (String j : owned) {

				levels.put(j, s.getLevelOf("" + UUID, j));
				broken.put(j, s.getBrokenOf("" + UUID, j));
				exp.put(j, s.getExpOf("" + UUID, j));
				date.put(j, s.getDateOf("" + UUID, j));

			}

			JobsPlayer jp = new JobsPlayer(name, current, owned, levels, exp, broken, s.getPoints("" + UUID),
					date, s.getMaxJobs(""+UUID));

			pllist.put("" + UUID, jp);
			players.put(name, "" + UUID);
			plugin.doLog("INFO", "§bLoaded Data of UUID: " + UUID);
		});
	}

	public void updateJobs(String j, JobsPlayer pl, String UUID) { 
		plugin.getExecutor().execute(() -> {
			
			SQLManager s = UltimateJobs.getPlugin().getSQLManager();
			
			s.createJobData(UUID, j);
			 
			HashMap<String, Integer> levels = pl.getLevels();
			HashMap<String, Integer> broken = pl.getBrokenList();
			HashMap<String, Double> exp = pl.getExpList();
			HashMap<String, String> date = pl.getDateList();

			levels.put(j, s.getLevelOf("" + UUID, j));
			broken.put(j, s.getBrokenOf("" + UUID, j));
			exp.put(j, s.getExpOf("" + UUID, j));
			date.put(j, s.getDateOf("" + UUID, j));
			plugin.doLog("INFO", "§bUpdated Data of UUID: " + UUID + " of Job : " + j);
		});
	}

}
