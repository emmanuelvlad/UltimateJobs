package de.warsteiner.jobs.editor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;

public class EditListEvent implements Listener {

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

		InventoryView inv = e.getView();
		String display = up.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		if (e.getView().getTitle().contains("§6Edit List")) {

			if (inv.getItem(4) == null) {
				return;
			}

			ItemStack item = inv.getItem(4);
			String[] displayofitem = item.getItemMeta().getDisplayName().split(" ");
			String jobasstring = displayofitem[1].replaceAll("§7", " ").replaceAll(" ", "");
			Job job = plugin.getJobCache().get(jobasstring); 
			
			if (inv.getItem(19) == null) {
				return;
			}
			
			ItemStack item2 = inv.getItem(19);
			String[] displayofitem2 = item2.getItemMeta().getDisplayName().split(" ");
			String modeofitem2 = displayofitem2[2].replaceAll("§b", " ").replaceAll(" ", "");
			
			List<String> toeditlist = item2.getItemMeta().getLore();
			  
			if (display.equalsIgnoreCase("§8< §cGo Back §8>")) {

				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getGUI().EditJobMenu(p, job);
				
				if(	plugin.getEditorManager().getEditList().containsKey(p)) {
					plugin.getEditorManager().getList().remove(p);
					plugin.getEditorManager().getWhichList().remove(p);
					plugin.getEditorManager().getEditList().remove(p);
				}
				
			} else 	if (display.equalsIgnoreCase("§8< §aAdd new Line §8>")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the Line which you want to add in the chat! §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_LIST_"+modeofitem2.toUpperCase()+"_ADD");
				plugin.getEditorManager().getWhichList().put(p, job);
				plugin.getEditorManager().getEditList().put(p, toeditlist);
			} else if(display.contains("§8< §cReset §8>")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1); 
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Reseted List to default : §b"+modeofitem2);
				plugin.getEditorMenuManager().openTwo(p, job, getFromName(modeofitem2, job), modeofitem2);
			} else if(display.equalsIgnoreCase("§8< §a§lFinish and Save §8>")) { 
				job.updateList(modeofitem2, toeditlist);

				Job newj  = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);
				
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1); 
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Update List : §b"+modeofitem2+" §7for Job "+newj.getDisplay()+"§7!");
				plugin.getGUI().EditJobMenu(p, newj);
			} else if(display.contains("§8< §cRemove one Line §8>")) {
				
				if(toeditlist == null) {
					p.sendMessage(plugin.getAPI().getPrefix()+" §cYou cant remove any line!");
					e.setCancelled(true);
					return;
				}
				
				int size = toeditlist.size();
				
				toeditlist.remove(size-1);
				 
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1); 
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Removed one line from List : §b"+modeofitem2);
				plugin.getEditorMenuManager().openTwo(p, job, toeditlist, modeofitem2);
			}
			
			e.setCancelled(true);
			return;
		}
	}
	 
	public List<String> getFromName(String name, Job job) {
		if(name.equalsIgnoreCase("Worlds")) {
			return job.getWorlds();
		}
		if(name.equalsIgnoreCase("Lore")) {
			return job.getLore();
		}
		return null;
	}
	
}
