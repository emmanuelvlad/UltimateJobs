package de.warsteiner.jobs.inventorys;
 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;

public class EditorClickEvent  implements Listener {
	
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
 
		Player p = (Player) e.getWhoClicked();
		
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		
		if(e.getView().getTitle().equalsIgnoreCase("§6Jobs Editor")) {
			
			if(display.equalsIgnoreCase("§8< §cClose §8>")) {
				p.closeInventory();
			}
			
			Job j = null;
			
			for(Job job : plugin.getLoaded()) {
				if(job.getDisplay().equalsIgnoreCase(display)) {
					j = job;
				}
			}
			
			if(j != null) {
				p.sendMessage("yes");
			}
			
			e.setCancelled(true);
		}
		
	}
}
