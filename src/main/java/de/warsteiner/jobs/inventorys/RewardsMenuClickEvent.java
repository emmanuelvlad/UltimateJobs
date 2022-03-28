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

public class RewardsMenuClickEvent  implements Listener {

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

		FileConfiguration cfg = plugin.getFileManager().getRewardsConfig();

		Player p = (Player) e.getWhoClicked();
		UUID UUID = p.getUniqueId();
		
		
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = up.toHex(e.getView().getTitle().replaceAll("&", "§"));

		for(String job : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(job);
			String name = plugin.getPluginManager().getSomethingFromPath(UUID, cfg.getString("Rewards_Name")).replaceAll("<job>", j.getDisplay(""+UUID));
			
			if(name.equalsIgnoreCase(title)) {
				plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Rewards_Custom", cfg);
				
				String next = plugin.getPluginManager().getSomethingFromPath(UUID, cfg.getString("PageItems.Next.Display"));
				String pre = plugin.getPluginManager().getSomethingFromPath(UUID, cfg.getString("PageItems.Previous.Display"));  
				int page = SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().getPageFromID(""+p.getUniqueId(),"REWARDS_"+j.getConfigID());
				if(display.equalsIgnoreCase(up.toHex(next).replaceAll("&", "§"))) { 
					int d = cfg.getStringList("Rewards_Slots").size();
					int perpage = d + 1; 
					int cl = page * perpage;
					 
					if (j.getAllNotRealIDSFromActionsAsArray().size() >= cl) {
						SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().addOnePageFromID(""+p.getUniqueId(),  "REWARDS_"+j.getConfigID());
						plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("NEW_PAGE_REWARDS", p);
						
					} else {
						p.sendMessage( plugin.getPluginManager().getSomethingFromPath(UUID, cfg.getString("PageItems.Next.NotFound")));
						plugin.getAPI().playSound("REWARDS_NO_NEXT", p);
					}
					
				} else if(display.equalsIgnoreCase(up.toHex(pre).replaceAll("&", "§"))) {
					if(page == 1) {
						p.sendMessage( plugin.getPluginManager().getSomethingFromPath(UUID, cfg.getString("PageItems.Previous.NotFound"))); 
						plugin.getAPI().playSound("REWARDS_FIRST_ALREADY", p);
					} else {
						SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().removeOnePageFromID(""+p.getUniqueId(),  "REWARDS_"+j.getConfigID());
						plugin.getGUIAddonManager().createRewardsGUI(p, UpdateTypes.REOPEN, j);
						plugin.getAPI().playSound("LAST_PAGE_REWARDS", p);
					}
				}
				
				e.setCancelled(true);
			}
			
		}
	}
}
