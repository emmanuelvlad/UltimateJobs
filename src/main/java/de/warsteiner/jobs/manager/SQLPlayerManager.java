package de.warsteiner.jobs.manager;
 
import java.util.ArrayList;
import java.util.Collection; 
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference; 

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.statements.SQLStatementAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.LogType; 

public class SQLPlayerManager {

	private SQLStatementAPI mg = SimpleAPI.getInstance().getSQLStatementAPI();

	public void createtables() {
		SQLStatementAPI s = SimpleAPI.getInstance().getSQLStatementAPI();
		UltimateJobs.getPlugin().getExecutor().execute(() -> {

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_stats (UUID varchar(200), JOB varchar(200), DATE varchar(200), LEVEL int, EXP double, BROKEN int)");

			s.executeUpdate("CREATE TABLE IF NOT EXISTS job_current (UUID varchar(200), JOB varchar(200))");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_players (UUID varchar(200), DATE varchar(200), POINTS int, MAX int)");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS earnings_all (UUID varchar(200), JOB varchar(200), DATE varchar(200), MONEY double)");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS earnings_stats (UUID varchar(200), JOB varchar(200), BLOCK varchar(200), TIMES int)");

			UltimateJobs.getPlugin().doLog(LogType.CREATED, "Created Tables of UltimateJobs");
		});
	}
	
	public void updatePoints(String UUID, double value) {
		final String insertQuery = "UPDATE `job_players` SET `POINTS`='" + value + "' WHERE UUID='" + UUID + "'";
		mg.executeUpdate(insertQuery);
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Max Jobs of: " + UUID);
	}

	public void updateLevel(String UUID, int value, String job) {
		final String insertQuery = "UPDATE `job_stats` SET `LEVEL`='" + value + "' WHERE UUID='" + UUID + "' AND JOB='"
				+ job + "'";
		mg.executeUpdate(insertQuery);
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Level of Job: " + UUID);
	}

	public void updateMax(String UUID, int value) {
		final String insertQuery = "UPDATE `job_players` SET `MAX`='" + value + "' WHERE UUID='" + UUID + "'";
		mg.executeUpdate(insertQuery);
		UltimateJobs.getPlugin().doLog(LogType.UPDATED, "Updated Max Jobs of: " + UUID);
	}

	public void savePlayer(JobsPlayer pl, String UUID) {
		mg.executeUpdate("DELETE FROM job_stats WHERE UUID='" + UUID + "';");
		mg.executeUpdate("DELETE FROM job_players WHERE UUID='" + UUID + "';");
		mg.executeUpdate("DELETE FROM job_current WHERE UUID='" + UUID + "';");

		Collection<String> current = pl.getCurrentJobs();
		Collection<String> owned = pl.getOwnJobs();
		int max = pl.getMaxJobs();
		double points = pl.getPoints();

		final String insertQuery_player = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
		mg.executeUpdate(insertQuery_player, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, UltimateJobs.getPlugin().getAPI().getDate());
			ps.setDouble(3, points);
			ps.setInt(4, max);

		});

		final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";

		for (String job : owned) {
			int level = pl.getLevelOf(job);
			double exp = pl.getExpOf(job);
			int broken = pl.getBrokenOf(job);
			String date = pl.getDateOfJob(job);

			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
				ps.setString(3, date);
				ps.setInt(4, level);
				ps.setDouble(5, exp);
				ps.setInt(6, broken);

			});

		}

		final String insertQuery_owned = "INSERT INTO job_current(UUID,JOB) VALUES(?,?)";

		if (current != null) {
			for (String job : current) {

				mg.executeUpdate(insertQuery_owned, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, job);

				});
			}
		}

		UltimateJobs.getPlugin().doLog(LogType.SAVED, "Saved Data of Player : " + UUID);

	}

	public void createJobData(String UUID, String job) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";
		mg.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, job);
			ps.setString(3, date);
			ps.setInt(4, 1);
			ps.setDouble(5, 0);
			ps.setInt(6, 0);

		});
		UltimateJobs.getPlugin().doLog(LogType.CREATED, "Created Job-Data of : " + job + " for : " + UUID);
	}

	public boolean ExistJobData(String UUID, String job) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("DATE"));
			}
			return 1;
		});
		UltimateJobs.getPlugin().doLog(LogType.CHECK, "Check for Data of job : " + job + " for player : " + UUID);
		return a.get() != null;
	}

	public int getLevelOf(String UUID, String job) {
		AtomicInteger a = new AtomicInteger(0);

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getInt("LEVEL"));
			}
			return 1;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Level of Player-Data for : " + UUID + " : job : " + job);
		return a.get();
	}

	public double getExpOf(String UUID, String job) {
		AtomicDouble a = new AtomicDouble();

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getDouble("EXP"));
			}
			return 0;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Exp of Player-Data for : " + UUID + " : job : " + job);
		return a.get();
	}

	public int getBrokenOf(String UUID, String job) {
		AtomicInteger a = new AtomicInteger(0);

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getInt("BROKEN"));
			}
			return 0;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Broken of Player-Data for : " + UUID + " : job : " + job);
		return a.get();
	}

	public String getDateOf(String UUID, String job) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("DATE"));
			}
			return 0;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Date of Player-Data for : " + UUID + " : job : " + job);
		return a.get();
	}

	public ArrayList<String> getOwnedJobs(String UUID) {
		Collection<String> jobs = new ArrayList<String>();
		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "'", rs -> {
		 
				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}
 
			return 1;
		});
		 
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Owned-Jobs of Player-Data for : " + UUID);
		return (ArrayList<String>) jobs;
	}
	 
	
	public ArrayList<String> getCurrentJobs(String UUID) {
		
		Collection<String> jobs = new ArrayList<String>();
		mg.executeQuery("SELECT * FROM job_current WHERE UUID= '" + UUID + "'", rs -> {
			 
				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}
 
			return 1;
		});
		 
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Owned-Jobs of Player-Data for : " + UUID);
		return (ArrayList<String>) jobs;
	}

	public int getPoints(String UUID) {
		AtomicInteger a = new AtomicInteger(0);

		mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getInt("POINTS"));
			}
			return 0;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Points of Player-Data for : " + UUID);
		return a.get();
	}

	public int getMaxJobs(String UUID) {
		AtomicInteger a = new AtomicInteger(0);

		mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getInt("MAX"));
			}
			return 0;
		});
		UltimateJobs.getPlugin().doLog(LogType.GET, "Get Max-Jobs of Player-Data for : " + UUID);
		return a.get();
	}

	public void createPlayer(String UUID, String name) {
		String date = UltimateJobs.getPlugin().getAPI().getDate();
		createPlayerDetails(UUID, date);
		UltimateJobs.getPlugin().doLog(LogType.CREATED, "Created player : " + name + " with uuid : " + UUID);
	}

	public void createPlayerDetails(String UUID, String date) {
		int max = UltimateJobs.getPlugin().getMainConfig().getConfig().getInt("MaxDefaultJobs");
		final String insertQuery = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
		mg.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, "" + date);
			ps.setInt(3, 0);
			ps.setInt(4, max);

		});
	}

	public boolean ExistPlayer(String UUID) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("DATE"));
			}
			return 1;
		});
		UltimateJobs.getPlugin().doLog(LogType.CHECK, "Check for Data of player : " + UUID);
		return a.get() != null;
	}

}
