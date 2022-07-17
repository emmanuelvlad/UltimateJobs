package de.warsteiner.jobs.jobs;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionEnchant implements Listener {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void enchClick(EnchantItemEvent e) {
		if (e.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				e.setCancelled(true);
			}
			return;
		}

		Player p = e.getEnchanter();
		int amount = 0;
		for (Map.Entry ench : e.getEnchantsToAdd().entrySet()) {
			Enchantment enchantment = (Enchantment) ench.getKey();
 
			amount += (Integer) ench.getValue();

			if (amount > 0) {
				 
				String f = enchantment.getKey().getKey().toUpperCase()+"_"+amount;
			 
				plugin.getJobWorkManager().executeEnchant(f, p.getUniqueId());
			}

		}
	}
}