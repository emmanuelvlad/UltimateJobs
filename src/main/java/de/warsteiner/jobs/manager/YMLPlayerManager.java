package de.warsteiner.jobs.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection; 
import org.bukkit.configuration.file.FileConfiguration; 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.LogType;

public class YMLPlayerManager {
 
	private UltimateJobs plugin = UltimateJobs.getPlugin();
	  
	public void updatePoints(String UUID, double value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Points", value);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Max Jobs of: " + UUID);
	}

	public void updateLevel(String UUID, int value, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Jobs."+UUID+"."+job+".Level", value);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Level of Job: " + UUID);
	}

	public void updateMax(String UUID, int value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Max", value);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Max Jobs of: " + UUID);
	}

	public void savePlayer(JobsPlayer pl, String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		Collection<String> current = pl.getCurrentJobs();
		Collection<String> owned = pl.getOwnJobs();
		int max = pl.getMaxJobs();
		double points = pl.getPoints();
		
		cfg.set("Player."+UUID+".Points", points);
		cfg.set("Player."+UUID+".Date", UltimateJobs.getPlugin().getAPI().getDate());
		cfg.set("Player."+UUID+".Max", max); 
  
		ArrayList<String> newowned = new ArrayList<String>();
		
		for (String job : owned) {
			int level = pl.getLevelOf(job);
			double exp = pl.getExpOf(job);
			int broken = pl.getBrokenOf(job);
			String date = pl.getDateOfJob(job);
			
			cfg.set("Jobs."+UUID+"."+job+".Level", level);
			cfg.set("Jobs."+UUID+"."+job+".Date", date);
			cfg.set("Jobs."+UUID+"."+job+".Broken", broken);
			cfg.set("Jobs."+UUID+"."+job+".Exp", exp);

			newowned.add(job); 
		}
 
		cfg.set("Player."+UUID+".Owned", newowned);
		cfg.set("Player."+UUID+".Current", current);
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UltimateJobs.getPlugin().doLog(LogType.SAVED, "Saved Data of Player : " + UUID);

	}

	public void createJobData(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		String date = plugin.getAPI().getDate();
		
		cfg.set("Jobs."+UUID+"."+job+".Date", date);
		cfg.set("Jobs."+UUID+"."+job+".Level", 1);
		cfg.set("Jobs."+UUID+"."+job+".Exp", 0);
		cfg.set("Jobs."+UUID+"."+job+".Broken", 0);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UltimateJobs.getPlugin().doLog(LogType.CREATED, "Created Job-Data of : " + job + " for : " + UUID);
	}

	public boolean ExistJobData(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.CHECK, "Check for Data of job : " + job + " for player : " + UUID);
		return cfg.getString("Jobs."+UUID+"."+job+".Date") != null;
	}

	public int getLevelOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Level of Player-Data for : " + UUID + " : job : " + job);
		return cfg.getInt("Jobs."+UUID+"."+job+".Level");
	}

	public double getExpOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Exp of Player-Data for : " + UUID + " : job : " + job);
		return cfg.getDouble("Jobs."+UUID+"."+job+".Exp");
	}

	public int getBrokenOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Broken of Player-Data for : " + UUID + " : job : " + job);
		return cfg.getInt("Jobs."+UUID+"."+job+".Broken");
	}

	public String getDateOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Date of Player-Data for : " + UUID + " : job : " + job);
		return cfg.getString("Jobs."+UUID+"."+job+".Date");
	}

	public ArrayList<String> getOwnedJobs(String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Owned-Jobs of Player-Data for : " + UUID);
		return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Owned");
	}
	 
	
	public ArrayList<String> getCurrentJobs(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Owned-Jobs of Player-Data for : " + UUID);
		return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Current");
	}

	public double getPoints(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		return cfg.getDouble("Player."+UUID+".Points");
	}

	public int getMaxJobs(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		
		return cfg.getInt("Player."+UUID+".Max");
	}

	public void createPlayer(String UUID, String name) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		createPlayerDetails(UUID, date);
		UltimateJobs.getPlugin().doLog(LogType.CREATED, "Created player : " + name + " with uuid : " + UUID);
	}

	public void createPlayerDetails(String UUID, String date) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		File file = plugin.getPlayerDataFile().getfile();
		int max = UltimateJobs.getPlugin().getMainConfig().getConfig().getInt("MaxDefaultJobs"); 
		ArrayList<String> list = new ArrayList<String>();
		cfg.set("Player."+UUID+".Points", 0);
		cfg.set("Player."+UUID+".Max", max);
		cfg.set("Player."+UUID+".Date", date);
		cfg.set("Player."+UUID+".Owned", list);
		cfg.set("Player."+UUID+".Current", list);
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean ExistPlayer(String UUID) { 
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		UltimateJobs.getPlugin().doLog(LogType.CHECK, "Check for Data of player : " + UUID);
		return cfg.getString("Player."+UUID+".Date") != null;
	}

	
}
