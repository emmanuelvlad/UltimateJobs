package de.warsteiner.jobs.manager;

import java.util.ArrayList;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.api.JobsPlayer;

public class PlayerDataManager {
 
	private SQLPlayerManager sql;
	private YMLPlayerManager yml;
	
	public PlayerDataManager(YMLPlayerManager yml, SQLPlayerManager sql) {
		this.yml = yml;
		this.sql = sql;
	}
	
	public void savePlayer(JobsPlayer jb, String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.savePlayer(jb, UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.savePlayer(jb, UUID);
		}
	}
	
	
	public void createJobData(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.createJobData(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.createJobData(UUID, job);
		}
	}
	
	public boolean ExistJobData(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.ExistJobData(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.ExistJobData(UUID, job);
		}
		return false;
	}

	public double getPoints(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getPoints(UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getPoints(UUID);
		}
		return 0;
	}
	

	public String getDateOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getDateOf(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getDateOf(UUID, job);
		}
		return null;
	}
	
	public double getExpOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getExpOf(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getExpOf(UUID, job);
		}
		return 0;
	}
	
	public int getBrokenOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getBrokenOf(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getBrokenOf(UUID, job);
		}
		return 0;
	}
	
	public int getLevelOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getLevelOf(UUID, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getLevelOf(UUID, job);
		}
		return 1;
	}
	
	public int getMax(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getMaxJobs(UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getMaxJobs(UUID);
		}
		return 0;
	}
	
	public boolean ExistPlayer(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.ExistPlayer(UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.ExistPlayer(UUID);
		}
		return false;
	}
	
	public void updatePoints(String UUID, double value) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.updatePoints(UUID, value);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.updatePoints(UUID, value);
		}
	}
	
	public void updateMax(String UUID, int value) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.updateMax(UUID, value);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.updateMax(UUID, value);
		}
	}
	
	public void updateLevel(String UUID, int value, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.updateLevel(UUID, value, job);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.updateLevel(UUID, value, job);
		}
	}
	
	public void createPlayer(String UUID, String name) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			sql.createPlayer(UUID, name);
		}  else if(mode.equalsIgnoreCase("YML")) {
			yml.createPlayer(UUID, name);
		}
	}
	
	public ArrayList<String> getOfflinePlayerCurrentJobs(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getCurrentJobs(UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getCurrentJobs(UUID);
		}
		return null;
	}
	
	public ArrayList<String> getOfflinePlayerOwnedJobs(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			return sql.getOwnedJobs(UUID);
		}  else if(mode.equalsIgnoreCase("YML")) {
			return yml.getOwnedJobs(UUID);
		}
		return null;
	}
	
}
