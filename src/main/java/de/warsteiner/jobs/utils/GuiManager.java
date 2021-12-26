package de.warsteiner.jobs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;

public class GuiManager {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	public Inventory createGui(int size, String name) {
		final Inventory inv = Bukkit.createInventory(null, size * 9,
				plugin.getJobAPI().toHex(name).replaceAll("&", "§"));
		return inv;
	}
	
	public void createMainGUIOfJobs(Player player) {
		
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		
		String name = config.getString("Main_Name");
		int size = config.getInt("Main_Size");
		
		Inventory inv = createGui(size, name);
		
		player.openInventory(inv);
		
		InventoryView inv_view = player.getOpenInventory();
		
		setPlaceHolders(player, inv, config.getStringList("Main_Place"), name);
		setCustomitems(player, inv_view, config, "Main_Custom.", config.getStringList("Main_Custom.List"), name);
		setMainInventoryJobItems(inv_view, player, name);
	}
	
	public void createSettingsGUI(Player player, File job) {
		
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		String dis = plugin.getJobAPI().getDisplay(job);
		String name = config.getString("Settings_Name").replaceAll("<job>", dis);
		int size = config.getInt("Settings_Size");
		
		Inventory inv = createGui(size, name);
		
		player.openInventory(inv);
		
		InventoryView inv_view = player.getOpenInventory();
		
		setPlaceHolders(player, inv, config.getStringList("Settings_Place"), name);
		setCustomitems(player, inv_view, config, "Settings_Custom.", config.getStringList("Settings_Custom.List"), name);
 
	}
	
	public File isSettingsGUI(String menu) {
		for(File file : plugin.getLoadedJobs()) {
			String dis = plugin.getJobAPI().getDisplay(file);
			String fin = plugin.getJobAPI().toHex(plugin.getMainConfig().getConfig().getString("Settings_Name").replaceAll("<job>", dis).replaceAll("&", "§")); 
			if(fin.equalsIgnoreCase(menu)) {
				return file;
			}
		}
		return null;
	}
	
