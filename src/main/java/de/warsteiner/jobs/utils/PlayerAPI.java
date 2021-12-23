package de.warsteiner.jobs.utils;

import java.util.ArrayList;
import java.util.List;

import de.warsteiner.jobs.UltimateJobs;
 
public class PlayerAPI {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public void createPlayer(String uuid) {
		DataFile cl = plugin.getPlayerData();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		List<String> players = cl.get().getStringList("Players");
		players.add(uuid);
		cl.get().set("Players", players);
		cl.get().set("Player." + uuid + ".CurrentJob", list);
		cl.get().set("Player." + uuid + ".OwnsJob", list2);
		cl.get().set("Player." + uuid + ".MaxJobs", plugin.getMainConfig().getConfig().getInt("MaxDefaultJobs"));

		cl.save();
	}

	public List<String> getPlayers() { 
		return plugin.getPlayerData().get().getStringList("Players");
	}

	public void UpdateFetcher(String uuid, String name) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("UUIDFetcher." + uuid + ".Name", name);
		cl.get().set("UUIDFetcher." + name.toUpperCase() + ".UUID", "" + uuid);
		cl.save();
	}

	public String getNameByUUID(String uuid) { 
		return plugin.getPlayerData().get().getString("UUIDFetcher." + uuid + ".Name");
	}

	public String getUUIDByName(String name) { 
		return plugin.getPlayerData().get().getString("UUIDFetcher." + name.toUpperCase() + ".UUID");
	}

	public int getPointsInJob(String uuid, String job) { 
		return plugin.getPlayerData().get().getInt("Job." + uuid + ".ID." + job + ".Points");
	}

	public void setPoints(String uuid, String job, int points) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Job." + uuid + ".ID." + job + ".Points", points);
		cl.save();
	}

	public int getGlobalPoints(String uuid) { 
		return plugin.getPlayerData().get().getInt("Job." + uuid + ".Global.Points");
	}

	public void setGlobalPoints(String uuid, int points) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Job." + uuid + ".Global.Points", points);
		cl.save();
	}

	public void createJob(String uuid, String job) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Job." + uuid + ".ID." + job + ".Level", 1);
		cl.get().set("Job." + uuid + ".ID." + job + ".Exp", 0);
		cl.get().set("Job." + uuid + ".ID." + job + ".Points", 0);
		cl.save();
	}

	public double getJobExp(String uuid, String job) { 
		return plugin.getPlayerData().get().getDouble("Job." + uuid + ".ID." + job + ".Exp");
	}

	public void setJobExp(String uuid, String job, double d) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Job." + uuid + ".ID." + job + ".Exp", d);
		cl.save();
	}

	public void addJobExp(String uuid, String job, double exp) {

		double old = getJobExp(uuid, job);

		setJobExp(uuid, job, old + exp);
	}

	public void remJobExp(String uuid, String job, int exp) {
		double old = getJobExp(uuid, job);

		setJobExp(uuid, job, old - exp);
	}

	public int getJobLevel(String uuid, String job) { 
		return plugin.getPlayerData().get().getInt("Job." + uuid + ".ID." + job + ".Level");
	}

	public void setJobLevel(String uuid, String job, int level) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Job." + uuid + ".ID." + job + ".Level", level);
		cl.save();
	}

	public void addJobLevel(String uuid, String job, int exp) { 
		int old = getJobLevel(uuid, job);

		setJobLevel(uuid, job, old + exp);
	}

	public void remJobLevel(String uuid, String job, int exp) {
		int old = getJobLevel(uuid, job);

		setJobLevel(uuid, job, old - exp);
	}

	public boolean isInJob(String uuid, String id) { 
		return plugin.getPlayerData().get().getStringList("Player." + uuid + ".CurrentJob").contains(id);
	}

	public void addOwnJob(String uuid, String job) {
		DataFile cl = plugin.getPlayerData();
		List<String> b = cl.get().getStringList("Player." + uuid + ".OwnsJob");
		b.add(job);
		cl.get().set("Player." + uuid + ".OwnsJob", b);
		cl.save();
	}

	public void remOwnJob(String uuid, String job) {
		DataFile cl = plugin.getPlayerData();
		List<String> b = cl.get().getStringList("Player." + uuid + ".OwnsJob");
		b.remove(job);
		cl.get().set("Player." + uuid + ".OwnsJob", b);
		cl.save();
	}

	public void addCurrentJobs(String uuid, String job) {
		DataFile cl = plugin.getPlayerData();
		List<String> b = cl.get().getStringList("Player." + uuid + ".CurrentJob");
		b.add(job);
		cl.get().set("Player." + uuid + ".CurrentJob", b);
		cl.save();
	}

	public void remCurrentJobs(String uuid, String job) {
		DataFile cl = plugin.getPlayerData();
		List<String> b = cl.get().getStringList("Player." + uuid + ".CurrentJob");
		b.remove(job);
		cl.get().set("Player." + uuid + ".CurrentJob", b);
		cl.save();
	}

	public void setCurrentJobsToNull(String uuid) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Player." + uuid + ".CurrentJob", null);
		cl.save();
	}

	public List<String> getCurrentJobs(String uuid) { 
		return plugin.getPlayerData().get().getStringList("Player." + uuid + ".CurrentJob");
	}

	public int getMaxJobs(String uuid) { 
		return plugin.getPlayerData().get().getInt("Player." + uuid + ".MaxJobs");
	}

	public void setMaxJobs(String uuid, int d) {
		DataFile cl = plugin.getPlayerData();
		cl.get().set("Player." + uuid + ".MaxJobs", d);
		cl.save();
	}

	public void addMaxJobs(String uuid, int m) {
		int old = getMaxJobs(uuid);

		setMaxJobs(uuid, old + m);

	}

	public void remMaxJobs(String uuid, int m) {
		int old = getMaxJobs(uuid);

		setMaxJobs(uuid, old - m);

	}

	public List<String> getOwn(String uuid) { 
		return plugin.getPlayerData().get().getStringList("Player." + uuid + ".OwnsJob");
	}

	public boolean ownJob(String uuid, String id) { 
		return plugin.getPlayerData().get().getStringList("Player." + uuid + ".OwnsJob").contains(id);
	}

	public boolean existPlayer(String uuid) { 
		return plugin.getPlayerData().get().contains("Player." + uuid + ".CurrentJob");
	}

}
