package de.warsteiner.jobs.utils;

import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.cevents.JobsPluginSoundEvent;

public class ClickManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
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
				new JobsPluginSoundEvent("" + player.getUniqueId(), player, "LEAVE_SINGLE");
				jb.remoCurrentJob(job.getID());
				gui.createMainGUIOfJobs(player);
				player.sendMessage(plugin.getAPI().getMessage("Left_Job").replaceAll("<job>", job.getDisplay()));
			} else if (action.equalsIgnoreCase("MONEYLIST")) {
				player.sendMessage("UltimateJobs extension Money-List");
			} else if (action.equalsIgnoreCase("EARNINGS")) {
				player.sendMessage("UltimateJobs extension Earnings-List");
			}
		}
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
				if (api.canBuyThisJob(player, j)) {
					if (jb.ownJob(job)) {

						if (jb.isInJob(job)) {
							gui.createSettingsGUI(player, j);
						} else {

							int max = jb.getMaxJobs() - 1;
							if (jb.getCurrentJobs().size() <= max) {
								jb.addCurrentJob(job);
								new JobsPluginSoundEvent("" + player.getUniqueId(), player, "JOB_JOINED");
								new BukkitRunnable() {
									public void run() {
										gui.setCustomitems(player, player.getName(), player.getOpenInventory(),
												"Main_Custom.", cfg.getStringList("Main_Custom.List"), name, cfg);
										gui.setMainInventoryJobItems(player.getOpenInventory(), player, name);
									}
								}.runTaskLater(plugin, 1);

								player.sendMessage(api.getMessage("Joined").replaceAll("<job>", dis));
							} else {
								player.sendMessage(api.getMessage("Full").replaceAll("<job>", dis));
							}
						}

					} else {

						double money = j.getPrice();

						if (plugin.getEco().getBalance(player) >= money) {

							plugin.getEco().withdrawPlayer(player, money);
							jb.addOwnedJob(job);

							plugin.getPlayerManager().updateJobs(job.toUpperCase(), jb, "" + player.getUniqueId());

							new JobsPluginSoundEvent("" + player.getUniqueId(), player, "JOB_BOUGHT");

							gui.UpdateMainInventory(player, name);

							player.sendMessage(api.getMessage("Bought_Job").replaceAll("<job>", dis));
						} else {
							player.sendMessage(api.getMessage("Not_Enough_Money").replaceAll("<job>", dis));
						}

					}
				} else {
					player.sendMessage(
							api.toHex(j.getPermMessage().replaceAll("&", "�").replaceAll("<prefix>", api.getPrefix())));
				}
				return;
			}
		}

	}

	public void executeCustomItem(String display, final Player player, String name, YamlConfiguration cf) {
		String item = api.isCustomItem(display, player, "Main_Custom", cf);
		JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get("" + player.getUniqueId());
		if (!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = cf.getString("Main_Custom." + item + ".Action");
			if (action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(plugin, 2);
			} else if (action.equalsIgnoreCase("LEAVE")) {
				if (jb.getCurrentJobs() != null) {
					new JobsPluginSoundEvent("" + player.getUniqueId(), player, "LEAVE_ALL");
					jb.updateCurrentJobs(null);
					gui.UpdateMainInventory(player, name);
					player.sendMessage(api.getMessage("Leave_All"));
				} else {
					player.sendMessage(api.getMessage("Already_Left_All"));
				}
			}
		}
	}

}
