package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.ItemAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.managers.GUIManager;
import de.warsteiner.datax.player.PlayerSaveAndLoadManager;
import de.warsteiner.datax.utils.Language;
import de.warsteiner.datax.utils.SimplePlayer;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.player.PlayerDataManager;

public class GuiAddonManager {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private ItemAPI im = SimpleAPI.getInstance().getItemAPI();
	private GUIManager gm = SimpleAPI.getInstance().getGUIManager();

	public GuiAddonManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}

	public void createSelfStatsGUI(Player player, UpdateTypes t) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
		String UUID = "" + player.getUniqueId();
		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer(UUID);
		String name = plugin.getPluginManager().getSomethingFromPath(sp.getUUID(), cfg.getString("Self_Name"));
		int size = cfg.getInt("Self_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_SELF_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Self_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Self_Custom.",
				cfg.getStringList("Self_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, UUID, player.getName(), player,"Self");
	}
	
	
	public boolean isStatsGUI(String title, UUID id) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig(); 
		for (Player list : Bukkit.getOnlinePlayers()) {
			 
			String name = list.getName(); 
			
			String named = plugin.getPluginManager().getSomethingFromPath(id, cfg.getString("Other_Name"));
			String fin = up.toHex(named. 
					replaceAll("<name>", name).replaceAll("&", "§"));
			 
			if (up.toHex(fin).equalsIgnoreCase(title.replaceAll("<name>", name)))
				return true;
		}
		return false;
	}
	
	public void createOtherStatsGUI(Player player, UpdateTypes t, String named, String ud) {
		FileConfiguration cfg = plugin.getFileManager().getStatsConfig();
	 
		JobsPlayer sp = plugin.getPlayerManager().getRealJobPlayer(""+player.getUniqueId());
		String name = plugin.getPluginManager().getSomethingFromPath(sp.getUUID(), cfg.getString("Other_Name")).replaceAll("<name>", named);
		int size = cfg.getInt("Other_Size");

		gm.openInventory(player, size, name);

		if (t.equals(UpdateTypes.OPEN)) {
			api.playSound("OPEN_OTHER_STATS_GUI", player);
		}

		InventoryView inv_view = player.getOpenInventory();

		plugin.getGUI().setPlaceHolders(player, inv_view, cfg.getStringList("Other_Place"), name);
		plugin.getGUI().setCustomitems(player, player.getName(), inv_view, "Other_Custom.",
				cfg.getStringList("Other_Custom.List"), name, cfg, null);
		setStatsItems(inv_view, name, cfg, ud, named, player,"Other");
	}

	public void setStatsItems(InventoryView inv, String name, FileConfiguration cf, String WATCHUUID, String NAME, Player pl, String prefix) {
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

				 
			 
				YamlConfiguration lang = plugin.getPluginManager().getLanguages().get(SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(pl.getUniqueId()).getLanguage().getName()).getConfig();

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
				  
				
				if (cf.getString(prefix+"_Skull.Material") != null) {
					String skull_item =cf.getString(prefix+"_Skull.Material");
					String skull_display = plugin.getPluginManager().getSomethingFromPath(pl.getUniqueId(), cf.getString(prefix+"_Skull.Display"));
					int skull_slot = cf.getInt(prefix+"_Skull.Slot");
					List<String> skull_lore = plugin.getPluginManager().getSomethingAsListFromPath(pl.getUniqueId(), cf.getString(prefix+"_Skull.Lore"));

					ItemStack skull = im.createAndGetItemStack(NAME, skull_item);
					ItemMeta m = skull.getItemMeta();

					ArrayList<String> l = new ArrayList<String>();

					int finalmaxjobs = max + 1;

					for (String b : skull_lore) {
						l.add(up.toHex(b).replaceAll("<points>", api.Format(points))
								.replaceAll("<max>", "" + finalmaxjobs).replaceAll("<name>", NAME).replaceAll("&", "§"));
					}

					m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
					m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

					m.setLore(l);

					m.setDisplayName(up.toHex(skull_display).replaceAll("<name>", NAME).replaceAll("&", "§"));

					skull.setItemMeta(m);

					inv.setItem(skull_slot, skull);
				}

				List<String> slots = cf.getStringList(prefix+"_Slots");

				for (int i = 0; i < slots.size(); i++) {

					if (i >= li.size()) {

						String error_item = cf.getString(prefix+"_Mot_Found_Item.Icon");
						String error_display = plugin.getPluginManager().getSomethingFromPath(pl.getUniqueId(), cf.getString(prefix+"_Mot_Found_Item.Display"));
						List<String> errorl_lore =  plugin.getPluginManager().getSomethingAsListFromPath(pl.getUniqueId(), cf.getString(prefix+"_Mot_Found_Item.Lore"));

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
						String id = job.getID();

						// loading stats
 
						String date = null;
						int level = 0;
						double exp = 0;
						String bought = null;
						Integer broken =   0;
						
						if (how.equalsIgnoreCase("CACHE")) {
							JobsPlayer jb = plugin.getPlayerManager().getRealJobPlayer(WATCHUUID);
 
							date = jb.getDateOfJob(id);
							level = jb.getLevelOf(id);
							exp = jb.getExpOf(id);
							bought = jb.getDateOfJob(id);
							broken = jb.getBrokenOf(id);
						
						} else {
							
							 
							date = plm.getDateOf(WATCHUUID, job.getID());
							level =plm.getLevelOf(WATCHUUID, job.getID());
							exp = plm.getExpOf(WATCHUUID, job.getID());
							bought = plm.getDateOf(WATCHUUID, job.getID());
							broken = plm.getBrokenOf(WATCHUUID, job.getID());
							
						}
					  
						String usedbuy = "";
						String lvl = job.getLevelDisplay(level, WATCHUUID);
						String usedlvl = "";
						 

						String item = job.getIcon();
						String display =  lang.getString("Jobs."+job.getID().toUpperCase()+".StatsGUI.Display");
						List<String> lore = lang.getStringList("Jobs."+job.getID().toUpperCase()+".StatsGUI.Lore."+prefix);
					 
						ItemStack it = im.createAndGetItemStack(pl, item);
						ItemMeta m = it.getItemMeta();

						if(lore != null) {
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
								l.add(up.toHex(b).replaceAll("<stats_args_4>", usedlvl)
									.replaceAll("<name>", NAME)	.replace("<earned>",
												"" + api.Format(plugin.getPlayerDataModeManager().getEarnedAtDate(
														WATCHUUID, id, plugin.getPluginManager().getDate())))
										.replaceAll("<stats_args_3>", "" + level)
										.replaceAll("<stats_args_2>", "" + broken)
										.replaceAll("<stats_args_6>",
												"" + api.Format(plugin.getLevelAPI().getJobNeedExpWithOutPlayer(job, level)))
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
