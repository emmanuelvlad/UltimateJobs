package de.warsteiner.jobs.command.playercommand;
 
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.utils.objects.Language;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class LangSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 

	@Override
	public String getName(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Language.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		JobsPlayer jb =UltimateJobs.getPlugin().getPlayerAPI().getRealJobPlayer(""+UUID);
		return  jb.getLanguage().getStringFromLanguage(UUID, "Commands.Language.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		UUID UUID = player.getUniqueId();
		if (args.length == 2) {

			String lang = args[1];

			if (plugin.getLanguageAPI().getLanguageFromID(lang) == null) {
				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_language_NotFound")
						.replaceAll("<lang>", lang));
				return;
			} else {

				String result = SimpleAPI.getPlugin().getPlayerDataAPI().getSettingData(jb.getUUIDAsString(), "LANG");

				Language file = plugin.getLanguageAPI().getLanguageFromID(lang);

				if (result.toLowerCase().equalsIgnoreCase(file.getName().toLowerCase())) {
					player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_language_Already")
							.replaceAll("<lang>", lang));
					plugin.getAPI().playSound("COMMAND_LANG_ALREADY", player);
					return;
				}

				SimpleAPI.getPlugin().getPlayerAPI().getPluginPlayer(UUID).updateLanguage(file);

				player.sendMessage(jb.getLanguage().getStringFromLanguage(UUID, "command_language_Changed")
						.replaceAll("<lang>", lang));
				plugin.getAPI().playSound("COMMAND_LANG_CHANGED", player);
				return;
			}

		} else {	
			plugin.getAPI().playSound("COMMAND_USAGE", player);
			player.sendMessage(
					jb.getLanguage().getStringFromLanguage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
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
		JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+UUID);
		return jb.getLanguage().getStringFromLanguage(UUID, "Commands.Language.UsageMessage");
	}
}