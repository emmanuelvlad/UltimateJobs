package de.warsteiner.jobs.utils.cevents;
 
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer; 

public class CustomEventManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin(); 

	private HashMap<String, Job> level = new HashMap<String, Job>();
	private HashMap<String, Integer> level_int = new HashMap<String, Integer>();
	private HashMap<String, Job> work = new HashMap<String, Job>();
	private HashMap<String, String> id = new HashMap<String, String>();

	public void startSystemCheck() {

		new BukkitRunnable() {

			public void run() {

				for (Player p : Bukkit.getOnlinePlayers()) {

					String uuid = ""+p.getUniqueId();
					JobsPlayer jb = plugin.getPlayerManager().getOnlineJobPlayers().get(uuid);
					
					if (level.containsKey(uuid)) { 
						Integer l = level_int.get(uuid);
						new PlayerLevelJobEvent(p, jb, level.get(uuid), l); 
						level.remove(uuid);
						level_int.remove(uuid);
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
	
	public HashMap<String, Integer> getLevelDetails() {
		return level_int;
	}

	public HashMap<String, Job> getLevelQueue() {
		return level;
	}
	
}