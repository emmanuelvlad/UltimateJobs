package de.warsteiner.jobs.inventorys;

import org.bukkit.Bukkit;
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

		FileConfiguration config = plugin.getFileManager().getConfirm();
		  
		Player p = (Player) e.getWhoClicked();
		String UUID = ""+p.getUniqueId();
		java.util.UUID ID = p.getUniqueId();
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String name =  plugin.getPluginManager().getSomethingFromPath(ID, config.getString("AreYouSureGUI_Name"));
 
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String mm = job.getDisplay(UUID);
			String newname = up.toHex(name).replaceAll("<job>", mm).replaceAll("&", "§");
		 
			if(e.getView().getTitle().equalsIgnoreCase(newname)) {
				plugin.getClickManager().executeCustomItem(display, p, "AreYouSureGUI_Custom", config);
				 
				String name_yes =  plugin.getPluginManager().getSomethingFromPath(ID, config.getString("AreYouSureItems.Button_YES.Display"));
				String name_no =  plugin.getPluginManager().getSomethingFromPath(ID, config.getString("AreYouSureItems.Button_NO.Display"));
				
				JobsPlayer jb =plugin.getPlayerManager().getRealJobPlayer(UUID);
				String yes =  up.toHex(name_yes).replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
				String no =  up.toHex(name_no).replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
				
				if(display.equalsIgnoreCase(yes)) {
				 
					double money = job.getPrice();
					
					plugin.getClickManager().buy(money, p, jb, job);
				} else if(display.equalsIgnoreCase(no)) {
					plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
					plugin.getAPI().playSound("REOPEN_MAIN_GUI", p);
				}
				
				e.setCancelled(true);
			}
		}
		 
	}
}