package de.warsteiner.jobs.api.plugins;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.alonsoaliaga.alonsolevels.AlonsoLevels;
import com.alonsoaliaga.alonsolevels.api.AlonsoLevelsAPI;

import de.warsteiner.jobs.api.Job;

public class AlonsoLevelsManager {
 
	public boolean canHaveJob(Player player, Job job) {
		UUID UUID = player.getUniqueId();
		
		int level = AlonsoLevelsAPI.getLevel(UUID);
		
		int need = job.getAlonsoLevelsReq();
		
		if(level <= need) {
			return false;
		}
		
		return true;	
	}
	
	public boolean canBypassJob(Player player, Job job) {
	
		UUID UUID = player.getUniqueId();
		
		int level = AlonsoLevelsAPI.getLevel(UUID);
		
		int need = job.getBypassAlonsoLevelsReq();
		
		if(level <= need) {
			return false;
		}
		
		return false;
	}

}
