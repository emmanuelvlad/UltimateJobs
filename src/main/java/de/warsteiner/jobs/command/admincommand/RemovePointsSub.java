package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.player.PlayerDataManager;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class RemovePointsSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private static SimpleAPI ap = SimpleAPI.getPlugin(); 

	@Override
	public String getName() {
		return "rempoints";
	}

	@Override
	public String getDescription() {
		return "Remove Player Points";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		 PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
		if (args.length == 3) {

			String player = args[1];
			String value = args[2];

			if (ap.getPlayerSaveAndLoadManager().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				return;
			}
 
			String uuid =  ap.getPlayerSaveAndLoadManager().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if (how.equalsIgnoreCase("CACHE")) {

					JobsPlayer jb =UltimateJobs.getPlugin().getPlayerManager().getRealJobPlayer(uuid);

					double old = jb.getPoints();
					
					jb.changePoints(old-Integer.valueOf(value));
					sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Points -> §a" + value
							+ "§7. §8(§eCache§8)");
					return;

				} else {

					double old = pl.getPoints(uuid);
					
					pl.updatePoints(uuid, old- Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Removed §c" + player + " §7Points -> §a" + value
							+ "§7. §8(§bSQL§8)");
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
		return "command rempoints players_online";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin rempoints <name> <value>";
	}

}

