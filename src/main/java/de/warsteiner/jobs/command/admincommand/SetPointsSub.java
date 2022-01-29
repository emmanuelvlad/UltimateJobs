package de.warsteiner.jobs.command.admincommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.datax.UltimateAPI;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class SetPointsSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private static UltimateAPI ap = UltimateAPI.getPlugin();

	@Override
	public String getName() {
		return "setpoints";
	}

	@Override
	public String getDescription() {
		return "Set Player's Points";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 3) {

			String player = args[1];
			String value = args[2];

			if (ap.getSQLManager().getUUIDFromName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				return;
			}

			String name = player;
			String uuid = ap.getSQLManager().getUUIDFromName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCacheOrSQL(name, uuid);

			if (plugin.getAPI().isInt(value)) {

				if (how.equalsIgnoreCase("CACHE")) {

					JobsPlayer jb = plugin.getPlayerManager().getJonPlayers().get(uuid);

					jb.changePoints(Integer.valueOf(value));
					sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Points to §a" + value
							+ "§7. §8(§eCache§8)");
					return;

				} else {

					plugin.getSQLManager().updatePoints(uuid, Integer.valueOf(value));

					sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Points to §a" + value
							+ "§7. §8(§bSQL§8)");
					return;
				}

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! The value must be a Integer");
				return;
			}

		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6/JobsAdmin setpoints <name> <value>");
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command setpoints players_online";
	}

}
