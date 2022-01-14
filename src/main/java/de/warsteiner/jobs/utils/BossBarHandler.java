package de.warsteiner.jobs.utils;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;

public class BossBarHandler {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public static HashMap<String, BossBar> g = new HashMap<String, BossBar>();

	public static void createBar(Player p, String name, BarColor color, String ID, double pr) {
		BossBar b = Bukkit.createBossBar(name, color, BarStyle.SOLID, new BarFlag[] {});
		b.setProgress(pr);
		b.setVisible(true);
		g.put(ID, b);

		((BossBar) g.get(ID)).addPlayer(p);
	}

	public static boolean exist(String id) {
		return g.get(id) != null;
	}

	public static void removeBossBar(String ID) {
		((BossBar) g.get(ID)).setVisible(false);
		g.remove(ID);
	}

	public static void renameBossBar(String name, String ID) {
		((BossBar) g.get(ID)).setTitle(name);
	}

	public static void updateProgress(double pr, String ID) {
		((BossBar) g.get(ID)).setProgress(pr);
	}

	public static void recolorBossBar(BarColor color, String ID) {
		((BossBar) g.get(ID)).setColor(color);
	}

	public static void createTempBossBar(Player p, String name, BarColor color, final String ID,
			Integer timeInSecondsBeforeRemove, double pr) {
		createBar(p, name, color, ID, pr);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) plugin, new Runnable() {
			public void run() {
				removeBossBar(ID);
			}
		}, (timeInSecondsBeforeRemove.intValue() * 20));
	}

	public static void startSystemCheck() {

		new BukkitRunnable() {

			public void run() {
				
				plugin.getExecutor().execute(() -> {

					for (Player p : Bukkit.getOnlinePlayers()) {
	
						Date lastworked = UltimateJobs.getPlugin().getAPI().lastworked_list.get(p.getName());
	
						if (lastworked == null) {
							continue;
						}
	
						boolean check = lastworked.after(new Date());
	
						if (check == false) {
							if (UltimateJobs.getPlugin().getAPI().lastworked_list.containsKey(p.getName())) {
								BossBarHandler.removeBossBar(p.getName());
								UltimateJobs.getPlugin().getAPI().lastworked_list.remove(p.getName());
							}
						}
	
					}
				});

			}
		}.runTaskTimer(plugin, 0, 25);
	}

	public static double calc(double exp, boolean ismaxlevel, double need) {
		double use;
		if (!ismaxlevel) {
			double jobneed = need / 100;

			double p = exp / jobneed;

			double max = 1.0 / 100;

			double one = max * p;

			if (one >= 1.0) {
				use = 1;
			} else {
				use = one;
			}
		} else {
			use = 1.0;
		}
		return use;
	}
}
