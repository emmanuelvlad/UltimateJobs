package de.warsteiner.jobs.utils.module;

import java.util.ArrayList;
import java.util.List;

public class JobsModuleRegistry {

	private final List<JobsModule> mlist = new ArrayList<JobsModule>();

	public List<JobsModule> getModuleList() {
		return mlist;
	}

}
