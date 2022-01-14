package de.warsteiner.jobs.utils;
  
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.api.PlayerManager; 
import de.warsteiner.rewards.RewardsPlugin;

public class CommandAPI {

	private UltimateJobs plugin;
	private JobAPI api = UltimateJobs.getPlugin().getAPI(); 
	private YamlConfiguration mg;
	private PlayerManager pm;

	public CommandAPI(UltimateJobs plugin, PlayerManager pm, YamlConfiguration mg) {
		this.plugin = plugin;
		this.pm = pm;
		this.mg = mg;
	}

	public void execute(Player player, String args[]) {

		int lg = args.length;
		JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(""+player.getUniqueId()); 
		String ar1 = args[0].toUpperCase();

		if (lg == 1 && isEnabled("HELP") && ar1.equalsIgnoreCase(getUsage("HELP"))) {
			for (String m : mg.getStringList("Help")) {
				player.sendMessage(api.toHex(m).replaceAll("&", "§"));
			}
		} else if (lg == 1 && isEnabled("LEAVEALL") && ar1.equalsIgnoreCase(getUsage("LEAVEALL"))) {
			if (!jb.hasAnyJob()) {
				jb.updateCurrentJobs(null);
				player.sendMessage(api.getMessage("Leave_All"));
			} else {
				player.sendMessage(api.getMessage("Already_Left_All"));
			}
		} else if (lg == 1 && isEnabled("STATSGUI") && ar1.equalsIgnoreCase(getUsage("STATSGUI"))
				&& isThereAsPlugin("UltimateJobs-Stats")) {
			//StatsAddon.getPlugin().getGUIManager().openSelfStatsGUI(player);
		} else if (lg == 2 && isEnabled("LEAVE") && ar1.equalsIgnoreCase(getUsage("LEAVE"))) {
			String job = args[1].toUpperCase();
			if (checkIfJobIsReal(job, player)) {
				Job file = api.isJobFromConfigID(job);
				if (jb.isInJob(job)) {
						jb.remoCurrentJob(job);
						player.sendMessage(api.getMessage("Left_Job").replaceAll("<job>", file.getDisplay()));
					} else {
						player.sendMessage(api.getMessage("Not_In_Job").replaceAll("<job>", file.getDisplay()));
					}
				}
			} else if (lg == 2 && isEnabled("STATSGUI") && ar1.equalsIgnoreCase(getUsage("STATSGUI"))
					&& isThereAsPlugin("UltimateJobs-Stats")) {
				String pl = args[1];
				if (pm.isInAnyExist(pl)) {
					//StatsAddon.getPlugin().getGUIManager().openOtherStatsGUI(player, pl,
					//	plugin.getDataManager().getUUIDByName(pl.toUpperCase()));
			} else {
				player.sendMessage(api.getMessage("Not_Found_Player").replaceAll("<name>", pl));
			}
		} else if (lg == 2 && isEnabled("SALARY") && ar1.equalsIgnoreCase(getUsage("SALARY"))
				&& isThereAsPlugin("UltimateJobs-Rewards")) {
			String job = args[1].toUpperCase();
			if (checkIfJobIsReal(job, player)) {
				RewardsPlugin.getPlugin().executePluginAction(job, player);
			}
		} else {
			player.sendMessage(api.getMessage("Not_Found_Command"));
		}
	}
	
	public void sendHelp(CommandSender player) {
		 player.sendMessage("§7");
		 player.sendMessage(" §8| §9UltimateJobs §8- §4Admin Help §8|");
		 player.sendMessage("§8-> §6/JobsAdmin setlevel <name> <job> <level>");
		 player.sendMessage("§8-> §6/JobsAdmin addjob <name> <job>");
		 player.sendMessage("§8-> §6/JobsAdmin removejob <name> <job>");
		 player.sendMessage("§8-> §6/JobsAdmin reload <type>");
		 player.sendMessage("§8-> §6/JobsAdmin help"); 
		 player.sendMessage("§8-> §6/JobsAdmin addons"); 
		 player.sendMessage("§8-> §6/JobsAdmin version");
		 player.sendMessage("§7");
	}

