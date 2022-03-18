package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.Job;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.player.PlayerDataManager;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class SetLevelSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private static SimpleAPI ap = SimpleAPI.getPlugin(); 
	
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
		 PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
		if (args.length == 4) {
			
			String player = args[1];
			String job = args[2];

			String value = args[3];
	 
			if (ap.getPlayerSaveAndLoadManager().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				return;
			}
 
			String uuid =ap.getPlayerSaveAndLoadManager().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if(plugin.getAPI().isJobFromConfigID(job.toUpperCase()) != null) {
					Job j = plugin.getAPI().isJobFromConfigID(job.toUpperCase());
					if (how.equalsIgnoreCase("CACHE")) {

						JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(uuid);

						if(jb.ownJob(j.getID())) {
							jb.updateLevel(j.getID(), Integer.valueOf(value));
							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7level in Job §a" + j.getID()
									+ " §7to §6"+value+". §8(§eCache§8)");
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							return;
						}

					} else {

						if(pl.getOfflinePlayerOwnedJobs(uuid).contains(job.toUpperCase())) {
							pl.updateLevel(uuid, Integer.valueOf(value), j.getID());

							sender.sendMessage(AdminCommand.prefix + "Set §c" + player + "'s §7level in Job §a" + j.getID()
							+ " §7to §6"+value+". §8(§bSQL§8)");
							 
							return;
						} else {
							sender.sendMessage(AdminCommand.prefix + "Error! Player does not own that Job!");
							return;
						}
					}
				} else {
					sender.sendMessage(AdminCommand.prefix + "Error! Cannot find that Job!");
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				return;
			}

		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
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

}

