package de.warsteiner.jobs.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.Action; 

public class JobAPI {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	public boolean canWorkThere(Player player, File file) {
		 
	    if(canWorkInWorld(player.getWorld().getName(), file)) {
	    	if(canWorkInRegion(player, file)) {
	    		return true;
	    	}
	    }
		
		return false;
	}
	

	public boolean needCustomItemToWork(File job) {
		YamlConfiguration cfg = getJobConfig(job);
		return cfg.contains("Items");
	}

	public boolean hasItemInhandWhichIsNeed(File job, Player p) {
		YamlConfiguration cfg = getJobConfig(job);
		List<String> items = cfg.getStringList("Items");
		ItemStack current = p.getItemInHand();
		for (String item : items) {
			ItemStack i = new ItemStack(Material.valueOf(item.toUpperCase()));
			if (current.equals(i)) {
				return true;
			}
		}

		return false;
	}
	
	public boolean canWorkInRegion(Player player, File file) {
		return true;
	}
	
	public boolean canReward(Player player, File file, String id) {
		
		double chance = getChanceOfID(file, id);
		 
		if(getIDsList(file).contains(id.toUpperCase())) {
			Random r = new Random();
			int chance2 = r.nextInt(100); 
			if (chance2 < chance) {
				return true;
			}
		}
		
		return false; 
	}
	
	public String getName(String name) {
		return name.replaceAll(".yml", "   ").replaceAll(" ", "");
	}

	public ArrayList<File> getJobsWithAction(Action at) {

		ArrayList<File> list = new ArrayList<File>();

		ArrayList<File> jlist = plugin.getLoadedJobs();

		for (File j : jlist) {

			YamlConfiguration config = getJobConfig(j);

			if (config.getString("Action") == null) {
				UltimateJobs.getPlugin().getLogger()
						.warning("§cThe option §aCONFIG.GET(ACTION) §cdoesnt exist in config of: §a" + j.getName());

			}

			String action = config.getString("Action");

			if (Action.valueOf(action) == null) {
				UltimateJobs.getPlugin().getLogger()
						.warning("§cThe action §a" + action + " §cdoesnt exist for the Job §a" + j.getName() + "§c!");

			}

			if (Action.valueOf(action).equals(at)) {

				list.add(j);
			}

		}

		return list;

	}

	public boolean canWorkInWorld(String world, File file) {

		return getWorlds(file).contains(world);
	}

	public boolean isJobFile(File file) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.contains("Display");
	}

	public YamlConfiguration getJobConfig(File file) {
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public Action getAction(File file) {
		return Action.valueOf(getJobConfig(file).getString("Action"));
	}
	
	public String getDisplay(File file) {
		return getJobConfig(file).getString("Display");
	}
	
	public String getMaterial(File file) {
		return getJobConfig(file).getString("Material");
	}

	public BarColor getColorOfBossBar(File file) {
		return BarColor.valueOf(getJobConfig(file).getString("ColorOfBossBar"));
	}
	
	public int getSlot(File file) {
		return getJobConfig(file).getInt("Slot");
	}
	
	public int getPrice(File file) {
		return getJobConfig(file).getInt("Price");
	}
	
	public List<String> getWorlds(File file) {
		return getJobConfig(file).getStringList("Worlds");
	}
	
	public List<String> getLore(File file) {
		return getJobConfig(file).getStringList("Lore");
	}
	
	public List<String> getIDsList(File file) {
		return getJobConfig(file).getStringList("IDS.List");
	}
	
	public String getDisplayOfID(File file, String id) {
		return getJobConfig(file).getString("IDS."+id+".Display");
	}
	
	public double getRewardOfID(File file, String id) {
		return getJobConfig(file).getDouble("IDS."+id+".Reward");
	}
	
	public double getExpOfID(File file, String id) {
		return getJobConfig(file).getDouble("IDS."+id+".Exp");
	}
	
	public double getPointsOfID(File file, String id) {
		return getJobConfig(file).getDouble("IDS."+id+".Points");
	}
	
	public double getChanceOfID(File file, String id) {
		return getJobConfig(file).getDouble("IDS."+id+".Chance");
	}
	
	public void createDemoFile(File file) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		// lists

		ArrayList<String> worlds = new ArrayList<String>();
		ArrayList<String> desc = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> commands = new ArrayList<String>();
		ArrayList<String> commands_2 = new ArrayList<String>();

		// loading lists
		worlds.add("world");
		worlds.add("world_nether");

		desc.add("&8&m-----------------------");
		desc.add("&7This is an example Job. Just break some sand or stone!");

		ids.add("STONE");
		ids.add("SAND");

		commands.add("say give <player> reward of <money>");
		commands.add("say message from example job, please update me :p");

		commands_2.add("say give <player> reward of <money>");
		commands_2.add("say message from example job, please update me :p");

		// basics
		config.set("Action", "BREAK");
		config.set("ID", "Example Job");
		config.set("Display", "&6&lExample Job");
		config.set("Material", "PAPER");
		config.set("ColorOfBossBar", "ORANGE");
		config.set("Slot", 10);
		config.set("Price", 150);
		config.set("Worlds", worlds);
		config.set("Lore", desc);

		// ids - list
		config.set("IDs.List", ids);

		// ids config
		config.set("IDs.STONE.Display", "&7&lStone");
		config.set("IDs.STONE.Reward", 1.15);
		config.set("IDs.STONE.Exp", 2.25);
		config.set("IDs.STONE.Points", 1);
		config.set("IDs.STONE.Chance", 85);

		config.set("IDs.SAND.Display", "&e&lSand");
		config.set("IDs.SAND.Reward", 1.15);
		config.set("IDs.SAND.Exp", 2.25);
		config.set("IDs.SAND.Points", 1);
		config.set("IDs.SAND.Chance", 85);

		// levels

		config.set("Levels.1.Display", "&6&lLevel I");
		config.set("Levels.1.Commands", commands);
		config.set("Levels.1.Need", 0);

		config.set("Levels.2.Display", "&6&lLevel II");
		config.set("Levels.2.Commands", commands_2);
		config.set("Levels.2.Need", 250);

		try {
			config.save(file);
		} catch (IOException iOException) {
		}
	}

}
