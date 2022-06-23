package de.warsteiner.jobs.inventorys;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;

public class SettingsMenuClickEvent implements Listener {

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

		FileConfiguration config = plugin.getFileManager().getSettings();

		Player p = (Player) e.getWhoClicked();

		String UUID = ""+p.getUniqueId();
		 
		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		 
		if(plugin.getGUI().isSettingsGUITitle(e.getView().getTitle(), UUID)) { 
			if (plugin.getGUI().isSettingsGUI(e.getView().getTitle(), UUID) != null) {
				Job job = plugin.getGUI().isSettingsGUI(e.getView().getTitle(), UUID);
				plugin.getClickManager().executeCustomItemInSubMenu(job, display, p, "Settings_Custom", config);
				e.setCancelled(true);
			}
		}
	}
}
