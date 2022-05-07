package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerLevelJobEvent extends Event {

	private static HandlerList list = new HandlerList();

	public UUID id; 
	public JobsPlayer pl;
	public Job job;
	public Player player; 
	public int level;

	public PlayerLevelJobEvent(Player player, JobsPlayer plt, Job job, int level) { 
		this.pl = plt; 
		this.level = level;
		this.job = job;
		this.player = player;
		Bukkit.getPluginManager().callEvent(this);
	}
	
	public int getNewLevel() {
		return level;
	}

	public HandlerList getHandlers() {
		return list;
	}
	
	public Job getJob() {
		return job;
	}

	public JobsPlayer getJobsPlayer() {
		return pl;
	}

	public Player getPlayer() {
		return player;
	}

	public static HandlerList getHandlerList() {
		return list;
	}
}