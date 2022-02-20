package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.ItemAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.managers.GUIManager;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.utils.Action;

public class JobsEditorMenuManager {
	
	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI(); 
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private ItemAPI im = SimpleAPI.getInstance().getItemAPI();
	private GUIManager gm = SimpleAPI.getInstance().getGUIManager(); 

	public JobsEditorMenuManager(UltimateJobs plugin) {
		this.plugin = plugin;
	}
	
	public void EditLevelsList(Player player, Job job, int page) {  
		String name = "§eJob Levels";
		gm.openInventory(player, 5, name);
		
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
				inv.setItem(40, it); 
			}
			
			ArrayList<ItemStack> itm = new ArrayList<ItemStack>();

			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			
			if(page == 1) {
			
				if(inv.getTitle().equalsIgnoreCase(name)) {
					ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
					ItemMeta meta = it.getItemMeta();
					
					meta.setDisplayName("§8< §cGo Back §8>");
					  
					it.setItemMeta(meta); 
					
					inv.setItem(40, it); 
				}  
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "SLIME_BALL");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §aAdd new Level §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(38, it); 
			}  
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BARRIER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cRemove one Level §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(37, it); 
			}  
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BOOK");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Page "+page+" §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(43, it); 
			}  
			
			//27 per page
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				
				int pageLength = 28;
				int items = job.getCountOfLevels() + 1;
				int cl = page * pageLength;
				 
				if(items >= cl) {
					if(inv.getTitle().equalsIgnoreCase(name)) {
						ItemStack it = im.createItemStack(player.getName(), "ARROW");
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName("§8< §aNext Page §8>");
						  
						it.setItemMeta(meta); 
						
						inv.setItem(44, it); 
					}  
				}
				
				if(page != 1) {
					if(inv.getTitle().equalsIgnoreCase(name)) {
						ItemStack it = im.createItemStack(player.getName(), "ARROW");
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName("§8< §cPrevious Page §8>");
						  
						it.setItemMeta(meta); 
						
						inv.setItem(42, it); 
					} 
				}
				
				 
				 for (int i = (page - 1) * pageLength; i < (page * pageLength) && i < items; i++) {

					if(i != 0) { 
						ItemStack it = im.createItemStack(player.getName(), "PAPER");
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName("§8< §6Level "+i+" §8>");
						
						ArrayList<String> lore = new ArrayList<String>();
						    
						lore.add("§7Display§8: " + job.getLevelDisplay(i).replaceAll("&", "§"));
						lore.add("§7Money§8: §b" + job.getVaultOnLevel(i));
						lore.add("§7Need§8: §b" + job.getExpOfLevel(i));
						lore.add("§7EarnMore§8: §b" + job.getMultiOfLevel(i));
						lore.add("§8");
						lore.add("§7Commands§8:");
						
						if(job.getCommands(i).size() == 0) {
							lore.add("§cNone");
						} else {
							for(String cmd : job.getCommands(i)) {
								lore.add("§8-> §7"+cmd);
							}
						}
						
						meta.setLore(lore);
						
						it.setItemMeta(meta); 
						
						itm.add(it);
					}
					}
				}
 			 
			
			int size = itm.size();
			
			for (int i = 0; i < size; i++) {

				if (size >= i) {
					
					ItemStack item = itm.get(i);
					
					int slot = 9 + i;
					
					inv.setItem(slot, item);
					
				}
			}
			
		});
		 
	}
	

	public void EditJobMenu(Player player, Job job) {  
		String name = "§6Job Settings";
		gm.openInventory(player, 5, name);
		
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
			
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();

			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cGo Back §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}  
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "PAPER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Action §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getAction());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), job.getIcon());
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Icon §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getIcon());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "CHEST");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Job Description §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§8> §7Current§8: §b");
				lore.add("§7");
				
				for(String line : job.getLore()) {
					lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "NAME_TAG");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Display §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getDisplay());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), job.getBarColor()+"_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change BossBar Color §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getBarColor());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "LEVER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Slot §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getSlot());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "EMERALD");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Change Job Price §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7Current§8: §b"+job.getPrice());
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "NETHER_STAR");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Bypass Permission §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				boolean has = job.hasBypassPermission();
				
				String wh = null;
				
				if(has) {
					wh = "§aYes";
				} else {
					wh = "§cNone";
				}
				
				lore.add("§8> §7Has§8: §b"+wh);
				if(has) {
					lore.add("§8> §7Current§8: §b"+job.getByPassPermission());
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "ENDER_PEARL");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Job Permission §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				boolean has = job.hasPermission();
				
				String wh = null;
				
				if(has) {
					wh = "§aYes";
				} else {
					wh = "§cNone";
				}
				
				lore.add("§8> §7Has§8: §b"+wh);
				if(has) {
					lore.add("§8> §7Current§8: §b"+job.getPermission());
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			} 
			if(job.hasPermission()) {
				if(inv.getTitle().equalsIgnoreCase(name)) {
					ItemStack it = im.createItemStack(player.getName(), "BOOK");
					ItemMeta meta = it.getItemMeta();
					
					meta.setDisplayName("§8< §7Permissions Lore §8>");
					
					ArrayList<String> lore = new ArrayList<String>(); 
					lore.add("§8> §7Current§8:");
					lore.add("§8");
					for(String line : job.getPermissionsLore()) {
						lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
					}
					
					meta.setLore(lore);
					
					it.setItemMeta(meta); 
					
					items.add(it);
				}
				if(inv.getTitle().equalsIgnoreCase(name)) {
					ItemStack it = im.createItemStack(player.getName(), "TORCH");
					ItemMeta meta = it.getItemMeta();
					
					meta.setDisplayName("§8< §7Permissions Message §8>");
					
					ArrayList<String> lore = new ArrayList<String>(); 
					lore.add("§8> §7Current§8:"); 
					lore.add("§7");
					lore.add(up.toHex(job.getPermMessage()).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				  
					meta.setLore(lore);
					
					it.setItemMeta(meta); 
					
					items.add(it);
				}
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRASS_BLOCK");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Worlds §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§8> §7List§8: §b");
				lore.add("§7");
				
				for(String line : job.getWorlds()) {
					lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "IRON_SWORD");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7Stats Lore §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				
				lore.add("§8> §7Current§8: §b");
				lore.add("§7");
				
				for(String line : job.getStatsMessage()) {
					lore.add(up.toHex(line).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§"));
				}
				
				meta.setLore(lore);
				
				it.setItemMeta(meta); 
				
				items.add(it);
			}
			 
			
			int size = items.size();
			
			for (int i = 0; i < size; i++) {

				if (size >= i) {
					
					ItemStack item = items.get(i);
					
					int slot = 9 + i;
					
					inv.setItem(slot, item);
					
				}
			}
			
		});
		 
	}
	
	public void EditorChooseJob(Player player, boolean sound, String name) {  
	 
		gm.openInventory(player, 5, name);
		
		if(sound) {
			player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
		}
		
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
	
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cClose §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
			ArrayList<String> jobs = plugin.getLoaded();
			int size = jobs.size();
			
			if(size == 0) {
				
				ItemStack it = im.createItemStack(player.getName(), "BARRIER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§cNo Jobs Found");
				  
				it.setItemMeta(meta);
				  
					inv.setItem(22, it); 
					
					return;
				
			} else {
				for (int i = 0; i < size; i++) {

					if (size >= i) {
						
						Job job = plugin.getJobCache().get(jobs.get(i));
						
						ItemStack it = im.createItemStack(player.getName(), job.getIcon());
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName(job.getDisplay());
						
						ArrayList<String> lore = new ArrayList<String>();
						  
						lore.add("§8< Click to edit this Job §8>");
						
						meta.setLore(lore);
						
						meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
						meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
						
						it.setItemMeta(meta);
						
						int slot = i + 9;
						 
						if(inv.getItem(slot) == null) {
							inv.setItem(slot, it); 
						}
						
					}
				}
			}
			
		});
	}

	
	public void openTwo(Player player, Job job, List<String> li, String mo) {
		new BukkitRunnable() {
			public void run() {

				plugin.getEditorMenuManager().openEditorForLists(player, job, li, mo);
			}
		}.runTaskLater(plugin, 5);
	}
	
	public void open(Player player, Job job) {
		new BukkitRunnable() {
			public void run() {
				EditJobMenu(player, job);
			}
		}.runTaskLater(plugin, 5);
	}
	
	 
	
	public void openEditMenuOfBarColor(Player player, Job job) {  
		String name = "§6Edit BossBar Color";
		gm.openInventory(player, 5, name);
		 
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
			 
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			 
			if(inv.getTitle().equalsIgnoreCase(name)) {
				
				int size = BarColor.values().length;
				
				for (int i = 0; i < size; i++) {

					if (size >= i) {
						
						BarColor[] split =  BarColor.values();
						
						BarColor ac = split[i];
						
						ItemStack it = im.createItemStack(player.getName(), ac+"_DYE");
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName("§8< §7"+ac+" §8>");
						  
						it.setItemMeta(meta);
						   
						int slot = i + 9;
						 
						if(inv.getItem(slot) == null) {
							inv.setItem(slot, it); 
						}
						
					}
				}
				  
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cGo Back §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
		});
	}
	
	public void openEditorForLists(Player player, Job job, List<String> list, String type) {  
		String name = "§6Edit List";
		gm.openInventory(player, 5, name);
		 
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
	

			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cGo Back §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				 
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "PAPER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§7§lCurrent Lore: §b"+type);
				
				ArrayList<String> lore = new ArrayList<String>();
			 
				for(String b : list) {
					lore.add(b.replaceAll("&", "§"));
				}
				 
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(19, it); 
	 
			}
	
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "LIME_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §aAdd new Line §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(21, it); 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "ORANGE_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cRemove one Line §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(22, it); 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BARRIER");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cReset §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(23, it); 
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "SLIME_BALL");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §a§lFinish and Save §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(25, it); 
			}
			
		});
	}
	
	public void openEditMenuOfActions(Player player, Job job) {  
		String name = "§6Edit Action";
		gm.openInventory(player, 5, name);
		 
		InventoryView inv = player.getOpenInventory();
		
		plugin.getExecutor().execute(() -> { 
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "GRAY_STAINED_GLASS_PANE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8-/-");
				  
				it.setItemMeta(meta);
				  
				inv.setItem(0, it); 
				inv.setItem(1, it); 
				inv.setItem(2, it); 
				inv.setItem(3, it); 
				inv.setItem(4, it); 
				inv.setItem(5, it); 
				inv.setItem(6, it); 
				inv.setItem(7, it); 
				inv.setItem(8, it); 
				inv.setItem(36, it); 
				inv.setItem(37, it); 
				inv.setItem(38, it); 
				inv.setItem(39, it);  
				inv.setItem(41, it);
				inv.setItem(42, it); 
				inv.setItem(43, it); 
				inv.setItem(44, it); 
	 
			}
			 
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "BEACON");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §7"+job.getID()+" §8>");
				
				ArrayList<String> lore = new ArrayList<String>();
				  
				lore.add("§8> §7You edit the Job "+job.getDisplay());
				
				meta.setLore(lore);
				  
				it.setItemMeta(meta);
				   
				inv.setItem(4, it); 
	 
			}
			 
			if(inv.getTitle().equalsIgnoreCase(name)) {
				
				int size = Action.values().length;
				
				for (int i = 0; i < size; i++) {

					if (size >= i) {
						
						Action[] split =  Action.values();
						
						Action ac = split[i];
						
						ItemStack it = im.createItemStack(player.getName(), "PAPER");
						ItemMeta meta = it.getItemMeta();
						
						meta.setDisplayName("§8< §7"+ac+" §8>");
						  
						it.setItemMeta(meta);
						   
						int slot = i + 9;
						 
						if(inv.getItem(slot) == null) {
							inv.setItem(slot, it); 
						}
						
					}
				}
				  
			}
			
			if(inv.getTitle().equalsIgnoreCase(name)) {
				ItemStack it = im.createItemStack(player.getName(), "RED_DYE");
				ItemMeta meta = it.getItemMeta();
				
				meta.setDisplayName("§8< §cGo Back §8>");
				  
				it.setItemMeta(meta); 
				
				inv.setItem(40, it); 
			}
			
		});
	}
	
}
