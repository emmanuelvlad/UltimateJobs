package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.statements.SQLStatementAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerDataAPI {
	
	private SQLStatementAPI mg = SimpleAPI.getInstance().getSQLStatementAPI();
	private UltimateJobs plugin = UltimateJobs.getPlugin();

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
					"CREATE TABLE IF NOT EXISTS earnings_stats_per_action (UUID varchar(200),IDACTION varchar(200), JOB varchar(200), ID varchar(200), TIMES int, MONEY double)");
 
		});
	}
  
	public void updateEarningsTimesOf(String UUID, String job , String id, int time, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `TIMES`='" + time + "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' AND IDACTION= '"+action+"'";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
		 
				cfg.set("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Times", time);
				save(cfg, file);
			
		}
	}
	
	public void updateEarningsAmountOf(String UUID, String job , String id, double money, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `MONEY`='" + money+ "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' AND IDACTION= '"+action+"'";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Money", money);
			save(cfg, file);
			
		}
	}
	
	public boolean ExistEarningsOfBlock(String UUID, String job, String id, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();
	
			mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' AND IDACTION= '"+action+"'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("ID"));
				}
				return 1;
			}); 
			return a.get() != null;
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			return cfg.contains("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Times");
		}
		return false;
	}
	
	public int getBrokenTimesOfBlock(String UUID, String job, String id, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			if(ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicInteger a = new AtomicInteger();
	
				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' AND IDACTION= '"+action+"'", rs -> {
					if (rs.next()) {
						a.set(rs.getInt("TIMES"));
					}
					return 0;
				}); 
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			} 
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			if(cfg.contains("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Times")) {
				return cfg.getInt("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Times");
			} else {
				cfg.set("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Times", 0);
				save(cfg, file);
			}
		}
		return 0;
	}
	
	public double getEarnedOfBlock(String UUID, String job, String id, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			if(ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicDouble a = new AtomicDouble();
	
				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND ID= '"+id+"' AND IDACTION= '"+action+"'", rs -> {
					if (rs.next()) {
						a.set(rs.getDouble("MONEY"));
					}
					return 0;
				}); 
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			}
			 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			if(cfg.contains("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Money")) {
				return cfg.getDouble("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Money");
			} else {
				cfg.set("Earnings."+UUID+"."+job+"."+id+".Action."+action+".Money", 0);
				save(cfg, file);
			}
			
		}
		return 0;
	}
	 
	public void createEarningsOfBlock(String UUID, String job, String id, String action) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "INSERT INTO earnings_stats_per_action(UUID,IDACTION,JOB,ID,TIMES,MONEY) VALUES(?,?,?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, action);
				ps.setString(3, job);
				ps.setString(4, id);
				ps.setInt(5, 0); 
				ps.setDouble(6, 0); 
			}); 
		}  
	}
	
	public boolean ExistEarningsDataToday(String UUID, String job, String date) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();
	
			mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND DATE= '"+date+"'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			}); 
			return a.get() != null;
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			return cfg.getString("Earnings."+UUID+"."+date+"."+job) != null;
		}
		return false;
	}
	
	public void updateEarnings(String UUID, String job , String date, double money) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_all` SET `MONEY`='" + money + "' WHERE UUID='" + UUID + "' AND JOB= '" + job + "' AND DATE= '"+date+"' ";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("Earnings."+UUID+"."+date+"."+job, money); 
			save(cfg, file);
			
		}
	}
 
	public double getEarnedAt(String UUID, String job, String date) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
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
		 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			if(cfg.contains("Earnings."+UUID+"."+date+"."+job)) {
				return cfg.getDouble("Earnings."+UUID+"."+date+"."+job);
			} else {
				cfg.set("Earnings."+UUID+"."+date+"."+job, 0);
				save(cfg, file);
			}
			
		}
		return 0;
	}
	 
	public void createEarningsData(String UUID, String job, String date) { 
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "INSERT INTO earnings_all(UUID,JOB,DATE,MONEY) VALUES(?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
				ps.setString(3, date);
				ps.setInt(4, 0); 
	
			}); 
		} 
	} 
	
	public void updatePoints(String UUID, double value) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_players` SET `POINTS`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			cfg.set("Player."+UUID+".Points", value); 
			save(cfg, file);
		}
	}

	public void updateLevel(String UUID, int value, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_stats` SET `LEVEL`='" + value + "' WHERE UUID='" + UUID + "' AND JOB='"
					+ job + "'";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			cfg.set("Jobs."+UUID+"."+job+".Level", value); 
			save(cfg, file);
			
		}
	}

	public void updateMax(String UUID, int value) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_players` SET `MAX`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery); 
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			
			cfg.set("Player."+UUID+".Max", value);
			save(cfg, file);
			
		}
	}

	public void savePlayer(JobsPlayer pl, String UUID) {
		
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
		
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
				
				Job j = plugin.getJobCache().get(job);
				
				JobStats stats = pl.getStatsOf(job);
				
				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getBrokenTimes();
				String date = stats.getDate();
	
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, job);
					ps.setString(3, date);
					ps.setInt(4, level);
					ps.setDouble(5, exp);
					ps.setInt(6, broken);
	
				});
				
				for(JobAction action : j.getActionList()) {
				
			 
						stats.getBrokenList().forEach((key, value) -> {
							
							updateEarningsAmountOf(UUID, job, key, value, ""+action); 
							
						});
						
						stats.getBrokenTimesOfIDList().forEach((key, value) -> {
							 
							updateEarningsTimesOf(UUID, job, key, value, ""+action);
							
						});
			
					 
					
				}
				
				stats.getEarningsList().forEach((key, value) -> {
					 
					updateEarnings(UUID, job, key, value);
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
		} else if(mode.equalsIgnoreCase("YML")) {
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
				
				Job j = plugin.getJobCache().get(job);
				
				JobStats stats = pl.getStatsOf(job);
				
				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getBrokenTimes();
				String date = stats.getDate();
				
				cfg.set("Jobs."+UUID+"."+job+".Level", level);
				cfg.set("Jobs."+UUID+"."+job+".Date", date);
				cfg.set("Jobs."+UUID+"."+job+".Broken", broken);
				cfg.set("Jobs."+UUID+"."+job+".Exp", exp);

				newowned.add(job); 
				

				for(JobAction action : j.getActionList()) {
				 
						stats.getBrokenList().forEach((key, value) -> {
							
							updateEarningsAmountOf(UUID, job, key, value, ""+action); 
							
						});
						
						stats.getBrokenTimesOfIDList().forEach((key, value) -> {
							 
							updateEarningsTimesOf(UUID, job, key, value, ""+action);
							
						});
			
					 
					
				}
				
				stats.getEarningsList().forEach((key, value) -> {
					 
					updateEarnings(UUID, job, key, value);
				});
			}
	 
			cfg.set("Player."+UUID+".Owned", newowned);
			cfg.set("Player."+UUID+".Current", current);
			
			save(cfg, file);
		}

	}

	public void createJobData(String UUID, String job) {
		
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
		
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
		
			} else if(mode.equalsIgnoreCase("YML")) {
				
				FileConfiguration cfg = plugin.getPlayerDataFile().get();
				File file = plugin.getPlayerDataFile().getfile();
				
				String date =plugin.getPluginManager().getDate();
				
				cfg.set("Jobs."+UUID+"."+job+".Date", date);
				cfg.set("Jobs."+UUID+"."+job+".Level", 1);
				cfg.set("Jobs."+UUID+"."+job+".Exp", 0);
				cfg.set("Jobs."+UUID+"."+job+".Broken", 0);
				save(cfg, file);
				
			}
		}

	public boolean ExistJobData(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();
	
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			}); 
			return a.get() != null;
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return cfg.getString("Jobs."+UUID+"."+job+".Date") != null;
		}
		return false;
	}

	public int getLevelOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);
	
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("LEVEL"));
				}
				return 1;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return cfg.getInt("Jobs."+UUID+"."+job+".Level");
		}
		return 1;
	}

	public double getExpOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicDouble a = new AtomicDouble();
	
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("EXP"));
				}
				return 0;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return cfg.getDouble("Jobs."+UUID+"."+job+".Exp");
		}
		return 0;
	}

	public int getBrokenOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);
	
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("BROKEN"));
				}
				return 0;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return cfg.getInt("Jobs."+UUID+"."+job+".Broken");
			
		}
		return 0;
	}

	public String getDateOf(String UUID, String job) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();
	
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 0;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return cfg.getString("Jobs."+UUID+"."+job+".Date");
		}
		return null;
	}

	public ArrayList<String> getOwnedJobs(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "'", rs -> {
			 
					while (rs.next()) {
						jobs.add(rs.getString("JOB"));
					}
	 
				return 1;
			});
			  
			return (ArrayList<String>) jobs;
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Owned");
		}
		return null;
	}
	 
	
	public ArrayList<String> getCurrentJobs(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_current WHERE UUID= '" + UUID + "'", rs -> {
				 
					while (rs.next()) {
						jobs.add(rs.getString("JOB"));
					}
	 
				return 1;
			});
			  
			return (ArrayList<String>) jobs;
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			 
			return (ArrayList<String>) cfg.getStringList("Player."+UUID+".Current");
		}
		return null;
	}

	public int getPoints(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
			if(mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);
	
			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("POINTS"));
				}
				return 0;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			
			return cfg.getInt("Player."+UUID+".Points");
		}
			return 0;
	}

	public int getMaxJobs(String UUID) {
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);
	
			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("MAX"));
				}
				return 0;
			}); 
			return a.get();
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get(); 
			
			return cfg.getInt("Player."+UUID+".Max");
		}
		return 0;
	}

	public void createPlayer(String UUID, String name) {
		
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
			
			String date = UltimateJobs.getPlugin().getPluginManager().getDate();
			createPlayerDetails(UUID, date); 
	
		} else if(mode.equalsIgnoreCase("YML")) {
			String date = plugin.getPluginManager().getDate();
			createPlayerDetails(UUID, date); 
		}
	}

	public void createPlayerDetails(String UUID, String date) {
		
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
		
			int max = UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs");
			final String insertQuery = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, "" + date);
				ps.setInt(3, 0);
				ps.setInt(4, max);
	
			});
			
		} else if(mode.equalsIgnoreCase("YML")) {
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
	}

	public boolean ExistPlayer(String UUID) {
		
		String mode = SimpleAPI.getPlugin().getPluginMode();
		if(mode.equalsIgnoreCase("SQL")) {
		
			AtomicReference<String> a = new AtomicReference<String>();
	
			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			}); 
			return a.get() != null;
			
		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();  
			return cfg.getString("Player."+UUID+".Date") != null;
		}
		return false;
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
