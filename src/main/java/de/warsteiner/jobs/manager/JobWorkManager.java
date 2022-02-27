package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.Action;
import de.warsteiner.jobs.utils.cevents.PlayerFinishedWorkEvent;

public class JobWorkManager {

	private UltimateJobs plugin;
	private JobAPI api;
	private PluginAPI up = SimpleAPI.getInstance().getAPI();

	public JobWorkManager(UltimateJobs plugin, JobAPI ap) {
		this.api = ap;
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public void executeHoneyAction(PlayerInteractEvent event, JobsPlayer pl) {
		Player player = event.getPlayer();
		if (event.isCancelled()) {
			return;
		}
		Material item = player.getItemInHand().getType();

		if (item == null) {
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

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {

				String UUID = "" + player.getUniqueId();
				PlayerDataManager dt = plugin.getPlayerDataModeManager();
				YamlConfiguration cfg = plugin.getMainConfig().getConfig();

				if (api.getJobsWithAction(UUID, pl, ac) == null) {
					return;
				}
				ArrayList<String> jobs = api.getJobsWithAction(UUID, pl, ac);

				for (String job : jobs) {

					Job jb = plugin.getJobCache().get(job);

					if (jb.getIDList().contains(id.toUpperCase())) {

						if (pl.isInJob(job.toUpperCase())) {

							if (api.canWorkThere(player, jb, flag)) {

								if (api.canReward(player, jb, id)) {

									boolean can = api.checkforDailyMaxEarnings(player, jb);

									String date = api.getDate();

									String jobid = jb.getID();
									int lvl = pl.getLevelOf(jobid);
									double reward = jb.getRewardOf(id);
									
									double exp_old = pl.getExpOf(jobid);
									double exp = jb.getExpOf(id) * amount;
									Integer broken = pl.getBrokenOf(jobid) + amount;
									double points = jb.getPointsOf(id) * amount;
									double old_points = pl.getPoints();
									
									double fixed = reward * amount;

									double next = fixed * jb.getMultiOfLevel(lvl);

									double calc = fixed + next;
									
									int times_old = dt.getTimesEarnedOf(UUID, jobid, id);
									double earned_old = dt.getAmountEarnedOf(UUID, jobid, id);

									double new_earned = calc + dt.getEarnedAtDate(UUID, jobid, date);
								   
									if (jb.hasVaultReward(id)) {
										if(can) {
											UltimateJobs.getPlugin().getEco().depositPlayer(player, calc);
										}
									}
									
									if(can == false) {
										
										if(cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Exp")) {
											pl.updateExp(jobid, exp_old + exp);
										}
										if(cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Points")) {
											pl.changePoints(points + old_points);
										}
										if(cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Stats")) {
											pl.updateBroken(jobid, broken);
											dt.updateEarningsAtDate(UUID, jobid, date, new_earned);
											
											dt.updateAmountTimesOf(UUID, jobid, id, times_old+1);
											dt.updateAmountEarnedOf(UUID, jobid, id, earned_old+calc);
										}
										
									} else {
										pl.updateExp(jobid, exp_old + exp);
										pl.changePoints(points + old_points);
										pl.updateBroken(jobid, broken);
										dt.updateEarningsAtDate(UUID, jobid, date, new_earned);
										dt.updateAmountTimesOf(UUID, jobid, id, times_old+1);
										dt.updateAmountEarnedOf(UUID, jobid, id, earned_old+calc);
									}
 
									api.playSound("FINISHED_WORK", player);

									new BukkitRunnable() {
										public void run() {
											new PlayerFinishedWorkEvent(player, pl, jb, id);
										}
									}.runTaskLater(plugin, 1);

									if (plugin.getMainConfig().getConfig().getBoolean("Enable_Levels")) {
										UltimateJobs.getPlugin().getLevelAPI().check(player, jb, pl, id);
									}
									UltimateJobs.getPlugin().getAPI().sendReward(pl, player, jb, exp, calc, id, can);
									return;

								}

							} else {
								player.sendMessage("max  reached");
							}
						}
					}
				}
			}
		});
		return;
	}

}
