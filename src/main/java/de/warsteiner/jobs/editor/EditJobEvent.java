package de.warsteiner.jobs.editor;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.Action;

public class EditJobEvent implements Listener {

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
		if (e.getView().getTitle().contains("§6Job Settings")) {

			if (inv.getItem(4) == null) {
				return;
			}

			ItemStack item = inv.getItem(4);
			String[] displayofitem = item.getItemMeta().getDisplayName().split(" ");
			String jobasstring = displayofitem[1].replaceAll("§7", " ").replaceAll(" ", "");
			Job job = plugin.getJobCache().get(jobasstring); 

			if (display.contains("§8< §cGo Back §8>")) {

				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getGUI().EditorChooseJob(p, false);
			} else if (display.contains("Change Job Action")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditMenuOfActions(p, job);
			} else if (display.contains("Change BossBar Color")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditMenuOfBarColor(p, job);
			}  else if (display.contains("Change Job Display")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Job-Display Name in the chat§7. §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_DISPLAY");
				plugin.getEditorManager().getWhichList().put(p, job);
			}  else if (display.contains("Change Job Icon")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Job-Icon in the chat§7. §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_ICON");
				plugin.getEditorManager().getWhichList().put(p, job);
			}  else if (display.contains("Change Job Slot")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Job-Slot in the chat§7. §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_SLOT");
				plugin.getEditorManager().getWhichList().put(p, job);
			} else if (display.contains("Change Job Price")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Job-Price in the chat§7. §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_PRICE");
				plugin.getEditorManager().getWhichList().put(p, job);
			} else if (display.contains("Bypass Permission")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Bypass Permission in the chat§7. Use §c§lnone §7to remove it! §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_BYPASS");
				plugin.getEditorManager().getWhichList().put(p, job);
			}  else if (display.contains("Job Permission")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Permission in the chat§7. Use §c§lnone §7to remove it! §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_PERM");
				plugin.getEditorManager().getWhichList().put(p, job);
			} else if (display.contains("Permissions Message")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				p.closeInventory();
				p.sendMessage(plugin.getAPI().getPrefix()+" §7Write the new Permissions-Message in the chat§7. §8(§7Use §ccancel §7to cancel the progress§8)");
				plugin.getEditorManager().getList().put(p, "JOB_PERM_MESSAGE");
				plugin.getEditorManager().getWhichList().put(p, job);
			}  else if (display.contains("Job Description")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditorForLists(p, job, job.getLore(), "Lore");
			} else if (display.contains("Worlds")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditorForLists(p, job, job.getWorlds(), "Worlds");
			} else if (display.contains("Permissions Lore")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditorForLists(p, job, job.getPermissionsLore(), "PermLore");
			}  else if (display.contains("Stats Lore")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getEditorMenuManager().openEditorForLists(p, job, job.getStatsMessage(), "Stats");
			}
			 

			e.setCancelled(true);

		} else if (e.getView().getTitle().contains("§6Edit Action")) {

			if (inv.getItem(4) == null) {
				return;
			}

			ItemStack item = inv.getItem(4);
			String[] displayofitem = item.getItemMeta().getDisplayName().split(" ");
			String jobasstring = displayofitem[1].replaceAll("§7", " ").replaceAll(" ", "");
			Job job = plugin.getJobCache().get(jobasstring); 
			
			if (display.contains("§8< §cGo Back §8>")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getGUI().EditJobMenu(p, job);
			} else if (display.contains("§8<") && display.contains("§8>") && !display.contains("Back")) {

				String[] ac = display.split(" ");

				String action = ac[1].replaceAll("§7", " ").replaceAll(" ", "");

				if (Action.valueOf(action) == null) {
					return;
				}
				 
				job.updateJobAction(action);

				Job newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				p.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Action of " + newj.getDisplay()
						+ "§8, §7set to §b" + action + "§7.");

				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

				plugin.getEditorMenuManager().open(p, newj);
			}

			e.setCancelled(true);

		} else if (e.getView().getTitle().contains("§6Edit BossBar Color")) {

			if (inv.getItem(4) == null) {
				return;
			}

			ItemStack item = inv.getItem(4);
			String[] displayofitem = item.getItemMeta().getDisplayName().split(" ");
			String jobasstring = displayofitem[1].replaceAll("§7", " ").replaceAll(" ", "");
			Job job = plugin.getJobCache().get(jobasstring); 
			
			if (display.contains("§8< §cGo Back §8>")) {
				p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
				plugin.getGUI().EditJobMenu(p, job);
			} else if (display.contains("§8<") && display.contains("§8>") && !display.contains("Back")) {

				String[] ac = display.split(" ");

				String color = ac[1].replaceAll("§7", " ").replaceAll(" ", "");

				if (BarColor.valueOf(color) == null) {
					return;
				}
				 
				job.updateJobBarColor(color);

				Job newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				p.sendMessage(plugin.getAPI().getPrefix() + " §7Updated BarColor of " + newj.getDisplay()
						+ "§8, §7set to §b" + color + "§7.");

				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

				plugin.getEditorMenuManager().open(p, newj);
			}

			e.setCancelled(true);

		}

	}

	 

}
