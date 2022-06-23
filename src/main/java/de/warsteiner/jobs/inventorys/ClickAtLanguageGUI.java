package de.warsteiner.jobs.inventorys;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerLanguageChangeEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;
import de.warsteiner.jobs.utils.objects.UpdateTypes;
 
public class ClickAtLanguageGUI implements Listener {

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

		String dis = e.getCurrentItem().getItemMeta().getDisplayName();
		Player player = (Player) e.getWhoClicked(); 
		
		JobsPlayer pp = plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId());
		
 
		FileConfiguration cfg = plugin.getFileManager().getLanguageGUIConfig();
		String name = pp.getLanguage().getStringFromPath(player.getUniqueId(),  cfg.getString("Name"));

		if (e.getView().getTitle().equalsIgnoreCase(plugin.getPluginManager().toHex(name))) {
			
			plugin.getClickManager().executeCustomItemInSubMenu(null, dis, player, "Custom", cfg);
			
			if(plugin.getClickManager().isLanguageItem(dis) != null) {
				Language lang  = plugin.getClickManager().isLanguageItem(dis);
			
				if(lang.getName().equalsIgnoreCase(plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId()).getLanguage().getName())) {
					player.sendMessage(pp.getLanguage().getStringFromLanguage(player.getUniqueId(), "LanguageChoosenMessage"));
					plugin.getAPI().playSound("LANGUAGE_ALREADY", player);
				} else {
					
					Language old = plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId()).getLanguage();
					Language newl = lang;
					
					new PlayerLanguageChangeEvent(player, pp, player.getUniqueId(), old, newl);
					
					plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId()).updateLanguage(lang);
					player.sendMessage(pp.getLanguage().getStringFromLanguage(player.getUniqueId(), "LanguageChangedMessage").replaceAll("<lang>", lang.getID().toLowerCase()));
					plugin.getAPI().playSound("LANGUAGE_UPDATED", player);
					plugin.getGUI().openLanguageMenu(player, UpdateTypes.REOPEN);
				}
			}
			
			e.setCancelled(true);
		}
	}
}


