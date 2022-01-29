package de.warsteiner.jobs.events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
 
public class BlockFireWorkDamage implements Listener {
 
	@EventHandler(priority = EventPriority.HIGHEST) 
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Firework) {
		    Firework fw = (Firework) event.getDamager();
		    if (fw.hasMetadata("nodamage")) {
		        event.setCancelled(true);
		    }
		}
	}
}
