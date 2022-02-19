package de.warsteiner.jobs.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.manager.JobsEditorManager;

public class PlayerEditorChatEvent implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	private List<String> blocked = Arrays.asList(new String[] { "JOBS_LIST_WORLDS_ADD", "JOBS_LIST_LORE_ADD" });

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		JobsEditorManager m = plugin.getEditorManager();
		Player player = event.getPlayer();

		if (m.isInList(player)) {

			String message = event.getMessage();

			Job newj = null;

			Job job = m.getWhichList().get(player);
			String mode = m.getList().get(player);

			if (message.toLowerCase().equalsIgnoreCase("cancel")) {
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

				m.getList().remove(player);
				m.getWhichList().remove(player);

				if (!mode.equalsIgnoreCase("JOB_LIST_WORLDS_ADD")) {
					plugin.getEditorMenuManager().open(player, job);
				} else if (mode.equalsIgnoreCase("JOBS_LIST_WORLDS_ADD")) {
					plugin.getEditorMenuManager().openTwo(player, job, job.getWorlds(), "Worlds");
				} else if (mode.equalsIgnoreCase("JOBS_LIST_LORE_ADD")) {
					plugin.getEditorMenuManager().openTwo(player, job, job.getLore(), "Lore");
				}
				event.setCancelled(true);
				return;
			}

			if (mode.equalsIgnoreCase("JOB_DISPLAY")) {
				job.updateDisplayName(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Display of " + job.getDisplay()
						+ "§8, §7set to §b" + message.replaceAll("&", "§") + "§7.");

			} else if (mode.equalsIgnoreCase("JOB_ICON")) {

				job.updateIcon(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Icon of " + job.getDisplay()
						+ "§8, §7set to §b" + message + "§7.");

			} else if (mode.equalsIgnoreCase("JOB_PRICE")) {

				if (!plugin.getAPI().isInt(message)) {
					player.sendMessage(plugin.getAPI().getPrefix() + "§cThe Price must be a Int! Try again!");
					event.setCancelled(true);
					return;
				}

				job.updatePrice(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Price of " + job.getDisplay()
						+ "§8, §7set to §b" + message + "§7.");

			} else if (mode.equalsIgnoreCase("JOB_SLOT")) {

				if (!plugin.getAPI().isInt(message)) {
					player.sendMessage(plugin.getAPI().getPrefix() + "§cThe Slot must be a Int! Try again!");
					event.setCancelled(true);
					return;
				}

				job.updateSlot(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Slot of " + job.getDisplay()
						+ "§8, §7set to §b" + message + "§7.");

			} else if (mode.equalsIgnoreCase("JOB_BYPASS")) {

				job.updateBypassPermission(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				if (message.toLowerCase().equalsIgnoreCase("none")) {
					player.sendMessage(
							plugin.getAPI().getPrefix() + " §7Removed Bypass Permission of " + job.getDisplay());
				} else {
					player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Bypass Permission of "
							+ job.getDisplay() + "§8, §7set to §b" + message + "§7.");
				}

			} else if (mode.equalsIgnoreCase("JOB_PERM")) {

				job.updatePermission(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				if (message.toLowerCase().equalsIgnoreCase("none")) {
					player.sendMessage(plugin.getAPI().getPrefix() + " §7Removed Permission of " + job.getDisplay());
				} else {
					player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Permission of " + job.getDisplay()
							+ "§8, §7set to §b" + message + "§7.");
				}

			} else if (mode.equalsIgnoreCase("JOB_PERM_MESSAGE")) {

				job.updatePermissionMessage(message);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Updated Permissions-Message of " + job.getDisplay()
						+ "§8, §7set to §b" + message.replaceAll("&", "§") + "§7.");

			} else if (mode.equalsIgnoreCase("JOB_PERM_LORE")) {

				ArrayList<String> l = new ArrayList<String>();

				String[] split = message.split("<n>");

				int size = split.length;

				for (int i = 0; i < size; i++) {

					if (size >= i) {

						l.add(split[i]);

					}
				}

				job.updatePermissionLore(l);

				newj = plugin.getAPI().loadSingleJob(plugin.getLogger(), job.getFile(), job);

				player.sendMessage(
						plugin.getAPI().getPrefix() + " §7Updated Permissions-Lore of " + job.getDisplay() + "§7!");

			} else if (mode.equalsIgnoreCase("JOB_LIST_WORLDS_ADD")) {

				List<String> li = null;

				if (plugin.getEditorManager().getEditList().get(player) == null) {
					li = new ArrayList<String>();
				} else {
					li = plugin.getEditorManager().getEditList().get(player);
				}

				li.add(message);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Added Line to the Worlds-List!");

				plugin.getEditorMenuManager().openTwo(player, job, li, "Worlds");
			} else if (mode.equalsIgnoreCase("JOB_LIST_LORE_ADD")) {

				List<String> li = null;

				if (plugin.getEditorManager().getEditList().get(player) == null) {
					li = new ArrayList<String>();
				} else {
					li = plugin.getEditorManager().getEditList().get(player);
				}

				li.add(message);

				player.sendMessage(plugin.getAPI().getPrefix() + " §7Added Line to the Lore-List!");

				plugin.getEditorMenuManager().openTwo(player, job, li, "Lore");
			}

			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

			plugin.getEditorManager().getList().remove(player);
			plugin.getEditorManager().getWhichList().remove(player);
Bukkit.broadcastMessage("-> "+mode);
			if (!blocked.contains(mode)) {
				plugin.getEditorMenuManager().open(player, newj);

			}

			event.setCancelled(true);

		}

	}

}