	public void executeAdminCommandAsPlayer(CommandSender player, String args[]) {

		int lg = args.length;
	 
		if (lg == 0) {
			if (api.checkPermissions(player, "admin.help")) {
				sendHelp(player);
				return;
			}
		} else if (lg == 1 && args[0].equalsIgnoreCase("HELP")) {
			if (api.checkPermissions(player, "admin.help")) {
				sendHelp(player);  
				return;
			}
		} else if (lg == 1 && args[0].equalsIgnoreCase("VERSION")) {
			if (api.checkPermissions(player, "admin.version")) {
				player.sendMessage("§9UltimateJobs §7is running on Version §6v" + plugin.getDescription().getVersion()
						+ " §7with API-Version §a" + plugin.getDescription().getAPIVersion() + " §7by §cWarsteiner37");
				return;
			}
		} else if (lg == 1 && args[0].equalsIgnoreCase("ADDONS")) {
			if (api.checkPermissions(player, "admin.addons")) {
			 
				if(plugin.getAddons() != null) {
					 player.sendMessage("§7");
					 player.sendMessage(" §8| §9UltimateJobs §8- §4Addons §8|");
					 for(Plugin c : plugin.getAddons()) {
						 player.sendMessage("§8-> §6"+c.getDescription().getName()+" §8- §7Version§8: §bv"+c.getDescription().getVersion()+" §7by §c"+c.getDescription().getAuthors().get(0));
					 }
					 player.sendMessage("§7");
				} else {
					player.sendMessage("§9UltimateJobs §8-> §cCannot find any installed Addons!");
				}
				
				return;
			}
		}  else if (lg == 2 && args[0].equalsIgnoreCase("RELOAD") && args[1] != null) {
			if (api.checkPermissions(player, "admin.reload")) {
				String type = args[1].toUpperCase();

				if (plugin.getTypes().contains(type.toUpperCase())) {
					if (type.equalsIgnoreCase("CONFIGS")) {
						plugin.setupConfigs();
						player.sendMessage("§9UltimateJobs §8-> §7Reloaded §6Config §7File!");
					} else if (type.equalsIgnoreCase("JOBS")) {
						api.loadJobs(plugin.getLogger());
						player.sendMessage("§9UltimateJobs §8-> §7Reloaded §bJobs §7Files!");
					} else if (type.equalsIgnoreCase("ADDONS")) {
						if (isThereAsPlugin("UltimateJobs-Rewards")) {
							RewardsPlugin.getPlugin().setupConfigs();
							player.sendMessage("§9UltimateJobs §8-> §7Reloaded §6Config of Rewards-Addon§7!");
						} else {
							player.sendMessage("§9UltimateJobs §8-> §cCannot find any installed Addons!");
						}
					}

					return;
				} else {
					player.sendMessage("§9UltimateJobs §8-> §7Error! Cannot find type §c"+args[1]+"§7.");
					return;
				}
			}
		} else if (lg == 3 && args[0].equalsIgnoreCase("addjob") && args[1] != null && args[2] != null) {
			if (api.checkPermissions(player, "admin.addjob")) {
				String pl = args[1];

				if (pm.isInAnyExist(pl)) {

					String job = args[2];

					if (checkIfJobIsReal(job.toUpperCase(), player)) {

						String ud = plugin.getSQLManager().getUUIDFromName(pl.toUpperCase());

						JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(ud);   
						
						if (jb.ownJob(job)) { 
							player.sendMessage("§9UltimateJobs §8-> §7Error! Player §c"+args[1]+" §7already owns the job §b"+job+"§7.");
							return;
						} else {
							jb.addOwnedJob(job);
							player.sendMessage("§9UltimateJobs §8-> §7Added! Player §c"+args[1]+" §7owns now the job §b"+job+"§7.");
							return;
						}

					}

				} else {
					player.sendMessage("§9UltimateJobs §8-> §7Error! Cannot find player §c"+args[1]+"§7.");
					return;
				}
			}
		} else if (lg == 3 && args[0].equalsIgnoreCase("removejob") && args[1] != null && args[2] != null) {
			if (api.checkPermissions(player, "admin.removejob")) {
				String pl = args[1];

				if (pm.isInAnyExist(pl)) {

					String job = args[2];

					if (checkIfJobIsReal(job.toUpperCase(), player)) {

						String ud = plugin.getSQLManager().getUUIDFromName(pl.toUpperCase());

						JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(ud);   
						
						if (jb.ownJob(job)) {
							jb.remOwnedJob(job);
							player.sendMessage("§9UltimateJobs §8-> §7Removed! Player §c"+args[1]+" §7no longer owns the job §b"+job+"§7.");
							return;
						} else {

							player.sendMessage("§9UltimateJobs §8-> §7Error! Player §c"+args[1]+" §7does not own the job §b"+job+"§7.");
							return;
						}

					}

				} else {
					player.sendMessage("§9UltimateJobs §8-> §7Error! Cannot find player §c"+args[1]+"§7.");
					return;
				}
			}
		} else if (lg == 4 && args[0].equalsIgnoreCase("setlevel") && args[1] != null && args[2] != null
				&& args[3] != null) {
			if (api.checkPermissions(player, "admin.setlevel")) {
				String pl = args[1];

				if (pm.isInAnyExist(pl)) {

					String job = args[2];

					if (checkIfJobIsReal(job.toUpperCase(), player)) {
 
						String ud = plugin.getSQLManager().getUUIDFromName(pl.toUpperCase());
						
						JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(ud);   
						
						if (api.isInt(args[3])) {

							jb.updateLevel(job, Integer.parseInt(args[3]));

							player.sendMessage("§9UltimateJobs §8-> §7Set! Player §c"+args[1]+"§7's Level in the Job §b"+job+" §7is now §c"+args[3]+".");
							return;
						} else {
							player.sendMessage("§9UltimateJobs §8-> §7Error! This value needs to be a Int§7.");
							return;
						}

					}

				} else {
					player.sendMessage("§9UltimateJobs §8-> §7Error! Cannot find player §c"+args[1]+"§7.");
					return;
				}
			}
		} else {
			player.sendMessage("§9UltimateJobs §8-> §7Error! Please use §6/JobsAdmin help§7.");
			return;
		}
	}

