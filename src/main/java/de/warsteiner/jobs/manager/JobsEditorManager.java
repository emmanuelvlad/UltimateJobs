package de.warsteiner.jobs.manager;
 
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import de.warsteiner.jobs.api.Job;

public class JobsEditorManager {
	
	private HashMap<Player, String> list = new HashMap<Player, String>();
	private HashMap<Player, Job> which = new HashMap<Player, Job>();
	private HashMap<Player, List<String>> editlist = new HashMap<Player, List<String>>();

	public HashMap<Player, String> getList() {
		return list;
	}
	
	public boolean isInList(Player player) {
		return list.containsKey(player);
	}
	
	public HashMap<Player, Job> getWhichList() {
		return which;
	}
	
	public boolean isInWhichList(Player player) {
		return which.containsKey(player);
	}
	
	public HashMap<Player, List<String>> getEditList() {
		return editlist;
	}
	
}
