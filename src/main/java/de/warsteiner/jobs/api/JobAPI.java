package de.warsteiner.jobs.api;

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

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta; 

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.datax.utils.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.plugins.WorldGuardManager;
import de.warsteiner.jobs.utils.Action;
import de.warsteiner.jobs.utils.BossBarHandler; 
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
 

public class JobAPI {

	private UltimateJobs plugin; 
	private YamlConfiguration tr;
	private PluginAPI up = UltimateAPI.getInstance().getAPI();

	public JobAPI(UltimateJobs plugin, YamlConfiguration tr) {
		this.tr = tr;
		this.plugin = plugin;
	}

	public HashMap<String, Date> lastworked_list = new HashMap<String, Date>();
	
	public void playSound(String ty, Player player) {
		YamlConfiguration config = plugin.getMainConfig().getConfig();
		if(config.contains("Sounds."+ty+".Sound")) {
			Sound sound = Sound.valueOf(config.getString("Sounds."+ty+".Sound"));
			int vol = config.getInt("Sounds."+ty+".Volume");
			int pitch = config.getInt("Sounds."+ty+".Pitch"); 
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

		Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		fw2.setFireworkMeta(fwm);

	}

	public boolean checkIfJobIsReal(String arg, CommandSender s) {
		String id = arg.toUpperCase();
		if (plugin.getAPI().isJobFromConfigID(id) != null) {
			return true;
		}
		s.sendMessage(plugin.getAPI().getMessage("Not_Found_Job").replaceAll("<job>", arg.toLowerCase()));
		return false;
	}

	public String isCurrentlyInCacheOrSQL(String name, String uuid) {
		if (plugin.getPlayerManager().existInCache(name)) {
			return "CACHE";
		}
		return "SQL";
	}

	public void sendReward(JobsPlayer pl, Player p, Job job, double exp, double reward, String block) {

		plugin.getExecutor().execute(() -> {

			String UUID = "" + p.getUniqueId();
			String ijob = job.getID();

			double all_exp = pl.getExpOf(ijob);
			int level = pl.getLevelOf(ijob);

			String disofid = job.getDisplayOf(block);
			double need = plugin.getLevelAPI().getJobNeedExp(job, pl);

			if (tr.getBoolean("Reward.Enable_BossBar")) {
				Date isago5seconds = new Date(new Date().getTime() + 3 * 1000);

				if (lastworked_list.containsKey(p.getName())) {
					lastworked_list.remove(p.getName());
				}

				lastworked_list.put(p.getName(), isago5seconds);

				double use = BossBarHandler.calc(all_exp, plugin.getLevelAPI().canLevelMore(UUID, job, level), need);
				BarColor color = job.getBarColor();
				String message =up.toHex(tr.getString("Reward.BossBar").replaceAll("<prefix>", getPrefix())
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
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));

			}
		});

	}

