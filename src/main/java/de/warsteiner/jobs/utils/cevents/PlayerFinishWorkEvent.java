package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;

public class PlayerFinishWorkEvent extends Event {

	private static HandlerList list = new HandlerList();

	public String uuid;
	public UUID id;
	public Job job;
	public String i;
	public JobsPlayer pl;
	public Player player;

	public PlayerFinishWorkEvent(String uuid, Job job, Player player, String i, JobsPlayer pl) {
		this.i = i;
		this.pl = pl;
		this.job = job;
		this.player = player;
		this.uuid = uuid;
		Bukkit.getPluginManager().callEvent(this);
	}

	public HandlerList getHandlers() {
		return list;
	}

	public JobsPlayer getJobsPlayer() {
		return this.pl;
	}

	public String getUUID() {
		return uuid;
	}

	public String getI() {
		return i;
	}

	public Player getPlayer() {
		return player;
	}

	public Job getJob() {
		return job;
	}

	public static HandlerList getHandlerList() {
		return list;
	}
}
