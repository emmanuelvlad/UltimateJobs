package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException; 
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.Action;

public class Job {

	private String idt;
	private YamlConfiguration cf; 
	private Action action;
	private String icon;
	private int slot;
	private double price;
	private String perm; 
	private List<String> worlds; 
	private List<String> idslist;
	private File file;
	
	public Job(String id, YamlConfiguration cfg, File f) {
		idt = id;
		cf = cfg; 
		perm = cfg.getString("Permission");
		action = Action.valueOf(cfg.getString("Action"));
		icon = cfg.getString("Material");
		slot = cfg.getInt("Slot");
		price = cfg.getDouble("Price"); 
		worlds = cfg.getStringList("Worlds"); 
		idslist = cfg.getStringList("IDS.List");
		file = f;
	}
 
	public void save() {
		try {
			cf.save(file);
		} catch (IOException e) { 
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to save File for Job "+getID()+"!");
			e.printStackTrace();
		}
	}

	public File getFile() {
		
		if(file == null) {
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to return Job File for Job "+getID()+"!");
		}
		
		return file;
	}
	
	public BarColor getBarColor() {
		
		if(BarColor.valueOf(cf.getString("ColorOfBossBar")) == null) {
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to return Bar Color for Job "+getID()+"!");
			return BarColor.RED;
		}
		
		return BarColor.valueOf(cf.getString("ColorOfBossBar"));
	}
	
	public YamlConfiguration getConfig() {
		if(cf == null) {
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to return Job Config for Job "+getID()+"!");
		}
		return cf;
	}
 
	public String getLevelDisplay(int i, String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		if(cf.getString("LEVELS." + i + ".Display") == null) {
			return "Error";
		}
		
		return UltimateJobs.getPlugin().getPluginManager().getSomethingFromPath(jb.getUUID(), cf.getString("LEVELS." + i + ".Display")); 
			 
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
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		if(!cf.contains("PermLore")) {
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to return Job PermLore for Job "+getID()+"!");
		}
		
		return UltimateJobs.getPlugin().getPluginManager().getSomethingAsListFromPath(jb.getUUID(), cf.getString("PermLore"));  
	}
  
	public String getPermMessage(String UUID) { 
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		if(!cf.contains("PermMessage")) {
			UltimateJobs.getPlugin().getLogger().warning("§4Failed to return Job PermMessage for Job "+getID()+"!");
		}
		return UltimateJobs.getPlugin().getPluginManager().getSomethingFromPath(jb.getUUID(), cf.getString("PermMessage")); 
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
	
	public boolean hasByPassNotQuestCon() {
		return cf.contains("BypassNotQuestCond");
	}
	
	public List<String> getByPassNotQuestCon() {
		return cf.getStringList("BypassNotQuestCond");
	}
	
	public boolean hasNotQuestCon() {
		return cf.contains("ReqNotQuestCond");
	}
 
	public List<String> getNotQuestCon() {
		return cf.getStringList("ReqNotQuestCond");
	}
	
	public List<String> getNotQuestConLore(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		return  UltimateJobs.getPlugin().getPluginManager().getSomethingAsListFromPath(jb.getUUID(), cf.getString("ReqNotQuestCondLore"));  
	}
	
	public boolean hasAlonsoLevelsReq() {
		return cf.contains("AlonsoLevelReq");
	}
	
	public int getAlonsoLevelsReq() {
		return cf.getInt("AlonsoLevelReq");
	}
	
	public List<String> getAlonsoLevelsLore(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		return UltimateJobs.getPlugin().getPluginManager().getSomethingAsListFromPath(jb.getUUID(), cf.getString("AlonsoLevelLore")); 
	}
	
	public boolean hasBypassAlonsoLevelsReq() {
		return cf.contains("AlonsoLevelReqBypass");
	}
	
	public boolean hasMaxEarningsPerDay() {
		return cf.contains("MaxEarnings");
	}
	
	public double getMaxEarningsPerDay() {
		return cf.getDouble("MaxEarnings");
	}
	
	public int getBypassAlonsoLevelsReq() {
		return cf.getInt("AlonsoLevelReqBypass");
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
	
	public List<String> getCommandsOfBlock(String id) {
		return cf.getStringList("IDS." + id + ".Commands");
	} 

	public boolean isCommandonBlock(String id) {
		return cf.getStringList("IDS." + id + ".Commands") != null;
	}

	public String getDisplayOf(String id, String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		return  UltimateJobs.getPlugin().getPluginManager().getSomethingFromPath(jb.getUUID(), cf.getString("IDS." + id + ".Display")); 
	}

	public int getChanceOf(String id) {
		return cf.getInt("IDS." + id + ".Chance");
	}

	public double getRewardOf(String id) {
		return cf.getDouble("IDS." + id + ".Money");
	}
	
	public boolean hasVaultReward(String id) {
		return getRewardOf(id) != 0;
	}

	public List<String> getCustomitems() {
		return cf.getStringList("Items");
	}

	public double getExpOf(String id) {
		
		if(!cf.contains("IDS." + id + ".Exp")) {
			return 0;
		}
		
		return cf.getDouble("IDS." + id + ".Exp");
	}

	public double getPointsOf(String id) {

		if(!cf.contains("IDS." + id + ".Points")) {
			return 0;
		}
		return cf.getDouble("IDS." + id + ".Points");
	}

	public List<String> getIDList() {
		return idslist;
	}

	public List<String> getLore(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		return UltimateJobs.getPlugin().getPluginManager().getSomethingAsListFromPath(jb.getUUID(), cf.getString("Lore"));
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public List<String> getStatsMessage(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		return UltimateJobs.getPlugin().getPluginManager().getSomethingAsListFromPath(jb.getUUID(), cf.getString("Stats"));
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

	public Action getAction() {
		
		if(action == null) {
			return Action.BREAK;
		}
		
		return action;
	}

	public String getDisplay(String UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(UUID);
		String display = UltimateJobs.getPlugin().getPluginManager().getSomethingFromPath(jb.getUUID(), cf.getString("Display"));
		
		if(display == null) {
			return "Error";
		}
		
		return SimpleAPI.getInstance().getAPI().toHex(display).replaceAll("&", "§");
	}

	public String getID() {
		return idt;
	}

}
