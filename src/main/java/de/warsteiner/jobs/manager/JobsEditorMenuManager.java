package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
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
				plugin.getGUI().EditJobMenu(player, job);
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
