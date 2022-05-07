package de.warsteiner.jobs.inventorys;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class LevelsMenuClickEvent implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();

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
		
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = up.toHex(e.getView().getTitle().replaceAll("&", "§"));

		for(String job : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(job);
			String name =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("Levels_Name")).replaceAll("<job>", j.getDisplay(""+UUID));
			
			if(name.equalsIgnoreCase(title)) {
				plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Levels_Custom", cfg);
				
				String next =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.Display"));
				String pre =  jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.Display"));  
				int page = SimpleAPI.getPlugin().getPlayerDataAPI().getPageFromID(""+p.getUniqueId(),"LEVELS_"+j.getConfigID());
				if(display.equalsIgnoreCase(up.toHex(next).replaceAll("&", "§"))) { 
					int d = cfg.getStringList("Level_Slots").size();
					int perpage = d + 1; 
					int cl = page * perpage;
					 
					if (j.getCountOfLevels() >= cl) {
						SimpleAPI.getPlugin().getPlayerDataAPI().addOnePageFromID(""+p.getUniqueId(),  "LEVELS_"+j.getConfigID());
						plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("NEW_PAGE_LEVELS", p);
						
					} else {
						p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Next.NotFound")));
						plugin.getAPI().playSound("LEVELS_NO_NEXT", p);
					}
					
				} else if(display.equalsIgnoreCase(up.toHex(pre).replaceAll("&", "§"))) {
					if(page == 1) {
						p.sendMessage( jb.getLanguage().getStringFromPath(p.getUniqueId(), cfg.getString("PageItems.Previous.NotFound"))); 
						plugin.getAPI().playSound("LEVELS_FIRST_ALREADY", p);
					} else {
						SimpleAPI.getPlugin().getPlayerDataAPI().removeOnePageFromID(""+p.getUniqueId(),  "LEVELS_"+j.getConfigID());
						plugin.getGUIAddonManager().createLevelsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("LAST_PAGE_LEVELS", p);
					}
				}
				
				e.setCancelled(true);
			}
			
		}
	}
}
