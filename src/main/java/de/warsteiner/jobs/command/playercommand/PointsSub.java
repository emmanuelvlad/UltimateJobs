package de.warsteiner.jobs.command.playercommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.warsteiner.jobs.UltimateJobs; 
import de.warsteiner.jobs.api.JobAPI;
import de.warsteiner.jobs.api.JobsPlayer;
import de.warsteiner.jobs.utils.playercommand.SubCommand;

public class PointsSub extends SubCommand {

	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@Override
	public String getName() {
		return plugin.getMainConfig().getConfig().getString("Command.POINTS.Usage");
	}

	@Override
	public String getDescription() {
		return plugin.getMainConfig().getConfig().getString("Command.POINTS.Desc");
	}

	@Override
	public void perform(CommandSender sender, String[] args, JobsPlayer jb) {
		final Player player = (Player) sender;
		JobAPI api = plugin.getAPI();
		if (args.length == 2) {
			String pl = args[1].toUpperCase();

			if (plugin.getSQLManager().getUUIDFromName(pl.toUpperCase()) == null) {
				player.sendMessage(
						plugin.getAPI().toHex(plugin.getMessages().getConfig().getString("Not_found_Player")
								.replaceAll("<name>",  args[1]).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
				return;
			} else {
				String uuid = plugin.getSQLManager().getUUIDFromName(pl.toUpperCase());
				
				String how = plugin.getAPI().isCurrentlyInCacheOrSQL(pl.toUpperCase(), uuid);
				
				if(how.equalsIgnoreCase("CACHE")) {
					double points = plugin.getPlayerManager().getJonPlayers().get(uuid).getPoints();
					player.sendMessage(
							plugin.getAPI().toHex(plugin.getMessages().getConfig().getString("Points_Other")
									.replaceAll("<name>",  args[1]).replaceAll("<points>", ""+points).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
					return;
				} else {  
					double points = plugin.getSQLManager().getPoints(uuid);
					player.sendMessage(
							plugin.getAPI().toHex(plugin.getMessages().getConfig().getString("Points_Other")
									.replaceAll("<name>",  args[1]).replaceAll("<points>", ""+points).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
					return;
				}
			} 
		} else if(args.length == 1) { 
			player.sendMessage(
					plugin.getAPI().toHex(plugin.getMessages().getConfig().getString("Points_Self")
							.replaceAll("<points>", ""+jb.getPoints()).replaceAll("<prefix>", plugin.getAPI().getPrefix()).replaceAll("&", "§")));
			return;
		}
		else {
			player.sendMessage(
					plugin.getAPI().toHex(plugin.getMainConfig().getConfig().getString("Command.POINTS.Syntax")
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
		return plugin.getMainConfig().getConfig().getBoolean("Command.POINTS.Enabled");
	}

}
