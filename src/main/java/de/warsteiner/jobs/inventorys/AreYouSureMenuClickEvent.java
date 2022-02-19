package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;

public class AreYouSureMenuClickEvent implements Listener {
	
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

		YamlConfiguration config = plugin.getMainConfig().getConfig();
		
		String name = config.getString("AreYouSureGUI_Name");
		
		Player p = (Player) e.getWhoClicked();
		
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
 
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String mm = job.getDisplay();
			String newname = up.toHex(name).replaceAll("<job>", mm).replaceAll("&", "§");
			if(e.getView().getTitle().equalsIgnoreCase(newname)) {
				plugin.getClickManager().executeCustomItem(display, p, "AreYouSureGUI_Custom", config);
				 
				JobsPlayer jb = plugin.getPlayerManager().getOnlineJobPlayers().get(""+p.getUniqueId());
				String yes =  up.toHex(config.getString("AreYouSureItems.Button_YES.Display")).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§");
				String no =  up.toHex(config.getString("AreYouSureItems.Button_NO.Display")).replaceAll("<job>", job.getDisplay()).replaceAll("&", "§");
				
				if(display.equalsIgnoreCase(yes)) {
				 
					double money = job.getPrice();
					
					plugin.getClickManager().buy(money, p, jb, job);
				} else if(display.equalsIgnoreCase(no)) {
					plugin.getGUI().createMainGUIOfJobs(p);
				}
				
				e.setCancelled(true);
			}
		}
		 
	}
}