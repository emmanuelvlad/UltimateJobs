package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.util.concurrent.AtomicDouble;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.database.statements.SQLStatementAPI;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerDataAPI {

	private SQLStatementAPI mg = UltimateJobs.getPlugin().getSQLStatementAPI();
	private UltimateJobs plugin = UltimateJobs.getPlugin();

	public void createtables() {
		SQLStatementAPI s = UltimateJobs.getPlugin().getSQLStatementAPI();
		UltimateJobs.getPlugin().getExecutor().execute(() -> {

			s.executeUpdate("CREATE TABLE IF NOT EXISTS playerlist (UUID varchar(200), NAME varchar(200))");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS playersettings (UUID varchar(200), TYPE varchar(200), MODE varchar(200))");

			s.executeUpdate("CREATE TABLE IF NOT EXISTS pagedata (UUID varchar(200), ID varchar(200), PAGE int)");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS pagecat (UUID varchar(200), ID varchar(200), TYPE varchar(200))");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_stats (UUID varchar(200), JOB varchar(200), DATE varchar(200), LEVEL int, EXP double, BROKEN int)");

			s.executeUpdate("CREATE TABLE IF NOT EXISTS job_current (UUID varchar(200), JOB varchar(200))");
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_players (UUID varchar(200), DATE varchar(200), POINTS int, MAX int)");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS earnings_all (UUID varchar(200), JOB varchar(200), DATE varchar(200), MONEY double)");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS earnings_stats_per_action (UUID varchar(200),IDACTION varchar(200), JOB varchar(200), ID varchar(200), TIMES int, MONEY double)");

			s.executeUpdate("CREATE TABLE IF NOT EXISTS jobs_plugin (DATE varchar(200), PLY varchar(200))");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS job_dates_joined (UUID varchar(200), JOBID varchar(200), DATE varchar(200))");

			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS jobs_earnings_storage (UUID varchar(200),  AMOUNT double)");
		
			s.executeUpdate(
					"CREATE TABLE IF NOT EXISTS jobs_earnings_storage_dates (UUID varchar(200), CDATE varchar(200))");
		});
	}
	
	public void updateSalaryDate(String UUID, String date) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage_dates` SET `CDATE`='" + date + "' WHERE UUID='" + UUID
						+ "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage_dates(UUID,CDATE) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, date);
				});
			}

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("CDATE." + UUID, date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public String getSalaryDate(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();  
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage_dates WHERE UUID= '" + UUID + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("CDATE"));
						}
						return 0;
					});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("CDATE." + UUID);

		}
		return null;
	}
	

	public boolean existSalaryDate(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage_dates WHERE UUID= '" + UUID + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("CDATE"));
						}
						return 1;
					});
			return a.get() != null;

		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("CDATE." + UUID);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	public double getSalary(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (!existSalary(UUID)) {
			updateSalary(UUID, 0.0);
		}
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage WHERE UUID= '" + UUID + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getDouble("AMOUNT"));
						}
						return 0;
					});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("Salary." + UUID);

		}
		return 0.0;
	}
	
	public void updateSalary(String UUID, double sal) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage` SET `AMOUNT`='" + sal + "' WHERE UUID='" + UUID
						+ "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage(UUID,AMOUNT) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setDouble(2, 0.0);
				});
			}

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Salary." + UUID, sal);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public boolean existSalary(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM jobs_earnings_storage WHERE UUID= '" + UUID + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("UUID"));
						}
						return 1;
					});
			return a.get() != null;

		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("Salary." + UUID);
		}
		return false;
	}
	
	

	public String getJobDateJoined(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (!existDateJoined(UUID, job)) {
			updateDateJoinedOfJob(UUID, job, plugin.getPluginManager().getDateTodayFromCal());
		}
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_dates_joined WHERE UUID= '" + UUID + "' AND JOBID= '" + job + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 0;
					});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("JobDates." + UUID + ".Job." + job);

		}
		return null;
	}

	public void updateDateJoinedOfJob(String UUID, String job, String date) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			if (existDateJoined(UUID, job)) {

				final String insertQuery = "UPDATE `job_dates_joined` SET `DATE`='" + date + "' WHERE UUID='" + UUID
						+ "' AND JOBID= '" + job + "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO job_dates_joined(UUID,JOBID,DATE) VALUES(?,?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, job);
					ps.setString(3, plugin.getPluginManager().getDateTodayFromCal());
				});
			}

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("JobDates." + UUID + ".Job." + job, date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean existDateJoined(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_dates_joined WHERE UUID= '" + UUID + "' AND JOBID= '" + job + "'",
					rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 1;
					});
			return a.get() != null;

		} else if(mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("JobDates." + UUID + ".Job." + job);
		}
		return false;
	}

	public void createFirstPluginStart(String date) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "INSERT INTO jobs_plugin(DATE,PLY) VALUES(?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, date);
				ps.setString(2, "null");
			});

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Plugin.Data.Date", date);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean isFirstPluginStart() {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicBoolean a = new AtomicBoolean(false);

			mg.executeQuery("SELECT * FROM jobs_plugin", rs -> {

				if (rs.next()) {
					if (rs.getString("DATE") != null) {
						a.set(true);
					}
				}
				return 1;
			});

			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Plugin.Data.Date") != null;

		}
		return false;
	}

	public boolean isInPageCategory(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM pagecat WHERE UUID= '" + UUID + "' AND ID= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("TYPE"));
				}
				return 1;
			});
			return a.get() != null;

		}
		return false;
	}

	public boolean isInSettingData(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playersettings WHERE UUID= '" + UUID + "' AND TYPE= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("TYPE"));
				}
				return 1;
			});
			return a.get() != null;

		}
		return false;
	}

	public boolean existPlayer(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("NAME"));
				}
				return 1;
			});
			return a.get() != null;

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Fetcher." + UUID + ".Name") != null;

		}
		return false;
	}

	public void createSettingData(String UUID, String type, String value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "INSERT INTO playersettings(UUID,TYPE,MODE) VALUES(?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, type);
				ps.setString(3, value);
			});

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Settings." + UUID + "." + type, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateSettingData(String UUID, String type, String value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `playersettings` SET `MODE`='" + value + "' WHERE UUID='" + UUID
					+ "' AND TYPE= '" + type + "'";
			mg.executeUpdate(insertQuery);

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Settings." + UUID + "." + type, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String getSettingData(String UUID, String type) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playersettings WHERE UUID= '" + UUID + "' AND TYPE= '" + type + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("MODE"));
				}
				return 0;
			});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Settings." + UUID + "." + type);

		}
		return null;
	}

	public void updateName(String UUID, String name) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `playerlist` SET `NAME`='" + name + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Fetcher." + UUID + ".Name", name);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateUUID(String UUID, String name) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `playerlist` SET `UUID`='" + UUID + "' WHERE NAME='" + name + "'";
			mg.executeUpdate(insertQuery);

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Fetcher." + name.toUpperCase() + ".UUID", UUID);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public int getPageFromID(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			if (!isInPageData(UUID, id)) {
				final String insertQuery = "INSERT INTO pagedata(UUID,ID,PAGE) VALUES(?,?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, id);
					ps.setInt(3, 1);
				});
			}
			AtomicInteger a = new AtomicInteger();

			mg.executeQuery("SELECT * FROM pagedata WHERE UUID= '" + UUID + "' AND ID= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("PAGE"));
				}
				return 1;
			});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			if (cfg.getInt("Pages." + UUID + "." + id) == 0) {
				return 1;
			}
			return cfg.getInt("Pages." + UUID + "." + id);

		}
		return 1;
	}

	public boolean isInPageData(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM pagedata WHERE UUID= '" + UUID + "' AND ID= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("ID"));
				}
				return 1;
			});
			return a.get() != null;

		} else if (mode.equalsIgnoreCase("YML")) {

		}
		return false;
	}

	public String getCategoryData(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM pagecat WHERE UUID= '" + UUID + "' AND ID= '" + id + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("TYPE"));
				}
				return 1;
			});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("PagesCate." + UUID + "." + id);

		}
		return null;
	}

	public void createPageCategory(String UUID, String id, String value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "INSERT INTO pagecat(UUID,ID,TYPE) VALUES(?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, id);
				ps.setString(3, value);
			});

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("PagesCate." + UUID + "." + id, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updatePageCategory(String UUID, String id, String value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `pagecat` SET `TYPE`='" + value + "' WHERE UUID='" + UUID + "' AND ID= '"
					+ id + "'";
			mg.executeUpdate(insertQuery);

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("PagesCate." + UUID + "." + id, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void updatePageFromID(String UUID, String id, int value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();

		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `pagedata` SET `PAGE`='" + value + "' WHERE UUID='" + UUID
					+ "' AND ID= '" + id + "'";
			mg.executeUpdate(insertQuery);

		} else if (mode.equalsIgnoreCase("YML")) {

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			cfg.set("Pages." + UUID + "." + id, value);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void addPlayerToPlayersList(String UUID, String name) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "INSERT INTO playerlist(UUID,NAME) VALUES(?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, name);

			});

		} else if (mode.equalsIgnoreCase("YML")) {

			List<String> list = getPlayerList();

			File file = plugin.getPlayerDataFile().getfile();
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			list.add(UUID);

			cfg.set("Players", list);
			cfg.set("Fetcher." + name.toUpperCase() + ".UUID", UUID);
			cfg.set("Fetcher." + UUID + ".Name", name.toUpperCase());
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public List<String> getPlayerList() {
		FileConfiguration cfg = plugin.getPlayerDataFile().get();
		return cfg.getStringList("Players");
	}

	public void addOnePageFromID(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		int value = getPageFromID(UUID, id);
		if (mode.equalsIgnoreCase("SQL")) {
			updatePageFromID(UUID, id, value + 1);
		} else if (mode.equalsIgnoreCase("YML")) {
			updatePageFromID(UUID, id, value + 1);
		}
	}

	public void removeOnePageFromID(String UUID, String id) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		int value = getPageFromID(UUID, id);
		if (mode.equalsIgnoreCase("SQL")) {
			updatePageFromID(UUID, id, value - 1);
		} else if (mode.equalsIgnoreCase("YML")) {
			updatePageFromID(UUID, id, value - 1);
		}
	}

	public String getUUIDByName(String name) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE NAME= '" + name + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("UUID"));
				}
				return 0;
			});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Fetcher." + name.toUpperCase() + ".UUID");
		}
		return null;
	}

	public String getNameByUUID(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM playerlist WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("NAME"));
				}
				return 0;
			});
			return a.get();

		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Fetcher." + UUID + ".Name");
		}
		return null;
	}

	public void updateEarningsTimesOf(String UUID, String job, String id, int time, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `TIMES`='" + time + "' WHERE UUID='"
					+ UUID + "' AND JOB= '" + job + "' AND ID= '" + id + "' AND IDACTION= '" + action + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times", time);
			save(cfg, file);

		}
	}

	public void updateEarningsAmountOf(String UUID, String job, String id, double money, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_stats_per_action` SET `MONEY`='" + money + "' WHERE UUID='"
					+ UUID + "' AND JOB= '" + job + "' AND ID= '" + id + "' AND IDACTION= '" + action + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money", money);
			save(cfg, file);

		}
	}

	public boolean ExistEarningsOfBlock(String UUID, String job, String id, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
					+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
						if (rs.next()) {
							a.set(rs.getString("ID"));
						}
						return 1;
					});
			return a.get() != null;
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times");
		}
		return false;
	}

	public int getBrokenTimesOfBlock(String UUID, String job, String id, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			if (ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicInteger a = new AtomicInteger();

				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getInt("TIMES"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			}
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			if (cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times")) {
				return cfg.getInt("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times");
			} else {
				cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Times", 0);
				save(cfg, file);
			}
		}
		return 0;
	}

	public double getEarnedOfBlock(String UUID, String job, String id, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			if (ExistEarningsOfBlock(UUID, job, id, action)) {
				AtomicDouble a = new AtomicDouble();

				mg.executeQuery("SELECT * FROM earnings_stats_per_action WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND ID= '" + id + "' AND IDACTION= '" + action + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getDouble("MONEY"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsOfBlock(UUID, job, id, action);
			}

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			if (cfg.contains("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money")) {
				return cfg.getDouble("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money");
			} else {
				cfg.set("Earnings." + UUID + "." + job + "." + id + ".Action." + action + ".Money", 0);
				save(cfg, file);
			}

		}
		return 0;
	}

	public void createEarningsOfBlock(String UUID, String job, String id, String action) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
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
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job + "' AND DATE= '"
					+ date + "'", rs -> {
						if (rs.next()) {
							a.set(rs.getString("DATE"));
						}
						return 1;
					});
			return a.get() != null;
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Earnings." + UUID + "." + date + "." + job) != null;
		}
		return false;
	}

	public void createEarningsData(String UUID, String job, String date) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
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
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_players` SET `POINTS`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Player." + UUID + ".Points", value);
			save(cfg, file);
		}
	}

	public void updateLevel(String UUID, int value, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_stats` SET `LEVEL`='" + value + "' WHERE UUID='" + UUID
					+ "' AND JOB='" + job + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Jobs." + UUID + "." + job + ".Level", value);
			save(cfg, file);

		}
	}

	public void updateMax(String UUID, int value) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `job_players` SET `MAX`='" + value + "' WHERE UUID='" + UUID + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Player." + UUID + ".Max", value);
			save(cfg, file);

		}
	}

	public void savePlayer(JobsPlayer pl, String UUID) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

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
				ps.setString(2, UltimateJobs.getPlugin().getDate());
				ps.setDouble(3, points);
				ps.setInt(4, max);

			});
			
			double sal = pl.getSalary();
			
			String sat = pl.getSalaryDate();
			
			updateSalaryDate(""+UUID, sat);
			
			if (existSalary(UUID)) {

				final String insertQuery = "UPDATE `jobs_earnings_storage` SET `AMOUNT`='" + sal + "' WHERE UUID='" + UUID
						+ "'";
				mg.executeUpdate(insertQuery);

			} else {
				final String insertQuery = "INSERT INTO jobs_earnings_storage(UUID,AMOUNT) VALUES(?,?)";
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setDouble(2, 0.0);
				});
			}

			final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";

			for (String job : owned) {

				Job j = plugin.getJobCache().get(job);

				JobStats stats = pl.getStatsOf(job);

				stats.getEarningsList().forEach((key, value) -> {

					updateEarnings(UUID, job, key, value);
				});
				
				plugin.getPlayerChunkAPI().savePlayerChunks(UUID, j);

				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getBrokenTimes();
				String date = stats.getDate();
				 
				String jd = stats.getJoinedDate();

				updateDateJoinedOfJob(UUID, job, jd); 
				
				mg.executeUpdate(insertQuery, ps -> {
					ps.setString(1, UUID);
					ps.setString(2, job);
					ps.setString(3, date);
					ps.setInt(4, level);
					ps.setDouble(5, exp);
					ps.setInt(6, broken);

				});

				for (JobAction action : j.getActionList()) {

					stats.getBrokenList().forEach((key, value) -> {

						updateEarningsAmountOf(UUID, job, key, value, "" + action);

					});

					stats.getBrokenTimesOfIDList().forEach((key, value) -> {

						updateEarningsTimesOf(UUID, job, key, value, "" + action);

					});

				}

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
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			Collection<String> current = pl.getCurrentJobs();
			Collection<String> owned = pl.getOwnJobs();
			int max = pl.getMaxJobs();
			double points = pl.getPoints();
			double sal = pl.getSalary();
			
			String sat = pl.getSalaryDate();
			
			updateSalaryDate(""+UUID, sat);
			
			cfg.set("Player." + UUID + ".Points", points);
			cfg.set("Player." + UUID + ".Date", plugin.getDate());
			cfg.set("Player." + UUID + ".Max", max);
			cfg.set("Salary." + UUID, sal);

			ArrayList<String> newowned = new ArrayList<String>();

			for (String job : owned) {

				Job j = plugin.getJobCache().get(job);

				JobStats stats = pl.getStatsOf(job);

				stats.getEarningsList().forEach((key, value) -> {
					updateEarnings(UUID, job, key, value);
				});

				int level = stats.getLevel();
				double exp = stats.getExp();
				int broken = stats.getBrokenTimes();
				String date = stats.getDate();

				cfg.set("Jobs." + UUID + "." + job + ".Level", level);
				cfg.set("Jobs." + UUID + "." + job + ".Date", date);
				cfg.set("Jobs." + UUID + "." + job + ".Broken", broken);
				cfg.set("Jobs." + UUID + "." + job + ".Exp", exp);

				newowned.add(job);
				
				plugin.getPlayerChunkAPI().savePlayerChunks(UUID, j);

				for (JobAction action : j.getActionList()) {

					stats.getBrokenList().forEach((key, value) -> {

						updateEarningsAmountOf(UUID, job, key, value, "" + action);

					});

					stats.getBrokenTimesOfIDList().forEach((key, value) -> {

						updateEarningsTimesOf(UUID, job, key, value, "" + action);

					});

				}

			}

			cfg.set("Player." + UUID + ".Owned", newowned);
			cfg.set("Player." + UUID + ".Current", current);

			save(cfg, file);
		}

	}

	public void updateEarnings(String UUID, String job, String date, double money) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			final String insertQuery = "UPDATE `earnings_all` SET `MONEY`='" + money + "' WHERE UUID='" + UUID
					+ "' AND JOB= '" + job + "' AND DATE= '" + date + "' ";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			cfg.set("EarnedDate." + UUID + "." + date + "." + job, money);
			save(cfg, file);

		}
	}

	public double getEarnedAt(String UUID, String job, String date) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			if (ExistEarningsDataToday(UUID, job, date)) {
				AtomicDouble a = new AtomicDouble();

				mg.executeQuery("SELECT * FROM earnings_all WHERE UUID= '" + UUID + "' AND JOB= '" + job
						+ "' AND DATE= '" + date + "'", rs -> {
							if (rs.next()) {
								a.set(rs.getDouble("MONEY"));
							}
							return 0;
						});
				return a.get();
			} else {
				createEarningsData(UUID, job, date);
			}

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("EarnedDate." + UUID + "." + date + "." + job);

		}
		return 0.9;
	}

	public void updateBrokenTimes(String UUID, String job, int val) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			final String insertQuery = "UPDATE `job_stats` SET `BROKEN`='" + val + "' WHERE UUID='" + UUID
					+ "' AND JOB='" + job + "'";
			mg.executeUpdate(insertQuery);
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			cfg.set("Jobs." + UUID + "." + job + ".Broken", val);
			save(cfg, file);
		}
	}

	public void createJobData(String UUID, String job) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			String date = UltimateJobs.getPlugin().getDate();
			final String insertQuery = "INSERT INTO job_stats(UUID,JOB,DATE,LEVEL,EXP,BROKEN) VALUES(?,?,?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, job);
				ps.setString(3, date);
				ps.setInt(4, 1);
				ps.setDouble(5, 0);
				ps.setInt(6, 0);

			});

		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();

			String date = plugin.getDate();

			cfg.set("Jobs." + UUID + "." + job + ".Date", date);
			cfg.set("Jobs." + UUID + "." + job + ".Level", 1);
			cfg.set("Jobs." + UUID + "." + job + ".Exp", 0);
			cfg.set("Jobs." + UUID + "." + job + ".Broken", 0);
			save(cfg, file);

		}
	}

	public boolean ExistJobData(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			});
			return a.get() != null;
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Jobs." + UUID + "." + job + ".Date") != null;
		}
		return false;
	}

	public int getLevelOf(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("LEVEL"));
				}
				return 1;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Jobs." + UUID + "." + job + ".Level");
		}
		return 1;
	}

	public double getExpOf(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicDouble a = new AtomicDouble();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getDouble("EXP"));
				}
				return 0;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getDouble("Jobs." + UUID + "." + job + ".Exp");
		}
		return 0;
	}

	public int getBrokenOf(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("BROKEN"));
				}
				return 0;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {

			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Jobs." + UUID + "." + job + ".Broken");

		}
		return 0;
	}

	public String getDateOf(String UUID, String job) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "' AND JOB= '" + job + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 0;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getString("Jobs." + UUID + "." + job + ".Date");
		}
		return null;
	}

	public ArrayList<String> getOwnedJobs(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_stats WHERE UUID= '" + UUID + "'", rs -> {

				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}

				return 1;
			});

			return (ArrayList<String>) jobs;
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return (ArrayList<String>) cfg.getStringList("Player." + UUID + ".Owned");
		}
		return null;
	}

	public ArrayList<String> getCurrentJobs(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			Collection<String> jobs = new ArrayList<String>();
			mg.executeQuery("SELECT * FROM job_current WHERE UUID= '" + UUID + "'", rs -> {

				while (rs.next()) {
					jobs.add(rs.getString("JOB"));
				}

				return 1;
			});

			return (ArrayList<String>) jobs;
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return (ArrayList<String>) cfg.getStringList("Player." + UUID + ".Current");
		}
		return null;
	}

	public int getPoints(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("POINTS"));
				}
				return 0;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Player." + UUID + ".Points");
		}
		return 0;
	}

	public int getMaxJobs(String UUID) {
		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {
			AtomicInteger a = new AtomicInteger(0);

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getInt("MAX"));
				}
				return 0;
			});
			return a.get();
		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();

			return cfg.getInt("Player." + UUID + ".Max");
		}
		return 0;
	}

	public void createPlayer(String UUID, String name) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			String date = UltimateJobs.getPlugin().getDate();
			createPlayerDetails(UUID, date);

		} else if (mode.equalsIgnoreCase("YML")) {
			String date = plugin.getDate();
			createPlayerDetails(UUID, date);
		}
	}

	public void createPlayerDetails(String UUID, String date) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			int max = UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs");
			final String insertQuery = "INSERT INTO job_players(UUID,DATE,POINTS,MAX) VALUES(?,?,?,?)";
			mg.executeUpdate(insertQuery, ps -> {
				ps.setString(1, UUID);
				ps.setString(2, "" + date);
				ps.setInt(3, 0);
				ps.setInt(4, max);

			});

		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			File file = plugin.getPlayerDataFile().getfile();
			int max = UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs");
			ArrayList<String> list = new ArrayList<String>();
			cfg.set("Player." + UUID + ".Points", 0);
			cfg.set("Player." + UUID + ".Max", max);
			cfg.set("Player." + UUID + ".Date", date);
			cfg.set("Player." + UUID + ".Owned", list);
			cfg.set("Player." + UUID + ".Current", list);
			save(cfg, file);
		}
	}

	public boolean ExistPlayer(String UUID) {

		String mode = UltimateJobs.getPlugin().getPluginMode();
		if (mode.equalsIgnoreCase("SQL")) {

			AtomicReference<String> a = new AtomicReference<String>();

			mg.executeQuery("SELECT * FROM job_players WHERE UUID= '" + UUID + "'", rs -> {
				if (rs.next()) {
					a.set(rs.getString("DATE"));
				}
				return 1;
			});
			return a.get() != null;

		} else if (mode.equalsIgnoreCase("YML")) {
			FileConfiguration cfg = plugin.getPlayerDataFile().get();
			return cfg.getString("Player." + UUID + ".Date") != null;
		}
		return false;
	}

	public void save(FileConfiguration cfg, File file) {
		if (file != null) {
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}
	}

}
