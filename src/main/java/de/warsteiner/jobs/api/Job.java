package de.warsteiner.jobs.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.utils.Action;

public class Job {

	private String idt;
	private YamlConfiguration cf;
	private String display;
	private Action action;
	private String icon;
	private int slot;
	private double price;
	private String perm;
	private List<String> stats_message;
	private List<String> worlds;
	private List<String> lore;
	private List<String> idslist;
	private File file;
	
	public Job(String id, YamlConfiguration cfg, File f) {
		idt = id;
		cf = cfg;
		display = cfg.getString("Display");
		perm = cfg.getString("Permission");
		action = Action.valueOf(cfg.getString("Action"));
		icon = cfg.getString("Material");
		slot = cfg.getInt("Slot");
		price = cfg.getDouble("Price");
		stats_message = cfg.getStringList("Stats");
		worlds = cfg.getStringList("Worlds");
		lore = cfg.getStringList("Lore");
		idslist = cfg.getStringList("IDS.List");
		file = f;
	}
	
	public void updateList(String mode, List<String> list) {
		cf.set(mode, list);
		save();
	}
	
	public void updateDesc(ArrayList<String> d) {
		cf.set("Lore", d);
		save();
	}
	
	public void updatePermissionLore(ArrayList<String> d) {
		cf.set("PermLore", d);
		save();
	}
	
	public void updatePermissionMessage(String d) {
		cf.set("PermMessage", d);
		save();
	}
	
	public void updatePermission(String value) {
		if(value.toLowerCase().equalsIgnoreCase("none")) {
			cf.set("Permission", null);
			cf.set("PermLore", null);
			cf.set("PermMessage", null);
			save();
		} else {
			
			ArrayList<String> example = new ArrayList<String>();
			
			example.add("§7This is a example Lore");
			
			cf.set("Permission", value);
			cf.set("PermLore", example);
			cf.set("PermMessage", "<prefix> &7This is a example Message!");
			save();
		}
	}
	
	public void updateBypassPermission(String value) {
		if(value.toLowerCase().equalsIgnoreCase("none")) {
			cf.set("BypassPermission", null);
			save();
		} else {
			cf.set("BypassPermission", value);
			save();
		}
	}
	
	public void updateSlot(String d) {
		cf.set("Slot", Integer.valueOf(d));
		save();
	}
	
	public void updatePrice(String d) {
		cf.set("Price", Integer.valueOf(d));
		save();
	}
	
	public void updateIcon(String d) {
		cf.set("Material", d);
		save();
	}
	
	
	public void updateDisplayName(String d) {
		cf.set("Display", d);
		save();
	}
	
	public void updateJobBarColor(String action) {
		cf.set("ColorOfBossBar", action);
		save();
	}
	
	public void updateJobAction(String action) {
		cf.set("Action", action);
		save();
	}
	
	public void save() {
		try {
			cf.save(file);
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}
	
	public BarColor getBarColor() {
		return BarColor.valueOf(cf.getString("ColorOfBossBar"));
	}
	
	public YamlConfiguration getConfig() {
		return cf;
	}

	public String getLevelDisplay(int i) {
		return cf.getString("LEVELS." + i + ".Display");
	}

	public List<String> getLevelCommands(int i) {
		return cf.getStringList("LEVELS." + i + ".Commands");
	}

	public List<String> getPermissionsLore() { 
		return cf.getStringList("PermLore");
	}
  
	public String getPermMessage() { 
		return cf.getString("PermMessage");
	}

	public boolean hasPermission() {
		return cf.getString("Permission") != null;
	}

	public boolean hasByoassPermission() {
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
	
	public List<String> getNotQuestConLore() {
		return cf.getStringList("ReqNotQuestCondLore");
	}

	public boolean isCommand(int level) {
		return cf.getStringList("LEVELS." + level + ".Commands") != null;
	}
	
	public double getVaultOnLevel(int level) {
		return cf.getDouble("LEVELS." + level + ".Money");
	} 

	public boolean isVaultOnLevel(int level) {
		return cf.getString("LEVELS." + level + ".Money") != null;
	}
 
	public String getNameOfLevel(int level) {
		return cf.getString("LEVELS." + level + ".Display");
	}

	public double getExpOfLevel(int level) {
		return cf.getDouble("LEVELS." + level + ".Need");
	}
	
	public List<String> getCommandsOfBlock(String id) {
		return cf.getStringList("IDS." + id + ".Commands");
	} 

	public boolean isCommandonBlock(String id) {
		return cf.getStringList("IDS." + id + ".Commands") != null;
	}

	public String getDisplayOf(String id) {
		return cf.getString("IDS." + id + ".Display");
	}

	public int getChanceOf(String id) {
		return cf.getInt("IDS." + id + ".Chance");
	}

	public double getRewardOf(String id) {
		return cf.getDouble("IDS." + id + ".Money");
	}

	public List<String> getCustomitems() {
		return cf.getStringList("Items");
	}

	public double getExpOf(String id) {
		return cf.getDouble("IDS." + id + ".Exp");
	}

	public double getPointsOf(String id) {
		return cf.getDouble("IDS." + id + ".Points");
	}

	public List<String> getIDList() {
		return idslist;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<String> getWorlds() {
		return worlds;
	}

	public List<String> getStatsMessage() {
		return stats_message;
	}

	public double getPrice() {
		return price;
	}

	public int getSlot() {
		return slot;
	}

	public String getIcon() {
		return icon;
	}

	public Action getAction() {
		return action;
	}

	public String getDisplay() {
		return SimpleAPI.getInstance().getAPI().toHex(display).replaceAll("&", "§");
	}

	public String getID() {
		return idt;
	}

}
