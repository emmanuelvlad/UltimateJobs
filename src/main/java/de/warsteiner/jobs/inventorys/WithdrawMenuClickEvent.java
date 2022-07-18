package de.warsteiner.jobs.inventorys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerWithdrawMoneyEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;

public class WithdrawMenuClickEvent implements Listener {

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

		FileConfiguration config = plugin.getFileManager().getWithdrawConfig();
		FileConfiguration config_2 = plugin.getFileManager().getWithdrawConfirmConfig();

		Player p = (Player) e.getWhoClicked();

		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(p.getUniqueId());

		YamlConfiguration lg = jb.getLanguage().getConfig();

		String display = plugin.getPluginManager()
				.toHex(e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("&", "§"));
		String title = plugin.getPluginManager().toHex(e.getView().getTitle().replaceAll("&", "§"));

		if (plugin.getGUIOpenManager().isWithdrawConfirmMenu(p, title) != null) {

			plugin.getClickManager().executeCustomItem(null, display, p, "ConfirmWithdraw_Custom", config_2, null);

			String dis_1 = plugin.getPluginManager().toHex(jb.getLanguage().getStringFromPath(jb.getUUID(),
					config_2.getString("ConfirmWithdrawItems.Button_YES.Display"))).replaceAll("&", "§");

			String dis_2 = plugin.getPluginManager().toHex(jb.getLanguage().getStringFromPath(jb.getUUID(),
					config_2.getString("ConfirmWithdrawItems.Button_NO.Display"))).replaceAll("&", "§");

			if (display.equalsIgnoreCase(dis_1)) {

				PlayerWithdrawMoneyEvent event = new PlayerWithdrawMoneyEvent(p, jb);

				if (!event.isCancelled()) {

					double d = jb.getSalary();

					UltimateJobs.getPlugin().getEco().depositPlayer(p, d);

					if (plugin.getFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {

						int days = plugin.getFileManager().getConfig().getInt("WithdrawCooldownAmount");

						DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
						Date data = new Date();

						Calendar c1 = Calendar.getInstance();
						c1.setTime(data);

						c1.add(Calendar.DATE, days);

						Date newdate = c1.getTime();

						String ddddddddd = "" + format.format(newdate);

						plugin.getPlayerAPI().updateSalaryDate(jb.getUUIDAsString(), ddddddddd);
					}

					plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), 0);

					if (plugin.getFileManager().getConfig().getBoolean("ReOpenMenuWhenSuccess")) {
						plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
					} else {
						p.closeInventory();
					}

					if (plugin.getFileManager().getConfig().getBoolean("SendMessageOnSuccess")) {
						p.sendMessage(jb.getLanguage()
								.getStringFromLanguage(p.getUniqueId(), "Withdraw_Custom.CollectButton.WithdrawMessage")
								.replaceAll("<amount>", plugin.getAPI().Format(d)));
					}

					plugin.getAPI().playSound("WITHDRAW_SUCCESS", p);
				}
			} else if (display.equalsIgnoreCase(dis_2)) {
				plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
				plugin.getAPI().playSound("WITHDRAW_CANCEL_PROGRESS", p);
			}

			e.setCancelled(true);

		} else if (plugin.getGUIOpenManager().isWithdrawMenu(p, title) != null) {
			plugin.getClickManager().executeCustomItem(null, display, p, "Withdraw_Custom", config, null);

			String dis1 = lg.getString("Withdraw_Custom.CollectButton.Display");

			if (display.equalsIgnoreCase(plugin.getPluginManager().toHex(dis1))) {

				if (plugin.getAPI().canWithdrawMoney(p, jb)) {

					if (plugin.getFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {
						plugin.getGUIAddonManager().createWithdrawConfigGUI(p, UpdateTypes.OPEN);
					} else {

						PlayerWithdrawMoneyEvent event = new PlayerWithdrawMoneyEvent(p, jb);

						if (!event.isCancelled()) {

							double d = jb.getSalary();

							UltimateJobs.getPlugin().getEco().depositPlayer(p, d);

							if (plugin.getFileManager().getConfig().getBoolean("NeedToConfirmOnWithdraw")) {

								int days = plugin.getFileManager().getConfig().getInt("WithdrawCooldownAmount");

								DateFormat format = new SimpleDateFormat(
										plugin.getFileManager().getConfig().getString("Date"));
								Date data = new Date();

								Calendar c1 = Calendar.getInstance();
								c1.setTime(data);

								c1.add(Calendar.DATE, days);

								Date newdate = c1.getTime();

								String ddddddddd = "" + format.format(newdate);

								plugin.getPlayerAPI().updateSalaryDate(jb.getUUIDAsString(), ddddddddd);
							}

							plugin.getPlayerAPI().updateSalary(jb.getUUIDAsString(), 0);

							if (plugin.getFileManager().getConfig().getBoolean("ReOpenMenuWhenSuccess")) {
								plugin.getGUIAddonManager().createWithdrawMenu(p, UpdateTypes.REOPEN);
							} else {
								p.closeInventory();
							}

							if (plugin.getFileManager().getConfig().getBoolean("SendMessageOnSuccess")) {
								p.sendMessage(jb.getLanguage()
										.getStringFromLanguage(p.getUniqueId(),
												"Withdraw_Custom.CollectButton.WithdrawMessage")
										.replaceAll("<amount>", plugin.getAPI().Format(d)));
							}

							plugin.getAPI().playSound("WITHDRAW_SUCCESS", p);
						}
					}

				} else {
					plugin.getAPI().playSound("WITHDRAW_REFUSED", p);
					p.sendMessage(jb.getLanguage().getStringFromLanguage(p.getUniqueId(),
							"Withdraw_Custom.CollectButton.CantMessage"));
				}

			}

			e.setCancelled(true);
		}

	}
}
