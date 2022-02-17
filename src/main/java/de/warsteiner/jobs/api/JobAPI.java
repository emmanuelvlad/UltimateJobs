package de.warsteiner.jobs.api;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.utils.Action;
import de.warsteiner.jobs.utils.BossBarHandler;
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
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
	  
	  private YamlConfiguration tr;
	  
	  private PluginAPI up = SimpleAPI.getInstance().getAPI();
	  
	  public HashMap<String, Date> lastworked_list;
	  
	  public JobAPI(UltimateJobs plugin, YamlConfiguration tr) {
	    this.lastworked_list = new HashMap<>();
	    this.tr = tr;
	    this.plugin = plugin;
	  }

	public void playSound(String ty, Player player) {
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		if (config.contains("Sounds." + ty + ".Sound")) {
			Sound sound = Sound.valueOf(config.getString("Sounds." + ty + ".Sound"));
			int vol = config.getInt("Sounds." + ty + ".Volume");
			int pitch = config.getInt("Sounds." + ty + ".Pitch");
			player.playSound(player.getLocation(), sound, vol, pitch);
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

	public boolean checkIfJobIsReal(String arg, CommandSender s) {
		String id = arg.toUpperCase();
		if (plugin.getAPI().isJobFromConfigID(id) != null)
			return true;
		s.sendMessage(plugin.getAPI().getMessage("Not_Found_Job").replaceAll("<job>", arg.toLowerCase()));
		return false;
	}

	public String isCurrentlyInCache(String uuid) {
		if (plugin.getPlayerManager().existInCacheByUUID(uuid))
			return "CACHE";
		return "SQL";
	}

	public void sendReward(JobsPlayer pl, Player p, Job job, double exp, double reward, String block) {
		plugin.getExecutor().execute(() -> {
			String UUID = "" + p.getUniqueId();
			String ijob = job.getID();
			double all_exp = pl.getExpOf(ijob).doubleValue();
			int level = pl.getLevelOf(ijob).intValue();
			String disofid = job.getDisplayOf(block);
			double need = plugin.getLevelAPI().getJobNeedExp(job, pl);
			if (tr.getBoolean("Reward.Enable_BossBar")) {
				Date isago5seconds = new Date((new Date()).getTime() + 3000L);
				if (lastworked_list.containsKey(p.getName()))
					lastworked_list.remove(p.getName());
				lastworked_list.put(p.getName(), isago5seconds);
				double use = BossBarHandler.calc(all_exp, plugin.getLevelAPI().canLevelMore(UUID, job, level),
						need);
				BarColor color = job.getBarColor();
				String message = up.toHex(tr.getString("Reward.BossBar").replaceAll("<prefix>", getPrefix())
						.replaceAll("<exp>", Format(all_exp)).replaceAll("<level_name>", job.getLevelDisplay(level))
						.replaceAll("<level_int>", "" + level).replaceAll("<id>", disofid)
						.replaceAll("<money>", Format(reward)).replaceAll("&", "§"));
				if (!BossBarHandler.exist(p.getName())) {
					BossBarHandler.createBar(p, message, color, p.getName(), use);
				} else {
					BossBarHandler.renameBossBar(message, p.getName());
					BossBarHandler.recolorBossBar(color, p.getName());
					BossBarHandler.updateProgress(use, p.getName());
				}
			}
			if (tr.getBoolean("Reward.Enable_Message")) {
				String message = up.toHex(tr.getString("Reward.Message").replaceAll("<prefix>", getPrefix())
						.replaceAll("<exp>", Format(all_exp)).replaceAll("<level_name>", job.getLevelDisplay(level))
						.replaceAll("<level_int>", "" + level).replaceAll("<id>", disofid)
						.replaceAll("<money>", Format(reward)).replaceAll("&", "§"));
				p.sendMessage(message);
			}
			if (tr.getBoolean("Reward.Enabled_Actionbar")) {
				String message = up.toHex(tr.getString("Reward.Actionbar").replaceAll("<prefix>", getPrefix())
						.replaceAll("<exp>", Format(all_exp)).replaceAll("<level_name>", job.getLevelDisplay(level))
						.replaceAll("<level_int>", "" + level).replaceAll("<id>", disofid)
						.replaceAll("<money>", Format(reward)).replaceAll("&", "§"));
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent) new TextComponent(message));
			}
		});
	}

	public void loadJobs(Logger logger) {
		File dataFolder = new File(plugin.getMainConfig().getConfig().getString("LoadJobsFrom"));
		File[] files = dataFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			File file = files[i];
			if (file.isFile()) {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				Job job = new Job(cfg.getString("ID"), YamlConfiguration.loadConfiguration(file));
				plugin.getLoaded().add(job);
				plugin.getID().put(job.getID(), job);
			} else if (file.isDirectory()) {
			}
		}
	}

	public Job isSettingsGUI(String menu) {
		for (Job j : plugin.getLoaded()) {
			String dis = j.getDisplay();
			String fin = up.toHex(plugin.getMainConfig().getConfig().getString("Settings_Name")
					.replaceAll("<job>", dis).replaceAll("&", "§"));
			if (fin.equalsIgnoreCase(menu))
				return j;
		}
		return null;
	}

	public boolean checkPermissions(CommandSender s, String text) {
		if (s.hasPermission("ultimatejobs." + text) || s.hasPermission("ultimatejobs.admin.all"))
			return true;
		s.sendMessage(plugin.getAPI().getMessage("No_Perm"));
		return false;
	}

	public String isCustomItem(String display, Player player, String path, YamlConfiguration cf) {
		List<String> custom_items = cf.getStringList(path + ".List");
		for (String b : custom_items) {
			String dis = up.toHex(cf.getString(path + "." + path + ".Display").replaceAll("&", "§"));
			if (display.equalsIgnoreCase(dis))
				return b;
		}
		return "NOT_FOUND";
	}

	public List<String> canGetJobWithSubOptions(Player player, Job job) {
		if (job.hasNotQuestCon() == true && plugin.isInstalledNotQuest()
				&& !plugin.getNotQuestManager().canHaveJob(player, job)) {
			return job.getNotQuestConLore();
		}
		return null;
	}

	public boolean canBuyWithoutPermissions(Player player, Job job) {
		if (job.hasPermission() == true) {
			return player.hasPermission(job.getPermission());
		}
		return true;
	}

	public boolean canByPass(Player player, Job job) {
		if (job.hasByoassPermission()) {
			return player.hasPermission(job.getByPassPermission());
		}
		if (job.hasByPassNotQuestCon() == true && plugin.isInstalledNotQuest()) {
			return plugin.getNotQuestManager().canBypassJob(player, job);
		}
		return false;
	}

	public boolean canWorkThere(Player player, Job j, String st) {
		if (canWorkInWorld(player.getWorld().getName(), j) && canWorkInRegion(player, j, st).equalsIgnoreCase("ALLOW"))
			return true;
		return false;
	}

	public ArrayList<String> getJobsWithAction(String UUID, JobsPlayer pl, Action ac) {
		ArrayList<String> l = new ArrayList<>();
		for (String j : pl.getCurrentJobs()) {
			Job jb = (Job) plugin.getJobCache().get(j);
			if (jb.getAction().equals(ac))
				l.add(jb.getID());
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

	public String canWorkInRegion(Player player, Job j, String st) {
		if (plugin.isInstalledWorldGuard()) {
			String ac = "" + j.getAction();
			String flag = ac.toLowerCase() + "_action";
			return WorldGuardManager.checkFlag(player.getLocation(), flag, player, st);
		}
		return "ALLOW";
	}

	public boolean canReward(Player player, Job j, String id) {
		double chance = j.getChanceOf(id);
		if (j.getIDList().contains(id)) {
			Random r = new Random();
			int chance2 = r.nextInt(100);
			if (chance2 < chance)
				return true;
		}
		return false;
	}

	public ArrayList<String> getJobsInListAsID() {
		ArrayList<String> list = new ArrayList<>();
		for (Job job : plugin.getLoaded())
			list.add(job.getID().toLowerCase());
		return list;
	}

	public Job isJobFromConfigID(String id) {
		for (Job job : plugin.getLoaded()) {
			String cfg_id = job.getID();
			if (cfg_id.equalsIgnoreCase(id))
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
		DecimalFormat t = new DecimalFormat(plugin.getMainConfig().getConfig().getString("Format"));
		String b = t.format(i).replaceAll(",", ".");
		return b;
	}

	public String getDate() {
		DateFormat format = new SimpleDateFormat(plugin.getMainConfig().getConfig().getString("Date"));
		Date data = new Date();
		return format.format(data);
	}

	public String getMessage(String path) {
		return up.toHex(plugin.getMessages().getConfig().getString(path).replaceAll("<prefix>", getPrefix())
				.replaceAll("&", "§"));
	}

	public String getPrefix() {
		return up.toHex(plugin.getMessages().getConfig().getString("Prefix").replaceAll("&", "§"));
	}
}
