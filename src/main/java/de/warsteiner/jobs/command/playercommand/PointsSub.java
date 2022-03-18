package de.warsteiner.jobs.command.playercommand;
 
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI; 
import de.warsteiner.jobs.UltimateJobs;  
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.player.PlayerDataManager;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class PointsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin(); 
	
	private static SimpleAPI ap = SimpleAPI.getPlugin(); 
	
	@Override
	public String getName(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Points.Usage");
	}

	@Override
	public String getDescription(UUID UUID) {
		return  plugin.getPluginManager().getMessage(UUID, "Commands.Points.Description");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender; 
		UUID UUID = player.getUniqueId();
		 PlayerDataManager plm = UltimateJobs.getPlugin().getPlayerDataModeManager();
		 
		if (args.length == 2) {
			String pl = args[1].toUpperCase();
 
			if (ap.getPlayerSaveAndLoadManager().getUUIDByName(pl.toUpperCase()) == null) {
				player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_points_not_found").replaceAll("<name>", args[1])); 
				return;
			} else {
				String uuid = ap.getPlayerSaveAndLoadManager().getUUIDByName(pl.toUpperCase());
				
				String how = plugin.getAPI().isCurrentlyInCache(uuid);
		 
				if(how.equalsIgnoreCase("CACHE")) {
					double points = jb.getPoints();
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_points_other").replaceAll("<points>", plugin.getAPI().Format(points)).replaceAll("<name>", args[1])); 
				 
					return;
				} else {  
					double points = plm.getPoints(uuid);
					player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_points_other").replaceAll("<points>", plugin.getAPI().Format(points)).replaceAll("<name>", args[1])); 
					return;
				}
			} 
		} else if(args.length == 1) { 
			player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_points_self").replaceAll("<points>", plugin.getAPI().Format(jb.getPoints())));  
			return;
		}
		else {
			player.sendMessage(plugin.getPluginManager().getMessage(UUID, "command_usage").replaceAll("<usage>", getUsage(UUID)));
		}
	}

	@Override
	public String FormatTab() {
		return "command points players_online";
	}

	@Override
	public int getTabLength() {
		return 2;
	}
	
	@Override
	public boolean isEnabled() { 
		return  plugin.getFileManager().getCMDSettings().getBoolean("Commands.Points.Enabled");
	}

	@Override
	public String getUsage(UUID UUID) { 
		return plugin.getPluginManager().getMessage(UUID, "Commands.Points.UsageMessage");
	}

}
