package de.warsteiner.jobs.inventorys;
 
import org.bukkit.configuration.file.FileConfiguration; 
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class AreYouSureMenuClickEvent implements Listener {
	
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

		FileConfiguration config = plugin.getFileManager().getConfirm();
		  
		Player p = (Player) e.getWhoClicked();
		String UUID = ""+p.getUniqueId(); 
		
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(UUID);
		
		String display =  plugin.getPluginManager().toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String name =  jb.getLanguage().getStringFromPath(p.getUniqueId(), config.getString("AreYouSureGUI_Name"));
 
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String mm = job.getDisplay(UUID);
			String newname =  plugin.getPluginManager().toHex(name).replaceAll("<job>", mm).replaceAll("&", "§");
		 
			if(e.getView().getTitle().equalsIgnoreCase(newname)) {
				plugin.getClickManager().executeCustomItem(display, p, "AreYouSureGUI_Custom", config);
				 
				String name_yes =   jb.getLanguage().getStringFromPath(p.getUniqueId(), config.getString("AreYouSureItems.Button_YES.Display"));
				String name_no =  jb.getLanguage().getStringFromPath(p.getUniqueId(), config.getString("AreYouSureItems.Button_NO.Display"));
				 
				String yes =   plugin.getPluginManager().toHex(name_yes).replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
				String no =   plugin.getPluginManager().toHex(name_no).replaceAll("<job>", job.getDisplay(UUID)).replaceAll("&", "§");
				
				if(display.equalsIgnoreCase(yes)) {
				 
					double money = job.getPrice();
					
					plugin.getClickManager().buy(money, p, jb, job);
				} else if(display.equalsIgnoreCase(no)) {
					plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);
					plugin.getAPI().playSound("REOPEN_MAIN_GUI", p);
				}
				
				e.setCancelled(true);
			}
		}
		 
	}
}