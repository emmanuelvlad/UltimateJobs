package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.JobAction;
import de.warsteiner.jobs.utils.cevents.PlayerFinishedWorkEvent;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class JobWorkManager {

	private UltimateJobs plugin;
	private JobAPI api;

	public JobWorkManager(UltimateJobs plugin, JobAPI ap) {
		this.api = ap;
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public void executeHoneyAction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		Material item = player.getItemInHand().getType();

		if (item == null) {
			return;
		}

		if (item != Material.GLASS_BOTTLE) {
			return;
		}

		finalWork("" + event.getClickedBlock().getType(), player.getUniqueId(), JobAction.HONEY, "honey-action", 1,
				event.getClickedBlock(), null, false, true, false);
		return;

	}

	public void executeEatAction(FoodLevelChangeEvent event) {
		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		if (event.getItem() == null) {
			return;
		}

		Material item = event.getItem().getType();

		finalWork("" + item, ((Player) event.getEntity()).getUniqueId(), JobAction.EAT, "eat-action", 1, null, null,
				true, false, false);
		return;

	}

	public void executeSaplingGrowAction(String type, UUID UUID, Block block) {

		finalWork(type, UUID, JobAction.GROWSAPLINGS, "grow-saplings-action", 1, block, null, false, true, false);
		return;

	}

	public void executeDrinkEvent(Player player, String id) {
		finalWork(id.toUpperCase(), player.getUniqueId(), JobAction.DRINK, "drink-action", 1, null, null, true, false,
				false);
		return;

	}

	public void executeBerrysEvent(Player player, String id, Block block) {
		finalWork(id.toUpperCase(), player.getUniqueId(), JobAction.COLLECTBERRYS, "collectberrys-action", 1, block,
				null, false, true, false);
		return;

	}

	public void executeKillByBowEvent(Player player, String id, Entity et) {
		finalWork(id.toUpperCase(), player.getUniqueId(), JobAction.KILL_BY_BOW, "killbybow-action", 1, null, et, true,
				false, true);
		return;

	}

	public void executeAchWork(PlayerAdvancementDoneEvent event) {
		finalWork(
				event.getAdvancement().getKey().getKey().replaceAll("story/", "   ").replaceAll(" ", "").toUpperCase(),
				event.getPlayer().getUniqueId(), JobAction.ADVANCEMENT, "advancement-action", 1, null, null, true,
				false, false);
		return;

	}

	public void executeShearWork(PlayerShearEntityEvent event) {
		if (event.getEntity() instanceof Sheep) {

			Sheep sheep = (Sheep) event.getEntity();

			DyeColor color = sheep.getColor();

			if (event.isCancelled()) {
				if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
					event.setCancelled(true);
				}
				return;
			}

			finalWork("" + color, event.getPlayer().getUniqueId(), JobAction.SHEAR, "shear-action", 1, null,
					event.getEntity(), true, false, true);

			return;
		}
	}

	public void executeCraftWork(CraftItemEvent event) {
		final Material block = event.getInventory().getResult().getType();
		final int amount = event.getInventory().getResult().getAmount();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		finalWork("" + block, ((Player) event.getWhoClicked()).getUniqueId(), JobAction.CRAFT, "craft-action", amount,
				null, null, true, false, false);

		return;
	}

	public void executeStripLogWork(PlayerInteractEvent event) {
		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getMaterial().toString().contains("_AXE"))
			if (event.getClickedBlock() != null)
				if (event.getClickedBlock().toString().contains("LOG")) {
					if (!event.getClickedBlock().toString().contains("STRIPPED")) {
						finalWork("" + event.getClickedBlock().getType(), event.getPlayer().getUniqueId(),
								JobAction.STRIPLOG, "strip-action", 1, event.getClickedBlock(), null, true, true,
								false);
					}
				}

		return;
	}

	public void executeBlockBreakWork(BlockBreakEvent event) {
		final Block block = event.getBlock();
		final Material type = event.getBlock().getType();

		if (block.hasMetadata("placed-by-player")) {
			return;
		}
		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		finalWork("" + type, event.getPlayer().getUniqueId(), JobAction.BREAK, "break-action", 1, event.getBlock(),
				null, true, true, false);

		return;
	}

	public void executeBlockPlaceWork(BlockPlaceEvent event) {
		final Material type = event.getBlock().getType();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		finalWork("" + type, event.getPlayer().getUniqueId(), JobAction.PLACE, "place-action", 1, event.getBlock(),
				null, true, true, false);
		return;
	}

	public void executeFishWork(PlayerFishEvent event) {
		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		if (event.getCaught() == null) {
			return;
		}

		EntityType id = event.getCaught().getType();

		finalWork("" + id, event.getPlayer().getUniqueId(), JobAction.FISH, "fish-action", 1, null, null, true, false,
				false);
		return;
	}

	public void executeBreedWork(EntityBreedEvent event) {
		final EntityType type = event.getEntity().getType();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		if (event.getEntity() == null) {
			return;
		}

		finalWork("" + type, ((Player) event.getBreeder()).getUniqueId(), JobAction.BREED, "breed-action", 1, null,
				event.getEntity(), true, false, true);
		return;
	}

	public void executeTameWork(EntityTameEvent event) {
		final EntityType type = event.getEntity().getType();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		if (event.getEntity() == null) {
			return;
		}

		finalWork("" + type, ((Player) event.getOwner()).getUniqueId(), JobAction.TAME, "tame-action", 1, null,
				event.getEntity(), true, false, true);
		return;
	}

	public void executeKillWork(EntityDeathEvent event) {

		Player player = event.getEntity().getKiller();

		finalWork("" + event.getEntity().getType(), player.getUniqueId(), JobAction.KILL_MOB, "kill-action", 1, null,
				event.getEntity(), true, false, true);
		return;
	}

	public void executeMilkWork(PlayerInteractAtEntityEvent event) {

		finalWork("" + event.getRightClicked().getType(), event.getPlayer().getUniqueId(), JobAction.MILK,
				"milk-action", 1, null, event.getRightClicked(), true, false, true);
		return;
	}

	public void executeFarmWork(BlockBreakEvent event) {
		final Block block = event.getBlock();
		final Material type = event.getBlock().getType();

		if (!plugin.getPluginManager().isFullyGrownOld(block)) {
			return;
		}

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		finalWork("" + type, event.getPlayer().getUniqueId(), JobAction.FARM_BREAK, "farm-break-action", 1,
				event.getBlock(), null, true, true, false);

		return;
	}

	public void executeFarmGrowWork(Block block, UUID UUID) {
		final Material type = block.getType();

		finalWork("" + type, UUID, JobAction.FARM_GROW, "farm-grow-action", 1, block, null, true, true, false);

		return;
	}

	public void executeTNTEvent(String type, Entity ent, UUID UUID) {
		finalWork("" + type, UUID, JobAction.TNT, "tnt-action", 1, null, ent, true, false, true);

		return;
	}

	public void finalWork(String real, UUID ID, JobAction ac, String flag, int amount, Block block, Entity ent,
			boolean checkplayer, boolean checkblock, boolean checkentity) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {

				String IDASSTRING = "" + ID;

				FileConfiguration cfg = plugin.getFileManager().getConfig();

				if (api.getJobsWithAction(IDASSTRING, ac) == null) {
					return;
				}
				ArrayList<String> jobs = api.getJobsWithAction(IDASSTRING, ac);

				for (String job : jobs) {

					Job jub = plugin.getJobCache().get(job);

					if (jub.getConfigIDOfRealID(ac, real, jub) == null) {
						return;
					}

					String iD = jub.getConfigIDOfRealID(ac, real, jub);

					if (jub.getListOfRealIDS(ac).contains(real.toUpperCase())) {

						if (plugin.getPlayerAPI().getCurrentJobs(IDASSTRING).contains(job.toUpperCase())) {

							if (checkplayer) {
								if (!api.canWorkThereByPlayer(IDASSTRING, jub, flag)) {
									return;
								}
							}

							if (checkblock) {
								if (block != null) {
									if (!api.canWorkThereByBlock(block.getLocation(), jub, flag)) {
										return;
									}
								}
							}
							if (checkentity) {
								if (ent != null) {
									if (ent.getLocation() != null) {
										if (!api.canWorkThereByEntity(ent.getLocation(), jub, flag)) {
											return;
										}
									}
								}
							}

							if (block != null) {
								if (jub.getConfig().getBoolean("GetMoneyOnlyWhenFullyGrown")) {
									if (!plugin.getPluginManager().isFullyGrownOld(block)) {
										return;
									}
								}
							}

							if (api.canReward(jub, iD, ac)) {
								
								

								boolean can = api.checkforDailyMaxEarnings(IDASSTRING, jub);

								String date = plugin.getDate();

								int lvl = plugin.getPlayerAPI().getLevelOF(IDASSTRING, jub);
								double reward = jub.getRewardOf(iD, ac);

								double exp_old = plugin.getPlayerAPI().getExpOf(IDASSTRING, jub);
								double exp = jub.getExpOf(iD, ac) * amount;
								Integer broken = plugin.getPlayerAPI().getBrokenTimes(IDASSTRING, jub) + amount;
								double points = jub.getPointsOf(iD, ac) * amount;
								double old_points = plugin.getPlayerAPI().getPoints(IDASSTRING);

								double fixed = reward * amount;

								double next = fixed * jub.getMultiOfLevel(lvl);

								double calc = fixed + next;

								String usedid = jub.getNotRealIDByRealOne(real.toUpperCase());

								double earned_old = plugin.getPlayerAPI().getEarnedFrom(IDASSTRING, jub, usedid,
										"" + ac);

								double earnedcalc = plugin.getPlayerAPI().getEarnedAt(IDASSTRING, jub, date) + calc;

								if (jub.hasVaultReward(iD, ac)) {
									if (can) {

										if (plugin.getFileManager().getConfig().getString("PayMentMode").toUpperCase()
												.equalsIgnoreCase("INSTANT")) {

											if (Bukkit.getPlayer(ID).isOnline()) {
												UltimateJobs.getPlugin().getEco().depositPlayer(Bukkit.getPlayer(ID),
														calc);
											} else {
												UltimateJobs.getPlugin().getEco()
														.depositPlayer(Bukkit.getOfflinePlayer(ID), calc);
											}
										} else {

											if (Bukkit.getPlayer(ID).isOnline()) {
												Player p = Bukkit.getPlayer(ID);

												if (UltimateJobs.getPlugin().getFileManager().getConfig()
														.getInt("MaxDefaultJobs") != 0) {
													p.sendMessage(AdminCommand.prefix
															+ "Withdraw System is not possible to be used with multiplie Jobs per Player");
													return;
												}

												double old = plugin.getPlayerAPI().getSalary("" + ID);

												plugin.getPlayerAPI().updateSalary("" + ID, old + calc);

											} else {

												if (UltimateJobs.getPlugin().getFileManager().getConfig()
														.getInt("MaxDefaultJobs") != 0) {
													return;
												}

												double old = plugin.getPlayerAPI().getSalary("" + ID);

												plugin.getPlayerAPI().updateSalary("" + ID, old + calc);

											}
										}
									}

								}

								plugin.getPlayerAPI().updateBrokenTimes(IDASSTRING, jub, broken);

								int ol = plugin.getPlayerAPI().getBrokenTimesOfID(IDASSTRING, jub, usedid, "" + ac);

								plugin.getPlayerAPI().updateBrokenTimesOf(IDASSTRING, jub, usedid, ol + 1, "" + ac);

								if (can == false) {

									if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Exp")) {
										plugin.getPlayerAPI().updateExp(IDASSTRING, jub, exp_old + exp);
									}
									if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Points")) {
										plugin.getPlayerAPI().updatePoints(IDASSTRING, points + old_points);
									}
									if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Stats")) {
										plugin.getPlayerAPI().updateEarningsOfToday(IDASSTRING, jub, earnedcalc);
										plugin.getPlayerAPI().updateBrokenMoneyOf(IDASSTRING, jub, usedid,
												earned_old + calc, "" + ac);

									}

								} else {
									plugin.getPlayerAPI().updateExp(IDASSTRING, jub, exp_old + exp);
									plugin.getPlayerAPI().updatePoints(IDASSTRING, points + old_points);

									plugin.getPlayerAPI().updateEarningsOfToday(IDASSTRING, jub, earnedcalc);

									plugin.getPlayerAPI().updateBrokenMoneyOf(IDASSTRING, jub, usedid,
											earned_old + calc, "" + ac);
								}

								if (Bukkit.getPlayer(ID).isOnline()) {

									Player player = Bukkit.getPlayer(ID);

									JobsPlayer d = plugin.getPlayerAPI().getRealJobPlayer(IDASSTRING);

									api.playSound("FINISHED_WORK", player);

									new BukkitRunnable() {
										public void run() {
											new PlayerFinishedWorkEvent(player, d, jub, iD, ac);
										}
									}.runTaskLater(plugin, 1);

									if (cfg.getBoolean("Enable_Levels")) {
										UltimateJobs.getPlugin().getLevelAPI().check(player, jub, d, iD);
									}
									UltimateJobs.getPlugin().getAPI().sendReward(d, player, jub, exp, calc, iD, can,
											ac);
								}

								return;

							}

						}
					}
				}
			}
		});
		return;
	}

}