	public void executeCustomItemInSettings(File job, String display,  final Player player, YamlConfiguration config) {
		String item = isCustomItem(display, player, config, "Settings_Custom");
		String UUID = ""+player.getUniqueId(); 
		if(!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = config.getString("Settings_Custom."+item+".Action");
			if(action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
				    public void run() {
				    	player.closeInventory();
				    }
				}.runTaskLater(plugin, 5);
			}else if(action.equalsIgnoreCase("BACK")) {
				    plugin.getGUIManager().createMainGUIOfJobs(player); 
			}  else if(action.equalsIgnoreCase("LEAVE")) {
				if(!plugin.getPlayerAPI().hasAnyJob(UUID)) {
					plugin.getPlayerAPI().remCurrentJobs(UUID, plugin.getJobAPI().getID(job));
					plugin.getGUIManager().createMainGUIOfJobs(player);
					player.sendMessage(plugin.getJobAPI().getMessage("Left_Job").replaceAll("<job>", plugin.getJobAPI().getDisplay(job)));
				}  
			}  else if(action.equalsIgnoreCase("MONEYLIST")) {
				player.sendMessage("UltimateJobs extension Money-List");
			}  else if(action.equalsIgnoreCase("EARNINGS")) {
				player.sendMessage("UltimateJobs extension Earnings-List");
			}
		}  
	}
	 
	public void executeCustomItem(String display, final Player player, YamlConfiguration config, String name) {
		String item = isCustomItem(display, player, config, "Main_Custom");
		String UUID = ""+player.getUniqueId();
		InventoryView inv = player.getOpenInventory();
		if(!item.equalsIgnoreCase("NOT_FOUND")) {
			String action = config.getString("Main_Custom."+item+".Action");
			if(action.equalsIgnoreCase("CLOSE")) {
				new BukkitRunnable() {
				    public void run() {
				    	player.closeInventory();
				    }
				}.runTaskLater(plugin, 5);
			} else if(action.equalsIgnoreCase("LEAVE")) {
				if(!plugin.getPlayerAPI().hasAnyJob(UUID)) {
					plugin.getPlayerAPI().setCurrentJobsToNull(UUID);
					setCustomitems(player, inv, config, "Main_Custom.", config.getStringList("Main_Custom.List"), name);
					setMainInventoryJobItems(inv, player, name);
					player.sendMessage(plugin.getJobAPI().getMessage("Leave_All"));
				} else {
					player.sendMessage(plugin.getJobAPI().getMessage("Already_Left_All"));
				} 
			}
		}  
	}
	
	public String isCustomItem(String display, Player player, YamlConfiguration config, String path) {
		List<String> custom_items = config.getStringList(path+".List");
		for (String b : custom_items) { 
			String dis = plugin.getJobAPI().toHex(config.getString(path+"."+b+".Display").replaceAll("&", "§"));  
			if(display.equalsIgnoreCase(dis)) {
				return b;
			}
		}
		
		return "NOT_FOUND";
	}
	
	public void executeJobClickEvent(String display, Player player, YamlConfiguration config) {
	 
		List<File> jobs = plugin.getLoadedJobs();
		JobAPI api =  plugin.getJobAPI();
		PlayerAPI p = plugin.getPlayerAPI();
	 
		String UUID =""+player.getUniqueId();
		
		 for (int i = 0; i <= jobs.size()-1; i++)
		  {
			File file = jobs.get(i);
			String dis = api.getDisplay(file); 
			if(display.equalsIgnoreCase(dis)) {
				String job = api.getID(file);
				
				InventoryView inv = player.getOpenInventory();
				String name = config.getString("Main_Name");
				
				if(p.ownJob(UUID, job)) {
					
					if(p.isInJob(UUID, job)) {
						createSettingsGUI(player, file);
					} else {
						
						int max = plugin.getPlayerAPI().getMaxJobs(UUID) - 1;
						if (p.getCurrentJobs(UUID).size() <= max) {
							p.addCurrentJobs(UUID, job);
							
							setCustomitems(player, inv, config, "Main_Custom.", config.getStringList("Main_Custom.List"), name);
							setMainInventoryJobItems(inv, player, name);
							
							player.sendMessage(api.getMessage("Joined").replaceAll("<job>", dis));
						} else {
							player.sendMessage(api.getMessage("Full").replaceAll("<job>", dis));
						}
					}
		 
				} else {
					
					double money = api.getPrice(file);
					
					if(plugin.getEco().getBalance(player) >= money) {
					 
						plugin.getEco().withdrawPlayer(player, money);
						p.addOwnJob(UUID, job);
						p.createJob(UUID, job, config);
						 
						setCustomitems(player, inv, config, "Main_Custom.", config.getStringList("Main_Custom.List"), name);
						setMainInventoryJobItems(inv, player, name);
						
						player.sendMessage(api.getMessage("Bought_Job").replaceAll("<job>", dis));
					} else {
						player.sendMessage(api.getMessage("Not_Enough_Money").replaceAll("<job>", dis));
					}
					 
				}
				return;
			}
			}
		 
	}
	
	public void setMainInventoryJobItems(InventoryView inv, Player player, String name) {
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		
		String title = player.getOpenInventory().getTitle();
		JobAPI api = plugin.getJobAPI();
		String UUID = ""+player.getUniqueId();
		PlayerAPI p = plugin.getPlayerAPI();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "§");
		if(title.equalsIgnoreCase(need)) {
			
			ArrayList<File> jobs = plugin.getLoadedJobs();
			
			for(File job : jobs) {
				
				String display = api.toHex(api.getDisplay(job).replaceAll("&", "§"));
				int slot = api.getSlot(job);
				List<String> lore = api.getLore(job);
				String mat = api.getMaterial(job);
				double price = api.getPrice(job);
				String id = api.getID(job);
				String date = api.getDate(); 
				
				ItemStack item = createItemStack(player, mat);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(display.replaceAll("&", "§"));
				
				List<String> see; 
				
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				
				if(p.ownJob(UUID, id)) {
					
					if(plugin.getPlayerAPI().isInJob(UUID, id)) {
						meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
					 
						see = config.getStringList("Jobs.Lore.In");  
					} else {
						see = config.getStringList("Jobs.Lore.Bought"); 
					}
					
				} else {
					see =config.getStringList("Jobs.Lore.Price"); 
				} 
				List<String> filore = new ArrayList<String>() ;
				for(String l : lore) {
					filore.add(plugin.getJobAPI().toHex(l).replaceAll("&", "§"));
				} 
				if(p.isInJob(UUID, id)) {
					for(String l : plugin.getJobAPI().getStatsDesc(job)) {
						int level = p.getJobLevel(UUID, id);
						filore.add(plugin.getJobAPI().toHex(l)
								.replaceAll("<stats_args_4>",getLevelname(job, level))
								.replaceAll("<stats_args_3>",""+ level)
								.replaceAll("<stats_args_2>",""+ p.getStatsArgAsInt(UUID, id, 2))
								.replaceAll("<stats_args_6>",""+ api.Format(plugin.getLevelAPI().getJobNeedExp(UUID, job, id)))
								.replaceAll("<stats_args_5>",""+ api.Format(p.getJobExp(UUID, id)))
								.replaceAll("<stats_args_9>",""+ api.Format(p.getStatsArg(UUID, id, 9)))
								.replaceAll("<stats_args_7>",""+ api.Format(p.getMoneyAll(UUID, id)))
								.replaceAll("<stats_args_8>",""+ api.Format(p.getMoneyToday(UUID, id, date)))
								.replaceAll("<stats_args_1>", p.getBoughtDate(UUID, id) )
								.replaceAll("&", "§"));
					} 
				}
				for(String l : see) {
					filore.add(plugin.getJobAPI().toHex(l).replaceAll("<price>", ""+price).replaceAll("&", "§"));
				} 
				meta.setLore(filore); 
				
				item.setItemMeta(meta);
				
				inv.setItem(slot, item); 
				
			} 
		}
	}
	 
	public String getLevelname(File job, int level) {
		if(plugin.getLevelAPI().getLevelName(job, level) != null) {
			return plugin.getLevelAPI().getLevelName(job, level);
		}
		return "NOT_FOUND";
	}
 
	public void setCustomitems(Player player, InventoryView inv, YamlConfiguration config, String prefix, List<String> list, String name) {
		String title = player.getOpenInventory().getTitle();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "§");
		if(title.equalsIgnoreCase(need)) {
			for (String pl : list) { 
				if(config.contains(prefix + pl + ".Display")) {
					String display = config.getString(prefix + pl + ".Display");
					String mat = config.getString( prefix + pl + ".Material").toUpperCase();
					int slot = config.getInt(prefix + pl + ".Slot");
				  
					ItemStack item = createItemStack(player, mat);
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(display.replaceAll("&", "§"));
				 
					if(config.contains(prefix + pl + ".Lore")) {
						List<String> lore = config.getStringList( prefix + pl + ".Lore");
						List<String> filore = new ArrayList<String>() ;
						for(String l : lore) {
							filore.add(plugin.getJobAPI().toHex(l).replaceAll("&", "§"));
						}
						meta.setLore(filore); 
					} 
					item.setItemMeta(meta);
		 
					inv.setItem(slot, item); 
				} else {
					plugin.getLogger().warning("§c§lMissing Element in "+ need + " §4§lCustom Item: §b§l"+pl);
				}
			}
		}	
	}
	
	public void setPlaceHolders(Player player, Inventory inv, List<String> list, String name) {
		String title = player.getOpenInventory().getTitle();
		String need = plugin.getJobAPI().toHex(name).replaceAll("&", "§");
		if(title.equalsIgnoreCase(need)) {
			for (String pl : list) {
				String[] t = pl.split(":");
	
				String mat = t[0].toUpperCase();
				int slot = Integer.valueOf(t[1]).intValue();
				String display = t[2];
	
				ItemStack item = createItemStack(player, mat);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(display.replaceAll("&", "§"));
				item.setItemMeta(meta);
	 
				inv.setItem(slot, item); 
			}
		}	
	}
	

	public ItemStack createItemStack(Player p, String item) { 
		ItemStack i; 
		if (Material.getMaterial(item.toUpperCase()) == null) {
			i = generateSkull(item.replaceAll("<skull>", p.getName()));
		} else {
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}
		return i;
	}

	public ItemStack generateSkull(String owner) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(owner);
		itemStack.setItemMeta((ItemMeta) skullMeta);
		return itemStack;
	}
	
}
