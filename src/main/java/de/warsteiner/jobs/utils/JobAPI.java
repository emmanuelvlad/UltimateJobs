package de.warsteiner.jobs.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor; 
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.cevents.PlayerJobExpEvent;
import net.md_5.bungee.api.ChatColor; 

public class JobAPI {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

	public String toHex(String motd) {
		Matcher matcher = pattern.matcher(motd);
		while (matcher.find()) {
			String color = motd.substring(matcher.start(), matcher.end());
			motd = motd.replace(color, "" + ChatColor.of(color));
		}

		return motd;
	}
	
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
		@SuppressWarnings("deprecation")
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
	
	public HashMap<String, Date> lastworked_list = new HashMap<String, Date>();
	
	public void executeBlockBreakWork( BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if (block.hasMetadata("placed-by-player")) {
			return;
		} 
		
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}
		 
		JobAPI api = plugin.getJobAPI(); 
		PlayerAPI p = plugin.getPlayerAPI();
		String id = ""+block.getType();
		
		ArrayList<File> found = api.getJobsWithAction(Action.BREAK);
		 
		for(File job : found) { 
			if(api.canReward(player, job, id)) { 
			  
			String UUID = ""+player.getUniqueId();
		 
			String jobid = api.getID(job);
		 
			if(api.needCustomItemToWork(job)) {
	            if(!api.hasItemInhandWhichIsNeed(job, player)) { 
	                return;
	            }
	        }
		
			if(canGetRewardFromJobAndID(p, api, player, job, jobid)) {
				addRewards(player, job, id, UUID, jobid);
				
				sendRewardMessage(player, job, id, jobid, UUID);
			}  
			}
		}
	}
	
	public ArrayList<String> getJobsInListAsID() {
		ArrayList<String> list = new ArrayList<String>();
		for(File job : plugin.getLoadedJobs()) {
			String id = getID(job);
			list.add(id.toLowerCase());
		}
		return list;
	}
	
	public ArrayList<String> getPlayerNameList() {
		ArrayList<String> list = new ArrayList<String>();
		for(Player p :Bukkit.getOnlinePlayers()) { 
			list.add(p.getName());
		}
		return list;
	}
	
	public boolean isInt(String str) {
	    try {
	        Integer.parseInt(str);
	    } catch (Throwable e) {
	        return false;
	    }
	    return true;
	}
	
	public ArrayList<String> getJobsInListAsIDIfIsIn(String UUID) {
		ArrayList<String> list = new ArrayList<String>();
		for(File job : plugin.getLoadedJobs()) {
			String id = getID(job);
			if(plugin.getPlayerAPI().isInJob(UUID, id)) {
				list.add(id.toLowerCase());
			}
		}
		return list;
	}
	
	
	public boolean canGetRewardFromJobAndID(PlayerAPI p, JobAPI api, Player player, File job, String id) {
		if (api.canWorkThere(player, job)) {
			if(p.isInJob(""+player.getUniqueId(), id)) { 
				return true;
			}
		}
		return false;
	}
	
	public void sendRewardMessage(Player player, File job, String id, String jobid, String UUID) {
		JobAPI api = plugin.getJobAPI(); 
		PlayerAPI p = plugin.getPlayerAPI();
		YamlConfiguration tr = plugin.getMessages().getConfig();
		 
		double reward = api.getRewardOfID(job, id); 
		int level = p.getJobLevel(UUID, jobid);
		double ex = p.getJobExp(UUID, jobid);
		
		if(tr.getBoolean("Reward.Enable_BossBar")) {
			Date isago5seconds = new Date(new Date().getTime() + 3 * 1000);

			if (lastworked_list.containsKey(player.getName())) {
				lastworked_list.remove(player.getName());
			}

			lastworked_list.put(player.getName(), isago5seconds);
			
			double use = calcBossbar(player, job);

			BarColor color = api.getColorOfBossBar(job);
			String message = api.toHex(tr.getString("Reward.BossBar")
					.replaceAll("<prefix>", api.getPrefix())	.replaceAll("<exp>", api.Format(ex)).replaceAll("<level_name>",plugin.getLevelAPI().getLevelName(job, level)).replaceAll("<level_int>", ""+level)	.replaceAll("<id>", api.getDisplayOfID(job, id))	.replaceAll("<money>", api.Format(reward)).replaceAll("&", "§"));
			
			if (!BossBarHandler.exist(player.getName())) {
				BossBarHandler.createBar(player, message,color, player.getName(), use);
			} else {
				BossBarHandler.renameBossBar(message, player.getName());
				BossBarHandler.recolorBossBar(color, player.getName());
				BossBarHandler.updateProgress(use, player.getName());
			}
		}
		if(tr.getBoolean("Reward.Enable_Message")) {
			String message = api.toHex(tr.getString("Reward.Message")
					.replaceAll("<prefix>", api.getPrefix()).replaceAll("<exp>", api.Format(ex)).replaceAll("<level_name>",plugin.getLevelAPI().getLevelName(job, level)).replaceAll("<level_int>", ""+level)	.replaceAll("<id>", api.getDisplayOfID(job, id))	.replaceAll("<money>", api.Format(reward)).replaceAll("&", "§"));
			player.sendMessage(message);
		}
	}
	
	public void addRewards(Player player,  File job, String id, String UUID, String jobid) {
		YamlConfiguration cfg = plugin.getMainConfig().getConfig();
		JobAPI api = plugin.getJobAPI(); 
		PlayerAPI p = plugin.getPlayerAPI();
		
		double points = api.getPointsOfID(job, id);
		double reward = api.getRewardOfID(job, id);
		double exp = api.getExpOfID(job, id);  
		
		String date = getDate();
		
		p.addStatsArg(UUID, jobid, 1, 2);
		p.addStatsArg(UUID, jobid, points, 9);
 
		p.addMoneyAll(UUID, jobid, reward);
		p.addMoneyToday(UUID, jobid, reward, date);
		
		if(cfg.getBoolean("Enable_Levels")) {
			//add exp with event
			new PlayerJobExpEvent(UUID, job, player);
			
			p.addJobExp(UUID, jobid, exp);
		}
		//money
		plugin.getEco().depositPlayer(player, reward);
	}
 
	public void startSystemCheck() {
	 
			new BukkitRunnable() {

				public void run() {
				 
							for (Player p : Bukkit.getOnlinePlayers()) {

								Date lastworked = lastworked_list.get(p.getName());

								if (lastworked == null) {
									continue;
								}

								boolean check = lastworked.after(new Date());

								if (check == false) {
									if (lastworked_list.containsKey(p.getName())) {
										BossBarHandler.removeBossBar(p.getName());
										lastworked_list.remove(p.getName());
									}
								}

							}
					  
 
				}
			}.runTaskTimer(plugin, 0, 25);
		}
 

 	
	public double calcBossbar(Player player, File job) {
		double use;
		PlayerAPI pl =  plugin.getPlayerAPI();
		String UUID = ""+player.getUniqueId();
		String id = plugin.getJobAPI().getID(job);
		int level = pl.getJobLevel(UUID, id);
		 if(!plugin.getLevelAPI().isMaxLevel(UUID, job, level)) {
			double jobexp = pl.getJobExp(UUID, id);
			double jobneed = plugin.getLevelAPI().getJobNeedExp(UUID, job, id) / 100;

			double p = jobexp / jobneed;

			double max = 1.0 / 100;

			double one = max * p;

			if (one >= 1.0) {
				use = 1;
			} else {
				use = one;
			}
		} else {
			use = 1.0;
		}
		return use;
	}
	
	public String Format(double i) {

		DecimalFormat t = new DecimalFormat(plugin.getMainConfig().getConfig().getString("Format"));

		String b = t.format(i).replaceAll(",", ".");

		return b;

	}

	public String getDate() {
		DateFormat format = new SimpleDateFormat(plugin.getMainConfig().getConfig().getString("Date"));
		Date data = new Date();
		return "" + format.format(data);
	}
	
	public String getMessage(String path) {
		return toHex(plugin.getMessages().getConfig().getString(path).replaceAll("<prefix>", getPrefix()).replaceAll("&", "§"));
	}
	
	public String getPrefix() {
		return toHex(plugin.getMessages().getConfig().getString("Prefix").replaceAll("&", "§"));
	}
	
	public File isJobFromConfigID(String id) {
		for(File job : plugin.getLoadedJobs()) {
			String cfg_id = getID(job);
			if(cfg_id.equalsIgnoreCase(id)) {
				return job;
			}
		}
		return null;
	}
	
	public boolean canWorkInWorld(String world, File file) {

		return getWorlds(file).contains(world);
	}

	public boolean isJobFile(File file) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return config.contains("Display");
	}

	public YamlConfiguration getJobConfig(File file) {
		return plugin.getLoadedConfigs().get(file);
	}
	
	public Action getAction(File file) {
		return Action.valueOf(getJobConfig(file).getString("Action"));
	}
	
	public String getDisplay(File file) {
		return plugin.getJobAPI().toHex( getJobConfig(file).getString("Display").replaceAll("&", "§"));
	}
	
	public String getID(File file) {
		return getJobConfig(file).getString("ID");
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
	
	public double getPrice(File file) {
		return getJobConfig(file).getDouble("Price");
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
		return getJobConfig(file).getDouble("IDS."+id+".Money");
	}
	
	public List<String> getStatsDesc(File file) {
		return getJobConfig(file).getStringList("Stats");
	}
	
	public List<String> getLevelRewards(File file, int level) {
		return getJobConfig(file).getStringList("LEVELS."+level+".Command");
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
 

}
