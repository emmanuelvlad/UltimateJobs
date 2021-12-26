package de.warsteiner.jobs.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
}
