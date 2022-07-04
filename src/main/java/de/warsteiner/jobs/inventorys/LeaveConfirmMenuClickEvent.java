package de.warsteiner.jobs.inventorys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.cevents.PlayerWithdrawMoneyEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class LeaveConfirmMenuClickEvent implements Listener {

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
 
		FileConfiguration config_2 = plugin.getFileManager().getLeaveConfirmConfig();

		Player p = (Player) e.getWhoClicked();

		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(p.getUniqueId());
 
		String display = plugin.getPluginManager()
				.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (plugin.getGUIOpenManager().isLeaveConfirmGUI(p, title) != null) {
			
			Job job =plugin.getGUIOpenManager().isLeaveConfirmGUI(p, title);

			plugin.getClickManager().executeCustomItem(null, display, p, "LeaveConfirm_Custom", config_2, null);

			String dis_1 = plugin.getPluginManager().toHex(jb.getLanguage().getStringFromPath(jb.getUUID(),
					config_2.getString("LeaveConfirmItems.Button_YES.Display"))).replaceAll("<job>", job.getDisplay(jb.getUUIDAsString())).replaceAll("&", "§");

			String dis_2 = plugin.getPluginManager().toHex(jb.getLanguage().getStringFromPath(jb.getUUID(),
					config_2.getString("LeaveConfirmItems.Button_NO.Display"))).replaceAll("<job>", job.getDisplay(jb.getUUIDAsString())).replaceAll("&", "§");

			if (display.equalsIgnoreCase(dis_1)) {

				plugin.getAPI().playSound("LEAVE_SINGLE", p);
				plugin.getClickManager().updateSalaryOnLeave(p, jb);
				jb.remCurrentJob(job.getConfigID());

				plugin.getGUI().createMainGUIOfJobs(p, UpdateTypes.REOPEN);

				if(plugin.getFileManager().getConfig().getBoolean("SendMessageOnLeave") ) {
					p.sendMessage(jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Other.Left_Job")
							.replaceAll("<job>", job.getDisplay("" + p.getUniqueId()))); 
				}
				 
			} else if (display.equalsIgnoreCase(dis_2)) {
				plugin.getGUI().createSettingsGUI(p, job, UpdateTypes.REOPEN);
				plugin.getAPI().playSound("LEAVE_JOB_CANCEL_PROGRESS", p);
			}

			e.setCancelled(true);

		}
	}
}
