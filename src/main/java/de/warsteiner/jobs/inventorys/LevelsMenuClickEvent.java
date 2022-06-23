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

public class LevelsMenuClickEvent implements Listener {

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

		FileConfiguration cfg = plugin.getFileManager().getLevelGUIConfig();

		Player p = (Player) e.getWhoClicked();
		UUID UUID = p.getUniqueId();
		
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+UUID);
		
		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title =  plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		for(String job : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(job);
			String name =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("Levels_Name")).replaceAll("<job>", j.getDisplay(""+UUID));
			
			if(name.equalsIgnoreCase(title)) {
				plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Levels_Custom", cfg);
				
				String next =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.Display"));
				String pre =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.Display"));  
				int page = plugin.getPlayerDataAPI().getPageFromID(""+p.getUniqueId(),"LEVELS_"+j.getConfigID());
				if(display.equalsIgnoreCase( plugin.getPluginManager().toHex(next).replaceAll("&", "§"))) { 
					int d = cfg.getStringList("Level_Slots").size();
					int perpage = d + 1; 
					int cl = page * perpage + 1;
					 
					if (j.getCountOfLevels() >= cl) {
						plugin.getPlayerDataAPI().addOnePageFromID(""+p.getUniqueId(),  "LEVELS_"+j.getConfigID());
						plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("NEW_PAGE_LEVELS", p);
						
					} else {
						p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.NotFound")));
						plugin.getAPI().playSound("LEVELS_NO_NEXT", p);
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
						
						 
						plugin.getAPI().playSound("LEVELS_FIRST_ALREADY", p);
					} else {
						plugin.getPlayerDataAPI().removeOnePageFromID(""+p.getUniqueId(),  "LEVELS_"+j.getConfigID());
						plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("LAST_PAGE_LEVELS", p);
					}
				}
				
				e.setCancelled(true);
			}
			
		}
	}
}
