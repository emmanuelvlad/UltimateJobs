package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.SimpleAPI; 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.manager.PlayerDataManager;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class SetMaxSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private static SimpleAPI ap = SimpleAPI.getPlugin(); 

	@Override
	public String getName() {
		return "setmax";
	}

	@Override
	public String getDescription() {
		return "Set Player's Max Jobs";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		 PlayerDataManager pl = UltimateJobs.getPlugin().getPlayerDataModeManager();
		if (args.length == 3) {

			String player = args[1];
			String value = args[2];

			if (ap.getPlayerManager().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				return;
			}
 
			String uuid = ap.getPlayerManager().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {

				if (how.equalsIgnoreCase("CACHE")) {

					JobsPlayer jb = plugin.getPlayerManager().getOnlineJobPlayers().get(uuid);

					jb.changeMax(Integer.valueOf(value));
					sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7max Jobs to §a" + value
							+ "§7. §8(§eCache§8)");
					return;

				} else {

					pl.updateMax(uuid, Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7max Jobs to §a" + value
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
		return "command setmax players_online";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin setmax <name> <job> <value>";
	}

}
