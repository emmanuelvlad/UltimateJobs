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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.Action;
import de.warsteiner.jobs.utils.BossBarHandler;
import de.warsteiner.jobs.utils.cevents.PlayerFinishWorkEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

public class JobAPI {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI();
	private YamlConfiguration tr;

	public JobAPI(UltimateJobs plugin, YamlConfiguration tr) {
		this.tr = tr;
		this.plugin = plugin;
	}

	public HashMap<String, Date> lastworked_list = new HashMap<String, Date>();

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

	public boolean isFullyGrownOld(Block block) {

		if (block.getBlockData() == null) {
			return false;
		}
		if (block.getType() == Material.MELON || block.getType() == Material.PUMPKIN) {
			return true;
		}

		BlockData bdata = block.getBlockData();
		if (bdata instanceof Ageable) {
			Ageable age = (Ageable) bdata;
			if (age.getAge() == age.getMaximumAge()) {
				return true;
			}
		}
		return false;
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
				String message = toHex(tr.getString("Reward.BossBar").replaceAll("<prefix>", getPrefix())
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
				String message = toHex(tr.getString("Reward.Message").replaceAll("<prefix>", getPrefix())
						.replaceAll("<exp>", Format(all_exp)).replaceAll("<level_name>", job.getLevelDisplay(level))
						.replaceAll("<level_int>", "" + level).replaceAll("<id>", disofid)
						.replaceAll("<money>", Format(reward)).replaceAll("&", "§"));
				p.sendMessage(message);
			}
			if (tr.getBoolean("Reward.Enabled_Actionbar")) {
				String message = toHex(tr.getString("Reward.Actionbar").replaceAll("<prefix>", getPrefix())
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
			String fin = plugin.getAPI().toHex(plugin.getMainConfig().getConfig().getString("Settings_Name")
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
		s.sendMessage(api.getMessage("No_Perm"));
		return false;
	}

	public String isCustomItem(String display, Player player, String path, YamlConfiguration cf) {
		List<String> custom_items = cf.getStringList(path + ".List");
		for (String b : custom_items) {
			String dis = plugin.getAPI().toHex(cf.getString(path + "." + b + ".Display").replaceAll("&", "§"));
			if (display.equalsIgnoreCase(dis)) {
				return b;
			}
		}

		return "NOT_FOUND";
	}

	public boolean canBuyThisJob(Player player, Job job) {
		if (job.hasPermission()) {
			return player.hasPermission(job.getPermission());
		}
		return true;
	}

	private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");

	public boolean canWorkThere(Player player, Job j) {

		if (canWorkInWorld(player.getWorld().getName(), j)) {
			if (canWorkInRegion(player, j)) {
				return true;
			}
		}

		return false;
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

		finalWork("" + type, event.getPlayer(), pl, Action.BREAK);

		return;
	}
 
	public void executeBlockPlaceWork(BlockPlaceEvent event, JobsPlayer pl) {
		final Material type = event.getBlock().getType();

		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + type, event.getPlayer(), pl, Action.PLACE);
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

		finalWork(id, event.getPlayer(), pl, Action.FISH);
		return;
	}

	public void executeKillWork(EntityDeathEvent event, JobsPlayer pl) {

		Player player = event.getEntity().getKiller();

		finalWork("" + event.getEntity().getType(), player, pl, Action.KILL_MOB);
		return;
	}

	@SuppressWarnings("deprecation")
	public void executeMilkWork(PlayerInteractAtEntityEvent event, JobsPlayer pl) {

		finalWork("" + event.getPlayer().getItemInHand().getType(), event.getPlayer(), pl, Action.MILK);
		return;
	}

	public void executeFarmWork(BlockBreakEvent event, JobsPlayer pl) {
		final Block block = event.getBlock();
		final Material type = event.getBlock().getType();

		if (!isFullyGrownOld(block)) {
			return;
		}

		if (event.isCancelled()) {
			event.setCancelled(true);
			return;
		}

		finalWork("" + type, event.getPlayer(), pl, Action.FARM);

		return;
	}

	public void finalWork(String id, Player player, JobsPlayer pl, Action ac) {

		String UUID = "" + player.getUniqueId();

		if (getJobsWithAction(UUID, pl, ac) == null) {
			return;
		}
		ArrayList<String> jobs = getJobsWithAction(UUID, pl, ac);

		for (String job : jobs) {

			Job jb = plugin.getJobCache().get(job);

			if (jb.getIDList().contains(id.toUpperCase())) {
				if (pl.isInJob(job.toUpperCase())) {
					if (canWorkThere(player, jb)) {
						if (canReward(player, jb, id)) { 
							PlayerFinishWorkEvent event = new PlayerFinishWorkEvent(UUID, jb, player, id, pl);

							Bukkit.getServer().getPluginManager().callEvent(event);

						}
					}
				}
			}

		}
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

	public boolean canWorkInRegion(Player player, Job j) {
		return true;
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

	public String toHex(String motd) {
		Matcher matcher = pattern.matcher(motd);
		while (matcher.find()) {
			String color = motd.substring(matcher.start(), matcher.end());
			motd = motd.replace(color, "" + ChatColor.of(color));
		}

		return motd;
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
		return toHex(plugin.getMessages().getConfig().getString(path).replaceAll("<prefix>", getPrefix())
				.replaceAll("&", "§"));
	}

	public String getPrefix() {
		return toHex(plugin.getMessages().getConfig().getString("Prefix").replaceAll("&", "§"));
	}

	public ItemStack createItemStack(Player p, String item) {
		ItemStack i;
		if (Material.getMaterial(item.toUpperCase()) == null) {
			i = generateSkull(item.replaceAll("<skull>", p.getName()));
		} else {
			i = new ItemStack(Material.valueOf(item.toUpperCase()), 1);
		}
		return i;
	}

	@SuppressWarnings("deprecation")
	public ItemStack generateSkull(String owner) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(owner);
		itemStack.setItemMeta((ItemMeta) skullMeta);
		return itemStack;
	}

}
