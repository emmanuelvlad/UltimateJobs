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
import de.warsteiner.jobs.api.Job;

public class SettingsMenuClickEvent implements Listener {

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

		FileConfiguration config = plugin.getFileManager().getSettings();

		Player p = (Player) e.getWhoClicked();

		String UUID = ""+p.getUniqueId();
		String name =  plugin.getPluginManager().getSomethingFromPath(p.getUniqueId(), config.getString("Settings_Name"));
		
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = up.toHex(name.replaceAll("&", "§"));

		if (plugin.getGUI().isSettingsGUI(title, UUID) != null) {
			Job job = plugin.getGUI().isSettingsGUI(title, UUID);
			plugin.getClickManager().executeCustomItemInSubMenu(job, display, p, "Settings_Custom", config);
			e.setCancelled(true);
		}

	}
}
