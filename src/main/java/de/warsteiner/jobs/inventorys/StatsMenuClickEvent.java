package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class StatsMenuClickEvent implements Listener {

	private static UltimateJobs  plugin= UltimateJobs.getPlugin(); 

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

		FileConfiguration config = plugin.getFileManager().getStatsConfig();

		Player p = (Player) e.getWhoClicked();

		String display = plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title =  plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));
		
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+p.getUniqueId());

		if (plugin.getGUIOpenManager().isStatsMenuOpendSelf(p, title) != null) { 
			plugin.getClickManager().executeCustomItem(null, display, p, "Self_Custom", config, null);
			e.setCancelled(true);
		} else if(plugin.getGUIOpenManager().isStatsMenuOpendAboutPlayer(p, title) != null) {
			 
			plugin.getClickManager().executeCustomItem(null, display, p, "Other_Custom", config, plugin.getGUI().getGUIsDetails().get(jb.getUUIDAsString()));
			e.setCancelled(true);
		}

	}
}
