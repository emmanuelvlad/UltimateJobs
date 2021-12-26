package de.warsteiner.jobs.utils.cevents;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJobExpEvent extends Event {

	private static HandlerList list = new HandlerList();

	public String uuid;
	public UUID id;
	public File job;
	public Player player;

	public PlayerJobExpEvent(String uuid, File job, Player player) {
		this.job = job;
		this.player = player;
		this.uuid = uuid;
		Bukkit.getPluginManager().callEvent(this);
	}

	public HandlerList getHandlers() {
		return list;
	}

	public String getUUID() {
		return uuid;
	}

	public Player getPlayer() {
		return player;
	}
	
	public File getJob() {
		return job;
	}

	public static HandlerList getHandlerList() {
		return list;
	}
}

