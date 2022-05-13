package de.warsteiner.jobs.utils.objects;

import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.datax.utils.objects.Language;
 

public class JobsPlayer {

	private String name;
	private String UUID;
	private UUID rUUID;
	private ArrayList<String> current;
	private ArrayList<String> owned; 
	private double points;
	private int max; 
	private Language lang;
	private HashMap<String, JobStats> stats;
	
	public JobsPlayer(String name, ArrayList<String> current2, ArrayList<String> owned2,  double points,
			int max, String UUID, UUID rUUID, Language lang, HashMap<String, JobStats> stats) {
		this.name = name;
		this.UUID = UUID; 
		this.points = points;
		this.max = max; 
		this.owned = owned2;
		this.rUUID = rUUID; 
		this.current = current2;
		this.lang = lang;
		this.stats = stats;
	}
	
	public JobStats getStatsOf(String job) {
		return stats.get(job);
	}
	
	public HashMap<String, JobStats> getStatsList() {
		return stats;
	}
	
	public YamlConfiguration getLanguageConfig() {
		return lang.getConfig();
	}
	
	public Language getLanguage() {
		return lang;
	}
	
	public java.util.UUID getUUID() {
		return this.rUUID;
	}
	
	public String getUUIDAsString() {
		return UUID;
	}
	
	public JobsPlayer getJobsPlayer() {
		return this;
	}

	public double getPoints() {
		return points;
	}

	public void updatePoints(double d) { 
		points = d;
	}

	public String getName() {
		return name;
	}

	public int getMaxJobs() {
		return max;
	}

	public void updateMax(int nw) {
		max = nw;
	}
  
	public void addCurrentJob(String job) {
		ArrayList<String> l = getCurrentJobs();
		l.add(job);
		updateCurrentJobs(l);
	}

	public void remCurrentJob(String job) {
		ArrayList<String> l = getCurrentJobs();
		l.remove(job);
		updateCurrentJobs(l);
	}

	public boolean ownJob(String id) {
		if(getOwnJobs().contains(id)) {
			return true;
		}
		return false;
	}

	public boolean isInJob(String id) {
		
		if(getCurrentJobs() == null) {
			return false;
		}
		
		return getCurrentJobs().contains(id.toUpperCase());
	}

	public ArrayList<String> getCurrentJobs() {
		if(current == null) {
			return new ArrayList<String>();
		}
		return current;
	}

	public void updateCurrentJobs(ArrayList<String> l) {
		current = l;
	}

	public ArrayList<String> getOwnJobs() {
		return owned;
	}

	public void addOwnedJob(String job) {
		ArrayList<String> l = getOwnJobs();
		l.add(job);
		updateOwnJobs(l);
	}

	public void remOwnedJob(String job) {
		ArrayList<String> l = getOwnJobs();
		l.remove(job);
		updateOwnJobs(l);
	}

	public void updateOwnJobs(ArrayList<String> l) {
		owned = l;
	}

}