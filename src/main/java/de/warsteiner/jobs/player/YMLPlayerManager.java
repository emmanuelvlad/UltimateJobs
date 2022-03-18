package de.warsteiner.jobs.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection; 
import org.bukkit.configuration.file.FileConfiguration; 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer; 

public class YMLPlayerManager {
 
	private UltimateJobs plugin = UltimateJobs.getPlugin();
	  
	public void updatePoints(String UUID, double value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Points", value); 
		save(cfg, file);
	}

	public void updateLevel(String UUID, int value, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Jobs."+UUID+"."+job+".Level", value); 
		save(cfg, file);
	}
	
	public void updateEarnings(String UUID, String job , String date, double money) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		cfg.set("Earnings."+UUID+"."+date+"."+job, money); 
		save(cfg, file);
	}
	
	public double getEarnedAt(String UUID, String job, String date) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		if(cfg.contains("Earnings."+UUID+"."+date+"."+job)) {
			return cfg.getDouble("Earnings."+UUID+"."+date+"."+job);
		} else {
			cfg.set("Earnings."+UUID+"."+date+"."+job, 0);
			save(cfg, file);
		}
		return 0;
		
	}
	
	public double getEarnedOfBlock(String UUID, String job, String id) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		if(cfg.contains("Earnings."+UUID+"."+job+"."+id+".Money")) {
			return cfg.getDouble("Earnings."+UUID+"."+job+"."+id+".Money");
		} else {
			cfg.set("Earnings."+UUID+"."+job+"."+id+".Money", 0);
			save(cfg, file);
		}
		return 0;
		
	}
	
	public void updateEarningsTimesOf(String UUID, String job , String id, int time) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
	 
			cfg.set("Earnings."+UUID+"."+job+"."+id+".Times", 0);
			save(cfg, file);
		 
	}
	
	public void updateEarningsAmountOf(String UUID, String job , String id, double money) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		cfg.set("Earnings."+UUID+"."+job+"."+id+".Money", 0);
		save(cfg, file);
	}
	

	public int getBrokenTimesOfBlock(String UUID, String job, String id) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		if(cfg.contains("Earnings."+UUID+"."+job+"."+id+".Times")) {
			return cfg.getInt("Earnings."+UUID+"."+job+"."+id+".Times");
		} else {
			cfg.set("Earnings."+UUID+"."+job+"."+id+".Times", 0);
			save(cfg, file);
		}
		return 0;
		
	}
	
	public void updateMax(String UUID, int value) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		cfg.set("Player."+UUID+".Max", value);
		save(cfg, file);
	}

	public void savePlayer(JobsPlayer pl, String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		Collection<String> current = pl.getCurrentJobs();
		Collection<String> owned = pl.getOwnJobs();
		int max = pl.getMaxJobs();
		double points = pl.getPoints();
		
		cfg.set("Player."+UUID+".Points", points);
		cfg.set("Player."+UUID+".Date", plugin.getPluginManager().getDate());
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
		
		save(cfg, file);
 
	}

	public void createJobData(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		File file = plugin.getPlayerDataFile().getfile();
		
		String date =plugin.getPluginManager().getDate();
		
		cfg.set("Jobs."+UUID+"."+job+".Date", date);
		cfg.set("Jobs."+UUID+"."+job+".Level", 1);
		cfg.set("Jobs."+UUID+"."+job+".Exp", 0);
		cfg.set("Jobs."+UUID+"."+job+".Broken", 0);
		save(cfg, file);
	}

	public boolean ExistJobData(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getString("Jobs."+UUID+"."+job+".Date") != null;
	}

	public int getLevelOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getInt("Jobs."+UUID+"."+job+".Level");
	}

	public double getExpOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getDouble("Jobs."+UUID+"."+job+".Exp");
	}

	public int getBrokenOf(String UUID, String job) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getInt("Jobs."+UUID+"."+job+".Broken");
	}

	public String getDateOf(String UUID, String job) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return cfg.getString("Jobs."+UUID+"."+job+".Date");
	}

	public ArrayList<String> getOwnedJobs(String UUID) { 
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
		return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Owned");
	}
	 
	
	public ArrayList<String> getCurrentJobs(String UUID) {
		
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		 
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
		String date = plugin.getPluginManager().getDate();
		createPlayerDetails(UUID, date); 
	}

	public void createPlayerDetails(String UUID, String date) {
		FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
		File file = plugin.getPlayerDataFile().getfile();
		int max = UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs"); 
		ArrayList<String> list = new ArrayList<String>();
		cfg.set("Player."+UUID+".Points", 0);
		cfg.set("Player."+UUID+".Max", max);
		cfg.set("Player."+UUID+".Date", date);
		cfg.set("Player."+UUID+".Owned", list);
		cfg.set("Player."+UUID+".Current", list);
		save(cfg, file);
	}

	public boolean ExistPlayer(String UUID) { 
		FileConfiguration cfg = plugin.getPlayerDataFile().get();  
		return cfg.getString("Player."+UUID+".Date") != null;
	}

	
	public void save(FileConfiguration cfg, File file) {
		if(file != null) {
			try {
				cfg.save(file);
			} catch (IOException e) { 
				e.printStackTrace();
			}
		} else { 
		}
	}
	
}
