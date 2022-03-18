package de.warsteiner.jobs.inventorys;
 
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;

public class MainMenuClickEvent implements Listener {
	
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

		FileConfiguration config = plugin.getFileManager().getGUI();
		Player p = (Player) e.getWhoClicked();
		String UUID = ""+p.getUniqueId();
		String name =  plugin.getPluginManager().getSomethingFromPath(p.getUniqueId(), config.getString("Main_Name"));
		 
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
 
		if (e.getView().getTitle().equalsIgnoreCase(up.toHex(name).replaceAll("&", "§"))) {
		 
			plugin.getClickManager().executeJobClickEvent(display, p);
			 
			plugin.getClickManager().executeCustomItem(display, p, "Main_Custom", config);
			e.setCancelled(true);
		}
		
	}
}
