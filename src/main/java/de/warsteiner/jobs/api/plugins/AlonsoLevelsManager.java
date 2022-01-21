package de.warsteiner.jobs.api.plugins;

import java.util.UUID;

import com.alonsoaliaga.alonsolevels.api.AlonsoLevelsAPI;


public class AlonsoLevelsManager {
	
	public void addLevels(UUID UUID, int i) {
		AlonsoLevelsAPI.addLevel(UUID, i);
	}
	
	public void addExp(UUID UUID, int i) {
		AlonsoLevelsAPI.addExperience(UUID, i);
	}

}
