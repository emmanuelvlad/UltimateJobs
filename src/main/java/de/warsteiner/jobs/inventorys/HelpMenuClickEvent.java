package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs; 

public class HelpMenuClickEvent implements Listener {

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

		FileConfiguration config = plugin.getFileManager().getHelpSettings();

		Player p = (Player) e.getWhoClicked();

		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title =  plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (title.equalsIgnoreCase( plugin.getPluginManager().toHex(config.getString("Help_Name").replaceAll("&", "§")))) { 
			plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Help_Custom", config);
			e.setCancelled(true);
		}

	}
}

