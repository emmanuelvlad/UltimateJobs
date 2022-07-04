package de.warsteiner.jobs.inventorys;
 
import org.bukkit.configuration.file.FileConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs; 

public class MainMenuClickEvent implements Listener {
	
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

		FileConfiguration config = plugin.getFileManager().getGUI();
		Player p = (Player) e.getWhoClicked(); 
	 
		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
 
		if (plugin.getGUIOpenManager().isMainOpend(p, e.getView().getTitle()) != null) {
		 
			plugin.getClickManager().executeJobClickEvent(display, p);
			 
			plugin.getClickManager().executeCustomItem(null, display, p, "Main_Custom", config, null);
			e.setCancelled(true);
		}
		
	}
}
