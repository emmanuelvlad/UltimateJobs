package de.warsteiner.jobs.events;
 
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
			String UUID = ""+player.getUniqueId();
			UltimateJobs.getPlugin().getEco().withdrawPlayer(player, reward);
			
			String id = job.getID(); 
			double exp = job.getExpOf(i);
			Integer broken = jb.getBrokenOf(id);
			 
			jb.updateBroken(id, broken + 1);
		 
			double exp_old = jb.getExpOf(id); 
			

			jb.updateExp(id, exp_old + exp); 
			
			UltimateJobs.getPlugin().getAPI().sendReward(jb, player, job, exp, reward, i);
		});

	}

}
