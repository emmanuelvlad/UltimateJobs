package de.warsteiner.jobs.manager;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.LogType;

public class PlayerManager {

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private HashMap<String, String> players = new HashMap<String, String>();

	public PlayerManager(UltimateJobs main) {
		plugin = main;
	}

	public HashMap<String, JobsPlayer> getOnlineJobPlayers() {
		return this.pllist;
	}

	public void startSave() {

		new BukkitRunnable() {

			public void run() {

				plugin.getExecutor().execute(() -> {

					for (Player p : Bukkit.getOnlinePlayers()) {
						if (players.get(p.getName()) != null) {
							JobsPlayer jb = pllist.get("" + p.getUniqueId());
							plugin.getSQLManager().savePlayer(jb, "" + p.getUniqueId());
							UltimateJobs.getPlugin().doLog(LogType.SAVED, "Auto-Save executed");
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

			UltimateJobs.getPlugin().doLog(LogType.REMOVED, "Removed Data of "+uuid+" from cache");
		});
	}
 

	public boolean existInCache(String name) {
		UltimateJobs.getPlugin().doLog(LogType.CHECK, "Check for Data in Cache of : "+name);
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

			ArrayList<String> owned = s.getOwnedJobs("" + UUID);
			ArrayList<String> current = s.getCurrentJobs("" + UUID);

			for (String j : owned) {

				levels.put(j, s.getLevelOf("" + UUID, j));
				broken.put(j, s.getBrokenOf("" + UUID, j));
				exp.put(j, s.getExpOf("" + UUID, j));
				date.put(j, s.getDateOf("" + UUID, j));

			}

			JobsPlayer jp = new JobsPlayer(name, current, owned, levels, exp, broken, s.getPoints("" + UUID), date,
					s.getMaxJobs("" + UUID));

			pllist.put("" + UUID, jp);
			players.put(name, "" + UUID);
			UltimateJobs.getPlugin().doLog(LogType.LOADED, "Loaded Data from : "+name+" : "+UUID);
		});
	}

	public void updateJobs(String j, JobsPlayer pl, String UUID) {
		plugin.getExecutor().execute(() -> {

			SQLManager s = UltimateJobs.getPlugin().getSQLManager();

			if(!s.ExistJobData(UUID, j)) {
				s.createJobData(UUID, j);

				HashMap<String, Integer> levels = pl.getLevels();
				HashMap<String, Integer> broken = pl.getBrokenList();
				HashMap<String, Double> exp = pl.getExpList();
				HashMap<String, String> date = pl.getDateList();

				levels.put(j, s.getLevelOf("" + UUID, j));
				broken.put(j, s.getBrokenOf("" + UUID, j));
				exp.put(j, s.getExpOf("" + UUID, j));
				date.put(j, s.getDateOf("" + UUID, j));
				UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Jobs of : "+UUID);
			}
		});
	}

}
