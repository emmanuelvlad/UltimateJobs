package de.warsteiner.jobs.inventorys;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;

public class SettingsMenuClickEvent implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = UltimateAPI.getInstance().getAPI();

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

		Player p = (Player) e.getWhoClicked();

		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = up.toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (plugin.getAPI().isSettingsGUI(title) != null) {
			Job job = plugin.getAPI().isSettingsGUI(title);
			plugin.getClickManager().executeCustomItemInSubMenu(job, display, p, "Settings_Custom", config);
			e.setCancelled(true);
		}

	}
}
