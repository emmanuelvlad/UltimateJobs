package de.warsteiner.jobs.player;
 
import java.util.ArrayList;
import java.util.Collection; 
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference; 

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.statements.SQLStatementAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer; 

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
					"CREATE TABLE IF NOT EXISTS earnings_stats (UUID varchar(200), JOB varchar(200), ID varchar(200), TIMES int, MONEY double)");
 
		});
	}
	
	public void updateEarningsTimesOf(String UUID, String job , String id, int time) {
		final String insertQuery = "UPDATE `earnings_stats` SET `TIMES`='" + time + "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' ";
		mg.executeUpdate(insertQuery); 
	}
	
	public void updateEarningsAmountOf(String UUID, String job , String id, double money) {
		final String insertQuery = "UPDATE `earnings_stats` SET `MONEY`='" + money + "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' ";
		mg.executeUpdate(insertQuery); 
	}
	
	public boolean ExistEarningsOfBlock(String UUID, String job, String id) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM earnings_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("ID"));
			}
			return 1;
		}); 
		return a.get() != null;
	}
	
	public int getBrokenTimesOfBlock(String UUID, String job, String id) {
		if(ExistEarningsOfBlock(UUID, job, id)) {
			AtomicInteger a = new AtomicInteger();

			mg.executeQuery("SELECT * FROM earnings_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("TIMES"));
				}
				return 0;
			}); 
			return a.get();
		} else {
			createEarningsOfBlock(UUID, job, id);
		}
		return 0;
	}
	
	public double getEarnedOfBlock(String UUID, String job, String id) {
		if(ExistEarningsOfBlock(UUID, job, id)) {
			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM earnings_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("MONEY"));
				}
				return 0;
			}); 
			return a.get();
		} else {
			createEarningsOfBlock(UUID, job, id);
		}
		return 0;
	}
	 
	public void createEarningsOfBlock(String UUID, String job, String id) { 
		final String insertQuery = "INSERT INTO earnings_stats(UUID,JOB,ID,TIMES,MONEY) VALUES(?,?,?,?,?)";
		mg.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, job);
			ps.setString(3, id);
			ps.setInt(4, 0); 
			ps.setDouble(5, 0); 
		}); 
	}
	
	public boolean ExistEarningsDataToday(String UUID, String job, String date) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND DATE= '"+date+"'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("DATE"));
			}
			return 1;
		}); 
		return a.get() != null;
	}
	
	public void updateEarnings(String UUID, String job , String date, double money) {
		final String insertQuery = "UPDATE `earnings_all` SET `MONEY`='" + money + "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND DATE= '"+date+"' ";
		mg.executeUpdate(insertQuery); 
	}
 
	public double getEarnedAt(String UUID, String job, String date) {
		if(ExistEarningsDataToday(UUID, job, date)) {
			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND DATE= '"+date+"'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("MONEY"));
				}
				return 0;
			}); 
			return a.get();
		} else {
			createEarningsData(UUID, job, date);
		}
		return 0;
	}
	 
	public void createEarningsData(String UUID, String job, String date) { 
		final String insertQuery = "INSERT INTO earnings_all(UUID,JOB,DATE,MONEY) VALUES(?,?,?,?)";
		mg.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, job);
			ps.setString(3, date);
			ps.setInt(4, 0); 

		}); 
	}
	
	public void updatePoints(String UUID, double value) {
		final String insertQuery = "UPDATE `job_players` SET `POINTS`='" + value + "' WHERE UUID='" + UUID + "'";
		mg.executeUpdate(insertQuery); 
	}

	public void updateLevel(String UUID, int value, String job) {
		final String insertQuery = "UPDATE `job_stats` SET `LEVEL`='" + value + "' WHERE UUID='" + UUID + "' AND JOB='"
				+ job + "'";
		mg.executeUpdate(insertQuery); 
	}

	public void updateMax(String UUID, int value) {
		final String insertQuery = "UPDATE `job_players` SET `MAX`='" + value + "' WHERE UUID='" + UUID + "'";
		mg.executeUpdate(insertQuery); 
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
			ps.setString(2, UltimateJobs.getPlugin().getPluginManager().getDate());
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
 

	}

	public void createJobData(String UUID, String job) {
		String date = UltimateJobs.getPlugin().getPluginManager().getDate();
		final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";
		mg.executeUpdate(insertQuery, ps -> {
			ps.setString(1, UUID);
			ps.setString(2, job);
			ps.setString(3, date);
			ps.setInt(4, 1);
			ps.setDouble(5, 0);
			ps.setInt(6, 0);

		}); 
	}

	public boolean ExistJobData(String UUID, String job) {
		AtomicReference<String> a = new AtomicReference<String>();

		mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
			if (rs.next()) {
				a.set(rs.getString("DATE"));
			}
			return 1;
		}); 
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
		return a.get();
	}

	public void createPlayer(String UUID, String name) {
		String date = UltimateJobs.getPlugin().getPluginManager().getDate();
		createPlayerDetails(UUID, date); 
	}

	public void createPlayerDetails(String UUID, String date) {
		int max = UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs");
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
		return a.get() != null;
	}

}
