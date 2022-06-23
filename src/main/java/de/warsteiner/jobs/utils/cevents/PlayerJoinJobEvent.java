package de.warsteiner.jobs.utils.cevents;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class PlayerJoinJobEvent extends Event implements Cancellable{

	private static HandlerList list = new HandlerList();

	public UUID id;
	public JobsPlayer pl;
	public Job job;
	public Player player;
	boolean cancelled = false;

	public PlayerJoinJobEvent(Player player, JobsPlayer plt, Job job) {
		this.pl = plt;
		this.job = job;
		this.player = player;
		Bukkit.getPluginManager().callEvent(this);
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

	public HandlerList getHandlerList() {
		return list;
	}

	@Override
	public boolean isCancelled() {

		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;

	}
}
