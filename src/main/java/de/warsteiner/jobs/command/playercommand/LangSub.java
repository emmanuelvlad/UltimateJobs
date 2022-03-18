package de.warsteiner.jobs.command.playercommand;
 
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.Language;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.api.JobsPlayer; 
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LangSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

	@Override
	public String getName(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Language.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Language.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 2) {

			String lang = args[1];

			if (plugin.getPluginManager().getLanguageFromID(lang) == null) {
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_language_NotFound")
						.replaceAll("<lang>", lang));
				return;
			} else {

				String result = SimpleAPI.getPlugin().getPlayerSaveAndLoadManager().getSettingData(jb.getUUIDAsString(), "LANG");

				Language file = plugin.getPluginManager().getLanguageFromID(lang);

				if (result.toLowerCase().equalsIgnoreCase(file.getName().toLowerCase())) {
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_language_Already")
							.replaceAll("<lang>", lang));
					return;
				}

				SimpleAPI.getPlugin().getPlayerCacheManager().getPluginPlayer(UUID).updateLanguage(file);

				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_language_Changed")
						.replaceAll("<lang>", lang));
				return;
			}

		} else {	
			player.sendMessage(
					plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command help languages";
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public boolean isEnabled() {
		return plugin.getFileManager().getCMDSettings().getBoolean("Commands.Language.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) {
		return plugin.getPluginManager().getMessage(UUID, "Commands.Language.UsageMessage");
	}
}