package de.warsteiner.jobs.api;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.manager.PluginManager;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import static java.util.Map.entry;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material; 
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
 

public class JobAPI {

	private UltimateJobs plugin;

	private PluginManager up = UltimateJobs.getPlugin().getPluginManager();

	public HashMap<String, Date> lastworked_list;

	public JobAPI(UltimateJobs plugin) {
		this.lastworked_list = new HashMap<>();
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public boolean canWithdrawMoney(Player player, JobsPlayer jb) {
		
		if(plugin.getFileManager().getConfig().getBoolean("WithdrawCooldown")) {
			
			if(jb.getSalaryDate() == null) {
				return true;
			}
		  
			String lasttime = jb.getSalaryDate();
			
			 
			Date date_last = new Date(lasttime);

			Date date2 = new Date(plugin.getPluginManager().getDateTodayFromCal());
			 
			if( date2.after(date_last)) {
				return true;
			} else {
				return false;
			}
			
		}  
		return true; 
	}
	
	public String compareData(Entity block) {
		
		List<MetadataValue> values = block.getMetadata("saplingby");
		if (!values.isEmpty()) {

			for(MetadataValue value : values) { 
				String val = value.value().toString();
				
				if(val.contains("uuid;")) {
					
					String[] split = val.split(";");
					
					String player = split[1];
					
					
					return player;
					
				}
			}
			
		}
		return null;
		
	}
	
	public String compareData(Block block) {
		
		List<MetadataValue> values = block.getMetadata("saplingby");
		if (!values.isEmpty()) {

			for(MetadataValue value : values) { 
				String val = value.value().toString();
				
				if(val.contains("uuid;")) {
					
					String[] split = val.split(";");
					
					String player = split[1];
					
					
					return player;
					
				}
			}
			
		}
		return null;
		
	}

	public void playSound(String ty, Player player) {
		FileConfiguration config = plugin.getFileManager().getConfig();
		if (config.contains("Sounds." + ty + ".Sound")) {

			if (Sound.valueOf(config.getString("Sounds." + ty + ".Sound")) == null) {
				Bukkit.getConsoleSender()
						.sendMessage("§cFailed to get Sound from : " + config.getString("Sounds." + ty + ".Sound"));
				return;
			}

			Sound sound = Sound.valueOf(config.getString("Sounds." + ty + ".Sound"));
			double vol = config.getDouble("Sounds." + ty + ".Volume", config.getInt("Sounds." + ty + ".Volume"));
			double pitch = config.getDouble("Sounds." + ty + ".Pitch", config.getInt("Sounds." + ty + ".Pitch"));
			player.playSound(player.getLocation(), sound, (float) vol, (float) pitch);
		}
	}

	public void spawnFireworks(Location location) {
		Location loc = location;
		Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.setPower(1);
		fwm.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).build());
		fw.setFireworkMeta(fwm);
		fw.detonate();
		fw.setMetadata("nodamage", (MetadataValue) new FixedMetadataValue((Plugin) plugin, Boolean.valueOf(true)));
		Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		fw2.setFireworkMeta(fwm);
	}

	public boolean checkIfJobIsReal(String arg, Player player) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return true;
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return true;
		}
		player.sendMessage(jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "job_not_found")
				.replaceAll("<job>", arg.toLowerCase()));
		return false;
	}
	
	public Job checkIfJobIsRealWithResult(String arg, Player player) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return isJobFromConfigID(id);
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return isJobFromDisplayID(id, "" + player.getUniqueId());
		}
		player.sendMessage(jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "job_not_found")
				.replaceAll("<job>", arg.toLowerCase()));
		return null;
	}

	public Job checkIfJobIsRealAndGet(String arg, Player player) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());

		String id = arg.toUpperCase();
		if (isJobFromConfigID(id) != null) {
			return isJobFromConfigID(id);
		}
		if (isJobFromDisplayID(id, "" + player.getUniqueId()) != null) {
			return isJobFromDisplayID(id, "" + player.getUniqueId());
		}
		player.sendMessage(jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "job_not_found")
				.replaceAll("<job>", arg.toLowerCase()));
		return null;
	}
 
	public String isCurrentlyInCache(String uuid) {
		if (plugin.getPlayerAPI().existInCacheByUUID(uuid))
			return "CACHE";
		return "SQL";
	}

	public void sendReward(JobsPlayer pl, Player p, Job job, double exp, double reward, String block, boolean can,
			JobAction ac, int amount) {
		plugin.getExecutor().execute(() -> {
			UUID UUID = p.getUniqueId();
			double all_exp = pl.getStatsOf(job.getConfigID()).getExp();
			int level = pl.getStatsOf(job.getConfigID()).getLevel();
			String disofid = job.getDisplayOf(block, "" + UUID, ac);
			double need = plugin.getLevelAPI().getJobNeedExp(job, pl);

			UltimateJobs.getPlugin().getAPI().playSound("REWARD", p);

			String prefix = null;

			if (can) {
				prefix = "Reward";
			} else {
				prefix = "MaxEarningsReached";
			}
			String prt = pl.getLanguage().getStringFromLanguage(UUID, "prefix");
			if (prefix != null) {
				Map<String, String> replacer = Map.ofEntries(
					entry("<amount>", ""+amount),
					entry("<prefix>", prt),
					entry("<job>", job.getDisplay("" + UUID)),
					entry("<exp>", Format(all_exp)),
					entry("<exp_gained>", Format(job.getExpOf(block, ac) * amount)),
					entry("<exp_required>", Format(plugin.getLevelAPI().getJobNeedExp(job, pl))),
					entry("<level_name>", job.getLevelDisplay(level, "" + UUID)),
					entry("<level_int>", ""+level),
					entry("<id>", disofid),
					entry("<action>", ac.toString().toLowerCase()),
					entry("<money>", Format(reward))
				);

				if (plugin.getFileManager().getConfig().getBoolean(prefix + ".Enable_BossBar")) {
					Date isago5seconds = new Date((new Date()).getTime() + 3000L);
					if (lastworked_list.containsKey(p.getName()))
						lastworked_list.remove(p.getName());
					lastworked_list.put(p.getName(), isago5seconds);
					double use = BossBarHandler.calc(all_exp, plugin.getLevelAPI().canLevelMore("" + UUID, job, level),
							need);
					BarColor color = job.getBarColor();
					String message = up.formatText(pl.getLanguage().getStringFromLanguage(UUID, prefix + ".BossBar"), replacer, p);
					if (!BossBarHandler.exist(p.getName())) {
						BossBarHandler.createBar(p, message, color, p.getName(), use);
					} else {
						BossBarHandler.renameBossBar(message, p.getName());
						BossBarHandler.recolorBossBar(color, p.getName());
						BossBarHandler.updateProgress(use, p.getName());
					}
				}
				if (plugin.getFileManager().getConfig().getBoolean(prefix + ".Enable_Message")) {
					String message = up.formatText(pl.getLanguage().getStringFromLanguage(UUID, prefix + ".Message"), replacer, p);
					p.sendMessage(message);
				}
				if (plugin.getFileManager().getConfig().getBoolean(prefix + ".Enabled_Actionbar")) {
					String message = up.formatText(pl.getLanguage().getStringFromLanguage(UUID, prefix + ".Actionbar"), replacer, p);
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(message));
				}
			}
		});
	}

	public void loadJobs(Logger logger) {
		File dataFolder = new File(plugin.getFileManager().getConfig().getString("LoadJobsFrom"));
		File[] files = dataFolder.listFiles();

		plugin.getLoaded().clear();
		plugin.getID().clear();

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				File file = files[i];
				Bukkit.getConsoleSender().sendMessage("§aChecking File " + name + "...");
				if (file.isFile()) {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					Job job = new Job(cfg.getString("ID"), YamlConfiguration.loadConfiguration(file), file);
					plugin.getLoaded().add(job.getConfigID());
					plugin.getID().put(job.getConfigID(), job);
					Bukkit.getConsoleSender()
							.sendMessage("§aLoaded Job " + job.getConfigID() + " from File " + name + "!");
				} else {
					Bukkit.getConsoleSender().sendMessage("§cFound File in Jobs Folder which isnt a real Job!");
				}
			}
		}
	}

	public boolean checkPermissions(Player player, String text) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + player.getUniqueId());
		if (player.hasPermission("ultimatejobs." + text) || player.hasPermission("ultimatejobs.admin.all"))
			return true;
		player.sendMessage(jb.getLanguage().getStringFromLanguage(player.getUniqueId(), "noperm"));
		return false;
	}

	public List<String> canGetJobWithSubOptions(Player player, Job job) {
		String UUID = "" + player.getUniqueId();
		//	if (job.hasNotQuestCon() == true && plugin.getPluginManager().isInstalled("NotQuests")
		//		&& !plugin.getNotQuestManager().canHaveJob(player, job)) {
		//	return job.getNotQuestConLore(UUID);
		//	}
		 
		return null;
	}

	public boolean canBuyWithoutPermissions(Player player, Job job) {
		if (job.hasPermission() == true) {
			return player.hasPermission(job.getPermission());
		}
		return true;
	}

	public boolean checkforDailyMaxEarnings(String UUID, Job job) {
 
		if (job.hasMaxEarningsPerDay()) {

			double max = job.getMaxEarningsPerDay();
 
			double current = plugin.getPlayerAPI().getEarningsOfToday(UUID, job);

			if (current >= max) {
				return false;
			}

		}

		return true;
	}

	public boolean canByPass(Player player, Job job) {
		if (job.hasBypassPermission()) {
			return player.hasPermission(job.getByPassPermission());
		}
 	//	if (job.hasByPassNotQuestCon() == true && plugin.getPluginManager().isInstalled("NotQuests")) {
		//return plugin.getNotQuestManager().canBypassJob(player, job);
			//} 
		return false;
	}

	public boolean canWorkThereByPlayer(String UUID, Job j, String st) {

		Location loc = null;

		if (Bukkit.getPlayer(UUID) == null) {
			loc = UltimateJobs.getPlugin().getLocationAPI().getLocation("LastLoc." + UUID);
		} else {
			loc = Bukkit.getPlayer(UUID).getLocation();
		}

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public boolean canWorkThereByBlock(Location loc, Job j, String st) {

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public boolean canWorkThereByEntity(Location loc, Job j, String st) {

		if (canWorkInWorld(loc.getWorld().getName(), j) && canWorkInRegion(loc, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public ArrayList<String> getJobsWithAction(String UUID, JobAction ac) {
		ArrayList<String> l = new ArrayList<>();
		for (String j : plugin.getPlayerAPI().getCurrentJobs(UUID)) {
			Job jb = (Job) plugin.getJobCache().get(j);
			for (JobAction actn : jb.getActionList()) {
				if (actn.equals(ac))
					l.add(jb.getConfigID());
			}

		}
		return l;
	}

	public boolean canWorkInWorld(String world, Job job) {
		return job.getWorlds().contains(world);
	}

	public boolean needCustomItemToWork(Job job) {
		return (job.getCustomitems() != null);
	}

	public boolean hasItemInhandWhichIsNeed(Job job, Player p) {
		List<String> items = job.getCustomitems();
		@SuppressWarnings("deprecation")
		ItemStack current = p.getItemInHand();
		for (String item : items) {
			ItemStack i = new ItemStack(Material.valueOf(item.toUpperCase()));
			if (current.equals(i))
				return true;
		}
		return false;
	}

	public String canWorkInRegion(Location loc, Job j, String flag) {
		if (plugin.getPluginManager().isInstalled("WorldGuard")) {

			return WorldGuardManager.checkFlag(loc, flag);

		}
		return "ALLOW";
	}

	public boolean canReward(Job j, String id, JobAction ac) {
		double chance = j.getChanceOf(id, ac);
		Random r = new Random();
		int chance2 = r.nextInt(100);
		if (chance2 < chance) {
			return true;
		}
		return false;
	}

	public ArrayList<String> getJobsInListAsID(String id) {
		ArrayList<String> list = new ArrayList<>();
		for (String l : plugin.getLoaded()) {
			Job j = plugin.getJobCache().get(l);
			list.add(j.getDisplayID(id).toLowerCase());
		}
		return list;
	}

	public Job isJobFromConfigID(String id) {
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String cfg_id = job.getConfigID();
			if (cfg_id.equalsIgnoreCase(id))
				return job;
		}
		return null;
	}

	public Job isJobFromDisplayID(String id, String UUID) {
		for (String list : plugin.getLoaded()) {
			Job job = plugin.getJobCache().get(list);
			String cfg_id = job.getDisplayID(UUID);
			if (cfg_id.toUpperCase().equalsIgnoreCase(id.toUpperCase()))

				return job;
		}
		return null;
	}

	public ArrayList<String> getPlayerNameList() {
		ArrayList<String> list = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers())
			list.add(p.getName());
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

	public String Format(double i) {
		DecimalFormat t = new DecimalFormat(plugin.getFileManager().getConfig().getString("Format"));
		String b = t.format(i).replaceAll(",", ".");
		return b;
	}

}
