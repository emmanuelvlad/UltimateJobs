package de.warsteiner.jobs.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.objects.Language;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerAPI {

	private UltimateJobs plugin;

	private HashMap<String, JobsPlayer> pllist = new HashMap<String, JobsPlayer>();
	private ArrayList<String> players = new ArrayList<String>();

	public PlayerAPI(UltimateJobs main) {
		plugin = main;
	}

	public HashMap<String, JobsPlayer> getCacheJobPlayers() {
		return pllist;
	}

	public JobsPlayer getRealJobPlayer(String ID) {
		return getCacheJobPlayers().get(ID);
	}

	public void startSave() {

		new BukkitRunnable() {

			public void run() {

				plugin.getExecutor().execute(() -> {
					PlayerDataAPI pl = UltimateJobs.getPlugin().getPlayerDataAPI();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (players.contains("" + p.getUniqueId())) {
							JobsPlayer jb = pllist.get("" + p.getUniqueId());
							pl.savePlayer(jb, "" + p.getUniqueId());
						}
					}
				});

			}
		}.runTaskTimer(plugin, 0, 20 * plugin.getFileManager().getConfig().getInt("Cache_Saved_Every"));
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

			if (existInCacheByUUID("" + UUID)) {
				removePlayerFromCache("" + UUID);
			}

			PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

			ArrayList<String> owned = plm.getOwnedJobs("" + UUID);
			ArrayList<String> current = plm.getCurrentJobs("" + UUID);

			HashMap<String, JobStats> stats = new HashMap<String, JobStats>();

			for (String loading : owned) {

				Job j = plugin.getJobCache().get(loading);

				int level = plm.getLevelOf("" + UUID, loading);
				double exp = plm.getExpOf("" + UUID, loading);
				String date = plm.getDateOf("" + UUID, loading);
				int broken = plm.getBrokenOf("" + UUID, loading);

				double earned = plm.getEarnedAt("" + UUID, loading, plugin.getPluginManager().getDate());

				HashMap<String, Double> listedofearned = new HashMap<String, Double>();

				listedofearned.put(plugin.getPluginManager().getDate(), earned);

				HashMap<String, Double> money = new HashMap<String, Double>();
				HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

				for (JobAction action : j.getActionList()) {
					for (String id : j.getNotRealIDSListByAction(action)) {

						double moneyearned = plm.getEarnedOfBlock("" + UUID, loading, id, "" + action);
						int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, loading, id, "" + action);

						money.put(id, moneyearned);
						broken2.put(id, brokentimes);

					}

				}

				JobStats sz = new JobStats(j, j.getConfigID(), exp, level, broken, date, money, broken2,
						listedofearned);

				stats.put(loading, sz);
			}

			Language api = SimpleAPI.getPlugin().getPlayerAPI().getPluginPlayer(UUID).getLanguage();
			
			Language langusged = plugin.getLanguageAPI().getLanguages().get(api.getName());
			
			JobsPlayer jp = new JobsPlayer(name, current, owned, plm.getPoints("" + UUID),

					plm.getMaxJobs("" + UUID), "" + UUID, UUID,
					langusged, stats);

			pllist.put("" + UUID, jp);
			players.add("" + UUID);
		});
	}

	public void updateJobs(String j, JobsPlayer pl, String UUID) {
		plugin.getExecutor().execute(() -> {

			PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

			if (!plm.ExistJobData(UUID, j)) {
				plm.createJobData(UUID, j);

				Job real = plugin.getJobCache().get(j);

				int level = plm.getLevelOf("" + UUID, j);
				double exp = plm.getExpOf("" + UUID, j);
				String date = plm.getDateOf("" + UUID, j);
				int broken = plm.getBrokenOf("" + UUID, j);

				double earned = plm.getEarnedAt("" + UUID, j, plugin.getPluginManager().getDate());

				HashMap<String, Double> listedofearned = new HashMap<String, Double>();

				listedofearned.put(plugin.getPluginManager().getDate(), earned);

				HashMap<String, Double> money = new HashMap<String, Double>();
				HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

				for (JobAction action : real.getActionList()) {
					for (String id : real.getNotRealIDSListByAction(action)) {

						double moneyearned = plm.getEarnedOfBlock("" + UUID, j, id, "" + action);
						int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, j, id, "" + action);

						money.put(id, moneyearned);
						broken2.put(id, brokentimes);

					}

				}

				JobStats sz = new JobStats(real, real.getConfigID(), exp, level, broken, date, money, broken2,
						listedofearned);

				pl.getStatsList().put(real.getConfigID(), sz);

			}

		});
	}

}
