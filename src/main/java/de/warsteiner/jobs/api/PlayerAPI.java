package de.warsteiner.jobs.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.manager.GuiOpenManager;
import de.warsteiner.jobs.utils.JobAction; 
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;

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
	
	public JobsPlayer getRealJobPlayer(UUID ID) {
		return getCacheJobPlayers().get(""+ID);
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
 
	public ArrayList<String> getOnlinePlayersInJob(Job job) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		for(Player p :  Bukkit.getOnlinePlayers()) {
			if(getRealJobPlayer(p.getUniqueId()) != null) {
				
				if(getRealJobPlayer(p.getUniqueId()).getCurrentJobs().contains(job.getConfigID())) {
					list.add(""+p.getUniqueId());
				}
				
			}
		}
		
		return list;
		
	}

	public double getEarnedAtDateFromAllJobs(String uuid, String date) {

		double f = 0;

		for (String job : getOwnedJobs(uuid)) {

			Job j = plugin.getJobCache().get(job);

			double earned = getEarnedAt(uuid, j, date);

			f = f + earned;
		}

		return f;

	}

	public boolean existInCacheByUUID(String uuid) {
		return this.players.contains(uuid);
	}
	
	public void executeCustomEvent(String UUID, String job, boolean online) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(online) { 
						JobsPlayer f = getRealJobPlayer(UUID);

						if (Bukkit.getPlayer(f.getUUID()) != null) {
					
							Player player = Bukkit.getPlayer(f.getUUID()); 
							if (player.getOpenInventory() != null) {

								InventoryView iv = player.getOpenInventory();
								
								if(iv.getTitle() != null) { 
									String title = iv.getTitle();
									 
									GuiOpenManager d = plugin.getGUIOpenManager();
									
									if(d.isMainOpend(player, title) != null) {
										
										 plugin.getGUI().UpdateMainInventoryItems(player, title);
										
									} else if(d.isLanguageOpend(player, title) != null) {
										 plugin.getGUI().updateLanguageInventory(player, title, f);
									} else if(d.isEarningsAboutJob(player, title) != null) {
										plugin.getGUIAddonManager().updateEarningsGUI_Single_Job(player, title, f, plugin.getGUIOpenManager().isEarningsAboutJob(player, title));
									} else if(d.isEarningsALL(player, title) != null) {
										plugin.getGUIAddonManager().updateEarningsGUI_All(player, title, f);
									} else if(d.isWithdrawMenu(player, title) != null) {
										plugin.getGUIAddonManager().updateWithdrawGUI(player, title, f);
									} else if(d.isLevelsMenu(player, title) != null) {
										plugin.getGUIAddonManager().updateLevelsGUI(player, title, f, plugin.getGUIOpenManager().isLevelsMenu(player, title));
									} else if(d.isRewardsMenu(player, title) != null) {
										plugin.getGUIAddonManager().updateRewardsGUI(player, title, f, plugin.getGUIOpenManager().isRewardsMenu(player, title));
									} else if(d.isStatsMenuOpendSelf(player, title) != null) {
										plugin.getGUIAddonManager().updateSelfUpdateGUI(player, title, f);
									} else if(d.isStatsMenuOpendAboutPlayer(player, title) != null) {
										plugin.getGUIAddonManager().updateOtherStatsGUI(player, title, f, plugin.getGUIOpenManager().isStatsMenuOpendAboutPlayer(player, title));
									} else if(d.isSettingsMenu(player, title) != null) {
										 plugin.getGUI().UpdateSettingsGUI(player, title, plugin.getGUIOpenManager().isSettingsMenu(player, title));
									}
									
								}
							}
						 
						}

					}
 
			}
		}.runTaskLater(plugin, 2);
	}
  
	public void updateSalary(String UUID, double d) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).updateCacheSalary(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerDataAPI().updateSalary(UUID, d);
		}
	}
	
	public void updateSalaryDate(String UUID, String d) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).updateCacheSalaryDate(d);
			executeCustomEvent(UUID, null, true);
		} else {
			plugin.getPlayerDataAPI().updateSalaryDate(UUID, d);
		}
	}
	

	public double getSalary(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getSalary();
		} else {
			return plugin.getPlayerDataAPI().getSalary(uuid);
		}
	}

	public void updateDateJoinedOfJob(String UUID, String job, String date) {
		if (existInCacheByUUID(UUID)) {
			getCacheJobPlayers().get(UUID).getStatsOf(job).updateCacheJoinedDate(date);
			executeCustomEvent(UUID, job, true);
		} else {
			plugin.getPlayerDataAPI().updateDateJoinedOfJob(UUID, job, date);
		}
	}
 
	public ArrayList<String> getOwnedJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getOwnJobs();
		} else {
			return plugin.getPlayerDataAPI().getOwnedJobs(uuid);
		}
	}

	public ArrayList<String> getCurrentJobs(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getCurrentJobs();
		} else {
			return plugin.getPlayerDataAPI().getCurrentJobs(uuid);
		}
	}

	public double getExpOf(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getExp();
		} else {
			return plugin.getPlayerDataAPI().getExpOf(uuid, job.getConfigID());
		}
	}
 
	public int getBrokenTimesOfID(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0;
			}

			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenTimesOf(id);
		} else {
			return plugin.getPlayerDataAPI().getBrokenTimesOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public double getEarnedFrom(String uuid, Job job, String id, String ac) {
		if (existInCacheByUUID(uuid)) {

			if (getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()) == null) {
				return 0.0;
			}

			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenOf(id);
		} else {
			return plugin.getPlayerDataAPI().getEarnedOfBlock(uuid, job.getConfigID(), id, ac);
		}
	}

	public void updateBrokenTimes(String uuid, Job job, int times) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheBrokenTimes(times);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateBrokenTimes(uuid, job.getConfigID(), times);
		}
	}

	public void updateBrokenTimesOf(String uuid, Job job, String id, int d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheBrokenTimesOf(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarningsTimesOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateBrokenMoneyOf(String uuid, Job job, String id, double d, String ac) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenList().put(id, d);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarningsAmountOf(uuid, job.getConfigID(), id, d, ac);
		}
	}

	public void updateEarningsAtDate(String uuid, Job job, double v, String date) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheEarnings(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}

	public void updateEarningsOfToday(String uuid, Job job, double v) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) { 
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).updateCacheEarnings(date, v);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else { 
			plugin.getPlayerDataAPI().updateEarnings(uuid, job.getConfigID(), date, v);
		}
	}
	
	public void updateExp(String uuid, Job job, double val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).changeCacheExp(val);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().getExpOf(uuid, job.getConfigID());
		}
	}

	public void updatePoints(String uuid, double val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).updateCachePoints(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerDataAPI().updatePoints(uuid, val);
		}
	}
	
	public void updateMax(String uuid, int val) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).updateCacheMax(val);
			executeCustomEvent(uuid, null, true);
		} else {
			plugin.getPlayerDataAPI().updateMax(uuid, val);
		}
	}

	public double getEarnedAt(String uuid, Job job, String date) {
		if (existInCacheByUUID(uuid)) {
 
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);

		} else {
			return plugin.getPlayerDataAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public int getBrokenTimes(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getBrokenTimes();
		} else {
			return plugin.getPlayerDataAPI().getBrokenOf(uuid, job.getConfigID());
		}
	}

	public int getLevelOF(String uuid, Job job) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getLevel();
		} else {
			return plugin.getPlayerDataAPI().getLevelOf(uuid, job.getConfigID());
		}
	}
	
	public void updateLevelOf(String uuid, Job job, int lvl) {
		if (existInCacheByUUID(uuid)) {
			getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).changeCacheLevel(lvl);
			executeCustomEvent(uuid, job.getConfigID(), true);
		} else {
			plugin.getPlayerDataAPI().updateLevel(uuid, lvl, job.getConfigID());
		}
	}
 

	public double getPoints(String uuid) {
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getPoints();
		} else {
			return plugin.getPlayerDataAPI().getPoints(uuid);
		}
	}

	public double getEarningsOfToday(String uuid, Job job) {
		String date = plugin.getDate();
		if (existInCacheByUUID(uuid)) {
			return getCacheJobPlayers().get(uuid).getStatsOf(job.getConfigID()).getEarningsofDate(date);
		} else {
			return plugin.getPlayerDataAPI().getEarnedAt(uuid, job.getConfigID(), date);
		}
	}

	public JobStats loadSingleJobData(UUID UUID, String job) {

		PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

		Job j = plugin.getJobCache().get(job);

		int level = plm.getLevelOf("" + UUID, job);
		double exp = plm.getExpOf("" + UUID, job);
		String date = plm.getDateOf("" + UUID, job);
		int broken = plm.getBrokenOf("" + UUID, job);
		String joined = plm.getJobDateJoined(""+UUID, job); 

		HashMap<String, Double> listedofearned = new HashMap<String, Double>();
		for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
			DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
			Date data = new Date();

			Calendar c1 = Calendar.getInstance();
			c1.setTime(data);

			c1.add(Calendar.DATE, -i);

			Date newdate = c1.getTime();

			String d = "" + format.format(newdate);
			
			double earned = plm.getEarnedAt("" + UUID, job, d);
			
			listedofearned.put(d, earned);

		} 
		
		plugin.getPlayerChunkAPI().loadChunks(""+UUID, j);
	 
		HashMap<String, Double> money = new HashMap<String, Double>();
		HashMap<String, Integer> broken2 = new HashMap<String, Integer>();

		for (JobAction action : j.getActionList()) {
			for (String id : j.getNotRealIDSListByAction(action)) {

				double moneyearned = plm.getEarnedOfBlock("" + UUID, job, id, "" + action);
				int brokentimes = plm.getBrokenTimesOfBlock("" + UUID, job, id, "" + action);

				money.put(id, moneyearned);
				broken2.put(id, brokentimes);

			}

		}

		JobStats sz = new JobStats(j, j.getConfigID(), exp, level, broken, date, money, broken2, listedofearned, joined);

		plugin.getPlayerAPI().getRealJobPlayer("" + UUID).getStatsList().put(date, sz);

		return sz;
	}

	public void loadData(String name, UUID UUID) {

		plugin.getExecutor().execute(() -> {

			if (existInCacheByUUID("" + UUID)) {
				removePlayerFromCache("" + UUID);
			}

			PlayerDataAPI plm = UltimateJobs.getPlugin().getPlayerDataAPI();

			ArrayList<String> owned = plm.getOwnedJobs("" + UUID);
			ArrayList<String> current = plm.getCurrentJobs("" + UUID);
			
			double sal = plm.getSalary(""+UUID);
			String sat = plm.getSalaryDate(""+UUID);

			HashMap<String, JobStats> stats = new HashMap<String, JobStats>();

			for (String loading : owned) {

				Job j = plugin.getJobCache().get(loading);
				
				plugin.getPlayerChunkAPI().loadChunks(""+UUID, j);

				int level = plm.getLevelOf("" + UUID, loading);
				double exp = plm.getExpOf("" + UUID, loading);
				String date = plm.getDateOf("" + UUID, loading);
				int broken = plm.getBrokenOf("" + UUID, loading);
				String joined = plm.getJobDateJoined(""+UUID, loading); 
				
				HashMap<String, Double> listedofearned = new HashMap<String, Double>();
				
				for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
					DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
					Date data = new Date();
 
					Calendar c1 = Calendar.getInstance();
					c1.setTime(data);

					c1.add(Calendar.DATE, -i);

					Date newdate = c1.getTime();

					String d = "" + format.format(newdate);
					
					double earned = plm.getEarnedAt("" + UUID, j.getConfigID(), d);
				 
					listedofearned.put(d, earned);

				} 

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
						listedofearned, joined);

				stats.put(loading, sz);
			}

			String lused = null;

			if (UltimateJobs.getPlugin().getPlayerDataAPI().getSettingData(""+UUID, "LANG") != null) {
				lused = UltimateJobs.getPlugin().getPlayerDataAPI().getSettingData(""+UUID, "LANG");
			} else {
				lused = UltimateJobs.getPlugin().getFileManager().getLanguageConfig().getString("PlayerDefaultLanguage");
			}

			Language langusged = plugin.getLanguageAPI().getLanguages().get(lused);

			JobsPlayer jp = new JobsPlayer(name, current, owned, plm.getPoints("" + UUID),

					plm.getMaxJobs("" + UUID), "" + UUID, UUID, langusged, stats, sal, sat);

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
				
				plugin.getPlayerChunkAPI().loadChunks(""+UUID, real);

				int level = plm.getLevelOf("" + UUID, j);
				double exp = plm.getExpOf("" + UUID, j);
				String date = plm.getDateOf("" + UUID, j);
				int broken = plm.getBrokenOf("" + UUID, j);
				String joined = plm.getJobDateJoined(""+UUID, j); 

				HashMap<String, Double> listedofearned = new HashMap<String, Double>();
				for (int i = 0; i != plugin.getFileManager().getConfig().getInt("LoadEarningsDataOfDays"); i++) {
					DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
					Date data = new Date();

					Calendar c1 = Calendar.getInstance();
					c1.setTime(data);

					c1.add(Calendar.DATE, -i);

					Date newdate = c1.getTime();

					String d = "" + format.format(newdate);
					
					double earned = plm.getEarnedAt("" + UUID, real.getConfigID(), d);
					
					listedofearned.put(d, earned);

				} 

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
						listedofearned, joined);

				pl.getStatsList().put(real.getConfigID(), sz);

			}

		});
	}

	public int getExpPercentageBoost(UUID UUID) {
		var permissionPrefix = "ultimatejobs.experience_bonus";
		var max = 0;

		Player player = Bukkit.getPlayer(UUID);
		if (player == null) {
			return 0;
		}

		for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
			var permission = attachmentInfo.getPermission();
			if (permission.startsWith(permissionPrefix) && attachmentInfo.getValue()) {
				var found = Integer.parseInt(permission.substring(permission.lastIndexOf(".")+1));
				if (found > max && found <= 100) {
					max = found;
				}
			}
		}

		return max;
	}
}
