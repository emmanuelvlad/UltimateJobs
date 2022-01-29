package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;

public class PlayerFinishedWorkEvent  extends Event {

	private static HandlerList list = new HandlerList();

	public UUID id; 
	public JobsPlayer pl;
	public Job job;
	public String ID;
	public Player player; 

	public PlayerFinishedWorkEvent(Player player, JobsPlayer plt, Job job, String id) { 
		this.pl = plt; 
		this.ID = id;
		this.job = job;
		this.player = player;
		Bukkit.getPluginManager().callEvent(this);
	}
	
	public String getID() {
		return ID;
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
