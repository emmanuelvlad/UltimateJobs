package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobsPlayer; 

public class Job {

	private String idt;
	private YamlConfiguration cf; 
	private ArrayList<JobAction> action;
	private String icon;
	private int slot;
	private double price;
	private String perm; 
	private List<String> worlds;  
	private File file;
	
	public Job(String id, YamlConfiguration cfg, File f) {
		
		ArrayList<JobAction> list = new ArrayList<JobAction>();
		
		if(cfg.getStringList("Action") == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Action");
		}
		
		for(String a : cfg.getStringList("Action")) {
			list.add(JobAction.valueOf(a.toUpperCase()));
		}
		
		idt = id;
		cf = cfg; 
		perm = cfg.getString("Permission");
		action =  list;
		
		if(cf.getString("Material") == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Material");
		}
		
		icon = cfg.getString("Material");
		slot = cfg.getInt("Slot");
		price = cfg.getDouble("Price"); 
		worlds = cfg.getStringList("Worlds");  
		file = f;
	}
	
	public int getModelData() {
		return cf.getInt("CustomModelData");
	}
	
	public boolean hasModelData() {
		return cf.contains("CustomModelData");
	}
 
	public void save() {
		try {
			cf.save(file);
		} catch (IOException e) { 
			Bukkit.getConsoleSender().sendMessage("§cFailed to save Config of "+this.getConfigID());
			e.printStackTrace();
		}
	}

	public File getFile() {
		
		if(file == null) {
			Bukkit.getConsoleSender().sendMessage("§cFailed to return Job File of "+this.getConfigID());
		}
		
		return file;
	}
	
	public BarColor getBarColor() {
		
		if(BarColor.valueOf(cf.getString("ColorOfBossBar")) == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing  or Wrong Option of "+this.getConfigID()+" Job -> BossBarColor");
			return BarColor.RED;
		}
		
		return BarColor.valueOf(cf.getString("ColorOfBossBar"));
	}
	
	public YamlConfiguration getConfig() { 
		return cf;
	}
 
	public String getLevelDisplay(int i, String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		String where = jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Jobs."+getConfigID()+".Levels."+i+".Display");
		 
		if(where == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Level Display from Level "+i);
		}
		
		return where;
			 
	}
	
	
	public List<String> getLevelLore(int i, String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		
		List<String> result = jb.getLanguage().getListFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".Levels."+i+".Lore");
		  
