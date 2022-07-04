package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.objects.UpdateTypes;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class WithdrawSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Withdraw.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Withdraw.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {

			if (UltimateJobs.getPlugin().getFileManager().getConfig().getInt("MaxDefaultJobs") != 0) {
				sender.sendMessage(AdminCommand.prefix
						+ "Withdraw System is not possible to be used with multiplie Jobs per Player");
				return;
			}

			plugin.getGUIAddonManager().createWithdrawMenu(player, UpdateTypes.OPEN);

		} else {
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>",
					getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command withdraw";
	}

	@Override
	public int getTabLength() {
		return 2;
	}

	@Override
	public boolean isEnabled() {
		
		if(plugin.getFileManager().getConfig().getString("PayMentMode").toUpperCase().equalsIgnoreCase("INSTANT")) {
			return false;
		}
		
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Withdraw.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		JobsPlayer jb = UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer("" + UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Withdraw.UsageMessage");
	}

}
