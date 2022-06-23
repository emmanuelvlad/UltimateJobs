package de.warsteiner.jobs.inventorys;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class RewardsMenuClickEvent  implements Listener {

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

		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();

		Player p = (Player) e.getWhoClicked();
		UUID UUID = p.getUniqueId();
		
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+UUID);
		
		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title =  plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		for(String job : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(job);
			String name =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("Rewards_Name")).replaceAll("<job>", j.getDisplay(""+UUID));
			
			if(name.equalsIgnoreCase(title)) {
				plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Rewards_Custom", cfg);
				
				String next =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.Display"));
				String pre = jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.Display"));  
				int page = plugin.getPlayerDataAPI().getPageFromID(""+p.getUniqueId(),"REWARDS_"+j.getConfigID());
				if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) { 
					int d = cfg.getStringList("Rewards_Slots").size();
					int perpage = d; 
					int cl = page * perpage + 1;
					 
					if (j.getAllNotRealIDSFromActionsAsArray().size() >= cl) {
						plugin.getPlayerDataAPI().addOnePageFromID(""+p.getUniqueId(),  "REWARDS_"+j.getConfigID());
						plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("NEW_PAGE_REWARDS", p);
						
					} else {
						p.sendMessage(   jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.NotFound")));
						plugin.getAPI().playSound("REWARDS_NO_NEXT", p);
					}
					
				} else if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(pre).replaceAll("&", "§"))) {
					if(page == 1) {
						
						String mode = cfg.getString("PageItems.WhatHappensWhenFirstPageAlreardyReached").toUpperCase();
						
						if(mode.equalsIgnoreCase("MESSAGE")) {
							p.sendMessage(   jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.NotFound"))); 
						} else 	if(mode.equalsIgnoreCase("MAINGUI")) {
							plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
						} else 	if(mode.equalsIgnoreCase("COMMAND")) {
							
							String c = cfg.getString("PageItems.WhatHappensWhenFirstPage_Command");
							
							p.performCommand(c);
							
						}
						
			 
						plugin.getAPI().playSound("REWARDS_FIRST_ALREADY", p);
					} else {
						plugin.getPlayerDataAPI().removeOnePageFromID(""+p.getUniqueId(),  "REWARDS_"+j.getConfigID());
						plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("LAST_PAGE_REWARDS", p);
					}
				}
				
				e.setCancelled(true);
			}
			
		}
	}
}
