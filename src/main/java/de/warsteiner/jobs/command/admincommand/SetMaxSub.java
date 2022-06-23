package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
 
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.command.AdminCommand; 
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

public class SetMaxSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

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
		 PlayerDataAPI pl = UltimateJobs.getPlugin().getPlayerDataAPI();
		if (args.length == 3) {

			String player = args[1];
			String value = args[2];

			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}
 
			String uuid = plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());

			String how = plugin.getAPI().isCurrentlyInCache(uuid);

			if (plugin.getAPI().isInt(value)) {
				
				if(sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				if (how.equalsIgnoreCase("CACHE")) {

					JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

					jb.updateMax(Integer.valueOf(value));
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
		return "command setmax players_online";
	}
	
	@Override
	public String getUsage() { 
		return "/JobsAdmin setmax <name> <job> <value>";
	}
	
	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.setmax";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}

}
