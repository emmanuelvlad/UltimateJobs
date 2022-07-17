package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.PlayerDataAPI;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.Language;

public class SetLanguageSub extends AdminSubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return "setlang";
	}

	@Override
	public String getDescription() {
		return "Set Player's Language";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		PlayerDataAPI pl = UltimateJobs.getPlugin().getPlayerDataAPI();
		if (args.length == 3) {

			String player = args[1];
			String value = args[2];

			if (plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase()) == null) {
				sender.sendMessage(AdminCommand.prefix + "Error! Player §c" + player + " §7does not exist!");
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

			String uuid = plugin.getPlayerDataAPI().getUUIDByName(player.toUpperCase());
 
			if (plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase()) != null) {

				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
				}

				Language lang = plugin.getLanguageAPI().getLanguageFromName(value.toUpperCase());

				if (UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid) != null) {
					JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(uuid);

					jb.updateLocalLanguage(lang);
				}
				
				plugin.getPlayerDataAPI().updateSettingData(uuid, "LANG", lang.getName());

				sender.sendMessage(AdminCommand.prefix + "Changed §c" + player + "'s §7Language to §a" + value
						+ "§7. §8(§bSQL§8)");
				return;

			} else {
				sender.sendMessage(AdminCommand.prefix + "Error! Language not found!");
				if (sender instanceof Player) {
					Player player3 = (Player) sender;
					player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
				}
				return;
			}

		} else {
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6" + getUsage());
			if (sender instanceof Player) {
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
		return "command setlang players_online languages";
	}

	@Override
	public String getUsage() {
		return "/JobsAdmin setlang <name> <lang>";
	}

	@Override
	public String getPermission() {
		return "ultimatejobs.admin.setlang";
	}

	@Override
	public boolean showOnHelp() {
		return true;
	}

}
