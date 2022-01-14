package de.warsteiner.jobs.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobsPlayer {

	private String name;

	private List<String> current = new ArrayList<String>();
	private List<String> owned = new ArrayList<String>();
	private HashMap<String, Integer> job_levels = new HashMap<String, Integer>();
	private HashMap<String, Double> job_exp = new HashMap<String, Double>();
	private HashMap<String, Integer> job_broken = new HashMap<String, Integer>();
	private HashMap<String, String> job_date = new HashMap<String, String>();
	private double points;
	private int max;

	public JobsPlayer(String name, List<String> current2, List<String> owned2, HashMap<String, Integer> levels,
			HashMap<String, Double> exp, HashMap<String, Integer> broken, double points, HashMap<String, String> date,
			int max) {
		this.name = name;
		this.job_date = date;
		this.points = points;
		this.max = max;
		this.job_levels = levels;
		this.job_broken = broken;
		this.owned = owned2;
		this.job_exp = exp;
		this.current = current2;
	}

	public double getPoints() {
		return points;
	}

	public void changePoints(double d) {
		points = d;
	}

	public String getName() {
		return name;
	}

	public int getMaxJobs() {
		return max;
	}

	public void changeMax(int nw) {
		max = nw;
	}

	public String getDateOfJob(String job) {
		return job_date.get(job);
	}

	public HashMap<String, String> getDateList() {
		return job_date;
	}

	public Integer getBrokenOf(String job) {
		return getBrokenList().get(job);
	}

	public void updateBroken(String job, int nw) {
		job_broken.remove(job);
		job_broken.put(job, nw);
	}

	public HashMap<String, Integer> getBrokenList() {
		return job_broken;
	}

	public Double getExpOf(String job) {
		return getExpList().get(job);
	}

	public void updateExp(String job, double nw) {
		job_exp.remove(job);
		job_exp.put(job, nw);
	}

	public HashMap<String, Double> getExpList() {
		return job_exp;
	}

	public Integer getLevelOf(String job) {
		return getLevels().get(job);
	}

	public void updateLevel(String job, int nw) {
		job_levels.remove(job);
		job_levels.put(job, nw);
	}

	public HashMap<String, Integer> getLevels() {
		return job_levels;
	}

	public void addCurrentJob(String job) {
		List<String> l = getCurrentJobs();
		l.add(job);
		updateCurrentJobs(l);
	}

	public void remoCurrentJob(String job) {
		List<String> l = getCurrentJobs();
		l.remove(job);
		updateCurrentJobs(l);
	}

	public boolean ownJob(String id) {
		return getOwnJobs().contains(id);
	}

	public boolean isInJob(String id) {
		return getCurrentJobs().contains(id.toUpperCase());
	}

	public List<String> getCurrentJobs() {
		return current;
	}

	public void updateCurrentJobs(List<String> list) {
		current = list;
	}

	public List<String> getOwnJobs() {
		return owned;
	}

	public void addOwnedJob(String job) {
		List<String> l = getOwnJobs();
		l.add(job);
		updateOwnJobs(l);
	}

	public void remOwnedJob(String job) {
		List<String> l = getOwnJobs();
		l.remove(job);
		updateOwnJobs(l);
	}

	public void updateOwnJobs(List<String> list) {
		owned = list;
	}

}
