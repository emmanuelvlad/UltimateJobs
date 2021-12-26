package de.warsteiner.jobs.events.inventorys;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;

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

		YamlConfiguration config = plugin.getMainConfig().getConfig();
 
		Player p = (Player) e.getWhoClicked();
		
		String display = plugin.getJobAPI().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = plugin.getJobAPI().toHex(e.getView().getTitle().replaceAll("&", "§"));
		
		if(plugin.getGUIManager().isSettingsGUI(title) != null) {
			File job = plugin.getGUIManager().isSettingsGUI(title);
			plugin.getGUIManager().executeCustomItemInSettings(job, display, p, config);
			e.setCancelled(true);
		}
		
	}
}
