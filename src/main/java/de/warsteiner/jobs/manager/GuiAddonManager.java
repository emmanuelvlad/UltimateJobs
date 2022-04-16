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

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.ItemAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.managers.GUIManager;
import de.warsteiner.datax.utils.SimplePlayer;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.player.PlayerDataManager;
import de.warsteiner.jobs.utils.JobAction;

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
		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer(UUID);
		String name = plugin.getPluginManager().getFromPath(sp.getUUID(), cfg.getString("Levels_Name"))
				.replaceAll("<job>", job.getDisplay(UUID));
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

			JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer("" + pl.getUniqueId()); 
			int page = SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().getPageFromID(sp.getUUIDAsString(),
					"LEVELS_" + job.getConfigID());

			List<String> slots = cf.getStringList("Level_Slots");
			
			String r = SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(pl.getUniqueId()).getLanguage().getName();
			YamlConfiguration lang = plugin.getPluginManager().getLanguages().get(r).getConfig();

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

						if(slots.size() >= i) {
							if(page == 1) {
								slot = Integer.valueOf(slots.get(i));
							} else {
								slot = Integer.valueOf(slots.get(i));
							}
							
							if (job.getIconOfLevel(lvl) != null) {
								
								ArrayList<String> lore = new ArrayList<String>();
					 
								String ic = null;
								int SPLEVEL = sp.getLevelOf(job.getConfigID());
							 
								boolean is = SPLEVEL + 1 == lvl;
						 
								if(!cf.getBoolean("Options.UseLevelIconsOrCustom")) {
								 
									if(is )  { 
								 
										ic = cf.getString("Options.Icons.CurrentlyWorkingOn");
									} else if(SPLEVEL >= lvl) {
										ic = cf.getString("Options.Icons.Reached");
									} else {
										ic = cf.getString("Options.Icons.NotReached");
									}
								} else {
									ic = job.getIconOfLevel(lvl);
								}

								ItemStack it = im.createAndGetItemStack(pl.getName(), ic);
								ItemMeta meta = it.getItemMeta();

								meta.setDisplayName(up.toHex(job.getLevelDisplay(lvl, "" + pl.getUniqueId()))
										.replaceAll("&", "§"));
								
								if(SPLEVEL >= lvl) {
									if(cf.getBoolean("Options.EnchantedWhenReached")) {
										meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
									}
								}
								
								if(job.getLevelLore(lvl, ""+pl.getUniqueId()) != null) {
								for(String line : job.getLevelLore(lvl, ""+pl.getUniqueId())) {
									lore.add(up.toHex(line));
								}
							}
								
								String prefix = null;
								
								if(is)  { 
									prefix = "Currently";
								} else if(SPLEVEL >= lvl) {
									prefix = "Reached";
								} else {
									prefix = "Locked";
								}
							
								double exp = sp.getExpOf(job.getConfigID());
								double need = plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, lvl);
								
								double calc = exp - need;
								
								if(calc <= 0) {
									calc = 0;
								}
								
								for(String line : lang.getStringList("Rewards_Item_Lores."+prefix)) {
									lore.add(up.toHex(line).replaceAll("<exp>", plugin.getAPI().Format(calc)));
								}
								
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
					next = sizeStop+1 < li;
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
						String dis = plugin.getPluginManager().getFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = plugin.getPluginManager().getListFromPath(sp.getUUID(),
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
						String dis = plugin.getPluginManager().getFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = plugin.getPluginManager().getListFromPath(sp.getUUID(),
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
		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer(UUID);
		String name = plugin.getPluginManager().getFromPath(sp.getUUID(), cfg.getString("Rewards_Name"))
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

			JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer("" + pl.getUniqueId());

			int page = SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().getPageFromID(sp.getUUIDAsString(),
					"REWARDS_" + job.getConfigID());

			List<String> slots = cf.getStringList("Rewards_Slots");
			int pageLength = slots.size();
			ArrayList<String> li = job.getAllNotRealIDSFromActionsAsArray();

			int cl = page * pageLength;

			if (inv != null) {
				boolean next = true;
				boolean pre = true;

				if (cf.getBoolean("PageItems.Next.ShowOnlyIfNextPageExist")) {
					next = li.size() >= cl;
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
						String dis = plugin.getPluginManager().getFromPath(sp.getUUID(),
								cf.getString("PageItems.Previous.Display"));
						List<String> lore = plugin.getPluginManager().getListFromPath(sp.getUUID(),
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
						String dis = plugin.getPluginManager().getFromPath(sp.getUUID(),
								cf.getString("PageItems.Next.Display"));
						List<String> lore = plugin.getPluginManager().getListFromPath(sp.getUUID(),
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

			if (inv != null) {

				for (int i = (page - 1) * pageLength; i < (page * pageLength) && i < li.size(); i++) {

					String id = li.get(i);

					JobAction real = job.getActionofID(id);
					 
					String icon = job.getConfig().getString("IDS." + real.toString() + "." + id + ".RewardsGUI.Icon");
					String display = plugin.getPluginManager().get(sp.getUUID(),
							"Jobs."+job.getConfigID()+".IDS."+id+ ".Rewards.Display");

					ItemStack i2 = im.createAndGetItemStack(pl, icon);
					ItemMeta m = i2.getItemMeta();

					ArrayList<String> l = new ArrayList<String>();

					m.setDisplayName(up.toHex(display).replaceAll("&", "§"));

					double reward = job.getRewardOf(id, real);
					int chance = job.getChanceOf(id, real);
					double points = job.getPointsOf(id, real);
					double exp = job.getExpOf(id, real); 
					
					for (String line : plugin.getPluginManager().getListFromLang(sp.getUUID(),
							"Jobs."+job.getConfigID()+".IDS."+id+ ".Rewards.Lore")) {
						l.add(up.toHex(line).replaceAll("<exp>", "" + exp).replaceAll("<points>", "" + points)
								.replaceAll("<chance>", "" + chance).replaceAll("<money>", "" + reward)
								.replaceAll("&", "§"));
					}

					m.setLore(l);

					i2.setItemMeta(m);

					inv.setItem(Integer.valueOf(slots.get(i)), i2);

				}

			}

		});
	}

	public void createSelfStatsGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer(UUID);
		String name = plugin.getPluginManager().getFromPath(sp.getUUID(), cfg.getString("Self_Name"));
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

			String named = plugin.getPluginManager().getFromPath(id, cfg.getString("Other_Name"));
			String fin = up.toHex(named.replaceAll("<name>", name).replaceAll("&", "§"));

			if (up.toHex(fin).equalsIgnoreCase(title.replaceAll("<name>", name)))
				return true;
		}
		return false;
	}

	public void createOtherStatsGUI(Player player, UpdateTypes t, String named, String ud) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();

		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer("" + player.getUniqueId());
		String name = plugin.getPluginManager().getFromPath(sp.getUUID(), cfg.getString("Other_Name"))
				.replaceAll("<name>", named);
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

			PlayerDataManager plm = plugin.getPlayerDataModeManager();

			if (title.equalsIgnoreCase(need)) {

				// informations

				double points = 0;
				int max = 0;
				String mode = cf.getString("DisplayMode").toUpperCase();

				List<String> li = null;

				YamlConfiguration lang = plugin.getPluginManager().getLanguages().get(SimpleAPI.getPlugin()
						.getPlayerCacheManager().getPluginPlayer(pl.getUniqueId()).getLanguage().getName()).getConfig();

				String how = api.isCurrentlyInCache(WATCHUUID);

				if (how.equalsIgnoreCase("CACHE")) {
					JobsPlayer jb = plugin.getPlayerManager().getRealJobPlayer(WATCHUUID);
					max = jb.getMaxJobs();
					points = jb.getPoints();

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = jb.getCurrentJobs();
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = jb.getOwnJobs();
					}

				} else {
					max = plm.getMax(WATCHUUID);
					points = plm.getPoints(WATCHUUID);

					if (mode.equalsIgnoreCase("CURRENT")) {
						li = plm.getOfflinePlayerCurrentJobs(WATCHUUID);
					} else if (mode.equalsIgnoreCase("OWNED")) {
						li = plm.getOfflinePlayerOwnedJobs(WATCHUUID);
					}
				}

				if (cf.getString(prefix + "_Skull.Material") != null) {
					String skull_item = cf.getString(prefix + "_Skull.Material");
					String skull_display = plugin.getPluginManager().getFromPath(pl.getUniqueId(),
							cf.getString(prefix + "_Skull.Display"));
					int skull_slot = cf.getInt(prefix + "_Skull.Slot");
					List<String> skull_lore = plugin.getPluginManager().getListFromPath(pl.getUniqueId(),
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
						String error_display = plugin.getPluginManager().getFromPath(pl.getUniqueId(),
								cf.getString(prefix + "_Mot_Found_Item.Display"));
						List<String> errorl_lore = plugin.getPluginManager().getListFromPath(
								pl.getUniqueId(), cf.getString(prefix + "_Mot_Found_Item.Lore"));

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

						String date = null;
						int level = 0;
						double exp = 0;
						String bought = null;
						Integer broken = 0;

						if (how.equalsIgnoreCase("CACHE")) {
							JobsPlayer jb = plugin.getPlayerManager().getRealJobPlayer(WATCHUUID);

							date = jb.getDateOfJob(id);
							level = jb.getLevelOf(id);
							exp = jb.getExpOf(id);
							bought = jb.getDateOfJob(id);
							broken = jb.getBrokenOf(id);

						} else {

							date = plm.getDateOf(WATCHUUID, job.getConfigID());
							level = plm.getLevelOf(WATCHUUID, job.getConfigID());
							exp = plm.getExpOf(WATCHUUID, job.getConfigID());
							bought = plm.getDateOf(WATCHUUID, job.getConfigID());
							broken = plm.getBrokenOf(WATCHUUID, job.getConfigID());

						}

						String usedbuy = "";
						String lvl = job.getLevelDisplay(level, WATCHUUID);
						String usedlvl = "";

						String item = job.getIcon();
						String display = lang
								.getString("Jobs." + job.getConfigID().toUpperCase() + ".StatsGUI.Display");
						List<String> lore = lang
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
												"" + api.Format(plugin.getPlayerDataModeManager().getEarnedAtDate(
														WATCHUUID, id, plugin.getPluginManager().getDate())))
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