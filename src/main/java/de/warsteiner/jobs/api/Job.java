package de.warsteiner.jobs.api;

import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.Action;

public class Job {

	private String id;
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

	public Job(String id, YamlConfiguration cfg) {
		this.id = id;
		this.cf = cfg;
		this.display = cfg.getString("Display");
		this.perm = cfg.getString("Permission");
		this.action = Action.valueOf(cfg.getString("Action"));
		this.icon = cfg.getString("Material");
		this.slot = cfg.getInt("Slot");
		this.price = cfg.getDouble("Price");
		this.stats_message = cfg.getStringList("Stats");
		this.worlds = cfg.getStringList("Worlds");
		this.lore = cfg.getStringList("Lore");
		this.idslist = cfg.getStringList("IDS.List");

	}

	public BarColor getBarColor() {
		return BarColor.valueOf(this.cf.getString("ColorOfBossBar"));
	}

	public String getLevelDisplay(int i) {
		return this.cf.getString("LEVELS." + i + ".Display");
	}

	public List<String> getLevelCommands(int i) {
		return this.cf.getStringList("LEVELS." + i + ".Command");
	}

	public List<String> getPermissionsLore() {
		return this.cf.getStringList("PermLore");
	}

	public String getPermMessage() {
		return this.cf.getString("PermMessage");
	}

	public boolean hasPermission() {
		return this.cf.contains("Permission");
	}

	public String getPermission() {
		return this.perm;
	}

	public Job get() {
		return this;
	}

	public boolean getNeedToGrown(String id) {
		return this.cf.getBoolean("IDS." + id + ".Need_Grown");
	}

	public String getNameOfLevel(int level) {
		return this.cf.getString("LEVELS." + level + ".Display");
	}

	public double getExpOfLevel(int level) {
		return this.cf.getDouble("LEVELS." + level + ".Need");
	}

	public String getDisplayOf(String id) {
		return this.cf.getString("IDS." + id + ".Display");
	}

	public int gettChanceOf(String id) {
		return this.cf.getInt("IDS." + id + ".Chance");
	}

	public double getRewardOf(String id) {
		return this.cf.getDouble("IDS." + id + ".Money");
	}

	public List<String> getCustomitems() {
		return this.cf.getStringList("Items");
	}

	public double getExpOf(String id) {
		return this.cf.getDouble("IDS." + id + ".Exp");
	}

	public double getPointsOf(String id) {
		return this.cf.getDouble("IDS." + id + ".Points");
	}

	public List<String> getIDList() {
		return this.idslist;
	}

	public List<String> getLore() {
		return this.lore;
	}

	public List<String> getWorlds() {
		return this.worlds;
	}

	public List<String> getStatsMessage() {
		return this.stats_message;
	}

	public double getPrice() {
		return this.price;
	}

	public int getSlot() {
		return this.slot;
	}

	public String getIcon() {
		return this.icon;
	}

	public Action getAction() {
		return this.action;
	}

	public String getDisplay() {
		return UltimateJobs.getPlugin().getAPI().toHex(this.display).replaceAll("&", "§");
	}

	public String getID() {
		return this.id;
	}

}
