package de.warsteiner.jobs.api.plugins;

import com.alonsoaliaga.alonsolevels.AlonsoLevels;
import com.alonsoaliaga.alonsolevels.api.AlonsoLevelsAPI;

public class AlonsoLevelsManager {

	private AlonsoLevels alonso;
	private AlonsoLevelsAPI api;

	public void setClass() {
		alonso = new AlonsoLevels();
		api = new AlonsoLevelsAPI();
	}

}
