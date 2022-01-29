package de.warsteiner.jobs.manager;
 
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.cevents.PlayerFinishedWorkEvent;
import de.warsteiner.jobs.utils.cevents.PlayerLevelJobEvent; 

public class CustomEventManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin(); 

	private HashMap<String, Job> level = new HashMap<String, Job>();
	private HashMap<String, Job> work = new HashMap<String, Job>();
	private HashMap<String, String> id = new HashMap<String, String>();

	public void startSystemCheck() {

		new BukkitRunnable() {

			public void run() {

				for (Player p : Bukkit.getOnlinePlayers()) {

					String uuid = ""+p.getUniqueId();
					JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(uuid);
					
					if (level.containsKey(uuid)) { 
						new PlayerLevelJobEvent(p, jb, level.get(uuid)); 
						level.remove(uuid);
					}
					
					if (work.containsKey(uuid)) { 
						
						Job job = work.get(uuid);
						String i = id.get(uuid);
						
						new PlayerFinishedWorkEvent(p, jb, job, i);
						
						work.remove(uuid);
						id.remove(uuid);
					}
					
				}

			}
		}.runTaskTimer(plugin, 0, 20);
	}
	
	public HashMap<String, String> getIDQueue() {
		return id;
	}
	
	public HashMap<String, Job> getWorkQueue() {
		return work;
	}

	public HashMap<String, Job> getLevelQueue() {
		return level;
	}
	
}
