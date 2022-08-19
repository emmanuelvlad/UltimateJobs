package de.warsteiner.jobs.manager;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.objects.GUIType;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class GuiOpenManager {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public String isStatsMenuOpendAboutPlayer(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.STATS_OTHER)) {
				String a = plugin.getGUI().getGUIsDetails().get(sp.getUUIDAsString());

				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Other_Name"))
						.replaceAll("<name>", a);

				if (name.equalsIgnoreCase(title)) {
					return a;
				}
			}

		}
		return null;
	}

	public Job isSettingsMenu(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getSettings();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.SETTINGS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Settings_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public Job isRewardsMenu(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.REWARDS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Rewards_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public String isStatsMenuOpendSelf(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.STATS_SELF)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Self_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isMainOpend(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.MAIN)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Main_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isLevelsMenu(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getLevelGUIConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.LEVELS)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Levels_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public String isHelpOpend(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getHelpSettings();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.HELP)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Help_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isWithdrawMenu(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.WITHDRAW)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Withdraw_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isWithdrawConfirmMenu(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getWithdrawConfirmConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM_WITHDRAW)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("ConfirmWithdraw_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public String isEarningsALL(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsAllConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.EARNINGS_ALL)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("All_Earnings_Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isEarningsAboutJob(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getEarningsJobConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.EARNINGS_JOB)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Job_Earnings_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public String isLanguageOpend(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getLanguageGUIConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.LANGUAGE)) {
				String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Name"));

				if (name.equalsIgnoreCase(title)) {
					return "FOUND";
				}
			}

		}
		return null;
	}

	public Job isConfirmGUI(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getConfirm();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());
				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("AreYouSureGUI_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (fin.equalsIgnoreCase(title)) {
					return j;
				}

			}

		}
		return null;
	}

	public Job isLeaveConfirmGUI(Player player, String title) {
		FileConfiguration cfg = plugin.getFileManager().getLeaveConfirmConfig();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (sp == null) {
			return null;
		}
		if (plugin.getGUI().getGUIS().containsKey(sp.getUUIDAsString())) {

			if (plugin.getGUI().getGUIS().get(sp.getUUIDAsString()).equals(GUIType.CONFIRM_LEAVE)) {

				Job j = plugin.getGUI().getGUIsJobs().get(sp.getUUIDAsString());

				String dis = j.getDisplay(sp.getUUIDAsString());
				String named = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("LeaveConfirm_Name"));
				String fin = plugin.getPluginManager().toHex(named.replaceAll("<job>", dis).replaceAll("&", "§"));

				if (title.equalsIgnoreCase(fin)) {
					return j;
				}

			}

		}
		return null;
	}

	public void openGuiByGuiID(CommandSender sender, GUIType type, Player player, Job job, String about, boolean y) {
		GuiManager gui = plugin.getGUI();
		GuiAddonManager addon = plugin.getGUIAddonManager();

		if (type != null) {

			boolean b = false;

			if (type.equals(GUIType.SETTINGS)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}

				if (!plugin.getPlayerAPI().getRealJobPlayer(player.getUniqueId()).getStatsList()
						.containsKey(job.getConfigID())) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: No Data Found");
					return;
				}
				b = true;
				gui.createSettingsGUI(player, job, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.MAIN)) {
				b = true;
				gui.createMainGUIOfJobs(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.LANGUAGE)) {
				b = true;
				gui.openLanguageMenu(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.HELP)) {
				b = true;
				gui.createHelpGUI(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.CONFIRM)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				gui.createAreYouSureGUI(player, job, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.CONFIRM_LEAVE)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				plugin.getGUIAddonManager().createLeaveConfirmGUI(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.STATS_SELF)) {
				b = true;
				addon.createSelfStatsGUI(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.STATS_OTHER)) {

				if (about == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: About Player");
					return;
				}

				if (plugin.getPlayerDataAPI().getUUIDByName(about.toUpperCase()) == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: About Player Not Found");
					return;
				}

				String id = plugin.getPlayerDataAPI().getUUIDByName(about.toUpperCase());
				b = true;
				addon.createOtherStatsGUI(player, UpdateTypes.OPEN, about, id);
			} else if (type.equals(GUIType.REWARDS)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				addon.createRewardsGUI(player, UpdateTypes.OPEN, job);

			} else if (type.equals(GUIType.LEVELS)) {
				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				if (!plugin.getPlayerAPI().getRealJobPlayer(player.getUniqueId()).getStatsList()
						.containsKey(job.getConfigID())) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: No Data Found");
					return;
				}
				b = true;
				addon.createLevelsGUI(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.EARNINGS_ALL)) {
				b = true;
				addon.createEarningsGUI_ALL_Jobs(player, UpdateTypes.OPEN);
			} else if (type.equals(GUIType.EARNINGS_JOB)) {

				if (job == null) {
					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					sender.sendMessage(AdminCommand.prefix + "Missing Element: Job");
					return;
				}
				b = true;
				addon.createEarningsGUI_Single_Job(player, UpdateTypes.OPEN, job);
			} else if (type.equals(GUIType.WITHDRAW)) {

				b = true;
				addon.createWithdrawMenu(player, UpdateTypes.OPEN);
			}
			if (b) {
				if (y) {
					if (job == null) {
						sender.sendMessage(AdminCommand.prefix + "Opend GUI §e" + type + " §7for Player §c"
								+ player.getName() + "§7!");
					} else {
						sender.sendMessage(AdminCommand.prefix + "Opend GUI §e" + type + " §7for Player §c"
								+ player.getName() + "§7! §8(§7Job: §b" + job.getConfigID() + "§8)");
					}

					if (sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
					}
				}
			}
		}

	}

}
