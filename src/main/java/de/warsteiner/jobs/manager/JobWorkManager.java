package de.warsteiner.jobs.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.listener.debounce.BlockPistonExtendKey;

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

		UUID UUID = player.getUniqueId();
		String type = "" + event.getClickedBlock().getType();

		if (getJobOnWork("" + UUID, JobAction.HONEY, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.HONEY, "" + type);

			finalWork(type, player.getUniqueId(), JobAction.HONEY, "honey-action", 1, event.getClickedBlock(), null,
					false, true, false, job);
		}
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

		UUID UUID = ((Player) event.getEntity()).getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.EAT, "" + item) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.EAT, "" + item);

			finalWork("" + item, ((Player) event.getEntity()).getUniqueId(), JobAction.EAT, "eat-action", 1, null, null,
					true, false, false, job);
			return;
		}
	}

	public void executeSaplingGrowAction(String type, UUID UUID, Block block) {

		if (getJobOnWork("" + UUID, JobAction.GROWSAPLINGS, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.GROWSAPLINGS, "" + type);

			finalWork(type, UUID, JobAction.GROWSAPLINGS, "grow-saplings-action", 1, block, null, false, true, false,
					job);
			return;
		}
	}
 
	public void executeBerrysEvent(Player player, String id, Block block) {

		UUID UUID = player.getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.COLLECTBERRYS, "" + id.toUpperCase()) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.COLLECTBERRYS, "" + id.toUpperCase());

			finalWork(id.toUpperCase(), player.getUniqueId(), JobAction.COLLECTBERRYS, "collectberrys-action", 1, block,
					null, false, true, false, job);
			return;
		}
	}

	public void executeKillByBowEvent(Player player, String id, Entity et) {

		String type = id.toUpperCase();

		UUID UUID = player.getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.KILL_BY_BOW, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.KILL_BY_BOW, "" + type);

			finalWork(type, player.getUniqueId(), JobAction.KILL_BY_BOW, "killbybow-action", 1, null, et, true, false,
					true, job);
			return;
		}
	}

	public void executeAchWork(PlayerAdvancementDoneEvent event) {
		String type = event.getAdvancement().getKey().getKey().replaceAll("story/", "   ").replaceAll(" ", "")
				.toUpperCase();

		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.ADVANCEMENT, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.ADVANCEMENT, "" + type);

			finalWork(type, event.getPlayer().getUniqueId(), JobAction.ADVANCEMENT, "advancement-action", 1, null, null,
					true, false, false, job);
			return;
		}

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

			UUID UUID = event.getPlayer().getUniqueId();

			if (getJobOnWork("" + UUID, JobAction.SHEAR, "" + color) != null) {

				Job job = getJobOnWork("" + UUID, JobAction.SHEAR, "" + color);

				finalWork("" + color, event.getPlayer().getUniqueId(), JobAction.SHEAR, "shear-action", 1, null,
						event.getEntity(), true, false, true, job);

				return;
			}
		}
	}

	public void executeCraftWork(CraftItemEvent event) {
		final Material type = event.getInventory().getResult().getType();
		final int amount = event.getInventory().getResult().getAmount();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		UUID UUID = event.getWhoClicked().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.CRAFT, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.CRAFT, "" + type);

			finalWork("" + type, ((Player) event.getWhoClicked()).getUniqueId(), JobAction.CRAFT, "craft-action",
					amount, null, null, true, false, false, job);

			return;
		}
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

						String type = "" + event.getClickedBlock().getType();
						UUID UUID = event.getPlayer().getUniqueId();

						if (getJobOnWork("" + UUID, JobAction.STRIPLOG, "" + type) != null) {

							Job job = getJobOnWork("" + UUID, JobAction.STRIPLOG, "" + type);

							finalWork(type, event.getPlayer().getUniqueId(), JobAction.STRIPLOG, "strip-action", 1,
									event.getClickedBlock(), null, true, true, false, job);
						}
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

		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.BREAK, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.BREAK, "" + type);

			finalWork("" + type, UUID, JobAction.BREAK, "break-action", 1, event.getBlock(), null, true, true, false,
					job);

			return;
		}
	}

	public void executeTreasureEvent(Block block, Player player) {
		final Material type = block.getType();

		UUID UUID = player.getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.FIND_TREASURE, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.FIND_TREASURE, "" + type);

			finalWork("" + type, UUID, JobAction.FIND_TREASURE, "find-treasure-action", 1, block, null, true, true,
					false, job);
			return;
		}
	}

	public void executeBlockPlaceWork(BlockPlaceEvent event) {
		final Material type = event.getBlock().getType();

		if (event.isCancelled()) {
			if (plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}

		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.PLACE, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.PLACE, "" + type);

			finalWork("" + type, UUID, JobAction.PLACE, "place-action", 1, event.getBlock(), null, true, true, false,
					job);
			return;
		}
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

		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.FISH, "" + id) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.FISH, "" + id);

			finalWork("" + id, UUID, JobAction.FISH, "fish-action", 1, null, null, true, false, false, job);
			return;
		}
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

		UUID UUID = ((Player) event.getBreeder()).getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.BREED, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.BREED, "" + type);

			finalWork("" + type, UUID, JobAction.BREED, "breed-action", 1, null, event.getEntity(), true, false, true,
					job);

			return;
		}
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

		UUID UUID = ((Player) event.getOwner()).getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.TAME, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.TAME, "" + type);

			finalWork("" + type, UUID, JobAction.TAME, "tame-action", 1, null, event.getEntity(), true, false, true,
					job);
			return;
		}
	}

	public void executeKillWork(EntityDeathEvent event) {

		Player player = event.getEntity().getKiller();
		UUID UUID = player.getUniqueId();
		String type = "" + event.getEntity().getType();

		if (getJobOnWork("" + UUID, JobAction.KILL_MOB, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.KILL_MOB, "" + type);

			finalWork(type, player.getUniqueId(), JobAction.KILL_MOB, "kill-action", 1, null, event.getEntity(), true,
					false, true, job);
			return;
		}
	}

	public void executeMilkWork(PlayerInteractAtEntityEvent event) {

		String type = "" + event.getRightClicked().getType();
		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.MILK, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.MILK, "" + type);

			finalWork(type, UUID, JobAction.MILK, "milk-action", 1, null, event.getRightClicked(), true, false, true,
					job);
			return;
		}
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

		UUID UUID = event.getPlayer().getUniqueId();

		if (getJobOnWork("" + UUID, JobAction.FARM_BREAK, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.FARM_BREAK, "" + type);

			int amount = 1;

			if (job.getConfig().getBoolean("CheckIfThereAreOtherCanesAbove")) {

				if (type == Material.SUGAR_CANE) {

					Location loc = block.getLocation();

					double x = loc.getX();

					World w = Bukkit.getWorld(block.getWorld().getName());

					double z = loc.getZ();
					for (int i = 0; i < 4; i++) {

						double y = loc.getY() + i;

						Location ne = new Location(w, x, y, z);

						if (w.getBlockAt(ne) != null) {

							if (w.getBlockAt(ne).getType() != Material.AIR) {

								if (w.getBlockAt(ne).getType() == Material.SUGAR_CANE) {
									amount++;
								}

							}

						}

					}

					amount = amount - 1;

				}
			}

			finalWork("" + type, UUID, JobAction.FARM_BREAK, "farm-break-action", amount, event.getBlock(), null, true,
					true, false, job);

			return;
		}
	}

	public void executeFarmGrowWork(Block block, UUID UUID) {

		final Material type = block.getType();

		if (getJobOnWork("" + UUID, JobAction.FARM_GROW, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.FARM_GROW, "" + type);

			if (job.getConfig().getBoolean("GetMoneyOnlyWhenFullyGrown")) {
				if (!plugin.getPluginManager().isFullyGrownOld(block)) {
					return;
				}
			}

			finalWork("" + type, UUID, JobAction.FARM_GROW, "farm-grow-action", 1, block, null, true, true, false, job);

			return;
		}
	}

	public void executeSmelt(String type, UUID UUID, int amount) {

		if (getJobOnWork("" + UUID, JobAction.SMELT, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.SMELT, "" + type);

			finalWork("" + type, UUID, JobAction.SMELT, "smelt-action", amount, null, null, true, false, false, job);

			return;
		}
	}

	public void executeMoveAction(UUID UUID, PlayerMoveEvent e) {

		if (getJobOnWork("" + UUID, JobAction.EXPLORE_CHUNK, "CHUNK") != null) {

			Job job = getJobOnWork("" + UUID, JobAction.EXPLORE_CHUNK, "CHUNK");

			HashMap<Job, List<String>> l = plugin.getPlayerChunkAPI().players.get("" + UUID);
			List<String> playerChunks = l.get(job);

			Chunk from = e.getFrom().getChunk();
			Chunk to = e.getTo().getChunk();
			if (!from.equals(to)) {
				int x = to.getX(), z = to.getZ();
				for (String s : playerChunks) {
					if (s.equals(x + ";" + z)) {
						return;
					}
				}
				plugin.getPlayerChunkAPI().addChunk("" + UUID, job, x + ";" + z);

				finalWork("CHUNK", UUID, JobAction.EXPLORE_CHUNK, "explore-action", 1, null, null, true, false, false,
						job);
			}

			return;
		}
	}
	
	public void executeEnchant(String type, UUID UUID) {

		if (getJobOnWork("" + UUID, JobAction.ENCHANT, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.ENCHANT, "" + type);

			finalWork("" + type, UUID, JobAction.ENCHANT, "enchant-action", 1, null, null, true, false, false, job);

			return;
		}
	}
	
	
	public void executePotionDrink(String type, UUID UUID) {

		if (getJobOnWork("" + UUID, JobAction.DRINK_POTION, "" + type) != null) {

			Job job = getJobOnWork("" + UUID, JobAction.DRINK_POTION, "" + type);

			finalWork("" + type, UUID, JobAction.DRINK_POTION, "drink-action", 1, null, null, true, false, false, job);

			return;
		}
	}


	public Job getJobOnWork(String id, JobAction ac, String real) {
		if (api.getJobsWithAction(id, ac) == null) {
			return null;
		}
		ArrayList<String> jobs = api.getJobsWithAction(id, ac);

		for (String job : jobs) {

			Job jub = plugin.getJobCache().get(job);

			if (jub.getConfigIDOfRealID(ac, real, jub) == null) {
				return null;
			}

			if (jub.getListOfRealIDS(ac).contains(real.toUpperCase())) {
				return jub;
			}
		}
		return null;
	}

	public void finalWork(String real, UUID ID, JobAction ac, String flag, int amount, Block block, Entity ent,
			boolean checkplayer, boolean checkblock, boolean checkentity, Job job) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {

				String IDASSTRING = "" + ID;

				FileConfiguration cfg = plugin.getFileManager().getConfig();

				if (plugin.getPlayerAPI().getCurrentJobs(IDASSTRING).contains(job.getConfigID().toUpperCase())) {

					String iD = job.getConfigIDOfRealID(ac, real, job);

					if (checkplayer) {
						if (!api.canWorkThereByPlayer(IDASSTRING, job, flag)) {
							return;
						}
					}

					if (checkblock) {
						if (block != null) {
							if (!api.canWorkThereByBlock(block.getLocation(), job, flag)) {
								return;
							}
						}
					}
					if (checkentity) {
						if (ent != null) {
							if (ent.getLocation() != null) {
								if (!api.canWorkThereByEntity(ent.getLocation(), job, flag)) {
									return;
								}
							}
						}
					}

					if (api.canReward(job, iD, ac)) {

						boolean can = api.checkforDailyMaxEarnings(IDASSTRING, job);

						String date = plugin.getDate();

						int lvl = plugin.getPlayerAPI().getLevelOF(IDASSTRING, job);
						double reward = job.getRewardOf(iD, ac);

						double exp_old = plugin.getPlayerAPI().getExpOf(IDASSTRING, job);
						double exp = job.getExpOf(iD, ac) * amount;
						Integer broken = plugin.getPlayerAPI().getBrokenTimes(IDASSTRING, job) + amount;
						double points = job.getPointsOf(iD, ac) * amount;
						double old_points = plugin.getPlayerAPI().getPoints(IDASSTRING);

						double fixed = reward * amount;

						double next = fixed * job.getMultiOfLevel(lvl);

						double calc = fixed + next;

						String usedid = job.getNotRealIDByRealOne(real.toUpperCase());

						double earned_old = plugin.getPlayerAPI().getEarnedFrom(IDASSTRING, job, usedid, "" + ac);

						double earnedcalc = plugin.getPlayerAPI().getEarnedAt(IDASSTRING, job, date) + calc;

						if (job.hasVaultReward(iD, ac)) {
							if (can) {

								if (plugin.getFileManager().getConfig().getString("PayMentMode").toUpperCase()
										.equalsIgnoreCase("INSTANT")) {

									if (Bukkit.getPlayer(ID).isOnline()) {
										UltimateJobs.getPlugin().getEco().depositPlayer(Bukkit.getPlayer(ID), calc);
									} else {
										UltimateJobs.getPlugin().getEco().depositPlayer(Bukkit.getOfflinePlayer(ID),
												calc);
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

						plugin.getPlayerAPI().updateBrokenTimes(IDASSTRING, job, broken);

						int ol = plugin.getPlayerAPI().getBrokenTimesOfID(IDASSTRING, job, usedid, "" + ac);

						plugin.getPlayerAPI().updateBrokenTimesOf(IDASSTRING, job, usedid, ol + amount, "" + ac);

						if (can == false) {

							if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Exp")) {
								plugin.getPlayerAPI().updateExp(IDASSTRING, job, exp_old + exp);
							}
							if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Earn_Points")) {
								plugin.getPlayerAPI().updatePoints(IDASSTRING, points + old_points);
							}
							if (cfg.getBoolean("Jobs.MaxEarnings.IfReached_Can_Stats")) {
								plugin.getPlayerAPI().updateEarningsOfToday(IDASSTRING, job, earnedcalc);
								plugin.getPlayerAPI().updateBrokenMoneyOf(IDASSTRING, job, usedid, earned_old + calc,
										"" + ac);

							}

						} else {
							plugin.getPlayerAPI().updateExp(IDASSTRING, job, exp_old + exp);
							plugin.getPlayerAPI().updatePoints(IDASSTRING, points + old_points);

							plugin.getPlayerAPI().updateEarningsOfToday(IDASSTRING, job, earnedcalc);

							plugin.getPlayerAPI().updateBrokenMoneyOf(IDASSTRING, job, usedid, earned_old + calc,
									"" + ac);
						}

						if (Bukkit.getPlayer(ID).isOnline()) {

							Player player = Bukkit.getPlayer(ID);

							JobsPlayer d = plugin.getPlayerAPI().getRealJobPlayer(IDASSTRING);

							api.playSound("FINISHED_WORK", player);

							new BukkitRunnable() {
								public void run() {
									new PlayerFinishedWorkEvent(player, d, job, iD, ac);
								}
							}.runTaskLater(plugin, 1);

							if (cfg.getBoolean("Enable_Levels")) {
								UltimateJobs.getPlugin().getLevelAPI().check(player, job, d, iD);
							}
							UltimateJobs.getPlugin().getAPI().sendReward(d, player, job, exp, calc, iD, can, ac,
									amount);
						}

						return;

					}
				}
			}
		});
		return;
	}
 

}
