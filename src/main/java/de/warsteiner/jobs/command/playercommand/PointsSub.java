package de.warsteiner.jobs.command.playercommand;
 
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.datax.SimpleAPI;
import de.warsteiner.datax.api.PluginAPI;
import de.warsteiner.jobs.UltimateJobs;  
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.manager.PlayerDataManager;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class PointsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();
	private PluginAPI up = SimpleAPI.getInstance().getAPI(); 
	
	private static SimpleAPI ap = SimpleAPI.getPlugin(); 
	
	@Override
	public String getName() {
		return plugin.getCommandConfig().getConfig().getString("Command.POINTS.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getCommandConfig().getConfig().getString("Command.POINTS.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender; 
		 PlayerDataManager plm = UltimateJobs.getPlugin().getPlayerDataModeManager();
		if (args.length == 2) {
			String pl = args[1].toUpperCase();
 
			if (ap.getPlayerManager().getUUIDByName(pl.toUpperCase()) == null) {
				player.sendMessage(
						up.toHex(plugin.getMessages().getConfig().getString("Not_found_Player")
								.replaceAll("<name>",  args[1]).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
				return;
			} else {
				String uuid = ap.getPlayerManager().getUUIDByName(pl.toUpperCase());
				
				String how = plugin.getAPI().isCurrentlyInCache(uuid);
		 
				if(how.equalsIgnoreCase("CACHE")) {
					double points = plugin.getPlayerManager().getOnlineJobPlayers().get(uuid).getPoints();
					player.sendMessage(
							up.toHex(plugin.getMessages().getConfig().getString("Points_Other")
									.replaceAll("<name>",  args[1]).replaceAll("<points>", ""+points).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
					return;
				} else {  
					double points = plm.getPoints(uuid);
					player.sendMessage(
							up.toHex(plugin.getMessages().getConfig().getString("Points_Other")
									.replaceAll("<name>",  args[1]).replaceAll("<points>", ""+points).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
					return;
				}
			} 
		} else if(args.length == 1) { 
			player.sendMessage(
					up.toHex(plugin.getMessages().getConfig().getString("Points_Self")
							.replaceAll("<points>", ""+jb.getPoints()).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
			return;
		}
		else {
			player.sendMessage(
					up.toHex(plugin.getCommandConfig().getConfig().getString("Command.POINTS.Syntax")
							.replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
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
		return plugin.getCommandConfig().getConfig().getBoolean("Command.POINTS.Enabled");
	}

}