	public boolean isThereAsPlugin(String name) {
		return plugin.getAddons().contains(name);
	}

	public boolean checkifConfigTypeExist(String arg, Player player) {
		String id = arg.toUpperCase();
		if (api.isJobFromConfigID(id) != null) {
			return true;
		}
		player.sendMessage(api.getMessage("Not_Found_Job").replaceAll("<job>", arg));
		return false;
	}

	public boolean checkIfJobIsReal(String arg, CommandSender s) {
		String id = arg.toUpperCase();
		if (api.isJobFromConfigID(id) != null) {
			return true;
		}
		s.sendMessage(api.getMessage("Not_Found_Job").replaceAll("<job>", arg.toLowerCase()));
		return false;
	}

	public boolean isEnabled(String t) {
		return plugin.getMainConfig().getConfig().getBoolean("Command." + t + ".Enabled");
	}

	public String getUsage(String t) {
		return plugin.getMainConfig().getConfig().getString("Command." + t + ".Usage").toUpperCase();
	}

	public boolean isAddon(String t) {
		return plugin.getMainConfig().getConfig().contains("Command." + t + ".Addon");
	}

	public String getAddonName(String t) {
		return plugin.getMainConfig().getConfig().getString("Command." + t + ".Usage").toUpperCase();
	}

}