	public void loadJobs(Logger logger) {
		File dataFolder = new File(plugin.getDataFolder() + File.separator + "jobs");
		File[] files = dataFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			File file = files[i];
			if (file.isFile()) {
				logger.info("§fChecking Jobs File §e§l" + name);

				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

				Job job = new Job(cfg.getString("ID"), YamlConfiguration.loadConfiguration(file));

				plugin.getLoaded().add(job);
				plugin.getID().put(job.getID(), job);

				logger.info("§bLoaded Job with File §e§l" + name);

			} else if (file.isDirectory()) {
				logger.info("§cCannot load Directory §e§l" + name);
			}
		}
	}

	public Job isSettingsGUI(String menu) {
		for (Job j : plugin.getLoaded()) {
			String dis = j.getDisplay();
			String fin = up.toHex(plugin.getMainConfig().getConfig().getString("Settings_Name")
					.replaceAll("<job>", dis).replaceAll("&", "§"));
			if (fin.equalsIgnoreCase(menu)) {
				return j;
			}
		}
		return null;
	}

	public boolean checkPermissions(CommandSender s, String text) {
		if (s.hasPermission("ultimatejobs." + text) || s.hasPermission("ultimatejobs.admin.all")) {
			return true;
		}
		s.sendMessage(plugin.getAPI().getMessage("No_Perm"));
		return false;
	}

	public String isCustomItem(String display, Player player, String path, YamlConfiguration cf) {
		List<String> custom_items = cf.getStringList(path + ".List");
		for (String b : custom_items) {
			String dis =up.toHex(cf.getString(path + "." + b + ".Display").replaceAll("&", "§"));
			if (display.equalsIgnoreCase(dis)) {
				return b;
			}
		}

		return "NOT_FOUND";
	}

	public boolean canBuyWithoutPermissions(Player player, Job job) {
		if (job.hasPermission() == true) {
			return player.hasPermission(job.getPermission());
		}
		return true;
	}

	 

	public boolean canWorkThere(Player player, Job j, String st) {

		if (canWorkInWorld(player.getWorld().getName(), j)) {
			if (canWorkInRegion(player, j, st).equalsIgnoreCase("ALLOW")) {
				return true;
			}
		}

		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void executeHoneyAction(PlayerInteractEvent  event, JobsPlayer pl) {
		Player player = event.getPlayer();
		if (event.isCancelled()) {
			return;
		} 
		Material item = player.getItemInHand().getType();
		
		if(item == null) {
			return;
		}
		
		if (item != Material.GLASS_BOTTLE) {
			return;
		}

		finalWork("" + item, player, pl, Action.HONEY, "honey-action", 1);
		return;

	}
	

	public void executeEatAction(FoodLevelChangeEvent event, JobsPlayer pl) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getItem() == null) {
			return;
		}

		Material item = event.getItem().getType();

		finalWork("" + item, (Player) event.getEntity(), pl, Action.EAT, "eat-action", 1);
		return;

	}

	public void executeAchWork(PlayerAdvancementDoneEvent event, JobsPlayer pl) {
		finalWork(
				event.getAdvancement().getKey().getKey().replaceAll("story/", "   ").replaceAll(" ", "").toUpperCase(),
				event.getPlayer(), pl, Action.ADVANCEMENT, "advancement-action", 1);
		return;

	}

	public void executeShearWork(PlayerShearEntityEvent event, JobsPlayer pl) {
		if (event.getEntity() instanceof Sheep) {

			Sheep sheep = (Sheep) event.getEntity();

			DyeColor color = sheep.getColor();

			if (event.isCancelled()) {
				event.setCancelled(true);
				return;
			}

			finalWork("" + color, (Player) event.getPlayer(), pl, Action.SHEAR, "shear-action", 1);

			return;
		}
	}

	public void executeCraftWork(CraftItemEvent event, JobsPlayer pl) {
		final Material block = event.getInventory().getResult().getType();
		final int amount = event.getInventory().getResult().getAmount();

		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + block, (Player) event.getWhoClicked(), pl, Action.CRAFT, "craft-action", amount);

		return;
	}

	public void executeBlockBreakWork(BlockBreakEvent event, JobsPlayer pl) {
		final Block block = event.getBlock();
		final Material type = event.getBlock().getType();

		if (block.hasMetadata("placed-by-player")) {
			return;
		}
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + type, event.getPlayer(), pl, Action.BREAK, "break-action", 1);

		return;
	}

	public void executeBlockPlaceWork(BlockPlaceEvent event, JobsPlayer pl) {
		final Material type = event.getBlock().getType();

		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + type, event.getPlayer(), pl, Action.PLACE, "place-action", 1);
		return;
	}

	public void executeFishWork(PlayerFishEvent event, JobsPlayer pl) {
		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		if (event.getCaught() == null) {
			return;
		}

		String id = event.getCaught().getName().toUpperCase().replaceAll(" ", "_");

		finalWork(id, event.getPlayer(), pl, Action.FISH, "fish-action", 1);
		return;
	}

	public void executeKillWork(EntityDeathEvent event, JobsPlayer pl) {

		Player player = event.getEntity().getKiller();

		finalWork("" + event.getEntity().getType(), player, pl, Action.KILL_MOB, "kill-action", 1);
		return;
	}

	@SuppressWarnings("deprecation")
	public void executeMilkWork(PlayerInteractAtEntityEvent event, JobsPlayer pl) {

		finalWork("" + event.getPlayer().getItemInHand().getType(), event.getPlayer(), pl, Action.MILK, "milk-action",
				1);
		return;
	}

	public void executeFarmWork(BlockBreakEvent event, JobsPlayer pl) {
		final Block block = event.getBlock();
		final Material type = event.getBlock().getType();

		if (!up.isFullyGrownOld(block)) {
			return;
		}

		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + type, event.getPlayer(), pl, Action.FARM, "farm-action", 1);

		return;
	}

	public void finalWork(String id, Player player, JobsPlayer pl, Action ac, String flag, int amount) {

		plugin.getExecutor().execute(() -> {

			String UUID = "" + player.getUniqueId();

			if (getJobsWithAction(UUID, pl, ac) == null) {
				return;
			}
			ArrayList<String> jobs = getJobsWithAction(UUID, pl, ac);

			for (String job : jobs) {

				Job jb = plugin.getJobCache().get(job);

				if (jb.getIDList().contains(id.toUpperCase())) {

					if (pl.isInJob(job.toUpperCase())) {

						if (canWorkThere(player, jb, flag)) {

							if (canReward(player, jb, id)) {

								String jobid = jb.getID();

								double reward = jb.getRewardOf(id);

								double fixed = reward * amount;

								UltimateJobs.getPlugin().getEco().depositPlayer(player, fixed);

								double exp = jb.getExpOf(id) * amount;
								Integer broken = pl.getBrokenOf(jobid) + amount;
								double points = jb.getPointsOf(id) * amount;
								double old_points = pl.getPoints();

								pl.updateBroken(jobid, broken);

								double exp_old = pl.getExpOf(jobid);

								pl.changePoints(points + old_points);

								pl.updateExp(jobid, exp_old + exp);

								// check for alonsolevels
								if (plugin.isInstalledAlonso()) {
									if (jb.isAlonsoLevels(id)) {
										String r = jb.getAlonsoLevels(id);
										plugin.getAlonsoLevelsPlugin().addExp(player.getUniqueId(), Integer.valueOf(r));
									}
								}
								if (plugin.getMainConfig().getConfig().getBoolean("Enable_Levels")) {
									UltimateJobs.getPlugin().getLevelAPI().check(player, jb, pl, id);
								}
								UltimateJobs.getPlugin().getAPI().sendReward(pl, player, jb, exp, fixed, id);
								return;

							}
						}
					}
				}
			}
		});
		return;
	}

	public ArrayList<String> getJobsWithAction(String UUID, JobsPlayer pl, Action ac) {
		ArrayList<String> l = new ArrayList<String>();
		for (String j : pl.getCurrentJobs()) {
			Job jb = plugin.getJobCache().get(j);
			if (jb.getAction().equals(ac)) {
				l.add(jb.getID());
			}
		}
		return l;
	}

	public boolean canWorkInWorld(String world, Job job) {
		return job.getWorlds().contains(world);
	}

	public boolean needCustomItemToWork(Job job) {
		return job.getCustomitems() != null;
	}

	public boolean hasItemInhandWhichIsNeed(Job job, Player p) {
		List<String> items = job.getCustomitems();
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

	public String canWorkInRegion(Player player, Job j, String st) {
		if (plugin.isInstalledWorldGuard()) {
			String ac = "" + j.getAction();
			String flag = ac.toLowerCase() + "_action";
			return WorldGuardManager.checkFlag(player.getLocation(), flag, player, st);
		}
		return "ALLOW";
	}

	public boolean canReward(Player player, Job j, String id) {
		double chance = j.gettChanceOf(id);
		if (j.getIDList().contains(id)) {
			Random r = new Random();
			int chance2 = r.nextInt(100);
			if (chance2 < chance) {
				return true;
			}
		}

		return false;
	}

	 
	public ArrayList<String> getJobsInListAsID() {
		ArrayList<String> list = new ArrayList<String>();
		for (Job job : plugin.getLoaded()) {
			list.add(job.getID().toLowerCase());
		}
		return list;
	}

	public Job isJobFromConfigID(String id) {
		for (Job job : plugin.getLoaded()) {
			String cfg_id = job.getID();
			if (cfg_id.equalsIgnoreCase(id)) {
				return job;
			}
		}
		return null;
	}

	public ArrayList<String> getPlayerNameList() {
		ArrayList<String> list = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers()) {
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
		return up.toHex(plugin.getMessages().getConfig().getString(path).replaceAll("<prefix>", getPrefix())
				.replaceAll("&", "§"));
	}

	public String getPrefix() {
		return up.toHex(plugin.getMessages().getConfig().getString("Prefix").replaceAll("&", "§"));
	}

 

}
