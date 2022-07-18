package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.objects.GUIType;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class GuiManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();

	public GuiManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}

	private HashMap<String, GUIType> guis = new HashMap<String, GUIType>();
	private HashMap<String, String> details_about = new HashMap<String, String>();
	private HashMap<String, Job> details_job = new HashMap<String, Job>();

	public HashMap<String, GUIType> getGUIS() {
		return guis;
	}

	public HashMap<String, String> getGUIsDetails() {
		return details_about;
	}

	public HashMap<String, Job> getGUIsJobs() {
		return details_job;
	}

	public void openInventory(Player player, int size, String name, GUIType id, String about, Job job) {
		final Inventory inv = Bukkit.createInventory(null, size * 9,
				plugin.getPluginManager().toHex(name).replaceAll("&", "§"));

		String UUID = "" + player.getUniqueId();

		if (guis.containsKey(UUID)) {
			guis.remove(UUID);
		}

		if (details_about.containsKey(UUID)) {
			details_about.remove(UUID);
		}

		if (about != null) {
			details_about.put(UUID, about);
		}

		if (details_job.containsKey(UUID)) {
			details_job.remove(UUID);
		}

		if (job != null) {
			details_job.put(UUID, job);
		}

		if (id != null) {
			guis.put(UUID, id);
		}

		player.openInventory(inv);
	}

	public void openLanguageMenu(Player player, UpdateTypes type) {

		JobsPlayer pp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		FileConfiguration cfg = plugin.getFileManager().getLanguageGUIConfig();

		if (type.equals(UpdateTypes.OPEN)) {
			plugin.getAPI().playSound("LANGUAGE_OPEN", player);
		}

		int size = cfg.getInt("Size");
		String name = pp.getLanguage().getStringFromPath(player.getUniqueId(), cfg.getString("Name"));

		openInventory(player, size, plugin.getPluginManager().toHex(name), GUIType.LANGUAGE, null, null);

		InventoryView inv = player.getOpenInventory();

		plugin.getExecutor().execute(() -> {

			setPlaceHolders(player, inv, cfg.getStringList("Place"), name);

			setCustomitems(player, player.getName(), inv, "Custom.", cfg.getStringList("Custom.List"), name, cfg, null);

			setLanguageItems(player, inv, cfg);
		});

	}

	public void updateLanguageInventory(Player player, String name, JobsPlayer jb) {
		FileConfiguration cfg = plugin.getFileManager().getLanguageGUIConfig();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				 
				setCustomitems(player, player.getName(), inv, "Custom.", cfg.getStringList("Custom.List"), name, cfg, null);

				setLanguageItems(player, inv, cfg);
			}
		}.runTaskLater(plugin, 2);
	}
	
	public void setLanguageItems(Player player, InventoryView inv, FileConfiguration cfg) {
		plugin.getExecutor().execute(() -> {

			ArrayList<Language> langs = plugin.getLanguageAPI().getLoadedLanguagesAsArray();

			List<String> li = cfg.getStringList("LangItems.Slots");

			for (int i = 0; i < langs.size(); i++) {

				Language lang = langs.get(i);
				String mat = lang.getIcon();
				String dis = lang.getDisplay();

				ItemStack item = plugin.getItemAPI().createItem(player, mat);

				if (item != null) {

					ItemMeta meta = item.getItemMeta();

					meta.setDisplayName(plugin.getPluginManager().toHex(dis));

					List<String> lore = null;

					if (lang.getName().equalsIgnoreCase(plugin.getPlayerAPI()
							.getRealJobPlayer("" + player.getUniqueId()).getLanguage().getName())) {
						lore = lang.getConfig().getStringList("LanguageChoosenLore");
					} else {
						lore = lang.getConfig().getStringList("LanguageCanChoose");
					}

					ArrayList<String> newlore = new ArrayList<String>();

					for (String line : lore) {
						newlore.add(plugin.getPluginManager().toHex(line));
					}

					meta.setCustomModelData(lang.getModelData());

					meta.setLore(newlore);

					item.setItemMeta(meta);

					inv.setItem(Integer.valueOf(li.get(i)), item);
				}

			}

		});
	}

	public void createHelpGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getHelpSettings();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Help_Name"));
		int size = cfg.getInt("Help_Size");

		openInventory(player, size, name, GUIType.HELP, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_HELP_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Help_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "Help_Custom.", cfg.getStringList("Help_Custom.List"), name,
				cfg, null);
	}

	public void createAreYouSureGUI(Player player, Job job, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getConfirm();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String name = sp.getLanguage().getStringFromPath(player.getUniqueId(), cfg.getString("AreYouSureGUI_Name"))
				.replaceAll("<job>", job.getDisplay("" + player.getUniqueId()));
		int size = cfg.getInt("AreYouSureGUI_Size");

		openInventory(player, size, name, GUIType.CONFIRM, null, job);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SURE_GUI", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("AreYouSureGUI_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "AreYouSureGUI_Custom.",
				cfg.getStringList("AreYouSureGUI_Custom.List"), name, cfg, null);
		setAreYouSureItems(player, job, name, inv_view);
	}

	public void setAreYouSureItems(Player player, Job job, String tit, InventoryView inv) {
		FileConfiguration cfg = plugin.getFileManager().getConfirm();
		plugin.getExecutor().execute(() -> {
			String UUID = "" + player.getUniqueId();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
			if (player != null) {

				if (cfg.getBoolean("AreYouSureItems.Button_YES.Show")) {

					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("AreYouSureItems.Button_YES.Icon"));

					String dis = plugin.getPluginManager()
							.toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
									cfg.getString("AreYouSureItems.Button_YES.Display")))
							.replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
					int slot = cfg.getInt("AreYouSureItems.Button_YES.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("AreYouSureItems.Button_YES.Lore"));
					ArrayList<String> l = new ArrayList<String>();

					ItemMeta meta = item.getItemMeta();

					for (String line : lore) {
						l.add(plugin.getPluginManager().toHex(line).replaceAll("<job>", job.getDisplay(UUID))
								.replaceAll("&", "§"));
					}

					if (cfg.contains("AreYouSureItems.Button_YES.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("AreYouSureItems.Button_YES.CustomModelData"));
					}

					meta.setDisplayName(dis);

					meta.setLore(l);

					item.setItemMeta(meta);

					inv.setItem(slot, item);
				}
			}

			if (player != null) {
				if (cfg.getBoolean("AreYouSureItems.Button_NO.Show")) {
					ItemStack item = plugin.getItemAPI().createItem(player,
							cfg.getString("AreYouSureItems.Button_NO.Icon"));

					String dis = plugin.getPluginManager()
							.toHex(sp.getLanguage().getStringFromPath(sp.getUUID(),
									cfg.getString("AreYouSureItems.Button_NO.Display")))
							.replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
					int slot = cfg.getInt("AreYouSureItems.Button_NO.Slot");
					List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
							cfg.getString("AreYouSureItems.Button_NO.Lore"));
					ArrayList<String> l = new ArrayList<String>();

					ItemMeta meta = item.getItemMeta();

					for (String line : lore) {
						l.add(plugin.getPluginManager().toHex(line).replaceAll("<job>", job.getDisplay(UUID))
								.replaceAll("&", "§"));
					}

					if (cfg.contains("AreYouSureItems.Button_NO.CustomModelData")) {
						meta.setCustomModelData(cfg.getInt("AreYouSureItems.Button_NO.CustomModelData"));
					}

					meta.setDisplayName(dis);

					meta.setLore(l);

					item.setItemMeta(meta);

					inv.setItem(slot, item);
				}
			}

		});
	}

	public void createMainGUIOfJobs(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String name = sp.getLanguage().getStringFromPath(player.getUniqueId(), cfg.getString("Main_Name"));
		int size = cfg.getInt("Main_Size");

		openInventory(player, size, name, GUIType.MAIN, null, null);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_MAIN", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Main_Place"), name);
		UpdateMainInventoryItems(player, name);
	}

	public void UpdateMainInventoryItems(Player player, String name) {
		FileConfiguration cfg = plugin.getFileManager().getGUI();
		new BukkitRunnable() {
			public void run() {
				setCustomitems(player, player.getName(), player.getOpenInventory(), "Main_Custom.",
						cfg.getStringList("Main_Custom.List"), name, cfg, null);
				setMainInventoryJobItems(player.getOpenInventory(), player, name);
			}
		}.runTaskLater(plugin, 2);
	}

	public void createSettingsGUI(Player player, Job job, UpdateTypes t) {
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		FileConfiguration cfg = plugin.getFileManager().getSettings();
		String dis = job.getDisplay("" + player.getUniqueId());
		String named = sp.getLanguage().getStringFromPath(player.getUniqueId(), cfg.getString("Settings_Name"));
		;
		String name = named.replaceAll("<job>", dis);
		int size = cfg.getInt("Settings_Size");

		openInventory(player, size, name, GUIType.SETTINGS, null, job);
		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SETTINGS", player);
		}
		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Settings_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "Settings_Custom.",
				cfg.getStringList("Settings_Custom.List"), name, cfg, job);

	}
	
	public void UpdateSettingsGUI(Player player, String name, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getSettings();
		InventoryView inv = player.getOpenInventory();
		new BukkitRunnable() {
			public void run() {
				setCustomitems(player, player.getName(), inv, "Settings_Custom.",
						cfg.getStringList("Settings_Custom.List"), name, cfg, job);
			}
		}.runTaskLater(plugin, 2);
	}

	public void setMainInventoryJobItems(InventoryView inv, Player player, String name) {
		plugin.getExecutor().execute(() -> {
			String UUID = "" + player.getUniqueId();
			String title = player.getOpenInventory().getTitle();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);

			String need = plugin.getPluginManager().toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {

				ArrayList<String> jobs = plugin.getLoaded();

				for (String li : jobs) {

					Job j = plugin.getJobCache().get(li);
					;

					String display = plugin.getPluginManager().toHex(j.getDisplay(UUID).replaceAll("&", "§"));
					int slot = j.getSlot();
					List<String> lore = j.getLore(UUID);
					String mat = j.getIcon();
					double price = j.getPrice();
					String id = j.getConfigID();

					inv.setItem(slot, null);

					ItemStack item = plugin.getItemAPI().createItem(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));

					List<String> see = null;

					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					if (api.canBuyWithoutPermissions(player, j)) {
						List<String> d = api.canGetJobWithSubOptions(player, j);

						if (d == null) {
							if (sp.ownJob(id) == true || api.canByPass(player, j) == true) {

								if (sp.isInJob(id)) {
									meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);

									see = sp.getLanguage().getListFromLanguage(sp.getUUID(), "Main_Job_Items.Lore.In");
								} else {
									see = sp.getLanguage().getListFromLanguage(sp.getUUID(),
											"Main_Job_Items.Lore.Bought");
								}

							} else {
								see = sp.getLanguage().getListFromLanguage(sp.getUUID(), "Main_Job_Items.Lore.Price");
							}

						} else {
							see = d;

						}

					} else {
						see = j.getPermissionsLore(UUID);
					}

					List<String> filore = new ArrayList<String>();
					if (lore != null) {
						for (String l : lore) {
							filore.add(plugin.getPluginManager().toHex(l).replaceAll("&", "§"));
						}
					}

					if (sp.isInJob(id)) {

						JobStats statsjob = sp.getStatsOf(id);

						int level = statsjob.getLevel();
						double exp = statsjob.getExp();
						String bought = statsjob.getDate();
						String usedbuy = "";
						String lvl = j.getLevelDisplay(level, UUID);
						String usedlvl = "";
						Integer broken = statsjob.getBrokenTimes();
						String joined = statsjob.getJoinedDate();

						if (j.getStatsMessage(UUID) != null) {

							if (lvl == null) {
								usedlvl = "Error";
							} else {
								usedlvl = lvl;
							}
							if (bought == null) {
								usedbuy = "Error";
							} else {
								usedbuy = bought;
							}

							for (String l : j.getStatsMessage(UUID)) {

								filore.add(plugin.getPluginManager().toHex(l).replaceAll("<stats_args_4>", usedlvl)
										.replaceAll("<online>", ""+plugin.getPlayerAPI().getOnlinePlayersInJob(j).size()).replace("<earned>",
												"" + api.Format(plugin.getPlayerAPI()
														.getEarnedAt("" + player.getUniqueId(), j, plugin.getDate())))
										.replaceAll("<stats_args_3>", "" + level)
										.replaceAll("<stats_args_2>", "" + broken)
										.replaceAll("<stats_args_6>",
												"" + api.Format(plugin.getLevelAPI().getJobNeedExp(j, sp)))
										.replaceAll("<stats_args_5>", "" + api.Format(exp))
										.replaceAll("<stats_args_1>", "" + usedbuy).replaceAll("<joined>", joined)
										.replaceAll("&", "§"));
							}
						}

					}

					if (see != null) {
						for (String l : see) {

							filore.add(plugin.getPluginManager().toHex(l).replaceAll("<price>", "" + price)
									.replaceAll("&", "§"));

						}
					}

					if (j.hasModelData()) {
						meta.setCustomModelData(j.getModelData());
					}

					meta.setLore(filore);

					item.setItemMeta(meta);

					inv.setItem(slot, item);

				}
			}
		});

	}

	public void setCustomitems(Player player, String pname, InventoryView inv, String prefix, List<String> list,
			String name, FileConfiguration cfg, Job job) {

		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
			String UUID = "" + player.getUniqueId();
			String title = player.getOpenInventory().getTitle();
			String need = plugin.getPluginManager().toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					if (cfg.contains(prefix + pl + ".Display")) {
						String display = cfg.getString(prefix + pl + ".Display");
						String mat = cfg.getString(prefix + pl + ".Material");
						int slot = cfg.getInt(prefix + pl + ".Slot");

						ItemStack item = plugin.getItemAPI().createItem(player, mat);
						ItemMeta meta = item.getItemMeta();

						meta.setDisplayName(sp.getLanguage().getStringFromPath(sp.getUUID(), display));

						int max = sp.getMaxJobs() + 1;

						if (cfg.contains(prefix + pl + ".Lore")) {

							List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
									cfg.getString(prefix + pl + ".Lore"));

							List<String> filore = new ArrayList<String>();

							if (job != null) {

								if (sp.getStatsOf(job.getConfigID()) != null) {

									JobStats statsjob = sp.getStatsOf(job.getConfigID());

									int level = statsjob.getLevel();
									double exp = statsjob.getExp();
									String bought = statsjob.getDate();
									String usedbuy = "";
									String lvl = job.getLevelDisplay(level, UUID);
									String usedlvl = "";
									Integer broken = statsjob.getBrokenTimes();

									if (job.getStatsMessage(UUID) != null) {

										if (lvl == null) {
											usedlvl = "Error";
										} else {
											usedlvl = lvl;
										}
										if (bought == null) {
											usedbuy = "Error";
										} else {
											usedbuy = bought;
										}

										for (String l : lore) {

											filore.add(plugin.getPluginManager().toHex(l)
													.replaceAll("<salary>", plugin.getAPI().Format(sp.getSalary()))
													.replaceAll("<stats_args_4>", usedlvl)
													.replace("<earned>",
															"" + api.Format(plugin.getPlayerDataAPI().getEarnedAt(
																	"" + player.getUniqueId(), job.getConfigID(),
																	plugin.getDate())))
													.replaceAll("<stats_args_3>", "" + level)
													.replaceAll("<stats_args_2>", "" + broken)
													.replaceAll("<stats_args_6>",
															"" + api.Format(
																	plugin.getLevelAPI().getJobNeedExp(job, sp)))
													.replaceAll("<stats_args_5>", "" + api.Format(exp))
													.replaceAll("<stats_args_1>", "" + usedbuy)
													.replaceAll("<points>", "" + api.Format(sp.getPoints()))
													.replaceAll("<max>", "" + max).replaceAll("&", "§"));
										}
									}
								}
							} else {
								for (String l : lore) {
									filore.add(plugin.getPluginManager().toHex(l)
											.replaceAll("<salary>", plugin.getAPI().Format(sp.getSalary()))
											.replaceAll("<points>", "" + api.Format(sp.getPoints()))
											.replaceAll("<max>", "" + max).replaceAll("&", "§"));
								}
							}

							meta.setLore(filore);
						}

						if (cfg.contains(prefix + pl + ".CustomModelData")) {
							meta.setCustomModelData(cfg.getInt(prefix + pl + ".CustomModelData"));
						}

						item.setItemMeta(meta);

						inv.setItem(slot, item);

					} else {
						plugin.getLogger().warning("§c§lMissing Element in " + need + " §4§lCustom Item: §b§l" + pl);
					}
				}
			}
		});

	}

	public void setPlaceHolders(Player player, InventoryView inv_view, List<String> list, String name) {
		plugin.getExecutor().execute(() -> {
			String title = player.getOpenInventory().getTitle();
			String need = plugin.getPluginManager().toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					String[] t = pl.split(":");

					String mat = t[0].toUpperCase();
					int slot = Integer.valueOf(t[1]).intValue();
					String display = t[2];

					ItemStack item = plugin.getItemAPI().createItem(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(plugin.getPluginManager().toHex(display.replaceAll("&", "§")));

					if (t.length == 4) {

						meta.setCustomModelData(Integer.valueOf(t[3]));

					}

					item.setItemMeta(meta);

					inv_view.setItem(slot, item);
				}

			}

		});

	}

}