		return result; 
			 
	}

	public List<String> getLevelCommands(int i) {
		return cf.getStringList("LEVELS." + i + ".Commands");
	}
	
	public double getMultiOfLevel(int i) {
		return cf.getDouble("LEVELS." + i + ".EarnMore");
	}
	
	public void updateMultiOfLevel(int i, double d) {
		cf.set("LEVELS." + i + ".EarnMore", d);
		save();
	}

	public List<String> getPermissionsLore(String UUID) { 
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		if(!cf.contains("PermLore")) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Permissions Lore");
		}
		
		return jb.getLanguage().getListFromPath(jb.getUUID(), cf.getString("PermLore"));  
	}
  
	public String getPermMessage(String UUID) { 
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		if(!cf.contains("PermMessage")) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Permissions Message");
		}
		return jb.getLanguage().getStringFromPath(jb.getUUID(), cf.getString("PermMessage")); 
	}

	public boolean hasPermission() {
		return cf.getString("Permission") != null;
	}

	public boolean hasBypassPermission() {
		return cf.getString("BypassPermission") != null;
	}
	
	public String getByPassPermission() {
		return cf.getString("BypassPermission");
	}
	
	public String getPermission() {
		return perm;
	}

	public Job get() {
		return this;
	}
	
	public List<String> getCommands(int level) {
		return cf.getStringList("LEVELS." + level + ".Commands");
	} 
 
	public boolean hasMaxEarningsPerDay() {
		return cf.contains("MaxEarnings");
	}
	
	public double getMaxEarningsPerDay() {
		return cf.getDouble("MaxEarnings");
	}
 
	public boolean isCommand(int level) {
		return cf.getStringList("LEVELS." + level + ".Commands") != null;
	}
	
	public void updateLevelVault(int i, double d) {
		cf.set("LEVELS." + i + ".Money", d);
		save();
	}
	
	public double getVaultOnLevel(int level) {
		return cf.getDouble("LEVELS." + level + ".Money");
	} 
	
	public String getIconOfLevel(int level) {
		return cf.getString("LEVELS." + level + ".Icon");
	}
	
	public String getModelDataOfLevel(int level) {
		return cf.getString("LEVELS." + level + ".CustomModelData");
	}
	
	public int getCountOfLevels() {
		return cf.getInt("LEVELS.CountOfLevels");
	}
	
 
 
	public boolean isVaultOnLevel(int level) {
		return cf.getString("LEVELS." + level + ".Money") != null;
	}
 
	public double getExpOfLevel(int level) {
		return cf.getDouble("LEVELS." + level + ".Need");
	}
	
	public void updateExpofLevel(int i, double d) {
		cf.set("LEVELS." + i + ".Need", d);
		save();
	}
	
	public List<String> getCommandsOfBlock(String id, JobAction ac) {
		return cf.getStringList("IDS." + ac.toString() + "." + id + ".Commands");
	} 

	public boolean isCommandonBlock(String id, JobAction ac) {
		return cf.getStringList("IDS." + ac.toString() + "." + id + ".Commands") != null;
	}

	public String getDisplayOf(String id, String UUID, JobAction ac) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		
		String result = jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".IDS."+id+".Display"); 
		
		if(result == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Display of "+id);
		}
		
		return result;
	}

	public int getChanceOf(String id, JobAction ac) {
		int result =  cf.getInt("IDS." + ac.toString() + "." + id + ".Chance");
	
		if(result == 0) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Chance if "+id);
		}
		return result;
	}
	 
	public String getConfigIDOfRealID(JobAction ac, String real, Job job) {
		for(String items : getNotRealIDSListByAction(ac)) {
			if(job.getRealIDOf(ac, items).equalsIgnoreCase(real)) {
				return items;
			}
		}
		return null;
	}
 
	public double getRewardOf(String id, JobAction ac) {
		return cf.getDouble("IDS." + ac.toString() + "." + id + ".Money");
	}
	
	public boolean hasVaultReward(String id, JobAction ac) {
		return getRewardOf(id, ac) != 0;
	}

	public List<String> getCustomitems() {
		return cf.getStringList("Items");
	}

	public double getExpOf(String id, JobAction ac) {
		
		if(!cf.contains("IDS." + ac.toString() + "." + id + ".Exp")) {
			return 0;
		}
		
		return cf.getDouble("IDS." + ac.toString() + "." + id + ".Exp");
	}

	public double getPointsOf(String id, JobAction ac) {

		if(!cf.contains("IDS." + ac.toString() + "." + id + ".Points")) {
			return 0;
		}
		return cf.getDouble("IDS." + ac.toString() + "." + id + ".Points");
	}

	public List<String> getNotRealIDSListByAction(JobAction ac) {
		List<String> result = cf.getStringList("IDS."+ac.toString()+".List");
		
		if(result == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Real IDs List from Action"+ac);
		}
		return result;
	}
	
	public String getRealIDOf(JobAction ac, String id) {
		String result = cf.getString("IDS." + ac.toString() + "." + id + ".ID");
	
		if(result == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Real ID of "+id);
		}
		
		return result;
	}
	
	public ArrayList<String> getListOfRealIDS(JobAction ac) {
		ArrayList<String> list = new ArrayList<String>();
	
		for(String b : getNotRealIDSListByAction(ac)) {
			list.add(getRealIDOf(ac, b));
		}
		
		return list;
	}
  
	public JobAction getActionofID(String id) {

		for(JobAction ac : getActionList()) {
			if(cf.contains("IDS." + ac.toString() + "." + id + ".ID")) {
				return ac;
			}
		}
		return null;
		
	}
	
	public HashMap<JobAction, String> getAllNotRealIDSFromActions() {
		HashMap<JobAction, String> list = new HashMap<JobAction, String>();
	
		for(JobAction b : getActionList()) {
			for(String l : getNotRealIDSListByAction(b)) {
				list.put(b, l);
			}
		}
		
		return list;
	}
	
	public ArrayList<String> getAllNotRealIDSFromActionsAsArray() {
		ArrayList<String> list = new ArrayList<String>();
	
		for(JobAction b : getActionList()) {
			for(String l : getNotRealIDSListByAction(b)) {
				list.add(l);
			}
		}
		
		return list;
	}
	
	public String getNotRealIDByRealOne(String id) { 
		for(JobAction b : getActionList()) {
			for(String l : getNotRealIDSListByAction(b)) {
			
				String real = getRealIDOf(b, l);
				
				if(real.equalsIgnoreCase(id))
				{
					return l;
				}
			}
			
			
		}
		
		return null;
	}
	
	public List<String> getLore(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		List<String> result =jb.getLanguage().getListFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".Lore");
		
		if(result == null) {
			Bukkit.getConsoleSender().sendMessage("§cMissing Option of "+this.getConfigID()+" Job -> Lore of Job");
		}
		
		return result;
		
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public List<String> getStatsMessage(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		return jb.getLanguage().getListFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".Stats");
	}

	public double getPrice() {
		return price;
	}

	public int getSlot() {
		return slot;
	}

	public String getIcon() {
		
		if(icon == null) {
			return "BARRIER";
		}
		
		return icon;
	}

	public ArrayList<JobAction> getActionList() {
	 
		return action;
	}

	public String getDisplay(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		String display = jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".Display");
		
		if(display == null) {
			return "Error";
		}
		
		return UltimateJobs.getPlugin().getPluginManager().toHex(display).replaceAll("&", "§");
	}

	public String getConfigID() {
		return idt;
	}
	
	public String getDisplayID(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(UUID);
		return  jb.getLanguage().getStringFromLanguage(jb.getUUID(), "Jobs."+this.getConfigID()+".DisplayID");
	}

}
