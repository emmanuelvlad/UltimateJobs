package de.warsteiner.jobs.utils.objects;
 
import java.util.HashMap;

import de.warsteiner.jobs.api.Job;

public class JobStats {

	private Job job;
	private String jobnamed;
	
	private double exp;
	private int level;
	private int broken;
	private String date;
	
	private HashMap<String, Double> brokenof_money;
	private HashMap<String, Integer> brokenof_times;
	
	private HashMap<String, Double> earnings;
	
	private String joined; 
	  
	public JobStats(Job job, String jobnamed, double exp, int level, int broken, String date, HashMap<String, Double> brokenof, HashMap<String, Integer> brokenof_times, HashMap<String, Double> earnings,
			String joinedy) {
		this.job = job;
		this.jobnamed = jobnamed;
		this.exp = exp;
		this.level = level;
		this.broken = broken;
		this.date = date;
		this.joined = joinedy;
		this.brokenof_money = brokenof;
		this.brokenof_times = brokenof_times;
		this.earnings = earnings;
	}
 
	public void updateJoinedDate(String jn) {
		joined = jn;
	}
	
	public String getJoinedDate() {
		return joined;
	}
	
	public HashMap<String, Integer> getBrokenTimesOfIDList() {
		return brokenof_times;
	}
 
	public Integer getBrokenTimesOf(String ID) {
		return brokenof_times.get(ID);
	}
	
	public void updateBrokenTimesOf(String id, int value) {
	 
		getBrokenTimesOfIDList().put(id, value);
	}
	
	public void addBrokenTimesOf(String id, int value) {
		
		Integer old = getBrokenTimesOf(id);
		 
		getBrokenTimesOfIDList().put(id, old+value);
	}
 
	public HashMap<String, Double> getEarningsList() {
		return earnings;
	}
	
	public Double getEarningsofDate(String date2) {
		
		if(!getEarningsList().containsKey(date2)) {
			return 0.0;
		}
		 
		return getEarningsList().get(date2);
	}
	
	public void updateEarnings(String date, double value) {  
		getEarningsList().put(date, value);
	}
 
	
	public HashMap<String, Double> getBrokenList() {
		return brokenof_money;
	}
	
	public Double getBrokenOf(String ID) {
		return brokenof_money.get(ID);
	}
	
	public void addBrokenOf(String id, double value) {
		
		Double old = getBrokenOf(id);
		 
		getBrokenList().put(id, old+value);
	}
	
	public void updateBrokenOf(String id, double value) {
	 
		getBrokenList().put(id, value);
	}
	
	public String getDate() {
		return date;
	}
	
	public int getBrokenTimes() {
		return broken;
	}
	
	public void addBrokenTimes(int add) {
		this.broken = broken + add;
	}
	
	public void updateBrokenTimes(int val) {
		this.broken = val;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void updateLevel(int lvl) {
		this.level = lvl;
	}
	
	public double getExp() {
		return exp;
	}
	
	public void addExp(double add) {
		this.exp = exp + add;
	}
	
	public void updateExp(double val) {
		this.exp = val;
	}
	
	public Job getJob() {
		return job;
	}
	
	public String getJobName() {
		return jobnamed;
	}
	
}
