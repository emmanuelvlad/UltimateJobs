package de.warsteiner.jobs.command.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.datax.utils.UpdateTypes;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class StatsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI();
	private static SimpleAPI ap = SimpleAPI.getPlugin();

	@Override
	public String getName(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Stats.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Stats.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 1) {
			plugin.getGUIAddonManager().createSelfStatsGUI(player, UpdateTypes.OPEN);
			return;
		}
		if (args.length == 2) {
			String pl = args[1].toUpperCase();

			if (ap.getPlayerSaveAndLoadManager().getUUIDByName(pl.toUpperCase()) == null) {
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_stats_not_found")
						.replaceAll("<name>", args[1]));
				return;
			} else {
				String uuid = ap.getPlayerSaveAndLoadManager().getUUIDByName(pl.toUpperCase());

				if (uuid.equalsIgnoreCase("" + player.getUniqueId())) {
					plugin.getGUIAddonManager().createSelfStatsGUI(player, UpdateTypes.OPEN);
				} else {
					plugin.getGUIAddonManager().createOtherStatsGUI(player, UpdateTypes.OPEN, args[1], uuid);
				}
			}
			return;
		} else {
			player.sendMessage(
					plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command stats players_online";
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Stats.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Stats.UsageMessage");
	}

}
