package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer; 

public class PlayerManager {

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private ArrayList<String> players = new ArrayList<String>();

	public PlayerManager(UltimateJobs main) {
		plugin = main;
	}

	public HashMap<String, JobsPlayer> getOnlineJobPlayers() {
		return pllist;
	}

	public void startSave() {

		new BukkitRunnable() {

			public void run() {

				plugin.getExecutor().execute(() -> {
					PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (players.contains(""+p.getUniqueId())) {
							JobsPlayer jb = pllist.get("" + p.getUniqueId());
							pl.savePlayer(jb, "" + p.getUniqueId()); 
						}
					}
				});

			}
		}.runTaskTimer(plugin, 0, 20 * plugin.getMainConfig().getConfig().getInt("Cache_Saved_Every"));
	}

	public void removePlayerFromCache(String uuid) {
		plugin.getExecutor().execute(() -> {

			players.remove(uuid);
			pllist.remove("" + uuid);
 
		});
	}

	public boolean existInCacheByUUID(String uuid) { 
		return this.players.contains(uuid);
	}

	public void loadData(String name, UUID UUID) {

		plugin.getExecutor().execute(() -> {

			if (existInCacheByUUID(""+UUID)) {
				removePlayerFromCache(""+UUID);
			}

			PlayerDataManager plm = UltimateJobs.getPlugin().getPlayerDataModeManager();

			HashMap<String, Integer> levels = new HashMap<String, Integer>();
			HashMap<String, Double> exp = new HashMap<String, Double>();
			HashMap<String, Integer> broken = new HashMap<String, Integer>();
			HashMap<String, String> date = new HashMap<String, String>();

			ArrayList<String> owned = plm.getOfflinePlayerOwnedJobs("" + UUID);
			ArrayList<String> current = plm.getOfflinePlayerCurrentJobs("" + UUID);

			for (String j : owned) {

				levels.put(j, plm.getLevelOf("" + UUID, j));
				broken.put(j, plm.getBrokenOf("" + UUID, j));
				exp.put(j, plm.getExpOf("" + UUID, j));
				date.put(j, plm.getDateOf("" + UUID, j));

			}

			JobsPlayer jp = new JobsPlayer(name, current, owned, levels, exp, broken, plm.getPoints("" + UUID), date,
					plm.getMax("" + UUID));
			 
			pllist.put("" + UUID, jp);
			players.add(""+UUID); 
		});
	}

	public void updateJobs(String j, JobsPlayer pl, String UUID) {
		plugin.getExecutor().execute(() -> {

			PlayerDataManager plm = UltimateJobs.getPlugin().getPlayerDataModeManager();

			if (!plm.ExistJobData(UUID, j)) {
				plm.createJobData(UUID, j);

				HashMap<String, Integer> levels = pl.getLevels();
				HashMap<String, Integer> broken = pl.getBrokenList();
				HashMap<String, Double> exp = pl.getExpList();
				HashMap<String, String> date = pl.getDateList();

				levels.put(j, plm.getLevelOf("" + UUID, j));
				broken.put(j, plm.getBrokenOf("" + UUID, j));
				exp.put(j, plm.getExpOf("" + UUID, j));
				date.put(j, plm.getDateOf("" + UUID, j)); 
			}
		});
	}

}
