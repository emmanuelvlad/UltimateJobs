package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;

public class StatsMenuClickEvent implements Listener {

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

		FileConfiguration config = plugin.getFileManager().getStatsConfig();

		Player p = (Player) e.getWhoClicked();

		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = up.toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (title.equalsIgnoreCase(up.toHex(plugin.getPluginManager().getFromPath(p.getUniqueId(), config.getString("Self_Name")).replaceAll("&", "§")))) { 
			plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Self_Custom", config);
			e.setCancelled(true);
		} else if(plugin.getGUIAddonManager().isStatsGUI(title, p.getUniqueId())) {
			plugin.getClickManager().executeCustomItemInSubMenu(null, display, p, "Other_Custom", config);
			e.setCancelled(true);
		}

	}
}
