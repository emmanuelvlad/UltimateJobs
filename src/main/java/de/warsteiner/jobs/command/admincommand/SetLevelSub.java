package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.command.AdminCommand; 
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class SetLevelSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	@Override
	public String getName() {
		return "setlevel";
	}

	@Override
	public String getDescription() {
		return "Set Player's Level in a Job";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		 PlayerDataAPI pl = UltimateJobs.getPlugin().getPlayerDataAPI();
		if (args.length == 4) {
			
			String player = args[1];
			String job = args[2];

			String value = args[3];
	 
			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid =plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
					if (how.equalsIgnoreCase("CACHE")) {

						JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

						if(jb.ownJob(j.getConfigID())) { 
							jb.getStatsOf(j.getConfigID()).updateLevel(Integer.valueOf(value));
							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7level in Job §a" + j.getConfigID()
									+ " §7to §6"+value+". §8(§eCache§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}

					} else {

						if(pl.getOwnedJobs(uuid).contains(job.toUpperCase())) {
							pl.updateLevel(uuid, Integer.valueOf(value), j.getConfigID());

							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7level in Job §a" + j.getConfigID()
							+ " §7to §6"+value+". §8(§bSQL§8)");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
							}
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							if(sender instanceof Player) {
								Player player3 = (Player) sender;
								player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
							}
							return;
						}
					}
				} else {
					sender.sendMessage(AdminCommand.prefix + "Error! Cannot find that Job!");
					if(sender instanceof Player) {
						Player player3 = (Player) sender;
						player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
					}
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command setlevel players_online jobs_listed";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin setlevel <name> <job> <value>";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.setlevel";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}

