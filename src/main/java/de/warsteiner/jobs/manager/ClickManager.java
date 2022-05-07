package de.warsteiner.jobs.manager;

import java.util.List; 

import org.bukkit.configuration.file.FileConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class ClickManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
	private PluginAPI up = SimpleAPI.getInstance().getAPI(); 
	private GuiManager gui;

	public ClickManager(UltimateJobs plugin, FileConfiguration fileConfiguration, GuiManager gui) {
		this.plugin = plugin;
		this.gui = gui; 
	}

	public void executeCustomItemInSubMenu(Job job, String display, final Player player, String prefix,
			FileConfiguration config) {
		String item = isCustomItem(display, prefix, config, ""+player.getUniqueId());
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId());
		if (!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = config.getString(prefix + "." + item + ".Action");
			if (action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 2);
			} else if (action.equalsIgnoreCase("BACK")) {
				plugin.getAPI().playSound("REOPEN_MAIN_GUI", player);
				plugin.getGUI().createMainGUIOfJobs(player, UpdateTypes.REOPEN);
			} else if (action.equalsIgnoreCase("LEAVE")) {
				api.playSound("LEAVE_SINGLE", player);
				jb.remCurrentJob(job.getConfigID());
			 
				plugin.getGUI().createMainGUIOfJobs(player, UpdateTypes.REOPEN);

				player.sendMessage(jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Left_Job").replaceAll("<job>", job.getDisplay(""+player.getUniqueId())));
			} else if (action.equalsIgnoreCase("COMMAND")) {
				player.closeInventory();
				String cmd = config.getString(prefix + "." + item + ".Command").replaceAll("<job>", job.getConfigID());
				player.performCommand(cmd);
			}
		}
	}

	public void joinJob(Player player, String job, JobsPlayer jb, String name, String dis, Job j) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		plugin.getPlayerAPI().updateJobs(job.toUpperCase(), jb, "" + player.getUniqueId());
		jb.addCurrentJob(job);
		api.playSound("JOB_JOINED", player);
		new BukkitRunnable() {
			public void run() {
				gui.setCustomitems(player, player.getName(), player.getOpenInventory(), "Main_Custom.",
						cfg.getStringList("Main_Custom.List"), name, cfg, plugin.getJobCache().get(job));
				gui.setMainInventoryJobItems(player.getOpenInventory(), player, name);
			}
		}.runTaskLater(plugin, 1);

		player.sendMessage(up.toHex(jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "Other.Joined").replaceAll("<job>", j.getDisplay(""+player.getUniqueId()))));
	}

	public void executeJobClickEvent(String display, Player player) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		List<String> jobs = plugin.getLoaded();
		String UUID = ""+player.getUniqueId();
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		for (int i = 0; i <= jobs.size() - 1; i++) {
			Job j = plugin.getJobCache().get(jobs.get(i));
			String dis = j.getDisplay(UUID);
			if (display.equalsIgnoreCase(dis)) {
				String job = j.getConfigID();

				String name =  jb.getLanguage().getStringFromLanguage(jb.getUUID(), cfg.getString("Main_Name"));
				if (api.canBuyWithoutPermissions(player, j)) {

					List<String> d = api.canGetJobWithSubOptions(player, j);
					if (d == null) {

						if (jb.ownJob(job) == true || api.canByPass(player, j) == true) {

							if (jb.isInJob(job)) {
								gui.createSettingsGUI(player, j, UpdateTypes.OPEN);
								return;
							} else {

								int max = jb.getMaxJobs();

								if (jb.getCurrentJobs().size() <= max) {
									joinJob(player, job, jb, name, dis, j);
									return;
								} else {
									player.sendMessage( jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Full").replaceAll("<job>", j.getDisplay(UUID)));
									return;
								}

							}
						} else {
							double money = j.getPrice();
							if (plugin.getEco().getBalance(player) >= money) {

								if (plugin.getFileManager().getConfig().getBoolean("Jobs.AreYouSureGUIonBuy")) {
									gui.createAreYouSureGUI(player, j, UpdateTypes.OPEN);
									return;
								} else {
									buy(money, player, jb, j);
									return;
								}

							} else {
								player.sendMessage( jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Not_Enough_Money").replaceAll("<job>", j.getDisplay(UUID)));
								return;
							}
						}
					}
				} else {
					player.sendMessage(j.getPermMessage(UUID).replaceAll("<job>", j.getDisplay(UUID)));
					return;

				}

			}
		}
	}

	public void buy(double money, Player player, JobsPlayer jb, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		plugin.getEco().withdrawPlayer(player, money);
		jb.addOwnedJob(job.getConfigID());

		plugin.getPlayerAPI().updateJobs(job.getConfigID().toUpperCase(), jb, "" + player.getUniqueId());

		api.playSound("JOB_BOUGHT", player);

		String title = player.getOpenInventory().getTitle();

		if (title.equalsIgnoreCase(up.toHex(cfg.getString("Main_Name")).replaceAll("&", "§"))) {
			gui.UpdateMainInventoryItems(player, title);
		} else {
			gui.createMainGUIOfJobs(player, UpdateTypes.REOPEN);
		}

		player.sendMessage( jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "Other.Bought_Job").replaceAll("<job>", job.getDisplay(""+player.getUniqueId())));
	}
	
	public String isCustomItem(String display, String path, FileConfiguration config, String UUID) {
		List<String> custom_items = config.getStringList(path + ".List");
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		for (String b : custom_items) {
			String real =  jb.getLanguage().getStringFromLanguage(jb.getUUID(), config.getString(path + "." + b + ".Display"));
			String dis = up.toHex(real.replaceAll("&", "§"));
			if (display.equalsIgnoreCase(dis))
				return b;
		}
		return "NOT_FOUND";
	}

	public void executeCustomItem(String display, final Player player, String name, FileConfiguration config) {
		String item = isCustomItem(display, name, config, ""+player.getUniqueId());
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId());
		if (!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = config.getString(name + "." + item + ".Action");
			if (action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 2);
			} else if (action.equalsIgnoreCase("LEAVE")) {
				if (jb.getCurrentJobs().size() >= 1) {
					api.playSound("LEAVE_ALL", player);
					jb.updateCurrentJobs(null);
					gui.UpdateMainInventoryItems(player, jb.getLanguage().getStringFromLanguage(jb.getUUID(), plugin.getFileManager().getGUI().getString("Main_Name")));
					player.sendMessage(jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Leave_All").replaceAll("<job>", display));
				} else {
					player.sendMessage(jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Already_Left_All").replaceAll("<job>", display));
				}
			} else if (action.equalsIgnoreCase("COMMAND")) {
				player.closeInventory();
				String cmd = config.getString(name + "." + item + ".Command");
				player.performCommand(cmd);
			} else if (action.equalsIgnoreCase("BACK")) {
				plugin.getAPI().playSound("REOPEN_MAIN_GUI", player);
				plugin.getGUI().createMainGUIOfJobs(player, UpdateTypes.REOPEN);
			}
		}
	}

}
