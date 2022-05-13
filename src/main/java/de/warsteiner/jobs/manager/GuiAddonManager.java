package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.ItemList;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.ItemAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.managers.GUIManager;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobStats;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class GuiAddonManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private ItemAPI im = SimpleAPI.getInstance().getItemAPI();
	private GUIManager gm = SimpleAPI.getInstance().getGUIManager();

	public GuiAddonManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}

	public void createLevelsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getLevelGUIConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Levels_Name")).replaceAll("<job>",
				job.getDisplay(UUID));
		int size = cfg.getInt("Levels_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_LEVELS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Levels_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Levels_Custom.",
				cfg.getStringList("Levels_Custom.List"), name, cfg, null);
		setLevelsItems(inv_view, name, cfg, player, job);
	}

	public void setLevelsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());
			int page = SimpleAPI.getPlugin().getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(),
					"LEVELS_" + job.getConfigID());

			List<String> slots = cf.getStringList("Level_Slots");

			int li = job.getCountOfLevels();

			ArrayList<String> levelslist = new ArrayList<String>();

			int sizeStart = (page - 1) * slots.size() + 1;
			int sizeStop = page * slots.size() + 1;

			for (int i = sizeStart; i != sizeStop; i++) {
				levelslist.add("" + i);
			}

			for (int i = 0; i < levelslist.size(); i++) {

				int lvl = Integer.valueOf(levelslist.get(i));

				if (lvl != 0) {
					if (levelslist.size() - 1 >= i) {
						int slot = 0;

						if (slots.size() >= i) {
							if (page == 1) {
								slot = Integer.valueOf(slots.get(i));
							} else {
								slot = Integer.valueOf(slots.get(i));
							}

							if (job.getIconOfLevel(lvl) != null) {

								ArrayList<String> lore = new ArrayList<String>();

								JobStats stats = sp.getStatsOf(job.getConfigID());

								String ic = null;
								int SPLEVEL = stats.getLevel();

								boolean is = SPLEVEL + 1 == lvl;

								if (!cf.getBoolean("Options.UseLevelIconsOrCustom")) {

									if (is) {

										ic = cf.getString("Options.Icons.CurrentlyWorkingOn");
									} else if (SPLEVEL >= lvl) {
										ic = cf.getString("Options.Icons.Reached");
									} else {
										ic = cf.getString("Options.Icons.NotReached");
									}
								} else {
									ic = job.getIconOfLevel(lvl);
								}

								ItemStack it = im.createAndGetItemStack(pl.getName(), ic);
								ItemMeta meta = it.getItemMeta();

								meta.setDisplayName(
										up.toHex(job.getLevelDisplay(lvl, "" + pl.getUniqueId())).replaceAll("&", "§"));

								if (SPLEVEL >= lvl) {
									if (cf.getBoolean("Options.EnchantedWhenReached")) {
										meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
									}
								}

								if (job.getLevelLore(lvl, "" + pl.getUniqueId()) != null) {
									for (String line : job.getLevelLore(lvl, "" + pl.getUniqueId())) {
										lore.add(up.toHex(line));
									}
								}

								String prefix = null;

								if (is) {
									prefix = "Currently";
								} else if (SPLEVEL >= lvl) {
									prefix = "Reached";
								} else {
									prefix = "Locked";
								}

								double exp = stats.getExp();
								double need = plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, lvl);

								double calc = exp - need;

								if (calc <= 0) {
									calc = 0;
								}

								for (String line : sp.getLanguage().getListFromLanguage(pl.getUniqueId(),
										"Rewards_Item_Lores." + prefix)) {
									lore.add(up.toHex(line).replaceAll("<exp>", plugin.getAPI().Format(calc)));
								}

								it.setAmount(lvl);

								meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

								meta.setLore(lore);

								it.setItemMeta(meta);

								inv.setItem(slot, it);
							}
						}
					}
				}
			}

			if (inv != null) {
				boolean next = true;
				boolean pre = true;

				if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
					next = sizeStop + 1 < li;
				}
				if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
					if (page == 1) {
						pre = false;
					}
				}

				if (pre) {

					boolean show = cf.getBoolean("PageItems.Previous.Show");

					if (show) {
						String icon = cf.getString("PageItems.Previous.Material");
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = im.createAndGetItemStack(pl.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(up.toHex(dis).replaceAll("&", "§"));

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(up.toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);

						it.setItemMeta(meta);

						inv.setItem(slot, it);
					}

				}

				if (next) {

					boolean show = cf.getBoolean("PageItems.Next.Show");

					if (show) {
						String icon = cf.getString("PageItems.Next.Material");
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = im.createAndGetItemStack(pl.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(up.toHex(dis).replaceAll("&", "§"));

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(up.toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);

						it.setItemMeta(meta);

						inv.setItem(slot, it);
					}

				}
			}

		});
	}

	public void createRewardsGUI(Player player, UpdateTypes t, Job job) {
		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Rewards_Name"))
				.replaceAll("<job>", job.getDisplay(UUID));
		int size = cfg.getInt("Rewards_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_REWARDS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Rewards_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Rewards_Custom.",
				cfg.getStringList("Rewards_Custom.List"), name, cfg, null);
		setRewardsItems(inv_view, name, cfg, player, job);
	}

	public void setRewardsItems(InventoryView inv, String name, FileConfiguration cf, Player pl, Job job) {
		plugin.getExecutor().execute(() -> {

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

			int page = SimpleAPI.getPlugin().getPlayerDataAPI().getPageFromID(sp.getUUIDAsString(),
					"REWARDS_" + job.getConfigID());

			List<String> slots = cf.getStringList("Rewards_Slots");
			int pageLength = slots.size();
			ArrayList<String> d = job.getAllNotRealIDSFromActionsAsArray();

			int cl = page * pageLength;

			if (inv != null) {
				boolean next = true;
				boolean pre = true;

				if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
					next = d.size() >= cl + 1;
				}
				if (!cf.getBoolean("PageItems.Previous.ShowOnPageOne")) {
					if (page == 1) {
						pre = false;
					}
				}

				if (pre) {

					boolean show = cf.getBoolean("PageItems.Previous.Show");

					if (show) {
						String icon = cf.getString("PageItems.Previous.Material");
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Lore"));
						int slot = cf.getInt("PageItems.Previous.Slot");

						ItemStack it = im.createAndGetItemStack(pl.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(up.toHex(dis).replaceAll("&", "§"));

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(up.toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);

						it.setItemMeta(meta);

						inv.setItem(slot, it);
					}

				}

				if (next) {

					boolean show = cf.getBoolean("PageItems.Next.Show");

					if (show) {
						String icon = cf.getString("PageItems.Next.Material");
						String dis = sp.getLanguage().getStringFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = sp.getLanguage().getListFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Lore"));
						int slot = cf.getInt("PageItems.Next.Slot");

						ItemStack it = im.createAndGetItemStack(pl.getName(), icon);
						ItemMeta meta = it.getItemMeta();

						meta.setDisplayName(up.toHex(dis).replaceAll("&", "§"));

						ArrayList<String> l = new ArrayList<String>();

						if (lore != null) {
							for (String b : lore) {
								l.add(up.toHex(b).replaceAll("&", "§"));
							}
						}

						meta.setLore(l);

						it.setItemMeta(meta);

						inv.setItem(slot, it);
					}

				}
			}

			ArrayList<String> itemslist = new ArrayList<String>();

			int sizeStart = (page - 1) * slots.size();
			int sizeStop = page * slots.size();

			for (int i = sizeStart; i != sizeStop; i++) {

				if (i < d.size()) {

					if (d.get(i).isEmpty()) {
						return;
					}

					itemslist.add(d.get(i));
				}

			}

			if (inv != null) {

				for (int i3 = 0; i3 < itemslist.size(); i3++) {

					String type = itemslist.get(i3);

					JobAction real = job.getActionofID(type);

					String icon = job.getConfig().getString("IDS." + real.toString() + "." + type + ".RewardsGUI.Icon");
					String display = sp.getLanguage().getConfig()
							.getString("Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Display");

					ItemStack i2 = im.createAndGetItemStack(pl, icon);
					ItemMeta m = i2.getItemMeta();

					ArrayList<String> l = new ArrayList<String>();

					String used = null;

					if (display == null) {
						Bukkit.getConsoleSender().sendMessage("§4Missing Display-Name on RewardsGUI from ID " + type);
						used = "§cNot Found";
					} else {
						used = display;
					}

					m.setDisplayName(up.toHex(used).replaceAll("&", "§"));

					double reward = job.getRewardOf(type, real);
					int chance = job.getChanceOf(type, real);
					double points = job.getPointsOf(type, real);
					double exp = job.getExpOf(type, real);

					for (String line : sp.getLanguage().getListFromLanguage(sp.getUUID(),
							"Jobs." + job.getConfigID() + ".IDS." + type + ".Rewards.Lore")) {
						l.add(up.toHex(line).replaceAll("<exp>", "" + exp).replaceAll("<points>", "" + points)
								.replaceAll("<chance>", "" + chance).replaceAll("<money>", "" + reward)
								.replaceAll("&", "§"));
					}

					m.setLore(l);

					i2.setItemMeta(m);

					inv.setItem(Integer.valueOf(slots.get(i3)), i2);
				}

			}

		});
	}

	public void createSelfStatsGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Self_Name"));
		int size = cfg.getInt("Self_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SELF_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Self_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Self_Custom.",
				cfg.getStringList("Self_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, UUID, player.getName(), player, "Self");
	}

	public boolean isStatsGUI(String title, UUID id) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		for (Player list : Bukkit.getOnlinePlayers()) {

			String name = list.getName();
			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + id);
			String named = sp.getLanguage().getStringFromPath(id, cfg.getString("Other_Name"));
			String fin = up.toHex(named.replaceAll("<name>", name).replaceAll("&", "§"));

			if (up.toHex(fin).equalsIgnoreCase(title.replaceAll("<name>", name)))
				return true;
		}
		return false;
	}

	public void createOtherStatsGUI(Player player, UpdateTypes t, String named, String ud) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();

		JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String name = sp.getLanguage().getStringFromPath(sp.getUUID(), cfg.getString("Other_Name")).replaceAll("<name>",
				named);
		int size = cfg.getInt("Other_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_OTHER_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Other_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Other_Custom.",
				cfg.getStringList("Other_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, ud, named, player, "Other");
	}

	public void setStatsItems(InventoryView inv, String name, FileConfiguration cf, String WATCHUUID, String NAME,
			Player pl, String prefix) {
		plugin.getExecutor().execute(() -> {
			String title = pl.getOpenInventory().getTitle();
			String need = up.toHex(name).replaceAll("&", "§");

			JobsPlayer sp = plugin.getPlayerAPI().getRealJobPlayer("" + pl.getUniqueId());

			if (title.equalsIgnoreCase(need)) {

				// informations

				double points = 0;
				int max = 0;
				String mode = cf.getString("DisplayMode").toUpperCase();

				List<String> li = null;

				String how = api.isCurrentlyInCache(WATCHUUID);

				if (how.equalsIgnoreCase("CACHE")) {
					JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(WATCHUUID);
					max = jb.getMaxJobs();
					points = jb.getPoints();

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = jb.getCurrentJobs();
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = jb.getOwnJobs();
					}

				} else {
					PlayerDataAPI plm = plugin.getPlayerDataAPI();
					max = plm.getMaxJobs(WATCHUUID);
					points = plm.getPoints(WATCHUUID);

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = plm.getCurrentJobs(WATCHUUID);
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = plm.getOwnedJobs(WATCHUUID);
					}
				}

				if (cf.getString(prefix + "_Skull.Material") != null) {
					String skull_item = cf.getString(prefix + "_Skull.Material");
					String skull_display = sp.getLanguage().getStringFromPath(pl.getUniqueId(),
							cf.getString(prefix + "_Skull.Display"));
					int skull_slot = cf.getInt(prefix + "_Skull.Slot");
					List<String> skull_lore = sp.getLanguage().getListFromPath(pl.getUniqueId(),
							cf.getString(prefix + "_Skull.Lore"));

					ItemStack skull = im.createAndGetItemStack(NAME, skull_item);
					ItemMeta m = skull.getItemMeta();

					ArrayList<String> l = new ArrayList<String>();

					int finalmaxjobs = max + 1;

					for (String b : skull_lore) {
						l.add(up.toHex(b).replaceAll("<points>", api.Format(points))
								.replaceAll("<max>", "" + finalmaxjobs).replaceAll("<name>", NAME)
								.replaceAll("&", "§"));
					}

					m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					m.setLore(l);

					m.setDisplayName(up.toHex(skull_display).replaceAll("<name>", NAME).replaceAll("&", "§"));

					skull.setItemMeta(m);

					inv.setItem(skull_slot, skull);
				}

				List<String> slots = cf.getStringList(prefix + "_Slots");

				for (int i = 0; i < slots.size(); i++) {

					if (i >= li.size()) {

						String error_item = cf.getString(prefix + "_Mot_Found_Item.Icon");
						String error_display = sp.getLanguage().getStringFromPath(pl.getUniqueId(),
								cf.getString(prefix + "_Mot_Found_Item.Display"));
						List<String> errorl_lore = sp.getLanguage().getListFromPath(pl.getUniqueId(),
								cf.getString(prefix + "_Mot_Found_Item.Lore"));

						ItemStack error = im.createAndGetItemStack(pl, error_item);
						ItemMeta m = error.getItemMeta();

						ArrayList<String> l = new ArrayList<String>();

						for (String b : errorl_lore) {
							l.add(up.toHex(b).replaceAll("&", "§"));
						}

						m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

						m.setLore(l);

						m.setDisplayName(up.toHex(error_display).replaceAll("<name>", NAME).replaceAll("&", "§"));

						error.setItemMeta(m);

						inv.setItem(Integer.valueOf(slots.get(i)), error);

					} else {

						Job job = plugin.getJobCache().get(li.get(i));
						String id = job.getConfigID();

						// loading stats

						int level = 0;
						double exp = 0;
						String bought = null;
						Integer broken = 0;

						if (how.equalsIgnoreCase("CACHE")) {
							JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(WATCHUUID);

							JobStats stats = jb.getStatsOf(job.getConfigID());

							level = stats.getLevel();
							exp = stats.getExp();
							bought = stats.getDate();
							broken = stats.getBrokenTimes();

						} else {

							PlayerDataAPI plm = plugin.getPlayerDataAPI();

							level = plm.getLevelOf(WATCHUUID, job.getConfigID());
							exp = plm.getExpOf(WATCHUUID, job.getConfigID());
							bought = plm.getDateOf(WATCHUUID, job.getConfigID());
							broken = plm.getBrokenOf(WATCHUUID, job.getConfigID());

						}

						String usedbuy = "";
						String lvl = job.getLevelDisplay(level, WATCHUUID);
						String usedlvl = "";

						String item = job.getIcon();
						String display = sp.getLanguage().getConfig()
								.getString("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Display");
						List<String> lore = sp.getLanguage().getConfig()
								.getStringList("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Lore." + prefix);

						ItemStack it = im.createAndGetItemStack(pl, item);
						ItemMeta m = it.getItemMeta();

						if (lore != null) {
							ArrayList<String> l = new ArrayList<String>();

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

							for (String b : lore) {
								l.add(up.toHex(b).replaceAll("<stats_args_4>", usedlvl).replaceAll("<name>", NAME)
										.replace("<earned>",
												"" + api.Format(plugin.getPlayerDataAPI().getEarnedAt(WATCHUUID, id,
														plugin.getPluginManager().getDate())))
										.replaceAll("<stats_args_3>", "" + level)
										.replaceAll("<stats_args_2>", "" + broken)
										.replaceAll("<stats_args_6>",
												"" + api.Format(
														plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, level)))
										.replaceAll("<stats_args_5>", "" + api.Format(exp))
										.replaceAll("<stats_args_1>", "" + usedbuy).replaceAll("&", "§"));
							}

							m.setLore(l);
						}

						m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

						m.setDisplayName(up.toHex(display).replaceAll("<name>", NAME).replaceAll("&", "§"));

						it.setItemMeta(m);

						inv.setItem(Integer.valueOf(slots.get(i)), it);

					}

				}

			}
		});
	}

}
