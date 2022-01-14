package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.api.JobsPlayer; 

public class JobsPluginSoundEvent extends Event {

	private static HandlerList list = new HandlerList();

	public String uuid;
	public UUID id;
	public Player player;
	public String type;

	public JobsPluginSoundEvent(String uuid, Player player, String type) { 
		this.player = player;
		this.uuid = uuid;
		this.type = type;
		Bukkit.getPluginManager().callEvent(this);
	}

	
	public HandlerList getHandlers() {
		return list;
	}

	public String getType() {
		return this.type.toUpperCase();
	}
	
	public String getUUID() {
		return uuid;
	}

	public Player getPlayer() {
		return player;
	} 

	public static HandlerList getHandlerList() {
		return list;
	}
	
	
	
}


