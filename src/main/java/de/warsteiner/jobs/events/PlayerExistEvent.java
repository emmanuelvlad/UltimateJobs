package de.warsteiner.jobs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.warsteiner.jobs.UltimateJobs;

public class PlayerExistEvent implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBreak(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String UUID = ""+player.getUniqueId();
		if(!plugin.getPlayerAPI().existPlayer(UUID)) {
			plugin.getPlayerAPI().createPlayer(UUID);
			plugin.getLogger().info("§aCreated Player§7: §a"+player.getName() + " §awith UUID§7: §a"+UUID);
		}
		plugin.getPlayerAPI().UpdateFetcher(UUID, player.getName());
	}
	
}