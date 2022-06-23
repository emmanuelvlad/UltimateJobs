package de.warsteiner.jobs.inventorys;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class EarningsMenuClickEvent implements Listener{
 
		private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

		@EventHandler
		public void onInvClick(InventoryClickEvent e) {
			if (e.getClickedInventory() == null) {
				return;
			}
			if (e.getCurrentItem() == null) {
				return;
			}

			if (e.getView().getTitle() == null) {
				return;
			}

			if (e.getCurrentItem().getItemMeta() == null) {
				return;
			}

			if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
				return;
			}

			FileConfiguration cfg = plugin.getFileManager().getEarningsAllConfig();

			Player p = (Player) e.getWhoClicked();
			UUID UUID = p.getUniqueId();
			
			JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+UUID);
			
			String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
			String title =  plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));
 
				String name =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("All_Earnings_Name"));
				
				if(name.equalsIgnoreCase(title)) {
					plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "All_Earnings_Custom", cfg);
					
					String next =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.Display"));
					String pre =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.Display"));  
					int page = plugin.getPlayerDataAPI().getPageFromID(""+p.getUniqueId(),"EARNINGS_ALL");
					
					ArrayList<String> l2 = plugin.getGUIAddonManager().getAmountToDisplay(cfg, p, page);
			 
					if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) { 
						int d = cfg.getStringList("All_Earnings_Slots").size();
						int perpage = d + 1; 
						int cl = page * perpage + 1;
						 
						if (l2.size() >= cl) {
							plugin.getPlayerDataAPI().addOnePageFromID(""+p.getUniqueId(),  "EARNINGS_ALL");
							plugin.getGUIAddonManager().createEarningsGUI_ALL_Jobs(p, UpdateTypes.REOPEN);
							plugin.getAPI().playSound("EARNINGS_ALL_PAGE_LEVELS", p);
							
						} else {
							p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.NotFound")));
							plugin.getAPI().playSound("EARNINGS_ALL_NO_NEXT", p);
						}
						
					} else if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
						if(page == 1) {
							
							String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();
							
							if(mode.equalsIgnoreCase("MESSAGE")) {
								p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.NotFound"))); 
							} else 	if(mode.equalsIgnoreCase("MAINGUI")) {
								plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
							} else 	if(mode.equalsIgnoreCase("COMMAND")) {
								
								String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");
								
								p.performCommand(c);
								
							}
							
							 
							plugin.getAPI().playSound("EARNINGS_ALL_FIRST_ALREADY", p);
						} else {
							plugin.getPlayerDataAPI().removeOnePageFromID(""+p.getUniqueId(),  "EARNINGS_ALL");
							plugin.getGUIAddonManager().createEarningsGUI_ALL_Jobs(p, UpdateTypes.REOPEN);
							plugin.getAPI().playSound("LAST_EARNINGS_ALL", p);
						}
					}
					
					e.setCancelled(true);
				 
			} else {
				
				FileConfiguration cf = plugin.getFileManager().getEarningsJobConfig();
				
				Job found = null;
				
				for(String j : plugin.getLoaded()) {
					
					Job job = plugin.getJobCache().get(j);
					
					String guinamed = jb.getLanguage().getStringFromPath(jb.getUUID(), cf.getString("Job_Earnings_Name")).replaceAll("<job>", job.getDisplay(""+p.getUniqueId()));
					
					if(title.equalsIgnoreCase(guinamed)) {
						
						found = job;
						
					}
				}
				
				
				if(found != null) {
					plugin.getClickManager().executeCustomItemInSubMenu(found, display, p, "Job_Earnings_Custom", cf);
					
					String next =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cf.getString("PageItems.Next.Display"));
					String pre =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cf.getString("PageItems.Previous.Display"));  
					int page = plugin.getPlayerDataAPI().getPageFromID(""+p.getUniqueId(),"EARNINGS_"+found.getConfigID());
					
					ArrayList<String> l2 = plugin.getGUIAddonManager().getAmountToDisplay(cf, p, page);
			 
					if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) { 
						int d = cfg.getStringList("Job_Earnings_Slots").size();
						int perpage = d + 1; 
						int cl = page * perpage + 1;
						 
						if (l2.size() >= cl) {
							plugin.getPlayerDataAPI().addOnePageFromID(""+p.getUniqueId(),  "EARNINGS_"+found.getConfigID());
							plugin.getGUIAddonManager().createEarningsGUI_Single_Job(p, UpdateTypes.REOPEN, found);
							plugin.getAPI().playSound("EARNINGS_JOB_PAGE_LEVELS", p);
							
						} else {
							p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cf.getString("PageItems.Next.NotFound")));
							plugin.getAPI().playSound("EARNINGS_JOB_NO_NEXT", p);
						}
						
					} else if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
						if(page == 1) {
							
							String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();
							
							if(mode.equalsIgnoreCase("MESSAGE")) {
								p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cf.getString("PageItems.Previous.NotFound"))); 
							} else 	if(mode.equalsIgnoreCase("MAINGUI")) {
								plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
							} else 	if(mode.equalsIgnoreCase("COMMAND")) {
								
								String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");
								
								p.performCommand(c);
								
							}
							
							 
							plugin.getAPI().playSound("EARNINGS_JOB_FIRST_ALREADY", p);
						} else {
							plugin.getPlayerDataAPI().removeOnePageFromID(""+p.getUniqueId(),  "EARNINGS_"+found.getConfigID());
							plugin.getGUIAddonManager().createEarningsGUI_Single_Job(p, UpdateTypes.REOPEN, found);
							plugin.getAPI().playSound("LAST_EARNINGS_ALL", p);
						}
					}
					
					e.setCancelled(true);
				}
				
				
			}
		}
	 


}
