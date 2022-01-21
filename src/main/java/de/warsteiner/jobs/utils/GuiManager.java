package de.warsteiner.jobs.utils;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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
import de.warsteiner.jobs.api.JobsPlayer; 
import de.warsteiner.jobs.utils.cevents.JobsPluginSoundEvent;

public class GuiManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI(); 
	private YamlConfiguration cfg; 

	public GuiManager(UltimateJobs plugin, YamlConfiguration cfg) {
		this.plugin = plugin;
		this.cfg = cfg;
	}

	public void openInventory(Player player, int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size * 9, api.toHex(name).replaceAll("&", "§"));
		player.openInventory(inv);
	}

	public void createMainGUIOfJobs(Player player) {
		String name = cfg.getString("Main_Name");
		int size = cfg.getInt("Main_Size");

		openInventory(player, size, name);

		new JobsPluginSoundEvent("" + player.getUniqueId(), player, "OPEN_MAIN");

		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Main_Place"), name);
		UpdateMainInventory(player, name);
	}

	public void UpdateMainInventory(Player player, String name) {
		new BukkitRunnable() {
			public void run() {
				setCustomitems(player, player.getName(), player.getOpenInventory(), "Main_Custom.",
						cfg.getStringList("Main_Custom.List"), name, cfg);
				setMainInventoryJobItems(player.getOpenInventory(), player, name);
			}
		}.runTaskLater(plugin, 1);
	}

	public void createSettingsGUI(Player player, Job job) {

		String dis = job.getDisplay();
		String name = cfg.getString("Settings_Name").replaceAll("<job>", dis);
		int size = cfg.getInt("Settings_Size");

		openInventory(player, size, name);

		new JobsPluginSoundEvent("" + player.getUniqueId(), player, "OPEN_SETTINGS");

		InventoryView inv_view = player.getOpenInventory();

		setPlaceHolders(player, inv_view, cfg.getStringList("Settings_Place"), name);
		setCustomitems(player, player.getName(), inv_view, "Settings_Custom.",
				cfg.getStringList("Settings_Custom.List"), name, cfg);

	}

	public void setMainInventoryJobItems(InventoryView inv, Player player, String name) {

		plugin.getExecutor().execute(() -> {
			String title = player.getOpenInventory().getTitle(); 
			JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(""+player.getUniqueId()); 
 
			String need = api.toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {

				ArrayList<Job> jobs = plugin.getLoaded();

				for (Job j : jobs) {

					String display = api.toHex(j.getDisplay().replaceAll("&", "§"));
					int slot = j.getSlot();
					List<String> lore = j.getLore();
					String mat = j.getIcon();
					double price = j.getPrice();
					String id = j.getID();

					inv.setItem(slot, null);

					ItemStack item = api.createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));

					List<String> see;

					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

					if (api.canBuyWithoutPermissions(player, j)) {
						if (jb.ownJob(id)) {

							if (jb.isInJob(id)) {
								meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);

								see = cfg.getStringList("Jobs.Lore.In");
							} else {
								see = cfg.getStringList("Jobs.Lore.Bought");
							}

						} else {
							see = cfg.getStringList("Jobs.Lore.Price");
						}

					} else {
						see = j.getPermissionsLore();
					}

					List<String> filore = new ArrayList<String>();
					for (String l : lore) {
						filore.add(api.toHex(l).replaceAll("&", "§"));
					}
					if (jb.isInJob(id)) {

						int level =  jb.getLevelOf(id);
						double exp = jb.getExpOf(id);
						String bought = jb.getDateOfJob(id);
						String lvl = j.getLevelDisplay(level);
						Integer broken = jb.getBrokenOf(id);
						
						for (String l : j.getStatsMessage()) {

							filore.add(api.toHex(l).replaceAll("<stats_args_4>", lvl)
									.replaceAll("<stats_args_3>", "" + level)
									.replaceAll("<stats_args_2>", "" + broken)
									.replaceAll("<stats_args_6>",
											"" + api.Format(plugin.getLevelAPI().getJobNeedExp(j, jb)))
									.replaceAll("<stats_args_5>", "" + api.Format(exp))
									.replaceAll("<stats_args_1>",""+ bought).replaceAll("&", "§"));
						}
					}
					for (String l : see) {
						filore.add(api.toHex(l).replaceAll("<price>", "" + price).replaceAll("&", "§"));
					}
					meta.setLore(filore);

					item.setItemMeta(meta);

					inv.setItem(slot, item);

				}
			}
		});

	}

	public void setCustomitems(Player player, String pname, InventoryView inv, String prefix, List<String> list,
			String name, YamlConfiguration cf) {

		plugin.getExecutor().execute(() -> {
			
			JobsPlayer jb = null;
			
			if(plugin.getPlayerManager().getJonPlayers().get(""+player.getUniqueId()) != null) {
				jb = plugin.getPlayerManager().getJonPlayers().get(""+player.getUniqueId());
			}
			
			String title = player.getOpenInventory().getTitle();
			String need = api.toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					if (cf.contains(prefix + pl + ".Display")) {
						String display = cf.getString(prefix + pl + ".Display");
						String mat = cf.getString(prefix + pl + ".Material").toUpperCase();
						int slot = cf.getInt(prefix + pl + ".Slot");

						ItemStack item = api.createItemStack(player, mat);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName(display.replaceAll("&", "§"));

						int max = jb.getMaxJobs() + 1;
						
						if (cf.contains(prefix + pl + ".Lore")) {
							List<String> lore = cf.getStringList(prefix + pl + ".Lore");
							List<String> filore = new ArrayList<String>();
							for (String l : lore) {
								filore.add(api.toHex(l)
										.replaceAll("<points>", ""+api.Format(jb.getPoints())).replaceAll("<max>", ""+max).replaceAll("&", "§"));
							}
							meta.setLore(filore);
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
			String need = plugin.getAPI().toHex(name).replaceAll("&", "§");
			if (title.equalsIgnoreCase(need)) {
				for (String pl : list) {
					String[] t = pl.split(":");

					String mat = t[0].toUpperCase();
					int slot = Integer.valueOf(t[1]).intValue();
					String display = t[2];

					ItemStack item = api.createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));
					item.setItemMeta(meta);

					inv_view.setItem(slot, item);
				}

			}

		});

	}

}
