package de.warsteiner.jobs.manager;

import java.util.List;
 
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer; 

public class ClickManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
	private PluginAPI up = UltimateAPI.getInstance().getAPI();
	private YamlConfiguration cfg;
	private GuiManager gui;

	public ClickManager(UltimateJobs plugin, YamlConfiguration cfg, GuiManager gui) {
		this.plugin = plugin;
		this.gui = gui;
		this.cfg = cfg;
	}

	public void executeCustomItemInSubMenu(Job job, String display, final Player player, String prefix,
			YamlConfiguration cf) {
		String item = api.isCustomItem(display, player, prefix, cf);
		JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get("" + player.getUniqueId());
		if (!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = cf.getString(prefix + "." + item + ".Action");
			if (action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 2);
			} else if (action.equalsIgnoreCase("BACK")) {
				plugin.getGUI().createMainGUIOfJobs(player);
			} else if (action.equalsIgnoreCase("LEAVE")) {
				api.playSound("LEAVE_SINGLE", player);
				jb.remoCurrentJob(job.getID());
				gui.createMainGUIOfJobs(player);
				player.sendMessage(plugin.getAPI().getMessage("Left_Job").replaceAll("<job>", job.getDisplay()));
			} else if (action.equalsIgnoreCase("COMMAND")) {
				player.closeInventory();
				String cmd = cf.getString(prefix + "." + item + ".Command").replaceAll("<job>", job.getID()); 
				player.performCommand(cmd);
			}  
		}
	}
	
	public void joinJob(Player player, String job, JobsPlayer jb, String name, String dis) { 
		plugin.getPlayerManager().updateJobs(job.toUpperCase(), jb, "" + player.getUniqueId()); 
		jb.addCurrentJob(job); 
		api.playSound("JOB_JOINED", player);
		new BukkitRunnable() {
			public void run() {
				gui.setCustomitems(player, player.getName(), player.getOpenInventory(),
						"Main_Custom.", cfg.getStringList("Main_Custom.List"), name, cfg);
				gui.setMainInventoryJobItems(player.getOpenInventory(), player, name);
			}
		}.runTaskLater(plugin, 1);

		player.sendMessage(api.getMessage("Joined").replaceAll("<job>", dis));
	}

	public void executeJobClickEvent(String display, Player player) {

		List<Job> jobs = plugin.getLoaded();

		JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get("" + player.getUniqueId());

		for (int i = 0; i <= jobs.size() - 1; i++) {
			Job j = jobs.get(i);
			String dis = j.getDisplay();
			if (display.equalsIgnoreCase(dis)) {
				String job = j.getID();

				String name = cfg.getString("Main_Name");
				if (api.canBuyWithoutPermissions(player, j)) {
					if (jb.ownJob(job)) {

						if (jb.isInJob(job)) {
							gui.createSettingsGUI(player, j);
							return;
						} else {

							int max = jb.getMaxJobs();
							 
							if (jb.getCurrentJobs().size() <= max) {
								 joinJob(player, job, jb, name, dis);
								 return;
							} else {
								player.sendMessage(api.getMessage("Full").replaceAll("<job>", dis));
								return;
							}
							
							}
						}
					else {
						double money = j.getPrice();
						if (plugin.getEco().getBalance(player) >= money) {
							
							if(cfg.getBoolean("Jobs.AreYouSureGUIonBuy")) {
								gui.createAreYouSureGUI(player, j);
								return;
							} else {
								buy(money, player, jb, j);
								return;
							}

							 
						} else {
							player.sendMessage(api.getMessage("Not_Enough_Money").replaceAll("<job>", dis));
							return;
						}

					}
				} else {
					player.sendMessage(
							up.toHex(j.getPermMessage().replaceAll("&", "§").replaceAll("<prefix>", api.getPrefix())));
					return;
				 
		}
				 
			}	 
		}
		}
 
	public void buy(double money, Player player, JobsPlayer jb, Job job) {
		plugin.getEco().withdrawPlayer(player, money);
		jb.addOwnedJob(job.getID());

		plugin.getPlayerManager().updateJobs(job.getID().toUpperCase(), jb, "" + player.getUniqueId());

		api.playSound("JOB_BOUGHT", player);
	
		String title = player.getOpenInventory().getTitle();
		
		if(title.equalsIgnoreCase(up.toHex(cfg.getString("Main_Name")).replaceAll("&", "§"))) {
			gui.UpdateMainInventory(player, title);
		} else {
			gui.createMainGUIOfJobs(player);
		}

		player.sendMessage(api.getMessage("Bought_Job").replaceAll("<job>", job.getDisplay() ));
	}

	public void executeCustomItem(String display, final Player player, String name, YamlConfiguration cf) {
		String item = api.isCustomItem(display, player, name, cf);
		JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get("" + player.getUniqueId());
		if (!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = cf.getString(name+"." + item + ".Action");
			if (action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 2);
			}  else if (action.equalsIgnoreCase("LEAVE")) {
				if (jb.getCurrentJobs().size() >= 1) { 
					api.playSound("LEAVE_ALL", player);
					jb.updateCurrentJobs(null);
					gui.UpdateMainInventory(player, cfg.getString("Main_Name"));
					player.sendMessage(api.getMessage("Leave_All"));
				} else {
					player.sendMessage(api.getMessage("Already_Left_All"));
				}
			} else if (action.equalsIgnoreCase("COMMAND")) {
				player.closeInventory();
				String cmd = cf.getString(api.getPrefix() + "." + item + ".Command"); 
				player.performCommand(cmd);
			}   else if (action.equalsIgnoreCase("BACK")) {
				plugin.getGUI().createMainGUIOfJobs(player);
			}
		}
	}

}
