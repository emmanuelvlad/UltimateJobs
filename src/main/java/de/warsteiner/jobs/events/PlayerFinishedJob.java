package de.warsteiner.jobs.events;
 
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.cevents.PlayerFinishWorkEvent;

public class PlayerFinishedJob implements Listener {

	@EventHandler
	public void onFinish(PlayerFinishWorkEvent event) {
		UltimateJobs.getPlugin().getExecutor().execute(() -> {
			Job job = event.getJob();
			JobsPlayer jb = event.getJobsPlayer();
			String i = event.getI(); 
			double reward = job.getRewardOf(i); 
			Player player = event.getPlayer();
			int am = event.getAmount();
			double fixed = reward * am;
			
			UltimateJobs.getPlugin().getEco().depositPlayer(player, fixed);
		 
			String id = job.getID(); 
			double exp = job.getExpOf(i);
			Integer broken = jb.getBrokenOf(id) + am;
			double points = job.getPointsOf(i)*am;
			double old_points = jb.getPoints();
			
			jb.updateBroken(id, broken);
		 
			double exp_old = jb.getExpOf(id) * am; 
			
			 jb.changePoints(points+old_points);

			jb.updateExp(id, exp_old + exp); 
			
			UltimateJobs.getPlugin().getAPI().sendReward(jb, player, job, exp, fixed, i);
		});

	}

}
